package org.bukkit.craftbukkit.v1_16_R1.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class CraftAttributeInstance implements AttributeInstance {

    private final net.minecraft.entity.attribute.EntityAttributeInstance handle;
    private final Attribute attribute;

    public CraftAttributeInstance(net.minecraft.entity.attribute.EntityAttributeInstance handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public double getBaseValue() {
        return handle.getBaseValue();
    }

    @Override
    public void setBaseValue(double d) {
        handle.setBaseValue(d);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (net.minecraft.entity.attribute.EntityAttributeModifier nms : handle.getModifiers()) {
            result.add(convert(nms));
        }

        return result;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.addModifier(convert(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.removeModifier(convert(modifier));
    }

    @Override
    public double getValue() {
        return handle.getValue();
    }

    @Override
    public double getDefaultValue() {
        return handle.getAttribute().getDefaultValue();
    }

    public static net.minecraft.entity.attribute.EntityAttributeModifier convert(AttributeModifier bukkit) {
        return new net.minecraft.entity.attribute.EntityAttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), net.minecraft.entity.attribute.EntityAttributeModifier.Operation.values()[bukkit.getOperation().ordinal()]);
    }

    public static AttributeModifier convert(net.minecraft.entity.attribute.EntityAttributeModifier nms) {
        return new AttributeModifier(nms.getId(), nms.getName(), nms.getValue(), AttributeModifier.Operation.values()[nms.getOperation().ordinal()]);
    }
}
