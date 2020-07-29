package red.mohist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * @author Mgazul
 * @date 2020/5/31 14:38
 */
public class Mohist {

	public static void main(String[] args) {
        try {
            Class.forName("cpw.mods.modlauncher.Launcher", false, ClassLoader.getSystemClassLoader());
            Class.forName("net.minecraftforge.forgespi.Environment", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("FATAL ERROR, You need to run the installer. The libraries required to launch a server are missing");
            System.exit(1);
        }

        final String launchArgs = Optional.ofNullable(Mohist.class.getProtectionDomain()).
                map(ProtectionDomain::getCodeSource).
                map(CodeSource::getLocation).
                map(Mohist::urlToManifest).
                map(Manifest::getMainAttributes).
                map(a -> a.getValue("ServerLaunchArgs")).
                orElseThrow(Mohist::throwMissingManifest);
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
	
	public static String getVersion() {
        return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "0.1";
    }

}

