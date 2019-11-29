package net.minecraft.launchwrapper.injector;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class AlphaVanillaTweakInjector implements IClassTransformer {
    public AlphaVanillaTweakInjector() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        return bytes;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz;

        try {
            clazz = getaClass("net.minecraft.client.MinecraftApplet");
        } catch (ClassNotFoundException ignored) {
            clazz = getaClass("com.mojang.minecraft.MinecraftApplet");
        }

        System.out.println("AlphaVanillaTweakInjector.class.getClassLoader() = " + AlphaVanillaTweakInjector.class.getClassLoader());
        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            String name = field.getType().getName();

            if (!name.contains("awt") && !name.contains("java") && !name.equals("long")) {
                System.out.println("Found likely Minecraft candidate: " + field);

                Field fileField = getWorkingDirField(name);
                if (fileField != null) {
                    System.out.println("Found File, changing to " + Launch.minecraftHome);
                    fileField.setAccessible(true);
                    fileField.set(null, Launch.minecraftHome);
                    break;
                }
            }
        }

        startMinecraft((Applet) object, args);
    }

    private static void startMinecraft(final Applet applet, String[] args) {
        final Map<String, String> params = new HashMap<String, String>();

        // Extract params
        String name = "Player" + System.currentTimeMillis() % 1000;
        if (args.length > 0) name = args[0];

        String sessionId = "-";
        if (args.length > 1) sessionId = args[1];

        params.put("username", name);
        params.put("sessionid", sessionId);

        final Frame launcherFrameFake = new Frame();
        launcherFrameFake.setTitle("Minecraft");
        launcherFrameFake.setBackground(Color.BLACK);

        final JPanel panel = new JPanel();
        launcherFrameFake.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(854, 480));
        launcherFrameFake.add(panel, BorderLayout.CENTER);
        launcherFrameFake.pack();

        launcherFrameFake.setLocationRelativeTo(null);
        launcherFrameFake.setVisible(true);

        launcherFrameFake.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });


        class LauncherFake extends Applet implements AppletStub {
            private static final long serialVersionUID = 1L;

            public void appletResize(int width, int height) {
                // Actually empty as well
            }

            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public URL getDocumentBase() {
                try {
                    return new URL("http://www.minecraft.net/game/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public URL getCodeBase() {
                try {
                    return new URL("http://www.minecraft.net/game/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getParameter(String paramName) {
                if (params.containsKey(paramName)) {
                    return params.get(paramName);
                }
                System.err.println("Client asked for parameter: " + paramName);
                return null;
            }
        }

        final LauncherFake fakeLauncher = new LauncherFake();
        applet.setStub(fakeLauncher);

        fakeLauncher.setLayout(new BorderLayout());
        fakeLauncher.add(applet, BorderLayout.CENTER);
        fakeLauncher.validate();

        launcherFrameFake.removeAll();
        launcherFrameFake.setLayout(new BorderLayout());
        launcherFrameFake.add(fakeLauncher, BorderLayout.CENTER);
        launcherFrameFake.validate();

        applet.init();
        applet.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                applet.stop();
            }
        });
    }

    private static Class<?> getaClass(String name) throws ClassNotFoundException {
        return Launch.classLoader.findClass(name);
    }

    private static Field getWorkingDirField(String name) throws ClassNotFoundException {
        Class<?> clazz = getaClass(name);

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().getName().equals("java.io.File")) {
                return field;
            }
        }

        return null;
    }
}
