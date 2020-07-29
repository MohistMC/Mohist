package net.md_5.bungee.api.chat;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;

public abstract class BaseComponent {
  BaseComponent parent;

  public int result;
  
  private ChatColor color;
  
  private Boolean bold;
  
  private Boolean italic;
  
  private Boolean underlined;
  
  private Boolean strikethrough;
  
  private Boolean obfuscated;
  
  private String insertion;
  
  private List<BaseComponent> extra;
  
  private ClickEvent clickEvent;
  
  private HoverEvent hoverEvent;
  
  public void setColor(ChatColor color) { this.color = color; }
  
  public void setBold(Boolean bold) { this.bold = bold; }
  
  public void setItalic(Boolean italic) { this.italic = italic; }
  
  public void setUnderlined(Boolean underlined) { this.underlined = underlined; }
  
  public void setStrikethrough(Boolean strikethrough) { this.strikethrough = strikethrough; }
  
  public void setObfuscated(Boolean obfuscated) { this.obfuscated = obfuscated; }
  
  public void setInsertion(String insertion) { this.insertion = insertion; }
  
  public void setClickEvent(ClickEvent clickEvent) { this.clickEvent = clickEvent; }
  
  public void setHoverEvent(HoverEvent hoverEvent) { this.hoverEvent = hoverEvent; }
  
