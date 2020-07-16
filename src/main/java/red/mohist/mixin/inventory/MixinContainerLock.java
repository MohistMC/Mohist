package red.mohist.mixin.inventory;

import net.minecraft.inventory.ContainerLock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.inventory.ExtraContainerLock;

@Mixin(ContainerLock.class)
public class MixinContainerLock implements ExtraContainerLock {

    @Shadow
    private String key;

    @Override
    public String getKey() {
        return this.key;
    }
}
