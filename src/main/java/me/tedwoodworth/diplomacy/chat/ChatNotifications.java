package me.tedwoodworth.diplomacy.chat;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class ChatNotifications {

    private static ChatNotifications instance = null;
    private int messageTaskID = -1;

    public static ChatNotifications getInstance() {
        if (instance == null) {
            instance = new ChatNotifications();
        }
        return instance;
    }

    public void startScheduler() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Diplomacy.getInstance(),
                new MessageSender((int) (Math.random() * DiplomacyConfig.getInstance().getMessages().size())),
                DiplomacyConfig.getInstance().getMessageInterval() * 20);
    }

    private class MessageSender implements Runnable {

        List<String> messages;
        Long interval;
        int messageIndex;

        public MessageSender(int messageIndex) {
            this.messages = DiplomacyConfig.getInstance().getMessages();
            this.interval = DiplomacyConfig.getInstance().getMessageInterval() * 20;
            this.messageIndex = messageIndex;
        }

        @Override
        public void run() {
            for (var player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.DARK_GREEN + messages.get(messageIndex));
            }
            var nextIndex = messageIndex + 1;
            if (nextIndex == messages.size()) {
                nextIndex = 0;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(Diplomacy.getInstance(),
                    new MessageSender(nextIndex),
                    interval);
        }
    }

}
