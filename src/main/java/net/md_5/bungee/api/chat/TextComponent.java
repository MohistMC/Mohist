package net.md_5.bungee.api.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public final class TextComponent extends BaseComponent {
  public void setText(String text) { this.text = text; }
  
  public TextComponent(String text) { this.text = text; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof TextComponent))
      return false; 
    TextComponent other = (TextComponent)o;
    if (!other.canEqual(this))
      return false; 
    if (!super.equals(o))
      return false; 
    Object this$text = getText(), other$text = other.getText();
    return !((this$text == null) ? (other$text != null) : !this$text.equals(other$text)); }
  
  protected boolean canEqual(Object other) { return other instanceof TextComponent; }
  
  public int hashCode() {
    int PRIME = 59;
    result = super.hashCode();
    Object $text = getText();
    return result * 59 + (($text == null) ? 43 : $text.hashCode());
  }
  
  private static final Pattern url = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
  
  private String text;
  
  public static BaseComponent[] fromLegacyText(String message) { return fromLegacyText(message, ChatColor.WHITE); }
  
  public static BaseComponent[] fromLegacyText(String message, ChatColor defaultColor) {
    ArrayList<BaseComponent> components = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    TextComponent component = new TextComponent();
    Matcher matcher = url.matcher(message);
    for (int i = 0; i < message.length(); i++) {
      char c = message.charAt(i);
      if (c == '§') {
        if (++i >= message.length())
          break; 
        c = message.charAt(i);
        if (c >= 'A' && c <= 'Z')
          c = (char)(c + 32); 
        ChatColor format = ChatColor.getByChar(c);
        if (format != null) {
          if (builder.length() > 0) {
            TextComponent old = component;
            component = new TextComponent(old);
            old.setText(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          } 
          switch (format) {
            case BOLD:
              component.setBold(Boolean.valueOf(true));
              break;
            case ITALIC:
              component.setItalic(Boolean.valueOf(true));
              break;
            case UNDERLINE:
              component.setUnderlined(Boolean.valueOf(true));
              break;
            case STRIKETHROUGH:
              component.setStrikethrough(Boolean.valueOf(true));
              break;
            case MAGIC:
              component.setObfuscated(Boolean.valueOf(true));
              break;
            case RESET:
              format = defaultColor;
            default:
              component = new TextComponent();
              component.setColor(format);
              break;
          } 
        } 
      } else {
        int pos = message.indexOf(' ', i);
        if (pos == -1)
          pos = message.length(); 
        if (matcher.region(i, pos).find()) {
          if (builder.length() > 0) {
            TextComponent old = component;
            component = new TextComponent(old);
            old.setText(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          } 
          TextComponent old = component;
          component = new TextComponent(old);
          String urlString = message.substring(i, pos);
          component.setText(urlString);
          component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, 
                urlString.startsWith("http") ? urlString : ("http://" + urlString)));
          components.add(component);
          i += pos - i - 1;
          component = old;
        } else {
          builder.append(c);
        } 
      } 
    } 
    component.setText(builder.toString());
    components.add(component);
    return components.toArray(new BaseComponent[components.size()]);
  }
  
  public String getText() { return this.text; }
  
  public TextComponent() { this.text = ""; }
  
  public TextComponent(TextComponent textComponent) {
    super(textComponent);
    setText(textComponent.getText());
  }
  
  public TextComponent(BaseComponent... extras) {
    this();
    if (extras.length == 0)
      return; 
    setExtra(new ArrayList<>(Arrays.asList(extras)));
  }
  
  public TextComponent duplicate() { return new TextComponent(this); }
  
  protected void toPlainText(StringBuilder builder) {
    builder.append(this.text);
    super.toPlainText(builder);
  }
  
  protected void toLegacyText(StringBuilder builder) {
    addFormat(builder);
    builder.append(this.text);
    super.toLegacyText(builder);
  }
  
  public String toString() { return String.format("TextComponent{text=%s, %s}", new Object[] { this.text, super.toString() }); }
}
