package com.mohistmc.forge.addon;

import com.google.common.eventbus.EventBus;
import com.mohistmc.MohistMC;
import com.mohistmc.forge.CustomMod;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModMetadata;

public class MohistMod extends DummyModContainer implements CustomMod {

    public MohistMod(InputStream inputStream) {
        super(MetadataCollection.from(inputStream, "Mohist").getMetadataForId("mohist", null));
        is.add(modinfo());
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    @Override
    public Disableable canBeDisabled() {
        return Disableable.YES;
    }

    public static InputStream modinfo() {
        String info = "[\n" +
                "{\n" +
                "  \"modid\": \"mohist\",\n" +
                "  \"name\": \"Mohist\",\n" +
                "  \"description\": \"Mohist built-in mark.\",\n" +
                "  \"version\": \"" + MohistMC.getVersion() + "\",\n" +
                "  \"mcversion\": \"1.12.2\",\n" +
                "  \"logoFile\": \"/mohist_logo.png\",\n" +
                "  \"url\": \"http://mohistmc.com/\",\n" +
                "  \"updateUrl\": \"\",\n" +
                "  \"authors\": [\"Mgazul\", \"CraftDream\", \"azbh111\", \"lliioollcn\", \"terrainwax\", \"lvyitian\", \"ChenHauShen\", \"Technetium\", \"Shawiiz_z\", \"Others\"],\n" +
                "  \"credits\": \"Made by the Mohist-Community\",\n" +
                "  \"parent\": \"\",\n" +
                "  \"screenshots\": [],\n" +
                "  \"dependencies\": []\n" +
                "}\n" +
                "]";
        return new ByteArrayInputStream(info.getBytes());
    }

    @Override
    public File jarFile() {
        return new File("mohist.jar");
    }
}
