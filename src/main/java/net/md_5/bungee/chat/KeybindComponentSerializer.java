package net.md_5.bungee.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.KeybindComponent;

public class KeybindComponentSerializer extends BaseComponentSerializer implements JsonSerializer<KeybindComponent>, JsonDeserializer<KeybindComponent> {
  public KeybindComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    KeybindComponent component = new KeybindComponent();
    JsonObject object = json.getAsJsonObject();
    deserialize(object, (BaseComponent)component, context);
    component.setKeybind(object.get("keybind").getAsString());
    return component;
  }
  
  public JsonElement serialize(KeybindComponent src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject object = new JsonObject();
    serialize(object, (BaseComponent)src, context);
    object.addProperty("keybind", src.getKeybind());
    return (JsonElement)object;
  }
}
