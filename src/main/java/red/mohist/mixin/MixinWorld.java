package red.mohist.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.ExtraWorld;

@Mixin(World.class)
public class MixinWorld implements ExtraWorld {

    private World world;

    @Override
    public World getCraftWorld() {
        return this.world;
    }
}
