package com.mohistmc;

import com.mohistmc.i18n.i18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

@Mod("mohist")
@OnlyIn(Dist.DEDICATED_SERVER)
public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER = LogManager.getLogger();

    public MohistMC() {
        String mohist_lang = MohistConfig.yml.getString("mohist.lang");
        String l = mohist_lang.split("_")[0];
        String c = mohist_lang.split("_")[1];
        new i18n().build(MohistMC.class.getClassLoader(), new Locale(l, c));
        //TODO: do something when mod loading
        LOGGER.info("Mohist loading.....");
    }
}