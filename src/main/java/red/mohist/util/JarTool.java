package red.mohist.util;

import java.io.File;

/**
 * @Author Mgazul
 * @create 2019/10/12 20:26
 */
public class JarTool {

    public static String getJarPath(){
        File file = getFile();
        if(file==null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static String getJarDir() {
        File file = getFile();
        if(file==null) {
            return null;
        }
        return getFile().getParent();
    }

    public static String getJarName() {
        File file = getFile();
        if(file==null) {
            return null;
        }
        return getFile().getName();
    }

    private static File getFile() {

        String path = JarTool.class.getProtectionDomain().getCodeSource()
                .getLocation().getFile();
        try{
            path = java.net.URLDecoder.decode(path, "UTF-8");
        }catch (java.io.UnsupportedEncodingException e){
            return null;
        }
        return new File(path);
    }
}
