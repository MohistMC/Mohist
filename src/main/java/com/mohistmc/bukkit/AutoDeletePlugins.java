package com.mohistmc.bukkit;

import com.mohistmc.util.FindClassInJar;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

  public static final List<String> classlist;

  static {
    classlist = Arrays.asList("test.test");
  }

  public static void jar() throws Exception {
    String libDir = "plugins";
    for (String classname : AutoDeletePlugins.classlist) {
      classname = classname.replaceAll("\\.", "/") + ".class";

      FindClassInJar ins = new FindClassInJar(libDir, classname);
      ins.checkDirectory(libDir);
    }
  }
}
