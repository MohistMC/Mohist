package com.mohistmc.bukkit.pluginfix;

import io.netty.util.Version;
import io.netty.util.internal.PlatformDependent;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/11 13:47:38
 */
public class ScriptBlockPlus {

    public static String version = "4.1.82.Final";

    private static final String PROP_VERSION = ".version";
    private static final String PROP_BUILD_DATE = ".buildDate";
    private static final String PROP_COMMIT_DATE = ".commitDate";
    private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
    private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
    private static final String PROP_REPO_STATUS = ".repoStatus";

    /**
     * Retrieves the version information of Netty artifacts using the current
     * {@linkplain Thread#getContextClassLoader() context class loader}.
     *
     * @return A {@link Map} whose keys are Maven artifact IDs and whose values are {@link Version}s
     */
    public static Map<String, ScriptBlockPlus> identify() {
        Map<String, ScriptBlockPlus> versions = new TreeMap<String, ScriptBlockPlus>();
        ScriptBlockPlus scriptBlockPlus = new ScriptBlockPlus(
                "netty-common",
                "4.1.82.Final",
                parseIso8601("2022-09-13 12\\:57\\:54 -0700"),
                parseIso8601("2022-09-13 19\\:12\\:20 +0000"),
                "4779963514",
                "47799635143d7a11b56c4a4e9a1e65ca221d28ca",
                "clean");
        versions.put("netty-common", scriptBlockPlus);
        return versions;
    }

    /**
     * Retrieves the version information of Netty artifacts using the specified {@link ClassLoader}.
     *
     * @return A {@link Map} whose keys are Maven artifact IDs and whose values are {@link Version}s
     */
    public static Map<String, ScriptBlockPlus> identify(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = PlatformDependent.getContextClassLoader();
        }

        // Collect all properties.
        Properties props = new Properties();
        try {
            Enumeration<URL> resources = classLoader.getResources("META-INF/io.netty.versions.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream in = url.openStream()) {
                    props.load(in);
                }
                // Ignore.
            }
        } catch (Exception ignore) {
            // Not critical. Just ignore.
        }

        // Collect all artifactIds.
        Set<String> artifactIds = new HashSet<>();
        for (Object o: props.keySet()) {
            String k = (String) o;

            int dotIndex = k.indexOf('.');
            if (dotIndex <= 0) {
                continue;
            }

            String artifactId = k.substring(0, dotIndex);

            // Skip the entries without required information.
            if (!props.containsKey(artifactId + PROP_VERSION) ||
                    !props.containsKey(artifactId + PROP_BUILD_DATE) ||
                    !props.containsKey(artifactId + PROP_COMMIT_DATE) ||
                    !props.containsKey(artifactId + PROP_SHORT_COMMIT_HASH) ||
                    !props.containsKey(artifactId + PROP_LONG_COMMIT_HASH) ||
                    !props.containsKey(artifactId + PROP_REPO_STATUS)) {
                continue;
            }

            artifactIds.add(artifactId);
        }

        Map<String, ScriptBlockPlus> versions = new TreeMap<>();
        for (String artifactId: artifactIds) {
            versions.put(
                    artifactId,
                    new ScriptBlockPlus(
                            artifactId,
                            props.getProperty(artifactId + PROP_VERSION),
                            parseIso8601(props.getProperty(artifactId + PROP_BUILD_DATE)),
                            parseIso8601(props.getProperty(artifactId + PROP_COMMIT_DATE)),
                            props.getProperty(artifactId + PROP_SHORT_COMMIT_HASH),
                            props.getProperty(artifactId + PROP_LONG_COMMIT_HASH),
                            props.getProperty(artifactId + PROP_REPO_STATUS)));
        }

        return versions;
    }

    private static long parseIso8601(String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(value).getTime();
        } catch (ParseException ignored) {
            return 0;
        }
    }

    /**
     * Prints the version information to {@link System#err}.
     */
    public static void main(String[] args) {
        for (ScriptBlockPlus v: identify().values()) {
            System.err.println(v);
        }
    }

    private final String artifactId;
    private final String artifactVersion;
    private final long buildTimeMillis;
    private final long commitTimeMillis;
    private final String shortCommitHash;
    private final String longCommitHash;
    private final String repositoryStatus;

    private ScriptBlockPlus(
            String artifactId, String artifactVersion,
            long buildTimeMillis, long commitTimeMillis,
            String shortCommitHash, String longCommitHash, String repositoryStatus) {
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
        this.buildTimeMillis = buildTimeMillis;
        this.commitTimeMillis = commitTimeMillis;
        this.shortCommitHash = shortCommitHash;
        this.longCommitHash = longCommitHash;
        this.repositoryStatus = repositoryStatus;
    }

    public String artifactId() {
        return artifactId;
    }

    public String artifactVersion() {
        return artifactVersion;
    }

    public long buildTimeMillis() {
        return buildTimeMillis;
    }

    public long commitTimeMillis() {
        return commitTimeMillis;
    }

    public String shortCommitHash() {
        return shortCommitHash;
    }

    public String longCommitHash() {
        return longCommitHash;
    }

    public String repositoryStatus() {
        return repositoryStatus;
    }

    @Override
    public String toString() {
        return artifactId + '-' + artifactVersion + '.' + shortCommitHash +
                ("clean".equals(repositoryStatus)? "" : " (repository: " + repositoryStatus + ')');
    }
}
