package red.mohist.forge;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import red.mohist.bukkit.MyPetTransformer;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MohistCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return !FMLLaunchHandler.isDeobfuscatedEnvironment() ? new String[]{
                 SendPacketTransformer.class.getCanonicalName(),
                MyPetTransformer.class.getCanonicalName()
        } : null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
