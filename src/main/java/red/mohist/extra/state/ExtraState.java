package red.mohist.extra.state;

import net.minecraft.state.property.Property;

import java.util.Map;
import java.util.function.Function;

public interface ExtraState {

    Function<Map.Entry<Property<?>, Comparable<?>>, String> getProperty();

}
