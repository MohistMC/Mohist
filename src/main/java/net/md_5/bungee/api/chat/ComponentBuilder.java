package net.md_5.bungee.api.chat;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;

public final class ComponentBuilder {
  private int cursor = -1;
  
  public int getCursor() { return this.cursor; }
  
  private final List<BaseComponent> parts = new ArrayList<>();
  
  private BaseComponent dummy;
  
  public List<BaseComponent> getParts() { return this.parts; }
  
  private ComponentBuilder(BaseComponent[] parts) {
    for (BaseComponent baseComponent : parts)
      this.parts.add(baseComponent.duplicate()); 
    resetCursor();
  }
  
  public ComponentBuilder(ComponentBuilder original) { this(original.parts.toArray(new BaseComponent[original.parts.size()])); }
  
  public ComponentBuilder(String text) { this(new TextComponent(text)); }
  
  public ComponentBuilder(BaseComponent component) { this(new BaseComponent[] { component }); }
  
  private BaseComponent getDummy() {
    if (this.dummy == null)
      this.dummy = new BaseComponent() {
          public BaseComponent duplicate() { return this; }
        }; 
    return this.dummy;
  }
  
  public ComponentBuilder resetCursor() {
    this.cursor = this.parts.size() - 1;
    return this;
  }
  
  public ComponentBuilder setCursor(int pos) throws IndexOutOfBoundsException {
    if (this.cursor != pos && (pos < 0 || pos >= this.parts.size()))
      throw new IndexOutOfBoundsException("Cursor out of bounds (expected between 0 + " + (this.parts.size() - 1) + ")"); 
    this.cursor = pos;
    return this;
  }
  
  public ComponentBuilder append(BaseComponent component) { return append(component, FormatRetention.ALL); }
  
  public ComponentBuilder append(BaseComponent component, FormatRetention retention) {
    BaseComponent previous = this.parts.isEmpty() ? null : this.parts.get(this.parts.size() - 1);
    if (previous == null) {
      previous = this.dummy;
      this.dummy = null;
    } 
    if (previous != null)
      component.copyFormatting(previous, retention, false); 
    this.parts.add(component);
    resetCursor();
    return this;
  }
  
  public ComponentBuilder append(BaseComponent[] components) { return append(components, FormatRetention.ALL); }
  
  public ComponentBuilder append(BaseComponent[] components, FormatRetention retention) {
    Preconditions.checkArgument((components.length != 0), "No components to append");
    for (BaseComponent component : components)
      append(component, retention); 
    return this;
  }
  
  public ComponentBuilder append(String text) { return append(text, FormatRetention.ALL); }
  
  public ComponentBuilder appendLegacy(String text) { return append(TextComponent.fromLegacyText(text)); }
  
  public ComponentBuilder append(String text, FormatRetention retention) { return append(new TextComponent(text), retention); }
  
  public ComponentBuilder append(Joiner joiner) { return joiner.join(this, FormatRetention.ALL); }
  
  public ComponentBuilder append(Joiner joiner, FormatRetention retention) { return joiner.join(this, retention); }
  
  public void removeComponent(int pos) throws IndexOutOfBoundsException {
    if (this.parts.remove(pos) != null)
      resetCursor(); 
  }
  
  public BaseComponent getComponent(int pos) throws IndexOutOfBoundsException { return this.parts.get(pos); }
  
  public BaseComponent getCurrentComponent() { return (this.cursor == -1) ? getDummy() : this.parts.get(this.cursor); }
  
  public ComponentBuilder color(ChatColor color) {
    getCurrentComponent().setColor(color);
    return this;
  }
  
  public ComponentBuilder bold(boolean bold) {
    getCurrentComponent().setBold(Boolean.valueOf(bold));
    return this;
  }
  
  public ComponentBuilder italic(boolean italic) {
    getCurrentComponent().setItalic(Boolean.valueOf(italic));
    return this;
  }
  
  public ComponentBuilder underlined(boolean underlined) {
    getCurrentComponent().setUnderlined(Boolean.valueOf(underlined));
    return this;
  }
  
  public ComponentBuilder strikethrough(boolean strikethrough) {
    getCurrentComponent().setStrikethrough(Boolean.valueOf(strikethrough));
    return this;
  }
  
  public ComponentBuilder obfuscated(boolean obfuscated) {
    getCurrentComponent().setObfuscated(Boolean.valueOf(obfuscated));
    return this;
  }
  
  public ComponentBuilder insertion(String insertion) {
    getCurrentComponent().setInsertion(insertion);
    return this;
  }
  
  public ComponentBuilder event(ClickEvent clickEvent) {
    getCurrentComponent().setClickEvent(clickEvent);
    return this;
  }
  
  public ComponentBuilder event(HoverEvent hoverEvent) {
    getCurrentComponent().setHoverEvent(hoverEvent);
    return this;
  }
  
  public ComponentBuilder reset() { return retain(FormatRetention.NONE); }
  
  public ComponentBuilder retain(FormatRetention retention) {
    getCurrentComponent().retain(retention);
    return this;
  }
  
  public BaseComponent[] create() {
    BaseComponent[] cloned = new BaseComponent[this.parts.size()];
    int i = 0;
    for (BaseComponent part : this.parts)
      cloned[i++] = part.duplicate(); 
    return cloned;
  }
  
  public ComponentBuilder() {}
  
  public enum FormatRetention {
    NONE, FORMATTING, EVENTS, ALL;
  }
  
  public static interface Joiner {
    ComponentBuilder join(ComponentBuilder param1ComponentBuilder, ComponentBuilder.FormatRetention param1FormatRetention);
  }
}
