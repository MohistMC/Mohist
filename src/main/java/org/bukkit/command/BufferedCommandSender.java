package org.bukkit.command;

public class BufferedCommandSender implements MessageCommandSender {
    private final StringBuffer buffer = new StringBuffer();
    @Override
    public void sendMessage(String message) {
        buffer.append(message);
        buffer.append("\n");
    }

    public String getBuffer() {
        return buffer.toString();
    }

    public void reset() {
        this.buffer.setLength(0);
    }
}
