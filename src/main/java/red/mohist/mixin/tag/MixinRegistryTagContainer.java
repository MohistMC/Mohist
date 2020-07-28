package red.mohist.mixin.tag;

import net.minecraft.tag.RegistryTagContainer;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.tag.ExtraRegistryTagContainer;

@Mixin(RegistryTagContainer.class)
public class MixinRegistryTagContainer implements ExtraRegistryTagContainer {

    public int version;

    @Override
    public int getVersion() {
        return version;
    }

}