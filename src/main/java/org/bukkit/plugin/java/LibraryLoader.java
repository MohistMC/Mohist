package org.bukkit.plugin.java;

import com.mohistmc.mjson.Json;
import com.mohistmc.mohist.Mohist;
import com.mohistmc.mohist.bukkit.PluginsLibrarySource;
import com.mohistmc.mohist.bukkit.remapping.RemappingURLClassLoader;
import com.mohistmc.tools.ConnectionUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

class LibraryLoader {

    public static Set<File> libraries = new HashSet<>();
    public static Set<Dependency> newDependencies = new HashSet<>();
    public static Set<DependencyIgnoreVersion> dependencyIgnoreVersion = new HashSet<>();

    public LibraryLoader() {
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc) {
        if (desc.getLibraries().isEmpty()) {
            return null;
        }
        Mohist.LOGGER.info("[{}] Loading {} libraries... please wait", desc.getName(), desc.getLibraries().size());

        List<Dependency> dependencies = new ArrayList<>();
        for (String desc_libraries : desc.getLibraries()) {
            String[] args = desc_libraries.split(":");
            if (args.length > 1) {
                Dependency dependency = new Dependency(args[0], args[1], args[2], false);
                if (has(dependency)) {
                    continue;
                }
                dependencies.add(dependency);
            }
        }
        var mohistLibs = mohistLibs();

        for (Dependency dependency : dependencies) {
            String group = dependency.group().replace(".", "/");
            String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());
            if (has(dependency)) {
                continue;
            }
            if (!mohistLibs.contains(fileName)) {
                if (dependency.version().equalsIgnoreCase("LATEST")) {
                    newDependencies.add(findDependency(group, dependency.name(), false));
                } else {
                    newDependencies.add(dependency);
                    String pomUrl = PluginsLibrarySource.DEFAULT + "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName.replace("jar", "pom"));
                    if (ConnectionUtil.isValid(pomUrl)) {
                        newDependencies.addAll(initDependencies0(pomUrl));
                    }
                }
            }
        }

        Mohist.LOGGER.info("[{}] Loading {} extra libraries... please wait", desc.getName(), newDependencies.size() - desc.getLibraries().size());

        for (Dependency dependency : newDependencies) {
            String group = dependency.group().replace(".", "/");
            String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());

