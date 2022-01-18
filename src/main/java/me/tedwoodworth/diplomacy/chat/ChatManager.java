package me.tedwoodworth.diplomacy.chat;

import com.earth2me.essentials.Essentials;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyConfig;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * Manages the in-game chat
 */
public class ChatManager {

    /**
     * Singleton instance of ChatManager
     */
    private static ChatManager instance = null;

    /**
     * String ID for global chat mode
     */
    private final String GLOBAL = "0";

    /**
     * String ID for local chat mode
     */
    private final String LOCAL = "1";

    /**
     * String ID for ally chat mode
     */
    private final String ALLY = "2";

    /**
     * String ID for nation chat mode
     */
    private final String NATION = "3";

    /**
     * Maps all online players to their current chat mode
     */
    private Map<Player, String> chatModes = new HashMap<>();

    /**
     * A set of all players currently on a chat cooldown
     */
    private Set<Player> cooldown = new HashSet<>();

    /**
     * Returns the singleton instance of ChatManager
     *
     * @return ChatManager instance
     */
    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    /**
     * Returns the value of chatModes
     *
     * @return chatModes
     */
    public Map<Player, String> getChatModes() {
        return chatModes;
    }

    /**
     * Adds a player and their chat mode to the chatModes map
     *
     * @param player player to map
     * @param mode   chat mode of player
     */
    public void addChatMode(Player player, String mode) {
        chatModes.put(player, mode);
    }

    /**
     * Sets the chat mode of a player
     *
     * @param player player to set chat mode of
     * @param mode   new chat mode of player
     */
    public void setChatMode(Player player, String mode) {
        chatModes.remove(player);
        chatModes.put(player, mode);
    }

    /**
     * Removes a player form the map of chat modes.
     *
     * @param player player to remove from map
     */
    public void removeChatMode(Player player) {
        chatModes.remove(player);
    }

    /**
     * Returns the chat mode of a given player.
     * If the player does not have a chat mode assigned, they
     * get automatically mapped to GLOBAL
     *
     * @param player player to get chat mode of
     * @return chat mode of player
     */
    public String getChatMode(Player player) {
        if (!chatModes.containsKey(player)) {
            chatModes.put(player, GLOBAL);
        }
        return chatModes.get(player);
    }

    /**
     * Registers an instance of the sub-class EventListener to the plugin
     */
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    /**
     * Listens for events related to chat management, and executes
     * code when such events take place.
     */
    private class EventListener implements Listener {

        /**
         * Executed when a player joins the server. Maps players
         * to global chat mode when they join the server.
         *
         * @param event the event taking place
         */
        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent event) {
            addChatMode(event.getPlayer(), GLOBAL);
        }

        /**
         * Executed when a player quits the server. Removes
         * players from the chat mode map when they leave the server.
         *
         * @param event the event taking place
         */
        @EventHandler
        private void onPlayerQuit(PlayerQuitEvent event) {
            removeChatMode(event.getPlayer());
        }

