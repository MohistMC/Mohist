package net.md_5.bungee.api.chat;

import java.beans.ConstructorProperties;

public final class ClickEvent {
    private final Action action;
    private final String value;

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "ClickEvent(action=" + (Object)((Object)this.getAction()) + ", value=" + this.getValue() + ")";
    }

    @ConstructorProperties(value={"action", "value"})
    public ClickEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public static enum Action {
        OPEN_URL,
        OPEN_FILE,
        RUN_COMMAND,
        SUGGEST_COMMAND;
        

        private Action() {
        }
    }

}

