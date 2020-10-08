package com.mohistmc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MohistInstaller {

    public static void main(String[] args) throws Throwable {

        System.out.println("\n" + "\n" +
                " __    __   ______   __  __   __   ______   ______  \n" +
                "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
                "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
                " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
                "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
                "                                                    \n" + "\n");
        afterSetup();
        MohistRuns.runs(args);
        MohistRuns.delete();
    }

    protected static void afterSetup() throws Throwable {
        Path path = Paths.get(String.format("libraries/net/minecraftforge/forge/" + MohistInstaller.getPath()  + "/forge-" + MohistInstaller.getPath() +"-server.jar"));
        if (!Files.exists(path)) {
            ExecutorService pool = Executors.newFixedThreadPool(8);
            if (!Files.exists(path)) {
                ProcessBuilder builder = new ProcessBuilder();
                builder.command("java", "-jar", String.format("forge-1.16.3-" + MohistInstaller.class.getPackage().getSpecificationVersion() + "-installer.jar"), "--installServer", ".");
                builder.inheritIO();
                Process process = builder.start();
                process.waitFor();
            }
            pool.shutdownNow();
        }
    }

    public static String getPath() {
        return "1.16.3-" + MohistInstaller.class.getPackage().getSpecificationVersion();
    }
}

