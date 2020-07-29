package net.md_5.bungee.api.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.chat.TranslationRegistry;

public final class TranslatableComponent extends BaseComponent {
  public void setTranslate(String translate) { this.translate = translate; }
  
  public String toString() { return "TranslatableComponent(format=" + getFormat() + ", translate=" + getTranslate() + ", with=" + getWith() + ")"; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof TranslatableComponent))
      return false; 
    TranslatableComponent other = (TranslatableComponent)o;
    if (!other.canEqual(this))
      return false; 
    if (!super.equals(o))
      return false; 
    Object this$format = getFormat(), other$format = other.getFormat();
    if ((this$format == null) ? (other$format != null) : !this$format.equals(other$format))
      return false; 
    Object this$translate = getTranslate(), other$translate = other.getTranslate();
    if ((this$translate == null) ? (other$translate != null) : !this$translate.equals(other$translate))
      return false; 
    Object this$with = getWith(), other$with = other.getWith();
    return !((this$with == null) ? (other$with != null) : !this$with.equals(other$with)); }
  
  protected boolean canEqual(Object other) { return other instanceof TranslatableComponent; }
  
  public int hashCode() {
    int PRIME = 59;
    result = super.hashCode();
    Object $format = getFormat();
    result = result * 59 + (($format == null) ? 43 : $format.hashCode());
    Object $translate = getTranslate();
    result = result * 59 + (($translate == null) ? 43 : $translate.hashCode());
    Object $with = getWith();
    return result * 59 + (($with == null) ? 43 : $with.hashCode());
  }
  
  private final Pattern format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
  
  private String translate;
  
  private List<BaseComponent> with;
  
  public Pattern getFormat() { return this.format; }
  
  public String getTranslate() { return this.translate; }
  
  public List<BaseComponent> getWith() { return this.with; }
  
  public TranslatableComponent(TranslatableComponent original) {
    super(original);
    setTranslate(original.getTranslate());
    if (original.getWith() != null) {
      List<BaseComponent> temp = new ArrayList<>();
      for (BaseComponent baseComponent : original.getWith())
        temp.add(baseComponent.duplicate()); 
      setWith(temp);
    } 
  }
  
  public TranslatableComponent(String translate, Object... with) {
    setTranslate(translate);
    if (with != null && with.length != 0) {
      List<BaseComponent> temp = new ArrayList<>();
      for (Object w : with) {
        if (w instanceof BaseComponent) {
          temp.add((BaseComponent)w);
        } else {
          temp.add(new TextComponent(String.valueOf(w)));
        } 
      } 
      setWith(temp);
    } 
  }
  
  public TranslatableComponent duplicate() { return new TranslatableComponent(this); }
  
  public void setWith(List<BaseComponent> components) {
    for (BaseComponent component : components)
      component.parent = this; 
    this.with = components;
  }
  
  public void addWith(String text) { addWith(new TextComponent(text)); }
  
  public void addWith(BaseComponent component) {
    if (this.with == null)
      this.with = new ArrayList<>(); 
    component.parent = this;
    this.with.add(component);
  }
  
  protected void toPlainText(StringBuilder builder) {
    convert(builder, false);
    super.toPlainText(builder);
  }
  
  protected void toLegacyText(StringBuilder builder) {
    convert(builder, true);
    super.toLegacyText(builder);
  }
  
  private void convert(StringBuilder builder, boolean applyFormat) {
    String trans = TranslationRegistry.INSTANCE.translate(this.translate);
    Matcher matcher = this.format.matcher(trans);
    int position = 0;
    int i = 0;
    while (matcher.find(position)) {
      BaseComponent withComponent;
      String withIndex;
      int pos = matcher.start();
      if (pos != position) {
        if (applyFormat)
          addFormat(builder); 
        builder.append(trans.substring(position, pos));
      } 
      position = matcher.end();
      String formatCode = matcher.group(2);
      switch (formatCode.charAt(0)) {
        case 'd':
        case 's':
          withIndex = matcher.group(1);
          withComponent = this.with.get((withIndex != null) ? (Integer.parseInt(withIndex) - 1) : i++);
          if (applyFormat) {
            withComponent.toLegacyText(builder);
            continue;
          } 
          withComponent.toPlainText(builder);
        case '%':
          if (applyFormat)
            addFormat(builder); 
          builder.append('%');
      } 
    } 
    if (trans.length() != position) {
      if (applyFormat)
        addFormat(builder); 
      builder.append(trans.substring(position, trans.length()));
    } 
  }
  
  public TranslatableComponent() {}
}
