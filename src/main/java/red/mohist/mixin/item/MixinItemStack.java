package red.mohist.mixin.item;

import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.serialization.Dynamic;

import net.minecraft.datafixer.NbtOps;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import red.mohist.extra.item.ExtraItemStack;

@Mixin(ItemStack.class)
public class MixinItemStack implements ExtraItemStack {

    @Shadow
    private Item item;

    public MixinItemStack() {
    }

    @Override
    public void setItem(Item item) {
        this.item = item;
    }

    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
    @Override
    public void convertStack(int version) {
        if (0 < version && version < CraftMagicNumbers.INSTANCE.getDataVersion()) {
            CompoundTag savedStack = new CompoundTag();

            ((ItemStack)(Object)this).toTag(savedStack);
            savedStack = (CompoundTag) CraftServer.server.getDataFixer().update(TypeReferences.ITEM_STACK, new Dynamic(NbtOps.INSTANCE, savedStack), version, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            ((ItemStack)(Object)this).setTag(savedStack);
        }
    }

}