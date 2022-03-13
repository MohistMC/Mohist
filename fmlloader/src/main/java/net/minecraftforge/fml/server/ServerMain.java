/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.server;

import com.mohistmc.util.i18n.i18n;
import cpw.mods.modlauncher.InvalidLauncherSetupException;
import cpw.mods.modlauncher.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Class.forName("cpw.mods.modlauncher.Launcher", false, ClassLoader.getSystemClassLoader());
            Class.forName("net.minecraftforge.forgespi.Environment", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(i18n.get("servermain.1"));
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
                System.err.println(i18n.get("servermain.2"));
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
        System.err.println(i18n.get("servermain.3"));
        return new RuntimeException("Missing the manifest");
    }
}
