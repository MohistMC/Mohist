package com.google.gson.internal.bind;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public final class TypeAdapters$EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<T, String> cn = new HashMap<T, String>();
    private final Map<String, T> nc = new HashMap<String, T>();

    public TypeAdapters$EnumTypeAdapter(Class<T> classOfT) {
        for (T constant : classOfT.getEnumConstants()) {
            String name = constant.name();

            try {
                SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                if (annotation == null) {
                    return;
                }
                name = annotation.value();
                for (String alternate : annotation.alternate()) {
                    nc.put(alternate, constant);
                }
            } catch (Exception e) {}

            nc.put(name, constant);
            cn.put(constant, name);
        }
    }
    @Override
    public void write(JsonWriter out, T value) throws Exception {
        if (value == null) {
            out.value(null);
        } else {
            out.value(cn.get(value));
        }
    }
    @Override
    public T read(JsonReader in) throws Exception {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return nc.get(in.nextString());
    }
} 
