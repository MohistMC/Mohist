package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.world.food.FoodProperties;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;

@SerializableAs("Food")
public final class CraftFoodComponent implements FoodComponent {

    private FoodProperties handle;

    public CraftFoodComponent(FoodProperties food) {
        this.handle = food;
    }

    public CraftFoodComponent(CraftFoodComponent food) {
        this.handle = food.handle;
    }

    public CraftFoodComponent(Map<String, Object> map) {
        Integer nutrition = SerializableMeta.getObject(Integer.class, map, "nutrition", false);
        Float saturationModifier = SerializableMeta.getObject(Float.class, map, "saturation", false);
        Boolean canAlwaysEat = SerializableMeta.getBoolean(map, "can-always-eat");

        Float eatSeconds = SerializableMeta.getObject(Float.class, map, "eat-seconds", true);
        if (eatSeconds == null) {
            eatSeconds = 1.6f;
        }

        ItemStack usingConvertsTo = SerializableMeta.getObject(ItemStack.class, map, "using-converts-to", true);

        ImmutableList.Builder<FoodEffect> effects = ImmutableList.builder();
        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, "effects", true);
        if (rawEffectList != null) {
            for (Object obj : rawEffectList) {
                Preconditions.checkArgument(obj instanceof FoodEffect, "Object (%s) in effect list is not valid", obj.getClass());
                effects.add(new CraftFoodEffect((FoodEffect) obj));
            }
        }

        this.handle = new FoodProperties(nutrition, saturationModifier, canAlwaysEat, eatSeconds, Optional.ofNullable(usingConvertsTo).map(CraftItemStack::asNMSCopy), effects.build().stream().map(CraftFoodEffect::new).map(CraftFoodEffect::getHandle).toList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nutrition", this.getNutrition());
        result.put("saturation", this.getSaturation());
        result.put("can-always-eat", this.canAlwaysEat());
        result.put("eat-seconds", this.getEatSeconds());
        result.put("using-converts-to", getUsingConvertsTo());
        result.put("effects", this.getEffects());
        return result;
    }

    public FoodProperties getHandle() {
        return this.handle;
    }

    @Override
    public int getNutrition() {
        return this.handle.nutrition();
    }

    @Override
    public void setNutrition(int nutrition) {
        Preconditions.checkArgument(nutrition >= 0, "Nutrition cannot be negative");
        this.handle = new FoodProperties(nutrition, this.handle.saturation(), this.handle.canAlwaysEat(), this.handle.eatSeconds(), this.handle.usingConvertsTo(), handle.effects());
    }

    @Override
    public float getSaturation() {
        return this.handle.saturation();
    }

    @Override
    public void setSaturation(float saturation) {
        this.handle = new FoodProperties(this.handle.nutrition(), saturation, this.handle.canAlwaysEat(), this.handle.eatSeconds(), this.handle.usingConvertsTo(), handle.effects());
    }

    @Override
    public boolean canAlwaysEat() {
        return this.handle.canAlwaysEat();
    }

    @Override
    public void setCanAlwaysEat(boolean canAlwaysEat) {
        this.handle = new FoodProperties(this.handle.nutrition(), this.handle.saturation(), canAlwaysEat, this.handle.eatSeconds(), this.handle.usingConvertsTo(), handle.effects());
    }

    @Override
    public float getEatSeconds() {
        return this.handle.eatSeconds();
    }

    @Override
    public void setEatSeconds(float eatSeconds) {
        handle = new FoodProperties(handle.nutrition(), handle.saturation(), handle.canAlwaysEat(), eatSeconds, handle.usingConvertsTo(), handle.effects());
    }

    @Override
    public ItemStack getUsingConvertsTo() {
        return handle.usingConvertsTo().map(CraftItemStack::asBukkitCopy).orElse(null);
    }

    @Override
    public void setUsingConvertsTo(ItemStack item) {
        handle = new FoodProperties(handle.nutrition(), handle.saturation(), handle.canAlwaysEat(), handle.eatSeconds(), Optional.ofNullable(item).map(CraftItemStack::asNMSCopy), handle.effects());
    }

    @Override
    public List<FoodEffect> getEffects() {
        return this.handle.effects().stream().map(CraftFoodEffect::new).collect(Collectors.toList());
    }

    @Override
    public void setEffects(List<FoodEffect> effects) {
        this.handle = new FoodProperties(this.handle.nutrition(), this.handle.saturation(), this.handle.canAlwaysEat(), this.handle.eatSeconds(),  handle.usingConvertsTo(), effects.stream().map(CraftFoodEffect::new).map(CraftFoodEffect::getHandle).toList());
    }

    @Override
    public FoodEffect addEffect(PotionEffect effect, float probability) {
        List<FoodProperties.PossibleEffect> effects = new ArrayList<>(this.handle.effects());

        FoodProperties.PossibleEffect newEffect = new net.minecraft.world.food.FoodProperties.PossibleEffect(CraftPotionUtil.fromBukkit(effect), probability);
        effects.add(newEffect);

        this.handle = new FoodProperties(this.handle.nutrition(), this.handle.saturation(), this.handle.canAlwaysEat(), this.handle.eatSeconds(), handle.usingConvertsTo(), effects);

        return new CraftFoodEffect(newEffect);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftFoodComponent other = (CraftFoodComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public String toString() {
        return "CraftFoodComponent{" + "handle=" + this.handle + '}';
    }

    @SerializableAs("FoodEffect")
    public static class CraftFoodEffect implements FoodEffect {

        private FoodProperties.PossibleEffect handle;

        public CraftFoodEffect(FoodProperties.PossibleEffect handle) {
            this.handle = handle;
        }

        public CraftFoodEffect(FoodEffect bukkit) {
            this.handle = new net.minecraft.world.food.FoodProperties.PossibleEffect(CraftPotionUtil.fromBukkit(bukkit.getEffect()), bukkit.getProbability());
        }

        public CraftFoodEffect(Map<String, Object> map) {
            PotionEffect effect = SerializableMeta.getObject(PotionEffect.class, map, "effect", false);

            Float probability = SerializableMeta.getObject(Float.class, map, "probability", true);
            if (probability == null) {
                probability = 1.0f;
            }

            this.handle = new net.minecraft.world.food.FoodProperties.PossibleEffect(CraftPotionUtil.fromBukkit(effect), probability);
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("effect", this.getEffect());
            result.put("probability", this.getProbability());
            return result;
        }

        public FoodProperties.PossibleEffect getHandle() {
            return this.handle;
        }

        @Override
        public PotionEffect getEffect() {
            return CraftPotionUtil.toBukkit(this.handle.effect());
        }

        @Override
        public void setEffect(PotionEffect effect) {
            this.handle = new net.minecraft.world.food.FoodProperties.PossibleEffect(CraftPotionUtil.fromBukkit(effect), this.handle.probability());
        }

        @Override
        public float getProbability() {
            return this.handle.probability();
        }

        @Override
        public void setProbability(float probability) {
            Preconditions.checkArgument(0 <= probability && probability <= 1, "Probability cannot be outside range [0,1]");
            this.handle = new net.minecraft.world.food.FoodProperties.PossibleEffect(this.handle.effect(), probability);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.handle);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final CraftFoodEffect other = (CraftFoodEffect) obj;
            return Objects.equals(this.handle, other.handle);
        }

        @Override
        public String toString() {
            return "CraftFoodEffect{" + "handle=" + this.handle + '}';
        }
    }
}
