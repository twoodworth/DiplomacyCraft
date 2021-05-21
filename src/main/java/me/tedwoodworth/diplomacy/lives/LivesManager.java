package me.tedwoodworth.diplomacy.lives;

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
            player.sendMessage(ChatColor.AQUA + "You have recieved an " + ChatColor.GOLD + "Apple of Life" + ChatColor.AQUA + " for " + reason + ".");
            var apple = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
            var extra = player.getInventory().addItem(apple);
            if (extra.size() > 0) {
                player.getWorld().dropItem(player.getLocation(), apple);
            }
        }, 20L);
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
            if (lives == 0) {
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You have 0 lives left. You will not be able to re-enter the world until you gain more lives.");
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Gain more lives by voting for our server, or waiting until the next day begins.");

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
            diplomacyPlayer.setLastLocation(null);
            DiplomacyPlayers.getInstance().combatLogged.remove(player);
            if (diplomacyPlayer.getLives() == 0) {
                var name = player.getName();
                for (var testPlayer : Bukkit.getOnlinePlayers()) {
                    testPlayer.sendMessage("" + ChatColor.RED + ChatColor.BOLD + name + " ran out of lives.");
                }
                return;
            } else {
                var label = " lives ";
                if (diplomacyPlayer.getLives() == 1) {
                    label = " life ";
                }
                player.sendMessage(ChatColor.AQUA + "You now have " + diplomacyPlayer.getLives() + label + "left.");
            }
        }
    }
}

