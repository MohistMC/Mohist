package net.md_5.bungee.api.chat;

public final class SelectorComponent extends BaseComponent {
  private String selector;
  
  public void setSelector(String selector) { this.selector = selector; }
  
  public String toString() { return "SelectorComponent(selector=" + getSelector() + ")"; }
  
  public SelectorComponent(String selector) { this.selector = selector; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof SelectorComponent))
      return false; 
    SelectorComponent other = (SelectorComponent)o;
    if (!other.canEqual(this))
      return false; 
    if (!super.equals(o))
      return false; 
    Object this$selector = getSelector(), other$selector = other.getSelector();
    return !((this$selector == null) ? (other$selector != null) : !this$selector.equals(other$selector)); }
  
  protected boolean canEqual(Object other) { return other instanceof SelectorComponent; }
  
  public int hashCode() {
    int PRIME = 59;
    result = super.hashCode();
    Object $selector = getSelector();
    return result * 59 + (($selector == null) ? 43 : $selector.hashCode());
  }
  
  public String getSelector() { return this.selector; }
  
  public SelectorComponent(SelectorComponent original) {
    super(original);
    setSelector(original.getSelector());
  }
  
  public SelectorComponent duplicate() { return new SelectorComponent(this); }
  
  protected void toPlainText(StringBuilder builder) {
    builder.append(this.selector);
    super.toPlainText(builder);
  }
  
  protected void toLegacyText(StringBuilder builder) {
    addFormat(builder);
    builder.append(this.selector);
    super.toLegacyText(builder);
  }
}
