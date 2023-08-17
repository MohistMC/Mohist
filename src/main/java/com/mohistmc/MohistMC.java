package com.mohistmc;

import com.mohistmc.eventhandler.EventDispatcherRegistry;
import com.mohistmc.i18n.i18n;
import com.mohistmc.plugins.MohistProxySelector;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ProxySelector;
import java.util.Locale;

@Mod("mohist")
@OnlyIn(Dist.DEDICATED_SERVER)
public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER = LogManager.getLogger();
    public static i18n i18n;
    public static String version;
    public static String modid = "mohist";
    public static ClassLoader classLoader;

    public MohistMC() {
        classLoader = MohistMC.class.getClassLoader();
        String mohist_lang = MohistConfig.yml.getString("mohist.lang", "xx_XX");
        String l = mohist_lang.split("_")[0];
        String c = mohist_lang.split("_")[1];
        i18n = new i18n(MohistMC.class.getClassLoader(), new Locale(l, c));
        //TODO: do something when mod loading
        LOGGER.info("Mohist mod loading.....");
        version = (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : "1.20.1";
        EventDispatcherRegistry.init();
        ProxySelector.setDefault(new MohistProxySelector(ProxySelector.getDefault()));
    }
}