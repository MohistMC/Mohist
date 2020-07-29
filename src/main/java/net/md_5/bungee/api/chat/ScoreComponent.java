package net.md_5.bungee.api.chat;

public final class ScoreComponent extends BaseComponent {
  private String name;
  
  private String objective;
  
  public void setName(String name) { this.name = name; }
  
  public void setObjective(String objective) { this.objective = objective; }
  
  public void setValue(String value) { this.value = value; }
  
  public String toString() { return "ScoreComponent(name=" + getName() + ", objective=" + getObjective() + ", value=" + getValue() + ")"; }
  
  public ScoreComponent(String name, String objective, String value) { this.name = name;
    this.objective = objective;
    this.value = value; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof ScoreComponent))
      return false; 
    ScoreComponent other = (ScoreComponent)o;
    if (!other.canEqual(this))
      return false; 
    if (!super.equals(o))
      return false; 
    Object this$name = getName(), other$name = other.getName();
    if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name))
      return false; 
    Object this$objective = getObjective(), other$objective = other.getObjective();
    if ((this$objective == null) ? (other$objective != null) : !this$objective.equals(other$objective))
      return false; 
    Object this$value = getValue(), other$value = other.getValue();
    return !((this$value == null) ? (other$value != null) : !this$value.equals(other$value)); }
  
  protected boolean canEqual(Object other) { return other instanceof ScoreComponent; }
  
  public int hashCode() {
    int PRIME = 59;
    result = super.hashCode();
    Object $name = getName();
    result = result * 59 + (($name == null) ? 43 : $name.hashCode());
    Object $objective = getObjective();
    result = result * 59 + (($objective == null) ? 43 : $objective.hashCode());
    Object $value = getValue();
    return result * 59 + (($value == null) ? 43 : $value.hashCode());
  }
  
  public String getName() { return this.name; }
  
  public String getObjective() { return this.objective; }
  
  private String value = "";
  
  public String getValue() { return this.value; }
  
  public ScoreComponent(String name, String objective) {
    setName(name);
    setObjective(objective);
  }
  
  public ScoreComponent(ScoreComponent original) {
    super(original);
    setName(original.getName());
    setObjective(original.getObjective());
    setValue(original.getValue());
  }
  
  public ScoreComponent duplicate() { return new ScoreComponent(this); }
  
  protected void toPlainText(StringBuilder builder) {
    builder.append(this.value);
    super.toPlainText(builder);
  }
  
  protected void toLegacyText(StringBuilder builder) {
    addFormat(builder);
    builder.append(this.value);
    super.toLegacyText(builder);
  }
}
