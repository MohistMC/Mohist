/*
 * MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.action;

import com.mohistmc.MohistMCStart;
import com.mohistmc.util.DataParser;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public abstract class Action {

    private static final PrintStream origin = System.out;
    public String mohistVer;
    public String forgeVer;
    public String mcpVer;
    public String mcVer;
    public String libPath = new File(JarTool.getJarDir(), "libraries").getAbsolutePath() + "/";

    public String forgeStart;
    public File universalJar;
    public File serverJar;

    public File lzma;
    public File installInfo;

    public String otherStart;
    public File extra;
    public File slim;
    public File srg;

    public String mcpStart;
    public File mcpZip;
    public File mcpTxt;

    public File minecraft_server;

    protected Action() {
        this.mohistVer = DataParser.versionMap.get("mohist");
        this.forgeVer = DataParser.versionMap.get("forge");
        this.mcpVer = DataParser.versionMap.get("mcp");
        this.mcVer = DataParser.versionMap.get("minecraft");

        this.forgeStart = libPath + "net/minecraftforge/forge/" + mcVer + "-" + forgeVer + "/forge-" + mcVer + "-" + forgeVer;
        this.universalJar = new File(forgeStart + "-universal.jar");
        this.serverJar = new File(forgeStart + "-server.jar");

        this.lzma = new File(libPath + "com/mohistmc/installation/data/server.lzma");
        this.installInfo = new File(libPath + "com/mohistmc/installation/installInfo");

        this.otherStart = libPath + "net/minecraft/server/" + mcVer + "-" + mcpVer + "/server-" + mcVer + "-" + mcpVer;

        this.extra = new File(otherStart + "-extra.jar");
        this.slim = new File(otherStart + "-slim.jar");
        this.srg = new File(otherStart + "-srg.jar");

        this.mcpStart = libPath + "de/oceanlabs/mcp/mcp_config/" + mcVer + "-" + mcpVer + "/mcp_config-" + mcVer + "-" + mcpVer;
        this.mcpZip = new File(mcpStart + ".zip");
        this.mcpTxt = new File(mcpStart + "-mappings.txt");

        this.minecraft_server = new File(libPath + "minecraft_server." + mcVer + ".jar");
    }

    protected void run(String mainClass, List<String> args, List<URL> classPath) throws Exception {
        System.out.println("EXECUTING CLASS " + mainClass);
        System.out.println(getParentClassloader() == null);
        try {
            Class.forName(mainClass);
            System.out.println("found class 2");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Class.forName(mainClass, true, new URLClassLoader(classPath.toArray(new URL[0]), getParentClassloader())).getDeclaredMethod("main", String[].class).invoke(null, (Object) args.toArray(new String[0]));
    }

    private ClassLoader getParentClassloader() {
        try {
            return (ClassLoader) ClassLoader.class.getDeclaredMethod("getPlatformClassLoader").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    protected List<URL> stringToUrl(List<String> strs) throws Exception {
        List<URL> temp = new ArrayList<>();
        for (String t : strs)
            temp.add(new File(t).toURI().toURL());
        return temp;
    }

    /*
    THIS IS TO NOT SPAM CONSOLE WHEN IT WILL PRINT A LOT OF THINGS
     */
    protected void mute() throws Exception {
        File out = new File(libPath + "com/mohistmc/installation/installationLogs.txt");
        if (!out.exists()) {
            out.getParentFile().mkdirs();
            out.createNewFile();
        }
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(out))));
    }

    protected void unmute() {
        System.setOut(origin);
    }

    protected void copyFileFromJar(File file, String pathInJar) {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar);
        if (!file.exists() || !MD5Util.getMd5(file).equals(MD5Util.getMd5(is)) || file.length() <= 1) {
            file.getParentFile().mkdirs();
            if (is != null) {
                try {
                    file.createNewFile();
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                }
            } else {
                System.out.println("[Mohist] The file " + file.getName() + " doesn't exists in the Mohist jar !");
                System.exit(0);
            }
        }
    }

    protected boolean isCorrupted(File f) {
        try {
            JarFile j = new JarFile(f);
            j.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

}
