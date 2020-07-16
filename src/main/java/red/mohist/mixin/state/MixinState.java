package red.mohist.mixin.state;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.mojang.serialization.MapCodec;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.state.ExtraState;

import java.util.Map;
import java.util.function.Function;

@Mixin(State.class)
public class MixinState implements ExtraState {

    @Shadow
    private static Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_MAP_PRINTER;



    @Override
    public Function<Map.Entry<Property<?>, Comparable<?>>, String> getProperty() {
        return PROPERTY_MAP_PRINTER;
    }
}
