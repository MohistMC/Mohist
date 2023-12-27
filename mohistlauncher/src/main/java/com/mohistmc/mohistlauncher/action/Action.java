/*
 * MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.mohistlauncher.action;

import com.mohistmc.mohistlauncher.Main;
import com.mohistmc.mohistlauncher.libraries.DefaultLibraries;
import com.mohistmc.mohistlauncher.util.DataParser;
import com.mohistmc.tools.FileUtils;
import com.mohistmc.tools.MD5Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.jar.JarFile;

public abstract class Action {

    private static final PrintStream origin = System.out;
    public final String mohistVer;
    public final String forgeVer;
    public final String mcpVer;
    public final String mcVer;
    public final String libPath;

    public final String forgeStart;
    public final File universalJar;
    public final File serverJar;

    public final File lzma;
    public final File installInfo;

    public final String otherStart;
    public final File unpacked;
    public final File srg;

    public final String mcpStart;
    public final File mcpZip;
    public final File mcpTsrg;

    public final File bundled;

    protected Action() {
        this.mohistVer = DataParser.versionMap.get("mohist");
        this.forgeVer = DataParser.versionMap.get("forge");
        this.mcpVer = DataParser.versionMap.get("mcp");
        this.mcVer = DataParser.versionMap.get("minecraft");
        this.libPath = new File("libraries").getAbsolutePath() + "/";

        this.forgeStart = libPath + "net/minecraftforge/forge/" + mcVer + "-" + forgeVer + "/forge-" + mcVer + "-" + forgeVer;
        this.universalJar = new File(forgeStart + "-universal.jar");
        this.serverJar = new File(forgeStart + "-server.jar");

        this.lzma = new File(libPath + "com/mohistmc/installation/data/server.lzma");
        this.installInfo = new File(libPath + "com/mohistmc/installation/installInfo");

        this.otherStart = libPath + "net/minecraft/server/" + mcVer + "-" + mcpVer + "/server-" + mcVer + "-" + mcpVer;

        this.unpacked = new File(libPath + "net/minecraft/server/" + mcVer + "/server-" + mcVer + "-unpacked.jar");
        this.srg = new File(otherStart + "-srg.jar");

        this.mcpStart = libPath + "de/oceanlabs/mcp/mcp_config/" + mcVer + "-" + mcpVer + "/mcp_config-" + mcVer + "-" + mcpVer;
        this.mcpZip = new File(mcpStart + ".zip");
        this.mcpTsrg = new File(mcpStart + "-mappings.tsrg");

        this.bundled = new File(libPath + "net/minecraft/server/" + mcVer + "/server-" + mcVer + "-bundled.jar");
    }

    protected void run(String mainClass, String... args) throws Exception {
        List<URL> classPath = DefaultLibraries.installer;
        URLClassLoader loader = URLClassLoader.newInstance(classPath.toArray(new URL[classPath.size()]));
        Class.forName(mainClass, true, loader).getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
        loader.clearAssertionStatus();
        loader.close();
    }

    protected void copyFileFromJar(File file, String pathInJar) {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(pathInJar);
        if (!file.exists() || !MD5Util.get(file).equals(MD5Util.get(is)) || file.length() <= 1) {
            // Clear old version
            File parentfile = file.getParentFile();
            if (file.getAbsolutePath().contains("minecraftforge")) {
                int lastSlashIndex = parentfile.getAbsolutePath().replaceAll("\\\\", "/").lastIndexOf("/");
                String result = parentfile.getAbsolutePath().substring(0, lastSlashIndex + 1);
                File old = new File(result);
                if (old.exists()) {
                    FileUtils.deleteFolders(old);
                }
            }
            file.getParentFile().mkdirs();
            if (is != null) {
                try {
                    file.createNewFile();
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ignored) {
                }
            } else {
                System.out.println("[Mohist] The file " + file.getName() + " doesn't exists in the Mohist jar !");
                System.exit(0);
            }
        }
    }

    public boolean checkDependencies() throws IOException {
        if (installInfo.exists()) {
            if (!serverJar.exists()) {
                return true;
            }
            return !MD5Util.get(serverJar).equals(Files.readString(installInfo.toPath()));
        }
        return true;
    }

}
