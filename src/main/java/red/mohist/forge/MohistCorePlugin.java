package red.mohist.forge;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.util.Map;
import javax.annotation.Nullable;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MohistCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
        };
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
