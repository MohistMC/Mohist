package com.mohistmc.forge;

import com.google.common.eventbus.EventBus;
import com.mohistmc.MohistMC;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class MohistMod extends DummyModContainer {

    public MohistMod(ModMetadata metadata) {
        super(metadata);
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
                "  \"url\": \"http://mohist.red/\",\n" +
                "  \"updateUrl\": \"\",\n" +
                "  \"authors\": [\"Mgazul\", \"CraftDream\", \"azbh111\", \"lliioollcn\", \"terrainwax\", \"lvyitian\", \"ChenHauShen\", \"Others\"],\n" +
                "  \"credits\": \"Made by the Mohist-Community\",\n" +
                "  \"parent\": \"\",\n" +
                "  \"screenshots\": [],\n" +
                "  \"dependencies\": []\n" +
                "}\n" +
                "]";
        return new ByteArrayInputStream(info.getBytes());
    }
}
