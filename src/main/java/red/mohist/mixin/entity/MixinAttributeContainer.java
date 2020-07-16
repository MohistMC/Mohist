package red.mohist.mixin.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraAttributeContainer;

import java.util.Map;

@Mixin(AttributeContainer.class)
public class MixinAttributeContainer implements ExtraAttributeContainer {

    @Shadow
    @Final
    private Map<EntityAttribute, EntityAttributeInstance> custom = Maps.newHashMap();

    @Override
    public EntityAttributeInstance getAttributeInstanceByName(String attributeName) {
        return this.custom.get(attributeName);
    }
}
