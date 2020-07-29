package net.md_5.bungee.api.chat;

import java.util.Arrays;

public final class HoverEvent {

  public int result;

  private final Action action;
  
  private final BaseComponent[] value;
  
  public String toString() { return "HoverEvent(action=" + getAction() + ", value=" + Arrays.deepToString((Object[])getValue()) + ")"; }
  
  public boolean equals(Object o) { if (o == this)
      return true; 
    if (!(o instanceof HoverEvent))
      return false; 
    HoverEvent other = (HoverEvent)o;
    Object this$action = getAction(), other$action = other.getAction();
    return ((this$action == null) ? (other$action != null) : !this$action.equals(other$action)) ? false : (!!Arrays.deepEquals((Object[])getValue(), (Object[])other.getValue())); }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $action = getAction();
    result = result * 59 + (($action == null) ? 43 : $action.hashCode());
    return result * 59 + Arrays.deepHashCode((Object[])getValue());
  }
  
  public HoverEvent(Action action, BaseComponent[] value) { this.action = action;
    this.value = value; }
  
  public Action getAction() { return this.action; }
  
  public BaseComponent[] getValue() { return this.value; }
  
  public enum Action {
    SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM, SHOW_ENTITY;
  }
}
