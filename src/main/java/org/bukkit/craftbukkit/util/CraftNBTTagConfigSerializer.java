package org.bukkit.craftbukkit.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class CraftNBTTagConfigSerializer {

    private static final Pattern ARRAY = Pattern.compile("^\\[.*]");
    private static final Pattern INTEGER = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)?i", Pattern.CASE_INSENSITIVE);
    private static final Pattern DOUBLE = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
    private static final StringNbtReader MOJANGSON_PARSER = new StringNbtReader(new StringReader(""));

    public static Object serialize(Tag base) {
        if (base instanceof CompoundTag) {
            Map<String, Object> innerMap = new HashMap<>();
            for (String key : ((CompoundTag) base).getKeys()) {
                innerMap.put(key, serialize(((CompoundTag) base).get(key)));
            }

            return innerMap;
        } else if (base instanceof ListTag) {
            List<Object> baseList = new ArrayList<>();
            for (int i = 0; i < ((AbstractListTag) base).size(); i++) {
                baseList.add(serialize((Tag) ((AbstractListTag) base).get(i)));
            }

            return baseList;
        } else if (base instanceof StringTag) {
            return base.asString();
        } else if (base instanceof IntTag) { // No need to check for doubles, those are covered by the double itself
            return base.toString() + "i";
        }

        return base.toString();
    }

    public static Tag deserialize(Object object) {
        if (object instanceof Map) {
            CompoundTag compound = new CompoundTag();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                compound.put(entry.getKey(), deserialize(entry.getValue()));
            }

            return compound;
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            if (list.isEmpty()) {
                return new ListTag(); // Default
            }

            ListTag tagList = new ListTag();
            for (Object tag : list) {
                tagList.add(deserialize(tag));
            }

            return tagList;
        } else if (object instanceof String) {
            String string = (String) object;

            if (ARRAY.matcher(string).matches()) {
                try {
                    return new StringNbtReader(new StringReader(string)).parseTagPrimitiveArray();
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException("Could not deserialize found list ", e);
                }
            } else if (INTEGER.matcher(string).matches()) { //Read integers on our own
                return IntTag.of(Integer.parseInt(string.substring(0, string.length() - 1)));
            } else if (DOUBLE.matcher(string).matches()) {
                return DoubleTag.of(Double.parseDouble(string.substring(0, string.length() - 1)));
            } else {
                Tag nbtBase = MOJANGSON_PARSER.parsePrimitive(string);

                if (nbtBase instanceof IntTag) { // If this returns an integer, it did not use our method from above
                    return StringTag.of(nbtBase.asString()); // It then is a string that was falsely read as an int
                } else if (nbtBase instanceof DoubleTag) {
                    return StringTag.of(String.valueOf(((DoubleTag) nbtBase).getDouble())); // Doubles add "d" at the end
                } else {
                    return nbtBase;
                }
            }
        }

        throw new RuntimeException("Could not deserialize NBTBase");
    }
}
