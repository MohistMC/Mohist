package com.mohistmc;

import com.mohistmc.eventhandler.EventDispatcherRegistry;
import com.mohistmc.i18n.i18n;
import com.mohistmc.plugins.MohistProxySelector;
import com.mohistmc.util.VersionInfo;
import java.net.ProxySelector;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;

@Mod("mohist")
@OnlyIn(Dist.DEDICATED_SERVER)
public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER = LogManager.getLogger();
    public static i18n i18n;
    public static String version = "1.20.1";
    public static String modid = "mohist";
    public static ClassLoader classLoader;
    public static VersionInfo versionInfo;

    public MohistMC() {
        classLoader = MohistMC.class.getClassLoader();

        //TODO: do something when mod loading
        LOGGER.info("Mohist mod loading.....");
        EventDispatcherRegistry.init();
        ProxySelector.setDefault(new MohistProxySelector(ProxySelector.getDefault()));
    }

    public static void initVersion() {
        String mohist_lang = MohistConfig.yml.getString("mohist.lang", Locale.getDefault().toString());
        i18n = new i18n(MohistMC.class.getClassLoader(), mohist_lang);

        Map<String, String> arguments = new HashMap<>();
        String[] cbs = CraftServer.class.getPackage().getImplementationVersion().split("-");
        arguments.put("mohist", (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : version);
        arguments.put("bukkit", cbs[0]);
        arguments.put("craftbukkit", cbs[1]);
        arguments.put("spigot", cbs[2]);
        arguments.put("neoforge", cbs[3]);
        arguments.put("forge", ForgeVersion.getVersion());
        versionInfo = new VersionInfo(arguments);
    }
}