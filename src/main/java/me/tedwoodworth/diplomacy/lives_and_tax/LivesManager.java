package me.tedwoodworth.diplomacy.lives_and_tax;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LivesManager {

    private static LivesManager instance = null;
    private Map<DiplomacyPlayer, GiveApple> giveLiveMap = new HashMap<>();

    public static LivesManager getInstance() {
        if (instance == null) {
            instance = new LivesManager();
        }
        return instance;
    }

    public void startScheduler() {
        var now = ZonedDateTime.now(ZoneId.of("UTC"));
        var nextRun = now.withHour(7).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initalDelay = duration.getSeconds();

        // Gives lives.
        var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new GiveApple(),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);


        // Does taxes.
        var taxScheduler = Executors.newScheduledThreadPool(1);
        taxScheduler.scheduleAtFixedRate(new Tax(),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }


    public String getStringTimeUntil() {
        var now = ZonedDateTime.now(ZoneId.of("UTC"));
        var nextRun = now.withHour(7).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }
        var time = nextRun.toEpochSecond() - now.toEpochSecond();
        var hours = time / 3600;
        var minutes = (time % 3600) / 60;
        var seconds = time % 60;

        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    void giveApple(Player player, String reason) {
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                player.sendMessage(ChatColor.AQUA + "You have recieved an " + ChatColor.GOLD + "Apple of Life" + ChatColor.AQUA + " for" + reason + ".");
                var apple = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
                var extra = player.getInventory().addItem(apple);
                if (extra.size() > 0) {
                    player.getWorld().dropItem(player.getLocation(), apple);
                }
        }, 100);
    }


    void removePlayer(DiplomacyPlayer diplomacyplayer) {
        giveLiveMap.remove(diplomacyplayer);
    }

    private class EventListener implements Listener {


        @EventHandler
        private void onPlayerLogin(PlayerLoginEvent event) {
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var lives = diplomacyPlayer.getLives();
            if (lives == 0 && diplomacyPlayer.getJoinedToday()) {

                var message = "" + net.md_5.bungee.api.ChatColor.RED + net.md_5.bungee.api.ChatColor.BOLD + "You have 0 lives left.\n\n" +
                        net.md_5.bungee.api.ChatColor.WHITE + "You will be able to join again in " + LivesManager.getInstance().getStringTimeUntil() + ".\n\n" +
                        net.md_5.bungee.api.ChatColor.WHITE + "To join sooner, get more lives by voting for our server.\n" +
                        net.md_5.bungee.api.ChatColor.WHITE + "The vote links are listed in discord.\n\n" +
                        net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + net.md_5.bungee.api.ChatColor.BOLD + "Discord: " + net.md_5.bungee.api.ChatColor.WHITE + net.md_5.bungee.api.ChatColor.BOLD + "discord.gg/PZd9gdf";

                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
            }
        }

        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent event) {
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            if (!diplomacyPlayer.getJoinedToday()) {
                giveApple(player, "playing today");
                diplomacyPlayer.setJoinedToday(true);
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onPlayerQuit(PlayerQuitEvent event) {
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var lives = diplomacyPlayer.getLives();
            if (lives == 0) {
                event.setQuitMessage(null);
            }
        }

        @EventHandler
        private void onPlayerDeath(PlayerDeathEvent event) {
            var player = (Player) event.getEntity();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            diplomacyPlayer.setLives(diplomacyPlayer.getLives() - 1);

            if (diplomacyPlayer.getLives() == 0) {
                var name = player.getName();
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                        player.kickPlayer(
                                "" + net.md_5.bungee.api.ChatColor.RED + net.md_5.bungee.api.ChatColor.BOLD + "You have 0 lives left.\n\n" +
                                        net.md_5.bungee.api.ChatColor.WHITE + "You will be able to join again in " + LivesManager.getInstance().getStringTimeUntil() + ".\n\n" +
                                        net.md_5.bungee.api.ChatColor.WHITE + "To join sooner, get more lives by voting for our server.\n" +
                                        net.md_5.bungee.api.ChatColor.WHITE + "The vote links are listed in discord.\n\n" +
                                        net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + net.md_5.bungee.api.ChatColor.BOLD + "Discord: " + net.md_5.bungee.api.ChatColor.WHITE + net.md_5.bungee.api.ChatColor.BOLD + "discord.gg/PZd9gdf"
                        ), 1);
                for (var testPlayer : Bukkit.getOnlinePlayers()) {
                    testPlayer.sendMessage("" + ChatColor.RED + ChatColor.BOLD + name + " ran out of lives.");
                }
                return;
            }
            var label = " lives ";
            if (diplomacyPlayer.getLives() == 1) {
                label = " life ";
            }
            player.sendMessage(ChatColor.AQUA + "You now have " + diplomacyPlayer.getLives() + label + "left.");
        }
    }
}

