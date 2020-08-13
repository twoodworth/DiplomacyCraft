package me.tedwoodworth.diplomacy.chat;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatManager {

    private static ChatManager instance = null;

    private final String GLOBAL = "0";
    private final String LOCAL = "1";
    private final String ALLY = "2";
    private final String NATION = "3";

    private Map<Player, String> chatModes = new HashMap<>();

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public Map<Player, String> getChatModes() {
        return chatModes;
    }

    public void addChatMode(Player player, String mode) {
        chatModes.put(player, mode);
    }

    public void setChatMode(Player player, String mode) {
        chatModes.remove(player);
        chatModes.put(player, mode);
    }

    public void removeChatMode(Player player) {
        chatModes.remove(player);
    }

    @Nullable
    public String getChatMode(Player player) {
        if (!chatModes.containsKey(player)) {
            return null;
        }
        return chatModes.get(player);
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent event) {
            addChatMode(event.getPlayer(), GLOBAL);
        }

        @EventHandler
        private void onPlayerQuit(PlayerQuitEvent event) {
            removeChatMode(event.getPlayer());
        }

        @EventHandler
        private void onPlayerChat(AsyncPlayerChatEvent event) {
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var nation = Nations.getInstance().get(diplomacyPlayer);
            if (getChatMode(player) == null) {
                setChatMode(player, GLOBAL);
            }
            if (nation == null && getChatMode(player).equals(ALLY)) {
                setChatMode(player, LOCAL);
                player.sendMessage(ChatColor.RED + "You have been automatically moved from ally chat to local chat because you don't belong to a nation.");
            } else if (nation == null && getChatMode(player).equals(NATION)) {
                setChatMode(player, LOCAL);
                player.sendMessage(ChatColor.RED + "You have been automatically moved from nation chat to local chat because you don't belong to a nation.");
            }
            switch (getChatMode(player)) {
                case GLOBAL -> {
                    for (var recipient : event.getRecipients()) {
                        if (nation != null) {
                            var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                            var prefix = nation.getMemberClass(diplomacyPlayer).getPrefix();
                            if (Objects.equals(nation, testNation) && prefix != null) {
                                recipient.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.WHITE + event.getMessage());
                            } else {
                                recipient.sendMessage("" + ChatColor.GOLD + player.getName() + " " + ChatColor.WHITE + event.getMessage());
                            }
                        }
                    }
                    event.setCancelled(true);
                }
                case ALLY -> {
                    for (var recipient : event.getRecipients()) {
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (!(nation != null && (Objects.equals(nation, testNation) || nation.getAllyNationIDs().contains(testNation.getNationID())))) {
                            continue;
                        }
                        var prefix = nation.getMemberClass(diplomacyPlayer).getPrefix();
                        if (Objects.equals(nation, testNation) && prefix != null) {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.AQUA + ChatColor.BOLD + "A" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.AQUA + event.getMessage());
                        } else {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.AQUA + ChatColor.BOLD + "A" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.AQUA + event.getMessage());
                        }
                    }
                    event.setCancelled(true);
                }
                case NATION -> {
                    for (var recipient : event.getRecipients()) {
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (!(nation != null && (Objects.equals(nation, testNation)))) {
                            continue;
                        }
                        var prefix = nation.getMemberClass(diplomacyPlayer).getPrefix();
                        if (prefix != null) {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "N" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + ChatColor.BOLD + prefix + " " + ChatColor.GOLD + player.getName() + " " + ChatColor.GREEN + event.getMessage());
                        } else {
                            recipient.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "N" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] " + ChatColor.GOLD + player.getName() + " " + ChatColor.GREEN + event.getMessage());
                        }
                    }
                    event.setCancelled(true);
                }
                case LOCAL -> {
                    var location = player.getLocation();
                    for (var recipient : event.getRecipients()) {
                        var testLocation = recipient.getLocation();
                        if (location.distanceSquared(testLocation) > 62500) {
                            continue;
                        }
                        var testNation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(recipient.getUniqueId()));
                        if (nation != null) {
                            var prefix = nation.getMemberClass(diplomacyPlayer).getPrefix();
                            if (prefix != null && Objects.equals(nation, testNation)) {
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
