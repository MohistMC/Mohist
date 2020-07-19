package red.mohist;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import red.mohist.common.async.MohistThreadBox;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 * <p>
 * Check out https://bStats.org/ to learn more about bStats!
 */
public class Metrics {

    private final String name;
    private final String serverUUID;
    private final List<CustomChart> charts = new ArrayList<>();

    public Metrics(String name, String serverUUID, boolean logFailedRequests) {
        this.name = name;
        this.serverUUID = serverUUID;

        startSubmitting();
    }

    private static void sendData(JSONObject data) throws Exception {
        if (data == null) throw new IllegalArgumentException("Data cannot be null!");

        HttpsURLConnection connection = (HttpsURLConnection) new URL("https://bStats.org/submitData/server-implementation").openConnection();
        byte[] compressedData = compress(data.toString());

        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
        connection.setRequestProperty("User-Agent", "MC-Server/" + 1);

        // Send data
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        outputStream.close();

        connection.getInputStream().close(); // We don't care about the response - Just send our data :)
    }

    private static byte[] compress(final String str) throws IOException {
        if (str == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        return outputStream.toByteArray();
    }

    public void addCustomChart(CustomChart chart) {
        if (chart == null) throw new IllegalArgumentException("Chart cannot be null!");
        charts.add(chart);
    }


    private void startSubmitting() {
        MohistThreadBox.METRICS.scheduleAtFixedRate(this::submitData, 1000 * 60 * 5, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
    }

    public JSONObject getPluginData() {
        JSONObject data = new JSONObject();

        data.put("pluginName", name); // Append the name of the server software
        JSONArray customCharts = new JSONArray();
        for (CustomChart customChart : charts) {
            JSONObject chart = customChart.getRequestJsonObject();
            if (chart == null) continue;
            customCharts.add(chart);
        }
        data.put("customCharts", customCharts);
        return data;
    }

    private JSONObject getServerData() {
        JSONObject data = new JSONObject();
        data.put("serverUUID", serverUUID);
        data.put("osName", System.getProperty("os.name"));
        data.put("osArch", System.getProperty("os.arch"));
        data.put("osVersion", System.getProperty("os.version"));
        data.put("coreCount", Runtime.getRuntime().availableProcessors());
        return data;
    }

    private void submitData() {
        final JSONObject data = getServerData();
        JSONArray pluginData = new JSONArray();
        pluginData.add(getPluginData());
        data.put("plugins", pluginData);

        try {
            sendData(data);
        } catch (Exception ignored) {
        }
    }

    public static abstract class CustomChart {
        final String chartId;

        CustomChart(String chartId) {
            if (chartId == null || chartId.isEmpty())
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            this.chartId = chartId;
        }

        private JSONObject getRequestJsonObject() {
            JSONObject chart = new JSONObject();
            chart.put("chartId", chartId);
            try {
                JSONObject data = getChartData();
                if (data == null) return null;
                chart.put("data", data);
            } catch (Throwable t) {
                return null;
            }
            return chart;
        }

        protected abstract JSONObject getChartData() throws Exception;

    }

    public static class SimplePie extends CustomChart {

        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            String value = callable.call();
            if (value == null || value.isEmpty()) return null;
            data.put("value", value);
            return data;
        }
    }

    public static class DrilldownPie extends CustomChart {

        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        public JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, Map<String, Integer>> map = callable.call();
            if (map == null || map.isEmpty()) return null;
            boolean reallyAllSkipped = true;
            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                JSONObject value = new JSONObject();
                boolean allSkipped = true;
                for (Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                    value.put(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    values.put(entryValues.getKey(), value);
                }
            }
            if (reallyAllSkipped) return null;
            data.put("values", values);
            return data;
        }
    }

    public static class SingleLineChart extends CustomChart {

        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            int value = callable.call();
            if (value == 0) return null;
            data.put("value", value);
            return data;
        }

    }

    public static class MohistMetrics {
        public static void startMetrics() {
            File configFile = new File(new File((File) MinecraftServer.getServerInst().options.valueOf("plugins"), "bStats"), "config.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            if (!config.isSet("serverUuid")) {
                config.addDefault("enabled", true);
                config.addDefault("serverUuid", UUID.randomUUID().toString());
                config.addDefault("logFailedRequests", false);

                config.options().header(
                        "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                                "To honor their work, you should not disable it.\n" +
                                "This has nearly no effect on the server performance!\n" +
                                "Check out https://bStats.org/ to learn more :)"
                ).copyDefaults(true);
                try {
                    config.save(configFile);
                } catch (IOException ignored) {
                }
            }

            String serverUUID = config.getString("serverUuid");
            boolean logFailedRequests = config.getBoolean("logFailedRequests", false);

            if (config.getBoolean("enabled", true)) {
                Metrics metrics = new Metrics("Mohist", serverUUID, logFailedRequests);

                metrics.addCustomChart(new Metrics.SimplePie("minecraft_version", () -> {
                    String minecraftVersion = Bukkit.getVersion();
                    minecraftVersion = minecraftVersion.substring(minecraftVersion.indexOf("MC: ") + 4, minecraftVersion.length() - 1);
                    return minecraftVersion;
                }));

                metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
                metrics.addCustomChart(new Metrics.SimplePie("online_mode", () -> Bukkit.getOnlineMode() ? "online" : "offline"));
                metrics.addCustomChart(new Metrics.SimplePie("mohist_version", Mohist::getVersion));

                metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
                    Map<String, Map<String, Integer>> map = new HashMap<>();
                    String javaVersion = System.getProperty("java.version");
                    Map<String, Integer> entry = new HashMap<>();
                    entry.put(javaVersion, 1);

                    String majorVersion = javaVersion.split("\\.")[0];
                    String release;

                    int indexOf = javaVersion.lastIndexOf('.');

                    if (majorVersion.equals("1")) release = "Java " + javaVersion.substring(0, indexOf);
                    else {
                        Matcher versionMatcher = Pattern.compile("\\d+").matcher(majorVersion);
                        if (versionMatcher.find()) {
                            majorVersion = versionMatcher.group(0);
                        }
                        release = "Java " + majorVersion;
                    }
                    map.put(release, entry);

                    return map;
                }));
            }

        }
    }
}