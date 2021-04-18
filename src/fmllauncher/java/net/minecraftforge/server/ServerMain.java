/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.server;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.mohistmc.MohistMCStart;
import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.i18n.i18n;

import cpw.mods.modlauncher.InvalidLauncherSetupException;
import cpw.mods.modlauncher.Launcher;

public class ServerMain {

    public static int mohistLibsChanged; // Mohist - Restart the server if libraries were changed

    public static void main(String[] args) {
        // Mohist start - Download Java 11 if required
        if (Float.parseFloat(System.getProperty("java.class.version")) < 55f) {
            if (!DownloadJava.javabin.exists()) System.err.println(i18n.get("oldjava.notify"));
            try {
                DownloadJava.run(args); // Mohist - Invoke DownloadJava with actual launchargs
            } catch (Exception ex) {
                System.err.println(i18n.get("oldjava.exception"));
                ex.printStackTrace();
                System.exit(1);
            }
        }
        // Mohist end

        try {
            MohistMCStart.main();
            // Mohist start - Restart the server if libraries were changed
            if (mohistLibsChanged > 0) {
                ArrayList<String> cmd = new ArrayList<>();
                Method current = Class.forName("java.lang.ProcessHandle").getMethod("current");
                Method info = Class.forName("java.lang.ProcessHandle").getMethod("info");
                Method command = Class.forName("java.lang.ProcessHandle$Info").getMethod("command");
                Optional<String> java = (Optional<String>) command.invoke(info.invoke(current.invoke(null)));
                if (java.isPresent()) {
                    cmd.add(java.get());
                    cmd.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
                    cmd.add("-jar");
                    cmd.add(new File(MohistMCStart.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName());
                    for (String arg : args) cmd.add(arg);
                    System.out.println(i18n.get("libraries.restart", mohistLibsChanged));
                    UpdateUtils.restartServer(cmd, true);
                }
            }
            // Mohist end
            Class.forName("cpw.mods.modlauncher.Launcher", false, ClassLoader.getSystemClassLoader());
            Class.forName("net.minecraftforge.forgespi.Environment", false, ClassLoader.getSystemClassLoader());
        } catch (Exception cnfe) {
            System.err.println(i18n.get("mohist.start.server.error"));
            System.exit(1);
        }

        final String launchArgs = Optional.ofNullable(ServerMain.class.getProtectionDomain()).
                map(ProtectionDomain::getCodeSource).
                map(CodeSource::getLocation).
                map(ServerMain::urlToManifest).
                map(Manifest::getMainAttributes).
                map(a -> a.getValue("ServerLaunchArgs")).
                orElseThrow(ServerMain::throwMissingManifest);
        String[] defaultargs = launchArgs.split(" ");
        String[] result = new String[args.length + defaultargs.length];
        System.arraycopy(defaultargs, 0, result, 0, defaultargs.length);
        System.arraycopy(args, 0, result, defaultargs.length, args.length);
        // separate class, so the exception can resolve
        new Runner().runLauncher(result);
    }

    private static class Runner {
        private void runLauncher(final String[] result) {
            try {
                Launcher.main(result);
            } catch (InvalidLauncherSetupException e) {
                System.err.println("The server is missing critical libraries and cannot load. Please run the installer to correct this");
                System.exit(1);
            }
        }
    }
    private static Manifest urlToManifest(URL url) {
        try {
            return new JarFile(new File(url.toURI())).getManifest();
        } catch (URISyntaxException | IOException e) {
            return null;
        }
    }

    private static RuntimeException throwMissingManifest() {
        System.err.println("This is not being run from a valid JAR file, essential data is missing.");
        return new RuntimeException("Missing the manifest");
    }
}
