package me.tedwoodworth.diplomacy.chat;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Is used to loop through configured notifications / tips in chat at regular
 * intervals.
 */
public class ChatNotifications {

    /**
     * Singleton instance of ChatNotifications
     */
    private static ChatNotifications instance = null;

    /**
     * TaskID of the task being scheduled. Set to -1 if the task
     * is not running.
     */
    private int messageTaskID = -1;

    /**
     * Returns the singleton instance of ChatNotifications
     *
     * @return instance of ChatNotifications
     */
    public static ChatNotifications getInstance() {
        if (instance == null) {
            instance = new ChatNotifications();
        }
        return instance;
    }

    /**
     * Starts the scheduler, which schedules the task to be
     * executed regularly at a configured interval.
     */
    public void startScheduler() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Diplomacy.getInstance(),
                new MessageSender((int) (Math.random() * DiplomacyConfig.getInstance().getMessages().size())),
                DiplomacyConfig.getInstance().getMessageInterval() * 20);
    }

    /**
     * Runnable class which executes itself at a regular interval.
     */
    private class MessageSender implements Runnable {

        /**
         * List of messages to send
         */
        List<String> messages;

        /**
         * Interval, in ticks, to send messages
         */
        Long interval;

        /**
         * Index of current message
         */
        int messageIndex;

        public MessageSender(int messageIndex) {
            // Retrieve messages & interval from config file
            this.messages = DiplomacyConfig.getInstance().getMessages();
            this.interval = DiplomacyConfig.getInstance().getMessageInterval() * 20;
            this.messageIndex = messageIndex;
        }

        @Override
        public void run() {
            // send message to each player
            for (var player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.DARK_GREEN + messages.get(messageIndex));
            }

            // increase index by 1, or loop back to 0 if max index reached
            var nextIndex = messageIndex + 1;
            if (nextIndex == messages.size()) {
                nextIndex = 0;
            }

            // Schedule new task to be run
            Bukkit.getScheduler().scheduleSyncDelayedTask(Diplomacy.getInstance(),
                    new MessageSender(nextIndex),
                    interval);
        }
    }

}
