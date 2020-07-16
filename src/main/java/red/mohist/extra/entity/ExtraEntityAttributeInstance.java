package red.mohist.extra.entity;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface ExtraEntityAttributeInstance {
    void getAddModifier(EntityAttributeModifier convert);

    double getAmount();
}
