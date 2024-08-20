/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

package com.mohistmc.libraries;

import com.mohistmc.MohistMCStart;
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.tools.FileUtils;
import com.mohistmc.tools.MD5Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.ToString;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

@ToString
public class LibrariesDownloadQueue {

    @ToString.Exclude
    public final Set<Libraries> allLibraries = new HashSet<>();
    @ToString.Exclude
    private final Set<Libraries> fail = new HashSet<>();
    @ToString.Exclude
    public InputStream inputStream = null;
    @ToString.Exclude
    public Set<Libraries> need_download = new LinkedHashSet<>();
    @ToString.Exclude
    public Set<Libraries> installer = new LinkedHashSet<>();
    public List<URL> installerTourls = new ArrayList<>();

    public String parentDirectory = "libraries";
    public String systemProperty = null;
    public boolean debug = false;


    public static LibrariesDownloadQueue create() {
        return new LibrariesDownloadQueue();
    }

    /**
     * Set the input stream for the list that needs to be downloaded
     *
     * @param inputStream The input stream of the target file
     * @return Returns the current real column
     */
    public LibrariesDownloadQueue inputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    /**
     * Set the file download directory
     *
     * @param parentDirectory The path to which the file is downloaded
     * @return Returns the current real column
     */
    public LibrariesDownloadQueue parentDirectory(String parentDirectory) {
        this.parentDirectory = parentDirectory;
        return this;
    }

    /**
     * Construct the final column
     *
     * @return Construct the final column
     */
    public LibrariesDownloadQueue build() {
        init();
        return this;
    }

    /**
     * Download in the form of a progress bar
     */
    public void progressBar() {
        if (needDownload()) {
            ProgressBarBuilder builder = new ProgressBarBuilder().setTaskName("")
                    .setStyle(ProgressBarStyle.ASCII)
                    .setUpdateIntervalMillis(100)
                    .setInitialMax(need_download.size());
            try (ProgressBar pb = builder.build()) {
                for (Libraries lib : need_download) {
                    File file = new File(parentDirectory, lib.path);
                    file.getParentFile().mkdirs();
                    String url = "META-INF/" + file.getPath();
                    if (copyFileFromJar(file, url.replaceAll("\\\\", "/"), lib)) {
                        debug("downloadFile: OK");
                        fail.remove(lib);
                    } else {
                        debug("downloadFile: No " + url);
                        fail.add(lib);
                    }
                    pb.step();
                }
            }
        }
        if (!fail.isEmpty()) {
            progressBar();
        }
    }

    protected boolean copyFileFromJar(File file, String pathInJar, Libraries lib) {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar);

        if (!file.exists() || !MD5Util.get(file).equals(lib.getMd5()) || file.length() <= 1) {
            file.getParentFile().mkdirs();
            if (is != null) {
                try {
                    file.createNewFile();
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    return true;
                } catch (IOException ignored) {
                }
            } else {
                System.out.println("[Mohist] The file " + file.getPath()+ " doesn't exists in the Mohist jar !");
                return false;
            }
        }
        return true;
    }

    public boolean needDownload() {
        for (Libraries libraries : allLibraries) {
            File lib = new File(parentDirectory, libraries.path);
            if (lib.exists() && Objects.equals(MD5Util.get(lib), libraries.md5)) {
                continue;
            }
            need_download.add(libraries);
        }
        return !need_download.isEmpty();
    }

    private void init() {
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));
            for (String line = b.readLine(); line != null; line = b.readLine()) {
                Libraries libraries = Libraries.from(line);
                allLibraries.add(libraries);
                if (libraries.isInstaller()) {
                    File file = new File(parentDirectory, libraries.getPath());
                    URL url = file.toURI().toURL();
                    installer.add(libraries);
                    installerTourls.add(url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void debug(String log) {
        if (debug) System.out.println(log + "\n");
    }
}
