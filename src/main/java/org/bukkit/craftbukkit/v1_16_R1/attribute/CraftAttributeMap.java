package org.bukkit.craftbukkit.v1_16_R1.attribute;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftNamespacedKey;

import java.util.Locale;

public class CraftAttributeMap
        implements Attributable {
    private final AttributeContainer handle;

    public CraftAttributeMap(AttributeContainer handle) {
        this.handle = handle;
    }

    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument((attribute != null), "attribute");
        EntityAttributeInstance nms = this.handle.getCustomInstance(toMinecraft(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }


    public static EntityAttribute toMinecraft(Attribute attribute) {
        return (EntityAttribute)Registry.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey()));
    }

    public static Attribute fromMinecraft(String nms) {
        String[] split = nms.split("\\.", 2);

        String generic = split[0];
        String descriptor = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, split[1]); // movementSpeed -> MOVEMENT_SPEED
        String fin = generic + "_" + descriptor;
        return EnumUtils.getEnum(Attribute.class, fin.toUpperCase(Locale.ROOT)); // so we can return null without throwing exceptions
    }
}
