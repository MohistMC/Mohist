package red.mohist.util;

import red.mohist.util.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class ClassJarUtil {

    public static void checkFiles(String libDir, String classname, boolean implementation) throws IOException {
        File[] files = new File(libDir).listFiles((dir, name) -> name.endsWith(".jar"));
        for (File file : files) {
            JarFile f = new JarFile(file);
            if(f.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null) {
                f.close();
                file.delete();
                if(!implementation)
                    System.out.println(Message.getFormatString("update.deleting", new Object[]{file.getName(), libDir}));
                else
                    System.out.println(Message.getFormatString("update.implementation", new Object[]{file.getName()}));
                break;
            }
        }
    }
}