        /**
         * Executed when a player attempts to send a message in chat.
         * <p>
         * Determines which players will receive the message being sent based
         * on the sender's chat mode. Additionally, will prevent all players
         * from receiving a message if the player is muted, on cool down, or
         * the message contains a blacklisted word.
         * <p>
         * This EventHandler method has a high event priority, meaning it is
         * executed after all other AsyncPlayerChatEvent handlers to ensure that
         * any changes made by this handler are final, and will not be reverted
         * by a lower priority handler.
         *
         * @param event the event taking place
         */
        @EventHandler(priority = EventPriority.HIGH)
        private void onPlayerChat(AsyncPlayerChatEvent event) {
            var player = event.getPlayer();


            /*
                Check if the player is muted. If so, the event gets
                cancelled and a message is sent to the player.
             */
            Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            if (essentials != null && essentials.getUser(player.getUniqueId()).isMuted()) {
                player.sendMessage(ChatColor.RED + "You cannot chat while muted.");
                return;
            }

            /*
                Check if the message contains a censored word. If so, the event gets
                cancelled and a message is sent to the player.
             */
            var censored = DiplomacyConfig.getInstance().getCensoredWords();
            var message = event.getMessage();
            var words = message.split(" ");
            for (var word : words) {
                var lower = word.toLowerCase();
                if (censored.contains(lower)) {
                    player.sendMessage(ChatColor.RED + "The word '" + word + "' is censored.");
                    event.setCancelled(true);
                    return;
                }
            }

            /*
                Check if the message contains a 'super-censored' word. If so, the event gets
                cancelled and a message is sent to the player.

                Super-censored words are sequences of characters that are blacklisted, regardless
                of whether sequence makes up a single word, is a part of a larger word, or
                spans across multiple words.
             */
            var superCensored = DiplomacyConfig.getInstance().getSuperCensoredWords();
            for (var word : superCensored) {
                var lower = message.toLowerCase();
                if (lower.contains(word)) {
                    player.sendMessage(ChatColor.RED + "The word '" + word + "' is censored.");
                    event.setCancelled(true);
                    return;
                }
            }

            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var nation = Nations.getInstance().get(diplomacyPlayer);
            // Sets chat mode to GLOBAL (the default mode) if null
            if (getChatMode(player) == null) {
                setChatMode(player, GLOBAL);
            }

            // sets chat mode to LOCAL if the player is in ALLY mode but no longer belongs to a nation
            if (nation == null && getChatMode(player).equals(ALLY)) {
                setChatMode(player, LOCAL);
                player.sendMessage(ChatColor.RED + "You have been automatically moved from ally chat to local chat because you don't belong to a nation.");
                // Sets chat mode to LOCAL if the player does not belong to a nation and is in NATION mode
            } else if (nation == null && getChatMode(player).equals(NATION)) {
                setChatMode(player, LOCAL);
                player.sendMessage(ChatColor.RED + "You have been automatically moved from nation chat to local chat because you don't belong to a nation.");
            }

            /*
                Sends the message to certain players, with a certain prefix and color-scheme,
                 based on what chat mode the sender is in.

                 If the receiver is an ally, the sender's rank prefix will also be added.
             */
            switch (getChatMode(player)) {
                case GLOBAL -> { // sends the message to all players
                    var rank = diplomacyPlayer.getRank();
                    if (rank.equals("None")) {
                        if (cooldown.contains(player)) {
                            player.sendMessage(ChatColor.RED + "You can only use global chat once every 5 seconds. You can chat without a cooldown in local chat (/lc), ally chat (/ac), and nation chat (/nc).");
                            event.setCancelled(true);
                            return;
                        } else {
                            cooldown.add(player);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> cooldown.remove(player), 100L);
                        }
                    }

                    System.out.println(player.getName() + ": " + event.getMessage());
                    for (var recipient : event.getRecipients()) {
                        if (nation != null) {
                            var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                            String prefix = "";
                            var memberClass = nation.getMemberClass(diplomacyPlayer);
                            if (memberClass != null) {
                                prefix = memberClass.getPrefix();
                            }
                            if ((Objects.equals(nation, testNation) || (testNation != null && nation.getAllyNationIDs().contains(testNation.getNationID()))) && prefix != null) {
                                recipient.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.WHITE + event.getMessage());
                            } else {
                                recipient.sendMessage("" + ChatColor.GOLD + player.getName() + " " + ChatColor.WHITE + event.getMessage());
                            }
                        } else {
                            recipient.sendMessage("" + ChatColor.GOLD + player.getName() + " " + ChatColor.WHITE + event.getMessage());
                        }

                        event.setCancelled(true);
                    }
                }
                case ALLY -> { // sends message to all allied nation members and same-nation members
                    for (var recipient : event.getRecipients()) {
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (!(nation != null && (Objects.equals(nation, testNation) || (testNation != null && nation.getAllyNationIDs().contains(testNation.getNationID()))))) {
                            continue;
                        }
                        String prefix = "";
                        var memberClass = nation.getMemberClass(diplomacyPlayer);
                        if (memberClass != null) {
                            prefix = memberClass.getPrefix();
                        }
                        if ((Objects.equals(nation, testNation) || (nation.getAllyNationIDs().contains(testNation.getNationID()))) && prefix != null) {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "A" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.DARK_GREEN + event.getMessage());
                        } else {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "A" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.DARK_GREEN + event.getMessage());
                        }
                    }
                    event.setCancelled(true);
                }
                case NATION -> { // sends message to all members of the same nation
                    for (var recipient : event.getRecipients()) {
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (!(nation != null && (Objects.equals(nation, testNation)))) {
                            continue;
                        }
                        String prefix = "";
                        var memberClass = nation.getMemberClass(diplomacyPlayer);
                        if (memberClass != null) {
                            prefix = memberClass.getPrefix();
                        }
                        if (prefix != null) {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "N" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.GREEN + event.getMessage());
                        } else {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "N" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.GREEN + event.getMessage());
                        }
                    }
                    event.setCancelled(true);
                }
                case LOCAL -> { // sends message to all players within 250 blocks
                    var location = player.getLocation();
                    for (var recipient : event.getRecipients()) {
                        var testLocation = recipient.getLocation();
                        if (!(Objects.equals(recipient.getWorld().getName(), player.getWorld().getName())) || location.distanceSquared(testLocation) > 62500) {
                            continue;
                        }
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (nation != null) {
                            String prefix = "";
                            var memberClass = nation.getMemberClass(diplomacyPlayer);
                            if (memberClass != null) {
                                prefix = memberClass.getPrefix();
                            }
                            if ((Objects.equals(nation, testNation) || (testNation != null && nation.getAllyNationIDs().contains(testNation.getNationID()))) && prefix != null) {
                                recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "L" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.LIGHT_PURPLE + event.getMessage());
                            } else {
                                recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "L" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.LIGHT_PURPLE + event.getMessage());
                            }
                        } else {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "L" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.LIGHT_PURPLE + event.getMessage());
                        }
                    }
                    event.setCancelled(true);
                }
                default -> event.setCancelled(true);
            }
        }

    }

}
