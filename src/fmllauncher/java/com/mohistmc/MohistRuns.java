package com.mohistmc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MohistRuns {

    public static void runs(String[] args) throws Throwable {
        String path1 = String.format("libraries/net/minecraftforge/forge/" + MohistInstaller.getPath()  + "/forge-" + MohistInstaller.getPath() +"-server.jar");

        if (!Files.exists(Paths.get(path1))) {
            System.out.println("Please reload it");
        }
        else {
            Class.forName("net.minecraftforge.server.ServerMain").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
        }
    }

    public static void delete() throws IOException {
        String log1 = String.format("forge-" + MohistInstaller.getPath() + ".jar.log");
        String path2 = String.format("forge-" + MohistInstaller.getPath() + ".jar");
            Files.deleteIfExists(Paths.get(path2));
            Files.deleteIfExists(Paths.get(log1));
    }
}
