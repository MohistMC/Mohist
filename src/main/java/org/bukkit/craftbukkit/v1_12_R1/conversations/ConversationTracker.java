package org.bukkit.craftbukkit.v1_12_R1.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import red.mohist.Mohist;

import java.util.LinkedList;
import java.util.logging.Level;

/**
 *
 */
public class ConversationTracker {

    private LinkedList<Conversation> conversationQueue = new LinkedList<>();

    public synchronized boolean beginConversation(Conversation conversation) {
        if (!conversationQueue.contains(conversation)) {
            conversationQueue.addLast(conversation);
            if (conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }
        return true;
    }

    public synchronized void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (!conversationQueue.isEmpty()) {
            if (conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }
            this.conversationQueue.remove(conversation);
            if (!conversationQueue.isEmpty()) {
                conversationQueue.getFirst().outputNextPrompt();
            }
        }
    }

    public synchronized void abandonAllConversations() {

        LinkedList<Conversation> oldQueue = conversationQueue;
        conversationQueue = new LinkedList<>();
        for (Conversation conversation : oldQueue) {
            try {
                conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
            } catch (Throwable t) {
                Mohist.LOGGER.error("Unexpected exception while abandoning a conversation", t);
            }
        }
    }

    public synchronized void acceptConversationInput(String input) {
        if (isConversing()) {
            Conversation conversation = conversationQueue.getFirst();
            try {
                conversation.acceptInput(input);
            } catch (Throwable t) {
                conversation.getContext().getPlugin().getLogger().log(Level.WARNING,
                        String.format("Plugin %s generated an exception whilst handling conversation input",
                                conversation.getContext().getPlugin().getDescription().getFullName()
                        ), t);
            }
        }
    }

    public synchronized boolean isConversing() {
        return !conversationQueue.isEmpty();
    }

    public synchronized boolean isConversingModaly() {
        return isConversing() && conversationQueue.getFirst().isModal();
    }
}
