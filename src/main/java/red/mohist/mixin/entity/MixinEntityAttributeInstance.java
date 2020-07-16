package red.mohist.mixin.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraEntityAttributeInstance;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Mixin(EntityAttributeInstance.class)
public class MixinEntityAttributeInstance implements ExtraEntityAttributeInstance {

    private double amount;

    @Shadow
    @Final
    private Map<UUID, EntityAttributeModifier> byId = new Object2ObjectArrayMap();

    @Shadow
    @Final
    private Map<EntityAttributeModifier.Operation, Set<EntityAttributeModifier>> operationToModifiers = Maps.newEnumMap(EntityAttributeModifier.Operation.class);

    @Shadow
    private boolean dirty = true;

    @Shadow
    @Final
    private Consumer<EntityAttributeInstance> updateCallback;

    @Shadow
    public Set<EntityAttributeModifier> getModifiers(EntityAttributeModifier.Operation operation) {
        return (Set)this.operationToModifiers.computeIfAbsent(operation, (operationx) -> {
            return Sets.newHashSet();
        });
    }

    @Shadow
    protected void onUpdate() {
        this.dirty = true;
        this.updateCallback.accept(((EntityAttributeInstance) (Object) this));
    }


    @Shadow
    private void addModifier(EntityAttributeModifier modifier) {
        EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)this.byId.putIfAbsent(modifier.getId(), modifier);
        if (entityAttributeModifier != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        } else {
            this.getModifiers(modifier.getOperation()).add(modifier);
            this.onUpdate();
        }
    }

    public EntityAttributeModifier modifier;

    @Override
    public void getAddModifier(EntityAttributeModifier convert) {
        this.addModifier(modifier);
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}
