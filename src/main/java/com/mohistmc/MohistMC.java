package com.mohistmc;

import com.mohistmc.eventhandler.EventDispatcherRegistry;
import com.mohistmc.i18n.i18n;
import com.mohistmc.plugins.MohistProxySelector;
import com.mohistmc.util.VersionInfo;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;

@Mod("mohist")
@OnlyIn(Dist.DEDICATED_SERVER)
public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER = LogManager.getLogger();
    public static i18n i18n;
    public static String version = "1.20.2";
    public static String modid = "mohist";
    public static ClassLoader classLoader;
    public static VersionInfo versionInfo;

    public MohistMC() {
        classLoader = MohistMC.class.getClassLoader();
        i18n = new i18n(MohistMC.class.getClassLoader(), MohistConfig.mohist_lang());

        //TODO: do something when mod loading
        LOGGER.info("Mohist mod loading.....");
        EventDispatcherRegistry.init();
        MohistProxySelector.init();
    }

    public static void initVersion() {
        Map<String, String> arguments = new HashMap<>();
        String[] cbs = CraftServer.class.getPackage().getImplementationVersion().split("-");
        arguments.put("mohist", (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : version);
        arguments.put("bukkit", cbs[0]);
        arguments.put("craftbukkit", cbs[1]);
        arguments.put("spigot", cbs[2]);
        arguments.put("forge", cbs[3]);
        versionInfo = new VersionInfo(arguments);
    }
}