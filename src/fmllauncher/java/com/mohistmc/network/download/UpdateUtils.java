package com.mohistmc.network.download;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mohistmc.MohistMCStart;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.i18n;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mohistmc.config.MohistConfigUtil.bMohist;
import static com.mohistmc.network.download.NetworkUtil.getConn;
import static com.mohistmc.network.download.NetworkUtil.getInput;

public class UpdateUtils {

	private static int percentage = 0;

	public static void versionCheck() {
		System.out.println(i18n.get("update.check"));
		System.out.println(i18n.get("update.stopcheck"));

		try {
			JsonElement root = new JsonParser().parse(new InputStreamReader(getInput("https://ci.codemc.io/job/MohistMC/job/Mohist-1.16.5/lastSuccessfulBuild/api/json")));

			String jar_sha = MohistMCStart.getVersion();
			String build_number = "1.16.5-" + root.getAsJsonObject().get("number").toString();
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(root.getAsJsonObject().get("timestamp").toString())));

			if(jar_sha.equals(build_number))
				System.out.println(i18n.get("update.latest", "1.0", jar_sha, build_number));
			else {
				System.out.println(i18n.get("update.detect", build_number, jar_sha, time));
				if(bMohist("check_update_auto_download", "false")) {
					downloadFile("https://ci.codemc.io/job/MohistMC/job/Mohist-1.16.5/lastSuccessfulBuild/artifact/projects/mohist/build/libs/mohist-" + build_number + "-server.jar", JarTool.getFile());
					restartServer(new ArrayList<>(Arrays.asList("java", "-jar", JarTool.getJarName())), true);
				}
			}
		} catch (Throwable e) {
			System.out.println(i18n.get("check.update.noci"));
		}
	}

	public static void downloadFile(String URL, File f) throws Exception {
		downloadFile(URL, f, null);
	}

	public static void downloadFile(String URL, File f, String md5) throws Exception {
		URLConnection conn = getConn(URL);
		System.out.println(i18n.get("download.file", f.getName(), getSize(conn.getContentLength())));
		ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
		FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		int fS = conn.getContentLength();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if(rbc.isOpen()) {
					if(percentage != Math.round((float) f.length() / fS * 100) && percentage < 100)
						System.out.println(i18n.get("file.download.percentage", f.getName(), percentage));
					percentage = Math.round((float) f.length() / fS * 100);
				} else t.cancel();
			}
		}, 3000, 1000);
		fc.transferFrom(rbc, 0, Long.MAX_VALUE);
		fc.close();
		rbc.close();
		percentage = 0;
		String MD5 = MD5Util.getMd5(f);
		if(md5 != null && MD5 != null && !MD5.equals(md5.toLowerCase())) {
			f.delete();
			System.out.println(i18n.get("file.download.nook.md5", URL, MD5, md5.toLowerCase()));
			throw new Exception("md5");
		}
		System.out.println(i18n.get("download.file.ok", f.getName()));
	}

	public static void restartServer(ArrayList<String> cmd, boolean shutdown) throws Exception {
		System.out.println(i18n.get("jarfile.restart"));
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
		processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		Process process = processBuilder.start();
		process.waitFor();
		Thread.sleep(2000);
		if(shutdown) System.exit(0);
	}

	public static String getSize(long size) {
		return (size >= 1048576L) ? (float) size / 1048576.0F + "MB" : ((size >= 1024) ? (float) size / 1024.0F + " KB" : size + " B");
	}

	public static long getSizeOfDirectory(File path) throws IOException {
		return Files.walk(path.toPath()).parallel()
				.map(Path::toFile)
				.filter(File::isFile)
				.mapToLong(File::length)
				.sum();
	}
}