  public String toString() { return "BaseComponent(color=" + getColor() + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", insertion=" + getInsertion() + ", extra=" + getExtra() + ", clickEvent=" + getClickEvent() + ", hoverEvent=" + getHoverEvent() + ")"; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof BaseComponent))
      return false; 
    BaseComponent other = (BaseComponent)o;
    if (!other.canEqual(this))
      return false; 
    Object this$color = getColor(), other$color = other.getColor();
    if ((this$color == null) ? (other$color != null) : !this$color.equals(other$color))
      return false; 
    Object this$bold = this.bold, other$bold = other.bold;
    if ((this$bold == null) ? (other$bold != null) : !this$bold.equals(other$bold))
      return false; 
    Object this$italic = this.italic, other$italic = other.italic;
    if ((this$italic == null) ? (other$italic != null) : !this$italic.equals(other$italic))
      return false; 
    Object this$underlined = this.underlined, other$underlined = other.underlined;
    if ((this$underlined == null) ? (other$underlined != null) : !this$underlined.equals(other$underlined))
      return false; 
    Object this$strikethrough = this.strikethrough, other$strikethrough = other.strikethrough;
    if ((this$strikethrough == null) ? (other$strikethrough != null) : !this$strikethrough.equals(other$strikethrough))
      return false; 
    Object this$obfuscated = this.obfuscated, other$obfuscated = other.obfuscated;
    if ((this$obfuscated == null) ? (other$obfuscated != null) : !this$obfuscated.equals(other$obfuscated))
      return false; 
    Object this$insertion = getInsertion(), other$insertion = other.getInsertion();
    if ((this$insertion == null) ? (other$insertion != null) : !this$insertion.equals(other$insertion))
      return false; 
    Object this$extra = getExtra(), other$extra = other.getExtra();
    if ((this$extra == null) ? (other$extra != null) : !this$extra.equals(other$extra))
      return false; 
    Object this$clickEvent = getClickEvent(), other$clickEvent = other.getClickEvent();
    if ((this$clickEvent == null) ? (other$clickEvent != null) : !this$clickEvent.equals(other$clickEvent))
      return false; 
    Object this$hoverEvent = getHoverEvent(), other$hoverEvent = other.getHoverEvent();
    return !((this$hoverEvent == null) ? (other$hoverEvent != null) : !this$hoverEvent.equals(other$hoverEvent)); }
  
  protected boolean canEqual(Object other) { return other instanceof BaseComponent; }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $color = getColor();
    result = result * 59 + (($color == null) ? 43 : $color.hashCode());
    Object $bold = this.bold;
    result = result * 59 + (($bold == null) ? 43 : $bold.hashCode());
    Object $italic = this.italic;
    result = result * 59 + (($italic == null) ? 43 : $italic.hashCode());
    Object $underlined = this.underlined;
    result = result * 59 + (($underlined == null) ? 43 : $underlined.hashCode());
    Object $strikethrough = this.strikethrough;
    result = result * 59 + (($strikethrough == null) ? 43 : $strikethrough.hashCode());
    Object $obfuscated = this.obfuscated;
    result = result * 59 + (($obfuscated == null) ? 43 : $obfuscated.hashCode());
    Object $insertion = getInsertion();
    result = result * 59 + (($insertion == null) ? 43 : $insertion.hashCode());
    Object $extra = getExtra();
    result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
    Object $clickEvent = getClickEvent();
    result = result * 59 + (($clickEvent == null) ? 43 : $clickEvent.hashCode());
    Object $hoverEvent = getHoverEvent();
    return result * 59 + (($hoverEvent == null) ? 43 : $hoverEvent.hashCode());
  }
  
  public String getInsertion() { return this.insertion; }
  
  public List<BaseComponent> getExtra() { return this.extra; }
  
  public ClickEvent getClickEvent() { return this.clickEvent; }
  
  public HoverEvent getHoverEvent() { return this.hoverEvent; }
  
  @Deprecated
  public BaseComponent() {}
  
  BaseComponent(BaseComponent old) {
    copyFormatting(old, ComponentBuilder.FormatRetention.ALL, true);
    if (old.getExtra() != null)
      for (BaseComponent extra : old.getExtra())
        addExtra(extra.duplicate());  
  }
  
  public void copyFormatting(BaseComponent component) { copyFormatting(component, ComponentBuilder.FormatRetention.ALL, true); }
  
  public void copyFormatting(BaseComponent component, boolean replace) { copyFormatting(component, ComponentBuilder.FormatRetention.ALL, replace); }
  
  public void copyFormatting(BaseComponent component, ComponentBuilder.FormatRetention retention, boolean replace) {
    if (retention == ComponentBuilder.FormatRetention.EVENTS || retention == ComponentBuilder.FormatRetention.ALL) {
      if (replace || this.clickEvent == null)
        setClickEvent(component.getClickEvent()); 
      if (replace || this.hoverEvent == null)
        setHoverEvent(component.getHoverEvent()); 
    } 
    if (retention == ComponentBuilder.FormatRetention.FORMATTING || retention == ComponentBuilder.FormatRetention.ALL) {
      if (replace || this.color == null)
        setColor(component.getColorRaw()); 
      if (replace || this.bold == null)
        setBold(component.isBoldRaw()); 
      if (replace || this.italic == null)
        setItalic(component.isItalicRaw()); 
      if (replace || this.underlined == null)
        setUnderlined(component.isUnderlinedRaw()); 
      if (replace || this.strikethrough == null)
        setStrikethrough(component.isStrikethroughRaw()); 
      if (replace || this.obfuscated == null)
        setObfuscated(component.isObfuscatedRaw()); 
      if (replace || this.insertion == null)
        setInsertion(component.getInsertion()); 
    } 
  }
  
  public void retain(ComponentBuilder.FormatRetention retention) {
    if (retention == ComponentBuilder.FormatRetention.FORMATTING || retention == ComponentBuilder.FormatRetention.NONE) {
      setClickEvent(null);
      setHoverEvent(null);
    } 
    if (retention == ComponentBuilder.FormatRetention.EVENTS || retention == ComponentBuilder.FormatRetention.NONE) {
      setColor(null);
      setBold(null);
      setItalic(null);
      setUnderlined(null);
      setStrikethrough(null);
      setObfuscated(null);
      setInsertion(null);
    } 
  }
  
  @Deprecated
  public BaseComponent duplicateWithoutFormatting() {
    BaseComponent component = duplicate();
    component.retain(ComponentBuilder.FormatRetention.NONE);
    return component;
  }
  
  public static String toLegacyText(BaseComponent... components) {
    StringBuilder builder = new StringBuilder();
    for (BaseComponent msg : components)
      builder.append(msg.toLegacyText()); 
    return builder.toString();
  }
  
  public static String toPlainText(BaseComponent... components) {
    StringBuilder builder = new StringBuilder();
    for (BaseComponent msg : components)
      builder.append(msg.toPlainText()); 
    return builder.toString();
  }
  
  public ChatColor getColor() {
    if (this.color == null) {
      if (this.parent == null)
        return ChatColor.WHITE; 
      return this.parent.getColor();
    } 
    return this.color;
  }
  
  public ChatColor getColorRaw() { return this.color; }
  
  public boolean isBold() {
    if (this.bold == null)
      return (this.parent != null && this.parent.isBold()); 
    return this.bold.booleanValue();
  }
  
  public Boolean isBoldRaw() { return this.bold; }
  
  public boolean isItalic() {
    if (this.italic == null)
      return (this.parent != null && this.parent.isItalic()); 
    return this.italic.booleanValue();
  }
  
  public Boolean isItalicRaw() { return this.italic; }
  
  public boolean isUnderlined() {
    if (this.underlined == null)
      return (this.parent != null && this.parent.isUnderlined()); 
    return this.underlined.booleanValue();
  }
  
  public Boolean isUnderlinedRaw() { return this.underlined; }
  
  public boolean isStrikethrough() {
    if (this.strikethrough == null)
      return (this.parent != null && this.parent.isStrikethrough()); 
    return this.strikethrough.booleanValue();
  }
  
  public Boolean isStrikethroughRaw() { return this.strikethrough; }
  
  public boolean isObfuscated() {
    if (this.obfuscated == null)
      return (this.parent != null && this.parent.isObfuscated()); 
    return this.obfuscated.booleanValue();
  }
  
  public Boolean isObfuscatedRaw() { return this.obfuscated; }
  
  public void setExtra(List<BaseComponent> components) {
    for (BaseComponent component : components)
      component.parent = this; 
    this.extra = components;
  }
  
  public void addExtra(String text) { addExtra(new TextComponent(text)); }
  
  public void addExtra(BaseComponent component) {
    if (this.extra == null)
      this.extra = new ArrayList<>(); 
    component.parent = this;
    this.extra.add(component);
  }
  
  public boolean hasFormatting() { return (this.color != null || this.bold != null || this.italic != null || this.underlined != null || this.strikethrough != null || this.obfuscated != null || this.insertion != null || this.hoverEvent != null || this.clickEvent != null); }
  
  public String toPlainText() {
    StringBuilder builder = new StringBuilder();
    toPlainText(builder);
    return builder.toString();
  }
  
  void toPlainText(StringBuilder builder) {
    if (this.extra != null)
      for (BaseComponent e : this.extra)
        e.toPlainText(builder);  
  }
  
  public String toLegacyText() {
    StringBuilder builder = new StringBuilder();
    toLegacyText(builder);
    return builder.toString();
  }
  
  void toLegacyText(StringBuilder builder) {
    if (this.extra != null)
      for (BaseComponent e : this.extra)
        e.toLegacyText(builder);  
  }
  
  void addFormat(StringBuilder builder) {
    builder.append(getColor());
    if (isBold())
      builder.append(ChatColor.BOLD); 
    if (isItalic())
      builder.append(ChatColor.ITALIC); 
    if (isUnderlined())
      builder.append(ChatColor.UNDERLINE); 
    if (isStrikethrough())
      builder.append(ChatColor.STRIKETHROUGH); 
    if (isObfuscated())
      builder.append(ChatColor.MAGIC); 
  }
  
  public abstract BaseComponent duplicate();
}
