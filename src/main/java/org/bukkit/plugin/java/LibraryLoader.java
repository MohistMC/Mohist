package org.bukkit.plugin.java;

import com.mohistmc.MohistMC;
import com.mohistmc.bukkit.remapping.RemappingURLClassLoader;
import com.mohistmc.util.IOUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import mjson.Json;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class LibraryLoader {

    public LibraryLoader() {
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc) throws IOException {
        if (desc.getLibraries().isEmpty()) {
            return null;
        }
        MohistMC.LOGGER.info("[{}] Loading {} libraries... please wait", desc.getName(), desc.getLibraries().size());

        List<Dependency> dependencies = new ArrayList<>();
        for (String libraries : desc.getLibraries()) {
            String[] args = libraries.split(":");
            if (args.length > 1) {
                Dependency dependency = new Dependency(args[0], args[1], args[2], false);
                dependencies.add(dependency);
            }

        }

        List<File> libraries = new ArrayList<>();
        List<Dependency> newDependencies = new ArrayList<>();

        for (Dependency dependency : dependencies) {
            newDependencies.add(dependency);
            String group = dependency.group().replace(".", "/");
            String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());

            String pomUrl = "https://repo.maven.apache.org/maven2/%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName.replace("jar", "pom"));
            newDependencies.addAll(initDependencies0(new URL(pomUrl)));
        }

        MohistMC.LOGGER.info("[{}] Loading {} extra libraries... please wait", desc.getName(), newDependencies.size() - desc.getLibraries().size());

        for (Dependency dependency : newDependencies) {
            String group = dependency.group().replace(".", "/");
            String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());
            String mavenUrl = "https://repo.maven.apache.org/maven2/%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName);

            File file = new File(new File("libraries", "plugins-lib"), "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName));

            if (file.exists()) {
                MohistMC.LOGGER.info("[{}] Found libraries {}", desc.getName(), file);
                libraries.add(file);
                continue;
            }
            try {
                file.getParentFile().mkdirs();

                InputStream inputStream = new URL(mavenUrl).openStream();
                ReadableByteChannel rbc = Channels.newChannel(inputStream);
                FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

                fc.transferFrom(rbc, 0, Long.MAX_VALUE);
                fc.close();
                rbc.close();

                libraries.add(file);
            } catch (IOException e) {
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

        return new RemappingURLClassLoader(jarFiles.toArray(new URL[0]), getClass().getClassLoader());
    }

    public static List<Dependency> initDependencies0(URL url) throws IOException {
        List<Dependency> list = new ArrayList<>();
        for (Dependency dependency : initDependencies(url)) {
            list.add(dependency);
            if (dependency.extra()) {
                String group = dependency.group().replace(".", "/");
                String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());
                String pomUrl = "https://repo.maven.apache.org/maven2/%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName.replace("jar", "pom"));
                if (IOUtil.hasUrl(pomUrl)) list.addAll(initDependencies(new URL(pomUrl)));
            }
        }
        return list;
    }

    public static List<Dependency> initDependencies(URL url) throws MalformedURLException {
        List<Dependency> list = new ArrayList<>();
        Json json2Json = Json.readXml(url).at("project");
        String version = json2Json.has("parent") ? json2Json.at("parent").asString("version") : json2Json.asString("version");

        if (!json2Json.has("dependencies")) return list;
        if (!json2Json.at("dependencies").toString().startsWith("{\"dependency\"")) return list;
        Json json3Json = json2Json.at("dependencies").at("dependency");
        if (json3Json.isArray()) {
            for (Json o : json2Json.at("dependencies").asJsonList("dependency")) {
                if (o.toString().contains("groupId") && o.toString().contains("artifactId")) {
                    String groupId = o.asString("groupId");
                    String artifactId = o.asString("artifactId");
                    if (o.toString().contains("version")) {
                        String versionAsString = o.asString("version");
                        if (versionAsString.contains("${project.version}")) {
                            Dependency dependency = new Dependency(groupId, artifactId, version, true);
                            list.add(dependency);
                        } else if (!versionAsString.contains("${")) {
                            Dependency dependency = new Dependency(groupId, artifactId, versionAsString, true);
                            list.add(dependency);
                        }
                    } else {
                        if (o.has("scope") && o.asString("scope").equals("compile")) {
                            URL mavenUrl = new URL("https://repo.maven.apache.org/maven2/%s/%s/%s".formatted(groupId.replace(".", "/"), artifactId, "maven-metadata.xml"));
                            Json compile_json2Json = Json.readXml(mavenUrl).at("metadata");;

                            String compile_version = compile_json2Json.at("versioning").asString("release");

                            Dependency dependency = new Dependency(groupId, artifactId, compile_version, true);
                            list.add(dependency);
                        }
                    }
                }
            }
        } else {
            Dependency dependency = new Dependency(json3Json.asString("groupId"), json3Json.asString("artifactId"), json3Json.asString("version"), true);
            list.add(dependency);
        }
        return list;
    }

    public record Dependency(String group, String name, String version, boolean extra) {
    }
}
