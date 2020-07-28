package red.mohist.mixin.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.mohist.extra.world.ExtraWorld;

import java.util.function.Supplier;

@Mixin(World.class)
public class MixinWorld implements ExtraWorld {

    private CraftWorld bukkitWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MutableWorldProperties a, RegistryKey b, RegistryKey c, DimensionType d, Supplier<Boolean> e, boolean f, boolean g, long h, CallbackInfo ci){
        this.bukkitWorld = new CraftWorld(((ServerWorld)(Object)this));
    }
    @Override
    public CraftWorld getCraftWorld() {
        return bukkitWorld;
    }
}
