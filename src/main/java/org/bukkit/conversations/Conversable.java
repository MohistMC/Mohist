package org.bukkit.conversations;

/**
 * The Conversable interface is used to indicate objects that can have
 * conversations.
 */
public interface Conversable {

    /**
     * Tests to see of a Conversable object is actively engaged in a
     * conversation.
     *
     * @return True if a conversation is in progress
     */
    boolean isConversing();

    /**
     * Accepts input into the active conversation. If no conversation is in
     * progress, this method does nothing.
     *
     * @param input The input message into the conversation
     */
    void acceptConversationInput(String input);

    /**
     * Enters into a dialog with a Conversation object.
     *
     * @param conversation The conversation to begin
     * @return True if the conversation should proceed, false if it has been
     * enqueued
     */
    boolean beginConversation(Conversation conversation);

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     */
    void abandonConversation(Conversation conversation);

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     * @param details      Details about why the conversation was abandoned
     */
    void abandonConversation(Conversation conversation, ConversationAbandonedEvent details);

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    void sendRawMessage(String message);
}
