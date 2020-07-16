package red.mohist.mixin.entity;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraBeaconBlockEntity;

@Mixin(BeaconBlockEntity.class)
public class MixinBeaconBlockEntity implements ExtraBeaconBlockEntity {

    @Shadow
    private Text customName;

    @Shadow
    private ContainerLock lock;

    @Shadow
    private StatusEffect secondary;

    @Shadow
    private StatusEffect primary;

    @Override
    public StatusEffect getPrimary() {
        return this.primary;
    }

    @Override
    public StatusEffect getSecondary() {
        return this.secondary;
    }

    @Override
    public Text getCustomName() {
        return this.customName;
    }

    @Override
    public ContainerLock getLock() {
        return this.lock;
    }
}
