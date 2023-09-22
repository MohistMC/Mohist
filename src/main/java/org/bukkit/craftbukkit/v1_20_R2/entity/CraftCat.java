package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_20_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Cat;

public class CraftCat extends CraftTameableAnimal implements Cat {

    public CraftCat(CraftServer server, net.minecraft.world.entity.animal.Cat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cat getHandle() {
        return (net.minecraft.world.entity.animal.Cat) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCat";
    }

    @Override
    public Type getCatType() {
        return CraftType.minecraftToBukkit(getHandle().getVariant());
    }

    @Override
    public void setCatType(Type type) {
        Preconditions.checkArgument(type != null, "Cannot have null Type");

        getHandle().setVariant(CraftType.bukkitToMinecraft(type));
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    public static class CraftType {

        public static Type minecraftToBukkit(CatVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            net.minecraft.core.Registry<CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            return Type.values()[registry.getId(minecraft)];
        }

        public static CatVariant bukkitToMinecraft(Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT)
                    .byId(bukkit.ordinal());
        }
    }
}
