package net.md_5.bungee.api.chat;

public final class ClickEvent {
  private final Action action;

  public int result;

  private final String value;
  
  public String toString() { return "ClickEvent(action=" + getAction() + ", value=" + getValue() + ")"; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof ClickEvent))
      return false; 
    ClickEvent other = (ClickEvent)o;
    Object this$action = getAction(), other$action = other.getAction();
    if ((this$action == null) ? (other$action != null) : !this$action.equals(other$action))
      return false; 
    Object this$value = getValue(), other$value = other.getValue();
    return !((this$value == null) ? (other$value != null) : !this$value.equals(other$value)); }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $action = getAction();
    result = result * 59 + (($action == null) ? 43 : $action.hashCode());
    Object $value = getValue();
    return result * 59 + (($value == null) ? 43 : $value.hashCode());
  }
  
  public ClickEvent(Action action, String value) { this.action = action;
    this.value = value; }
  
  public Action getAction() { return this.action; }
  
  public String getValue() { return this.value; }
  
  public enum Action {
    OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE, COPY_TO_CLIPBOARD;
  }
}
