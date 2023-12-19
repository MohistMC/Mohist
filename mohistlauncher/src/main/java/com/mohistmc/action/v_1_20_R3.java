package com.mohistmc.action;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.util.I18n;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class v_1_20_R3 {

    public static final List<String> loadedLibsPaths = new ArrayList<>();

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
        public final File mohistplugin;
        public final File mojmap;
        public final File mc_unpacked;
        public final File mergedMapping;

        protected Install() throws Exception {
            super();
            this.fmlloader = new File(libPath + "net/minecraftforge/fmlloader/" + mcVer + "-" + forgeVer + "/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
            this.fmlcore = new File(libPath + "net/minecraftforge/fmlcore/" + mcVer + "-" + forgeVer + "/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
            this.javafmllanguage = new File(libPath + "net/minecraftforge/javafmllanguage/" + mcVer + "-" + forgeVer + "/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.mclanguage = new File(libPath + "net/minecraftforge/mclanguage/" + mcVer + "-" + forgeVer + "/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.lowcodelanguage = new File(libPath + "net/minecraftforge/lowcodelanguage/" + mcVer + "-" + forgeVer + "/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar");
            this.mohistplugin = new File(libPath + "com/mohistmc/mohistplugins/mohistplugins-" + mcVer + ".jar");
            this.mojmap = new File(otherStart + "-mappings.tsrg");
            this.mc_unpacked = new File(otherStart + "-unpacked.jar");
            this.mergedMapping = new File(mcpStart + "-mappings-merged.txt");
            install();
        }

        private void install() throws Exception {
            copyFileFromJar(lzma, "data/server.lzma");
            copyFileFromJar(fmlloader, "data/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(fmlcore, "data/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(javafmllanguage, "data/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(mclanguage, "data/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
            copyFileFromJar(lowcodelanguage, "data/lowcodelanguage-" + mcVer + "-" + forgeVer + ".jar");

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
                System.exit(0);
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

            if (isCorrupted(unpacked)) {
                unpacked.delete();
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

            if (!srg.exists()) {
                mute();
                run("net.minecraftforge.fart.Main",
                        new String[]{"--input", unpacked.getAbsolutePath(), "--output", srg.getAbsolutePath(), "--names", mergedMapping.getAbsolutePath(), "--ann-fix", "--ids-fix", "--src-fix", "--record-fix", "--strip-sigs"},
                        stringToUrl(loadedLibsPaths));
                unmute();
            }

            String storedServerMD5 = null;
            String storedMohistMD5 = null;
            String serverMD5 = MD5Util.get(serverJar);
            String mohistMD5 = MD5Util.get(universalJar);

            if (installInfo.exists()) {
                List<String> infoLines = Files.readAllLines(installInfo.toPath());
                if (!infoLines.isEmpty()) {
                    storedServerMD5 = infoLines.get(0);
                }
                if (infoLines.size() > 1) {
                    storedMohistMD5 = infoLines.get(1);
                }
            }

            if (!serverJar.exists() || storedServerMD5 == null || storedMohistMD5 == null || !storedServerMD5.equals(serverMD5) || !storedMohistMD5.equals(mohistMD5)) {
                mute();
                run("net.minecraftforge.binarypatcher.ConsoleTool",
                        new String[]{"--clean", srg.getAbsolutePath(), "--output", serverJar.getAbsolutePath(), "--apply", lzma.getAbsolutePath()},
                        stringToUrl(Arrays.asList(
                                libPath + "net/minecraftforge/binarypatcher/1.1.1/binarypatcher-1.1.1.jar",
                                libPath + "commons-io/commons-io/2.13.0/commons-io-2.13.0.jar",
                                libPath + "com/google/guava/guava/32.1.2-jre/guava-32.1.2-jre.jar",
                                libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar",
                                libPath + "com/github/jponge/lzma-java/1.3/lzma-java-1.3.jar",
                                libPath + "com/nothome/javaxdelta/2.0.1/javaxdelta-2.0.1.jar",
                                libPath + "com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar",
                                libPath + "org/checkerframework/checker-qual/2.0.0/checker-qual-2.0.0.jar",
                                libPath + "com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar",
                                libPath + "com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar",
                                libPath + "org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar",
                                libPath + "trove/trove/1.0.2/trove-1.0.2.jar"
                        )));
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
        }
    }
}
