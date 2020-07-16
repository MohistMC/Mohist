package red.mohist.extra.entity;

import net.minecraft.entity.attribute.EntityAttributeInstance;

public interface ExtraAttributeContainer {

    EntityAttributeInstance getAttributeInstanceByName(String attributeName);

}