            String mavenUrl = PluginsLibrarySource.DEFAULT + "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName);
            File file = new File(new File("libraries", "plugins-lib"), "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName));

            if (has(dependency)) {
                continue;
            }
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                InputStream inputStream = new URL(mavenUrl).openStream();
                ReadableByteChannel rbc = Channels.newChannel(inputStream);
                FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

                fc.transferFrom(rbc, 0, Long.MAX_VALUE);
                fc.close();
                rbc.close();

                libraries.add(file);
            } catch (IOException ignored) {
            }
        }

        List<URL> jarFiles = new ArrayList<>();
        for (File file : libraries) {
            try {
                jarFiles.add(file.toURI().toURL());
                Mohist.LOGGER.info("[{}] Loaded libraries {}", desc.getName(), file);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return new RemappingURLClassLoader(jarFiles.toArray(new URL[0]), getClass().getClassLoader());
    }

    public Set<Dependency> initDependencies0(String url) {
        Set<Dependency> list = new HashSet<>();
        for (Dependency dependency : initDependencies(url)) {
            if (newDependencies.contains(dependency)){
                continue;
            }
            list.add(dependency);

            if (dependencyIgnoreVersion.contains(dependency.toIgnoreVersion())) {
                continue;
            }
            dependencyIgnoreVersion.add(dependency.toIgnoreVersion());
            if (dependency.extra()) {
                String group = dependency.group().replace(".", "/");
                String fileName = "%s-%s.pom".formatted(dependency.name(), dependency.version());
                String pomUrl = PluginsLibrarySource.DEFAULT + "%s/%s/%s/%s".formatted(group, dependency.name(), dependency.version(), fileName);
                if (ConnectionUtil.isValid(pomUrl)) list.addAll(initDependencies(pomUrl));
            }
        }
        return list;
    }

    public Set<Dependency> initDependencies(String url) {
        Set<Dependency> list = new HashSet<>();
        Json json = Json.readXml(url);
        if (json != null) {
            Json json2Json = json.at("project");
            String version = json2Json.has("parent") ? json2Json.at("parent").asString("version") : json2Json.asString("version");
            String groupId = json2Json.has("parent") ? json2Json.at("parent").asString("groupId") : json2Json.asString("groupId");

            if (!json2Json.has("dependencies")) return list;
            if (!json2Json.at("dependencies").toString().startsWith("{\"dependency\"")) return list;
            Json json3Json = json2Json.at("dependencies").at("dependency");
            if (json3Json.isArray()) {
                for (Json o : json2Json.at("dependencies").asJsonList("dependency")) {
                    dependency(o, list, version, groupId);
                }
            } else {
                dependency(json3Json, list, version, groupId);
            }
            list.addAll(findDependency(list));
        }
        return list;
    }

    public void dependency(Json json, Set<Dependency> list, String version, String parent_groupId) {
        try {
            if (json.has("groupId") && json.has("artifactId")) {
                String groupId = json.asString("groupId");
                if (groupId.startsWith("${") ) {
                    groupId = parent_groupId;
                }
                String artifactId = json.asString("artifactId");
                DependencyIgnoreVersion d = new DependencyIgnoreVersion(groupId, artifactId);
                if (dependencyIgnoreVersion.contains(d)) {
                    return;
                }
                if (json.has("optional")) {
                    return;
                }
                if (json.has("scope") && (json.asString("scope").equals("test") || json.asString("scope").equals("provided"))) {
                    return;
                }
                if (json.has("version")) {
                    String versionAsString = json.asString("version");
                    if (versionAsString.contains("${project.version}") || versionAsString.contains("${project.parent.version}")) {
                        Dependency dependency = new Dependency(groupId, artifactId, version, true);
                        list.add(dependency);
                    } else if (!versionAsString.contains("${")) {
                        Dependency dependency = new Dependency(groupId, artifactId, versionAsString, true);
                        list.add(dependency);
                    }
                } else {
                    if (json.has("scope") && json.asString("scope").equals("compile")) {
                        list.add(findDependency(groupId, artifactId, true));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dependency findDependency(String groupId, String artifactId, boolean extra) {
        String mavenUrl = PluginsLibrarySource.DEFAULT + "%s/%s/%s".formatted(groupId.replace(".", "/"), artifactId, "maven-metadata.xml");
        Json compile_json2Json = Json.readXml(mavenUrl).at("metadata");
        List<Object> v = compile_json2Json.at("versioning").at("versions").at("version").asList();
        return new Dependency(groupId, artifactId, (String) v.get(v.size() - 1), extra);
    }

    public Set<Dependency> findDependency(Set<Dependency> dependencySet) {
        Set<Dependency> list = new HashSet<>();
        for (Dependency dependency : dependencySet) {
            if (dependencyIgnoreVersion.contains(dependency.toIgnoreVersion())) {
                continue;
            }
            dependencyIgnoreVersion.add(dependency.toIgnoreVersion());
            String group = dependency.group.replace(".", "/");
            String fileName = "%s-%s.pom".formatted(dependency.name, dependency.version);
            String pomUrl = PluginsLibrarySource.DEFAULT + "%s/%s/%s/%s".formatted(group, dependency.name, dependency.version, fileName);
            if (ConnectionUtil.isValid(pomUrl)) {
                list.addAll(initDependencies(pomUrl));
            }
        }

        return list;
    }

    public List<String> mohistLibs() {
        List<String> temp = new ArrayList<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(LibraryLoader.class.getClassLoader().getResourceAsStream("libraries.txt")));
        String str;
        try {
            while ((str = b.readLine()) != null) {
                String[] s = str.split("\\|");
                temp.add(new File("libraries", s[0]).getName());
            }
            b.close();
        } catch (Exception ignored) {}
        return temp;
    }

    public boolean has(Dependency dependency) {
        String fileName = "%s-%s.jar".formatted(dependency.name(), dependency.version());
        File file = new File(new File("libraries", "plugins-lib"), "%s/%s/%s/%s".formatted(dependency.group, dependency.name, dependency.version(), fileName));

        if (file.exists()) {
            if (!dependencyIgnoreVersion.contains(dependency.toIgnoreVersion())) {
                libraries.add(file);
                dependencyIgnoreVersion.add(dependency.toIgnoreVersion());
                Mohist.LOGGER.info("[{}] Found libraries {}", dependency.name, file);
                return true;
            }
        }
        return false;
    }

    public record Dependency(String group, String name, String version, boolean extra) {

        public DependencyIgnoreVersion toIgnoreVersion() {
            return new DependencyIgnoreVersion(group, name);
        }
    }

    public record DependencyIgnoreVersion(String group, String name) {
    }
}
