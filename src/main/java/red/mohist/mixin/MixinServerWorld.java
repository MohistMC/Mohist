package red.mohist.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.ExtraServerWorld;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements ExtraServerWorld {

    private CraftWorld world;

    @Override
    public CraftWorld getCraftWorld() {
        return this.world;
    }
}
