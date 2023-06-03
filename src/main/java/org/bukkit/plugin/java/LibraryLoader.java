// CHECKSTYLE:OFF
package org.bukkit.plugin.java;

import com.mohistmc.MohistMC;
import com.mohistmc.bukkit.nms.proxy.DelegateURLClassLoder;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class LibraryLoader {

    public LibraryLoader() {
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc) {
        if (desc.getLibraries().isEmpty()) {
            return null;
        }
        MohistMC.LOGGER.info("[{}] Loading {} libraries... please wait", desc.getName(), desc.getLibraries().size());

        List<Dependency> dependencies = new ArrayList<>();
        for (String libraries : desc.getLibraries()) {
            String[] args = libraries.split(":");
            if (args.length > 1) {
                Dependency dependency = new Dependency(args[0], args[1], args[2]);
                dependencies.add(dependency);
            }

        }

        List<File> libraries = new ArrayList<>();

        for (Dependency dependency : dependencies) {
            String group = dependency.group().replaceAll("\\.", "/");
            String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());
            String mavenUrl = "https://repo.maven.apache.org/maven2/%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName);

            File file = new File(new File("libraries", "spigot-lib"), "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName));

            if (file.exists()) {
                MohistMC.LOGGER.info("[{}] Found libraries {}", desc.getName(), file);
                libraries.add(file);
                continue;
            }

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                InputStream inputStream = new URL(mavenUrl).openStream();
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[8 * 1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                libraries.add(file);
            } catch (IOException e) {
                MohistMC.LOGGER.error(e);
            }
        }

        List<URL> jarFiles = new ArrayList<>();
        for (File file : libraries) {
            try {
                jarFiles.add(file.toURI().toURL());
                MohistMC.LOGGER.info("[{}] Loaded libraries {}", desc.getName(), file);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return new DelegateURLClassLoder(jarFiles.toArray(new URL[0]), getClass().getClassLoader());
    }

    public record Dependency(String group, String name, String version) {}
}
