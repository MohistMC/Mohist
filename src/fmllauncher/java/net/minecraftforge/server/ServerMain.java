/*
 * Minecraft Forge
 * Copyright (c) 2016-2022.
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

import com.mohistmc.MohistMCStart;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.i18n.i18n;
import cpw.mods.modlauncher.InvalidLauncherSetupException;
import cpw.mods.modlauncher.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static com.mohistmc.util.InstallUtils.universalJar;

public class ServerMain {

	public static ArrayList<String> mainArgs = null;
	public static float javaVersion = Float.parseFloat(System.getProperty("java.class.version"));

	public static void main(String[] args) throws Exception {
		mainArgs = new ArrayList<>(Arrays.asList(args));
		String path = JarTool.getJarPath();
		if(path != null && (path.contains("+") || path.contains("!"))) {
			System.out.println("[Mohist - ERROR] Unsupported characters have been detected in your server path. \nPlease remove + or ! in your server's folder name (in the folder which contains this character).\nPath : " + path);
			System.exit(0);
		}

		// Mohist start - Download Java 11 if required
		if(MohistConfigUtil.bMohist("use_custom_java11", "false")) {
			try {
				DownloadJava.run(); // Mohist - Invoke DownloadJava
			} catch (Exception ex) {
				System.err.println(i18n.get("oldjava.exception"));
				ex.printStackTrace();
				System.exit(1);
			}
		}

		/*
		59.0 -> Java 15
		53.0 -> Java 9
		 */
		if(javaVersion >= 53.0) {
			System.setProperty((javaVersion >= 59.0 ? "-D" : "")+"nashorn.args", "--no-deprecation-warning");
		}

		try {
			MohistMCStart.main();
			// Mohist end
			Class.forName("cpw.mods.modlauncher.Launcher", false, ClassLoader.getSystemClassLoader());
			Class.forName("net.minecraftforge.eventbus.EventBus", false, ClassLoader.getSystemClassLoader());
		} catch (Exception cnfe) {
			System.err.println(i18n.get("mohist.start.server.error"));
			cnfe.printStackTrace();
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
}
