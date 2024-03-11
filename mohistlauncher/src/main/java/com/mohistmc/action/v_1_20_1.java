package com.mohistmc.action;

import com.mohistmc.MohistMCStart;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.util.I18n;
import com.mohistmc.util.MohistModuleManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class v_1_20_1 {

    public static final List<String> loadedLibsPaths = new ArrayList<>();

    public static void restartServer(List<String> cmd, boolean shutdown) throws Exception {
        if (cmd.stream().anyMatch(s -> s.contains("-Xms"))) {
            System.out.println("[WARNING] We detected that you're using the -Xms argument and it will add the specified ram to the current Java process and the Java process which will be created by the ProcessBuilder, and this could lead to double RAM consumption.\nIf the server does not restart, please try remove the -Xms jvm argument.");
        }
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(MohistMCStart.jarTool.getJarDir());
        pb.inheritIO().start().waitFor();
        Thread.sleep(2000);
        if (shutdown) {
            System.exit(0);
        }
    }

    public static void run() {
        try {
            new Install_1_19();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Install_1_19 extends Action {

        public static ArrayList<String> launchArgs = new ArrayList<>(Arrays.asList("java", "-jar"));
        public final File fmlloader;
        public final File fmlcore;
        public final File javafmllanguage;
        public final File mclanguage;
        public final File lowcodelanguage;
        public final File mohistplugin;
        public final File mojmap;
        public final File mc_unpacked;
        public final File mergedMapping;

        protected Install_1_19() throws Exception {
            super();
            this.fmlloader = new File(libPath + "net/minecraftforge/fmlloader/" + mcVer + "-" + forgeVer + "/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
            this.fmlcore = new File(libPath + "net/minecraftforge/fmlcore/" + mcVer + "-" + forgeVer + "/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
            this.javafmllanguage = new File(libPath + "net/minecraftforge/javafmllanguage/" + mcVer + "-" + forgeVer + "/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.mclanguage = new File(libPath + "net/minecraftforge/mclanguage/" + mcVer + "-" + forgeVer + "/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.lowcodelanguage = new File(libPath + "net/minecraftforge/lowcodelanguage/" + mcVer + "-" + forgeVer + "/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.mohistplugin = new File(libPath + "com/mohistmc/mohistplugins/mohistplugins-" + mcVer + ".jar");
            this.mojmap = new File(otherStart + "-mappings.txt");
            this.mc_unpacked = new File(otherStart + "-unpacked.jar");
            this.mergedMapping = new File(mcpStart + "-mappings-merged.txt");
            libPath();
            install();
        }

        private void install() throws Exception {
            launchArgs.add(new File(MohistModuleManager.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName());
            launchArgs.addAll(MohistMCStart.mainArgs);
            copyFileFromJar(lzma, "data/server.lzma");
            copyFileFromJar(fmlloader, "data/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(fmlcore, "data/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(javafmllanguage, "data/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(mclanguage, "data/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(lowcodelanguage, "data/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(mohistplugin, "data/mohistplugins-" + mcVer + ".jar");

            if (!checkDependencies()) return;
            System.out.println(I18n.as("installation.start"));

            copyFileFromJar(universalJar, "data/forge-" + mcVer + "-" + forgeVer + "-universal.jar");

            if (mohistVer == null || mcpVer == null) {
                System.out.println("[Mohist] There is an error with the installation, the forge / mcp version is not set.");
                System.exit(0);
            }

            if (minecraft_server.exists()) {
                mute();
                run("net.minecraftforge.installertools.ConsoleTool",
                        new String[]{"--task", "BUNDLER_EXTRACT", "--input", minecraft_server.getAbsolutePath(), "--output", libPath, "--libraries"},
                        stringToUrl(loadedLibsPaths));
                unmute();
                if (!mc_unpacked.exists()) {
                    mute();
                    run("net.minecraftforge.installertools.ConsoleTool",
                            new String[]{"--task", "BUNDLER_EXTRACT", "--input", minecraft_server.getAbsolutePath(), "--output", mc_unpacked.getAbsolutePath(), "--jar-only"},
                            stringToUrl(loadedLibsPaths));
                    unmute();
                }
            } else {
                System.out.println(I18n.as("installation.minecraftserver"));
            }

            if (mcpZip.exists()) {
                if (!mcpTxt.exists()) {

                    // MAKE THE MAPPINGS TXT FILE

                    System.out.println(I18n.as("installation.mcp"));
                    mute();
                    run("net.minecraftforge.installertools.ConsoleTool",
                            new String[]{"--task", "MCP_DATA", "--input", mcpZip.getAbsolutePath(), "--output", mcpTxt.getAbsolutePath(), "--key", "mappings"},
                            stringToUrl(loadedLibsPaths));
                    unmute();
                }
            } else {
                System.out.println(I18n.as("installation.mcpfilemissing"));
                System.exit(0);
            }

            if (isCorrupted(extra)) {
                extra.delete();
            }
            if (isCorrupted(slim)) {
                slim.delete();
            }
            if (isCorrupted(srg)) {
                srg.delete();
            }

            if (!mergedMapping.exists()) {
                mute();
                run("net.minecraftforge.installertools.ConsoleTool",
                        new String[]{"--task", "MERGE_MAPPING", "--left", mcpTxt.getAbsolutePath(), "--right", mojmap.getAbsolutePath(), "--output", mergedMapping.getAbsolutePath(), "--classes", "--reverse-right"},
                        stringToUrl(loadedLibsPaths));
                unmute();
            }

            if (!slim.exists() || !extra.exists()) {
                mute();
                run("net.minecraftforge.jarsplitter.ConsoleTool",
                        new String[]{"--input", minecraft_server.getAbsolutePath(), "--slim", slim.getAbsolutePath(), "--extra", extra.getAbsolutePath(), "--srg", mergedMapping.getAbsolutePath()},
                        stringToUrl(loadedLibsPaths));
                run("net.minecraftforge.jarsplitter.ConsoleTool",
                        new String[]{"--input", mc_unpacked.getAbsolutePath(), "--slim", slim.getAbsolutePath(), "--extra", extra.getAbsolutePath(), "--srg", mergedMapping.getAbsolutePath()},
                        stringToUrl(loadedLibsPaths));
                unmute();
            }

            if (!srg.exists()) {
                mute();
                run("net.minecraftforge.fart.Main",
                        new String[]{"--input", slim.getAbsolutePath(), "--output", srg.getAbsolutePath(), "--names", mergedMapping.getAbsolutePath(), "--ann-fix", "--ids-fix", "--src-fix", "--record-fix"},
                        stringToUrl(loadedLibsPaths));
                unmute();
            }

            String storedServerMD5 = null;
            String storedMohistMD5 = null;
            String serverMD5 = MD5Util.get(serverJar);
            String mohistMD5 = MD5Util.get(MohistMCStart.jarTool.getFile());

            if (installInfo.exists()) {
                List<String> infoLines = Files.readAllLines(installInfo.toPath());
                if (!infoLines.isEmpty()) {
                    storedServerMD5 = infoLines.get(0);
                }
                if (infoLines.size() > 1) {
                    storedMohistMD5 = infoLines.get(1);
                }
            }

            if (!serverJar.exists()
                    || storedServerMD5 == null
                    || storedMohistMD5 == null
                    || !storedServerMD5.equals(serverMD5)
                    || !storedMohistMD5.equals(mohistMD5)) {
                mute();
                run("net.minecraftforge.binarypatcher.ConsoleTool",
                        new String[]{"--clean", srg.getAbsolutePath(), "--output", serverJar.getAbsolutePath(), "--apply", lzma.getAbsolutePath()},
                        stringToUrl(loadedLibsPaths));
                unmute();
                serverMD5 = MD5Util.get(serverJar);
            }

            FileWriter fw = new FileWriter(installInfo);
            fw.write(serverMD5 + "\n");
            fw.write(mohistMD5);
            fw.close();

            System.out.println(I18n.as("installation.finished"));
            MohistConfigUtil.yml.set("mohist.installation-finished", true);
            MohistConfigUtil.save();
            restartServer(launchArgs, true);
        }

        protected void libPath() throws Exception {
            File out = new File(libPath + "com/mohistmc/cache", "libPath.txt");
            if (!out.exists()) {
                out.getParentFile().mkdirs();
                out.createNewFile();
            }
            fileWriterMethod(out.getPath(), libPath);
        }

        public static void fileWriterMethod(String filepath, String content) throws IOException {
            try (FileWriter fileWriter = new FileWriter(filepath)) {
                fileWriter.append(content);
            }
        }
    }
}
