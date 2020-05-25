package co.aikar.timings;

import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.MessageCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class TimingsReportListener implements MessageCommandSender {
    private final List<CommandSender> senders;
    private final Runnable onDone;
    private String timingsURL;

    public TimingsReportListener(CommandSender senders) {
        this(senders, null);
    }
    public TimingsReportListener(CommandSender sender, Runnable onDone) {
        this(Lists.newArrayList(sender), onDone);
    }
    public TimingsReportListener(List<CommandSender> senders) {
        this(senders, null);
    }
    public TimingsReportListener(List<CommandSender> senders, Runnable onDone) {
        Validate.notNull(senders);
        Validate.notEmpty(senders);

        this.senders = Lists.newArrayList(senders);
        this.onDone = onDone;
    }

    public String getTimingsURL() {
        return timingsURL;
    }

    public void done() {
        done(null);
    }

    public void done(String url) {
        this.timingsURL = url;
        if (onDone != null) {
            onDone.run();
        }
        for (CommandSender sender : senders) {
            if (sender instanceof TimingsReportListener) {
                ((TimingsReportListener) sender).done();
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        senders.forEach((sender) -> sender.sendMessage(message));
    }

    public void addConsoleIfNeeded() {
        boolean hasConsole = false;
        for (CommandSender sender : this.senders) {
            if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
                hasConsole = true;
            }
        }
        if (!hasConsole) {
            this.senders.add(Bukkit.getConsoleSender());
        }
    }
}
