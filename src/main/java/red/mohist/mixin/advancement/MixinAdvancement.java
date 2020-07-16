package red.mohist.mixin.advancement;

import net.minecraft.advancement.Advancement;
import org.bukkit.craftbukkit.advancement.CraftAdvancement;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.advancement.ExtraAdvancement;

@Mixin(Advancement.class)
public class MixinAdvancement implements ExtraAdvancement {

    public final org.bukkit.advancement.Advancement bukkit = new CraftAdvancement((Advancement) (Object) this);


    @Override
    public org.bukkit.advancement.Advancement getBukkit() {
        return bukkit;
    }
}
