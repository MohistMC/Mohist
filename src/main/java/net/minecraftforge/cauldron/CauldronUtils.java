package net.minecraftforge.cauldron;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class CauldronUtils {
    private static boolean deobfuscated = false;

    public static boolean isOverridingUpdateEntity(Class<? extends TileEntity> c) 
    {
        Class clazz = null;
        String method = deobfuscatedEnvironment() ? "updateEntity" : "func_145845_h"; // updateEntity
        try 
        {
            clazz = c.getMethod(method).getDeclaringClass();
        }
        catch (Throwable e)
        {
            //e.printStackTrace(); no need for spam
        }

        return clazz != TileEntity.class;
    }

    public static boolean canTileEntityUpdate(Class<? extends TileEntity> c)
    {
        boolean canUpdate = false;
        try 
        {
            Constructor<? extends TileEntity> ctor = c.getConstructor();
            TileEntity te = ctor.newInstance();
            canUpdate = te.canUpdate();
        } 
        catch (Throwable e) 
        {
            // ignore
        }
        return canUpdate;
    }

    public static <T> void dumpAndSortClassList(List<Class<? extends T>> classList)
    {
        List<String> sortedClassList = new ArrayList<String>();
        for (Class clazz : classList)
        {
            sortedClassList.add(clazz.getName());
        }
        Collections.sort(sortedClassList);
        if (MinecraftServer.tileEntityConfig.enableTECanUpdateWarning.getValue())
        {
            for (String aSortedClassList : sortedClassList) {
                MinecraftServer.getServer().logInfo("Detected TE " + aSortedClassList + " with canUpdate set to true and no updateEntity override!. This is NOT good, please report to mod author as this can hurt performance.");
            }
        }
    }

    public static boolean migrateWorlds(String worldType, String oldWorldContainer, String newWorldContainer, String worldName)
    {
        boolean result = true;
        File newWorld = new File(new File(newWorldContainer), worldName);
        File oldWorld = new File(new File(oldWorldContainer), worldName);

        if ((!newWorld.isDirectory()) && (oldWorld.isDirectory()))
        {
            MinecraftServer.getServer().logInfo("---- Migration of old " + worldType + " folder required ----");
            MinecraftServer.getServer().logInfo("Cauldron has moved back to using the Forge World structure, your " + worldType + " folder will be moved to a new location in order to operate correctly.");
            MinecraftServer.getServer().logInfo("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Cauldron in the future.");
            MinecraftServer.getServer().logInfo("Attempting to move " + oldWorld + " to " + newWorld + "...");

            if (newWorld.exists())
            {
                MinecraftServer.getServer().logSevere("A file or folder already exists at " + newWorld + "!");
                MinecraftServer.getServer().logInfo("---- Migration of old " + worldType + " folder failed ----");
                result = false;
            }
            else if (newWorld.getParentFile().mkdirs() || newWorld.getParentFile().exists())
            {
                MinecraftServer.getServer().logInfo("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);

                // Migrate world data
                try
                {
                    com.google.common.io.Files.move(oldWorld, newWorld);
                }
                catch (IOException exception)
                {
                    MinecraftServer.getServer().logSevere("Unable to move world data.");
                    exception.printStackTrace();
                    result = false;
                }
                try
                {
                    com.google.common.io.Files.copy(new File(oldWorld.getParent(), "level.dat"), new File(newWorld, "level.dat"));
                }
                catch (IOException exception)
                {
                    MinecraftServer.getServer().logSevere("Unable to migrate world level.dat.");
                }

                MinecraftServer.getServer().logInfo("---- Migration of old " + worldType + " folder complete ----");
            }
            else result = false;
        }
        return result;
    }

    public static InventoryHolder getOwner(TileEntity tileentity)
    {
        org.bukkit.block.BlockState state = tileentity.worldObj.getWorld().getBlockAt(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord).getState();

        if (state instanceof InventoryHolder)
        {
            return (InventoryHolder) state;
        }

        return null;
    }

    public static boolean deobfuscatedEnvironment()
    {
        try
        {
            // Are we in a 'decompiled' environment?
            byte[] bs = ((net.minecraft.launchwrapper.LaunchClassLoader)CauldronUtils.class.getClassLoader()).getClassBytes("net.minecraft.world.World");
            if (bs != null)
            {
                //FMLRelaunchLog.info("Managed to load a deobfuscated Minecraft name- we are in a deobfuscated environment. Skipping runtime deobfuscation");
                deobfuscated = true;
            }
        }
        catch (IOException e1)
        {
        }
        return deobfuscated;
    }
}
