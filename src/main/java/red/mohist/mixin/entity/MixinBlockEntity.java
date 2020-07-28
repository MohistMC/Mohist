package red.mohist.mixin.entity;

import org.bukkit.craftbukkit.v1_16_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_16_R1.persistence.CraftPersistentDataTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import red.mohist.extra.entity.ExtraBlockEntity;

@Mixin(BlockEntity.class)
public class MixinBlockEntity implements ExtraBlockEntity {

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();
    public CraftPersistentDataContainer persistentDataContainer;

    @Override
    public CraftPersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Inject(at = @At("TAIL"), method = "fromTag")
    public void loadEnd(BlockState state, CompoundTag tag, CallbackInfo callback) {
        this.persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

        CompoundTag persistentDataTag = tag.getCompound("PublicBukkitValues");
        if (persistentDataTag != null)
            this.persistentDataContainer.putAll(persistentDataTag);
    }

    @Inject(at = @At("RETURN"), method = "toTag")
    public void saveEnd(CompoundTag tag, @SuppressWarnings("rawtypes") CallbackInfoReturnable callback) {
        if (this.persistentDataContainer != null && !this.persistentDataContainer.isEmpty())
            tag.put("PublicBukkitValues", this.persistentDataContainer.toTagCompound());
    }

}
