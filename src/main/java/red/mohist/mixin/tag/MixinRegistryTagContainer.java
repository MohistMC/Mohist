package red.mohist.mixin.tag;

import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.tag.ExtraRegistryTagContainer;

@Mixin(ExtraRegistryTagContainer.class)
public class MixinRegistryTagContainer implements ExtraRegistryTagContainer {

    public int version; // CraftBukkit


    @Override
    public int getVersion() {
        return this.version;
    }
}
