/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
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

package net.minecraftforge.fml.relauncher;

import java.lang.reflect.Method;

import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import red.mohist.Mohist;
import red.mohist.util.i18n.Message;

public class ServerLaunchWrapper {

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new ServerLaunchWrapper().run(args);
    }

    private ServerLaunchWrapper()
    {

    }

    private void run(String[] args)
    {
        if (System.getProperty("log4j.configurationFile") == null)
        {
            // Set this early so we don't need to reconfigure later
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }
        Class<?> launchwrapper = null;
        try {
            launchwrapper = Class.forName("net.minecraft.launchwrapper.Launch", true, Mohist.class.getClassLoader());
            Class.forName("org.objectweb.asm.Type", true, Mohist.class.getClassLoader());
            System.out.println("");
            System.out.println("                   __                     __      ");
            System.out.println(" /'\\_/`\\          /\\ \\       __          /\\ \\__   ");
            System.out.println("/\\      \\     ___ \\ \\ \\___  /\\_\\     ____\\ \\ ,_\\  ");
            System.out.println("\\ \\ \\__\\ \\   / __`\\\\ \\  _ `\\\\/\\ \\   /',__\\\\ \\ \\/  ");
            System.out.println(" \\ \\ \\_/\\ \\ /\\ \\L\\ \\\\ \\ \\ \\ \\\\ \\ \\ /\\__, `\\\\ \\ \\_ ");
            System.out.println("  \\ \\_\\\\ \\_\\\\ \\____/ \\ \\_\\ \\_\\\\ \\_\\\\/\\____/ \\ \\__\\");
            System.out.println("   \\/_/ \\/_/ \\/___/   \\/_/\\/_/ \\/_/ \\/___/   \\/__/");
            System.out.println("");
            System.out.println("");
            System.out.println("                        " + Message.getString("forge.serverlanunchwrapper.1"));
            System.out.println(Message.getString("mohist.start"));
            System.out.println(Message.getString("load.libraries"));
            Mohist.LOGGER = LogManager.getLogger("Mohist");
        } catch (Exception e) {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            e.printStackTrace(System.err);
            System.exit(1);
        }

        try {
            Method main = launchwrapper != null ? launchwrapper.getMethod("main", String[].class) : null;
            String[] allArgs = new String[args.length + 2];
            allArgs[0] = "--tweakClass";
            allArgs[1] = "net.minecraftforge.fml.common.launcher.FMLServerTweaker";
            System.arraycopy(args, 0, allArgs, 2, args.length);
            Objects.requireNonNull(main).invoke(null, (Object) allArgs);
        } catch (Exception e) {
            System.out.println(Message.getString("mohist.start.error"));
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}