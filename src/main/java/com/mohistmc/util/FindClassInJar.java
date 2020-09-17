package com.mohistmc.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author Mgazul
 * @date 2019/12/15 21:42
 */
public class FindClassInJar {
    private String m_libDir;
    private String m_classname;

    public FindClassInJar(String libDir, String classname) {
        m_libDir = libDir;
        m_classname = classname;
    }

    class JarZipFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }

            String name = file.getName().toLowerCase();
            if (name.endsWith(".jar") || name.endsWith(".zip")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void process() throws IOException {
        checkDirectory(m_libDir);
    }

    public void checkDirectory(String libDir) throws IOException {
        File file = new File(libDir);
        if (file.exists()) {
            if (file.isFile()) {
                if (checkFile(file)) {
                    // TODO delete jar here?
                }
            } else {
                File[] files = file.listFiles(new JarZipFileFilter());

                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f.isDirectory()) {
                        checkDirectory(f.getAbsolutePath());
                    } else {
                        if (checkFile(f)) {
                            File newf = new File("delete/mods");
                            File qnewf = new File("delete", f.getPath());
                            if (!newf.exists()) {
                                newf.mkdirs();
                            } else {
                                if (qnewf.exists()) qnewf.delete();
                            }
                            Files.copy(f.toPath(), qnewf.toPath(), REPLACE_EXISTING);
                            f.delete();
                        }
                    }
                }
            }
        }
    }

    private boolean checkFile(File f) {
        boolean result = false;

        try {
            JarFile jarFile = new JarFile(f);
            Enumeration<JarEntry> enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry entry = enu.nextElement();
                String name = entry.getName();
                if (entry.isDirectory()) {
                    continue;
                }

                if (name.equals(m_classname)) {
                    result = true;
                    break;
                }
            }
            jarFile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
