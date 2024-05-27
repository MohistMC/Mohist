package com.mohistmc.mohistlauncher.action;

import com.mohistmc.mohistlauncher.util.I18n;
import com.mohistmc.tools.JarTool;
import com.mohistmc.tools.MD5Util;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

public class v_1_20_R3 {

    public static void run() {
        try {
            new Install();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Install extends Action {
        public final File fmlloader;
        public final File fmlcore;
        public final File javafmllanguage;
        public final File mclanguage;
        public final File lowcodelanguage;
        public final File mojmap;
        public final File mergedMapping;
        public final File PATCHED;

        protected Install() throws Exception {
            super();
            this.fmlloader = new File(libPath + "net/minecraftforge/fmlloader/" + mcVer + "-" + forgeVer + "/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
            this.fmlcore = new File(libPath + "net/minecraftforge/fmlcore/" + mcVer + "-" + forgeVer + "/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
            this.javafmllanguage = new File(libPath + "net/minecraftforge/javafmllanguage/" + mcVer + "-" + forgeVer + "/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.mclanguage = new File(libPath + "net/minecraftforge/mclanguage/" + mcVer + "-" + forgeVer + "/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.lowcodelanguage = new File(libPath + "net/minecraftforge/lowcodelanguage/" + mcVer + "-" + forgeVer + "/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.PATCHED = new File(libPath + "net/minecraftforge/forge/" + mcVer + "-" + forgeVer + "/forge-" + mcVer + "-" + forgeVer + "-server.jar");

            this.mojmap = new File(otherStart + "-mappings.tsrg");
            this.mergedMapping = new File(mcpStart + "-mappings-merged.tsrg");
            install();
        }

        private void install() throws Exception {
            copyFileFromJar(lzma, "data/server.lzma", true);
            copyFileFromJar(fmlloader, "data/fmlloader-" + mcVer + "-" + forgeVer + ".jar", true);
            copyFileFromJar(fmlcore, "data/fmlcore-" + mcVer + "-" + forgeVer + ".jar", true);
            copyFileFromJar(javafmllanguage, "data/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar", true);
            copyFileFromJar(mclanguage, "data/mclanguage-" + mcVer + "-" + forgeVer + ".jar", true);
            copyFileFromJar(lowcodelanguage, "data/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar", true);
            copyFileFromJar(universalJar, "data/forge-" + mcVer + "-" + forgeVer + "-universal.jar", false);
            copyFileFromJar(mohistplugin, "data/mohistplugins-" + mcVer + ".jar", true);

            if (!needsInstall()) return;
            System.out.println(I18n.as("installation.start"));

            copyFileFromJar(universalJar, "data/forge-" + mcVer + "-" + forgeVer + "-universal.jar", true);

            if (mohistVer == null || mcpVer == null) {
                System.out.println("[Mohist] There is an error with the installation, the forge / mcp version is not set.");
                System.exit(0);
            }

            mute();
            if (bundled.exists()) {
                run("net.minecraftforge.installertools.ConsoleTool",
                        "--task", "BUNDLER_EXTRACT", "--input", bundled.getAbsolutePath(), "--output", libPath, "--libraries");
                if (!unpacked.exists()) {
                    run("net.minecraftforge.installertools.ConsoleTool",
                            "--task", "BUNDLER_EXTRACT", "--input", bundled.getAbsolutePath(), "--output", unpacked.getAbsolutePath(), "--jar-only");
                }
            } else {
                System.out.println(I18n.as("installation.minecraftserver") + bundled.getAbsolutePath());
                System.exit(0);
            }

            if (mcpZip.exists()) {
                if (!mcpTsrg.exists()) {
                    run("net.minecraftforge.installertools.ConsoleTool",
                            "--task",
                            "MCP_DATA",
                            "--input", mcpZip.getAbsolutePath(),
                            "--output", mcpTsrg.getAbsolutePath(),
                            "--key",
                            "mappings");
                }
            } else {
                System.out.println(I18n.as("installation.mcpfilemissing"));
                System.exit(0);
            }

            if (JarTool.isCorrupted(unpacked)) {
                unpacked.delete();
            }

            if (JarTool.isCorrupted(official)) {
                official.delete();
            }

            if (!official.exists()) {
                run("net.minecraftforge.fart.Main",
                        "--input", unpacked.getAbsolutePath(),
                        "--output", official.getAbsolutePath(),
                        "--names", mojmap.getAbsolutePath(),
                        "--ann-fix",
                        "--ids-fix",
                        "--src-fix",
                        "--record-fix",
                        "--strip-sigs",
                        "--reverse");
            }

            String storedServerMD5 = null;
            String storedLzmaMD5 = null;
            String storeduniversalJarMD5 = MD5Util.get(universalJar);
            String serverMD5 = MD5Util.get(official);
            String lzmaMD5 = MD5Util.get(lzma);

            if (installInfo.exists()) {
                List<String> infoLines = Files.readAllLines(installInfo.toPath());
                if (!infoLines.isEmpty()) {
                    storedServerMD5 = infoLines.get(0);
                }
                if (infoLines.size() > 1) {
                    storedLzmaMD5 = infoLines.get(1);
                }
            }

            if (!PATCHED.exists() || storedServerMD5 == null || storedLzmaMD5 == null || !storedServerMD5.equals(serverMD5) || !storedLzmaMD5.equals(lzmaMD5)) {
                run("net.minecraftforge.binarypatcher.ConsoleTool",
                        "--clean", official.getAbsolutePath(),
                        "--output", PATCHED.getAbsolutePath(),
                        "--apply", lzma.getAbsolutePath(),
                        "--data",
                        "--unpatched");
                serverMD5 = MD5Util.get(PATCHED);
            }

            FileWriter fw = new FileWriter(installInfo);
            fw.write(serverMD5 + "\n");
            fw.write(lzmaMD5 + "\n");
            fw.write(storeduniversalJarMD5);
            fw.close();
            unmute();

            System.out.println(I18n.as("installation.finished"));
        }
    }
}
