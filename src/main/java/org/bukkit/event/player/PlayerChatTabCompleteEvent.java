package org.bukkit.event.player;

import java.util.Collection;

import com.sun.deploy.security.SelectableSecurityManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player attempts to tab-complete a chat message.
 *
 * @deprecated This event is no longer fired due to client changes
 */
@Deprecated
@Warning(reason = "This event is no longer fired due to client changes")
public class PlayerChatTabCompleteEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String message;
    private final String lastToken;
    private final Collection<String> completions;
    private final boolean isPinging;

    public PlayerChatTabCompleteEvent(@NotNull final Player who, @NotNull final String message, @NotNull final Collection<String> completions) {
        super(who);
        Validate.notNull(message, "Message cannot be null");
        Validate.notNull(completions, "Completions cannot be null");
        this.message = message;
        int i = message.lastIndexOf(' ');
        if (i < 0) {
            if (message.length() > 0 && message.charAt(0) == '@') {
                this.lastToken = message.substring(1);
                this.isPinging = true;
            } else {
                this.lastToken = message;
                this.isPinging = false;
            }
        }
        else
        {
            String lastToken = message.substring(i+1);
            if (lastToken.length() > 0 && lastToken.charAt(0) == '@')
            {
                if (lastToken.length() == 1)
                {
                    lastToken = "";
                }
                else
                    lastToken = lastToken.substring(1);
                this.isPinging = true;
            }
            else
            {
                this.isPinging = false;
            }
            this.lastToken = lastToken;
        }
        this.completions = completions;
    }

    public boolean isPinging()
    {
        return this.isPinging;
    }
    /**
     * Gets the chat message being tab-completed.
     *
     * @return the chat message
     */
    @NotNull
    public String getChatMessage() {
        return message;
    }

    /**
     * Gets the last 'token' of the message being tab-completed.
     * <p>
     * The token is the substring starting with the character after the last
     * space in the message.
     *
     * @return The last token for the chat message
     */
    @NotNull
    public String getLastToken() {
        return lastToken;
    }

    /**
     * This is the collection of completions for this event.
     *
     * @return the current completions
     */
    @NotNull
    public Collection<String> getTabCompletions() {
        return completions;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
