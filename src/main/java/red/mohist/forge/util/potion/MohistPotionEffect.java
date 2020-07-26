package red.mohist.forge.util.potion;

import net.minecraft.potion.Effect;
import org.bukkit.craftbukkit.v1_15_R1.potion.CraftPotionEffectType;

public class MohistPotionEffect extends CraftPotionEffectType {

    private final String name;

    public MohistPotionEffect(Effect handle, String name) {
        super(handle);
        this.name = name;
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name.startsWith("UNKNOWN_EFFECT_TYPE_")) {
            return this.name;
        } else {
            return name;
        }
    }
}
