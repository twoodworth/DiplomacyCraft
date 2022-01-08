package me.tedwoodworth.diplomacy.hub;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HubTimer {
    /**
     * Equals the hour of the day that the server opens, in America/New_York time.
     */
    public static final int OPEN_HOUR = 20;

    /**
     * Equals the minute of the day that the server opens.
     */
    public static final int OPEN_MINUTE = 0;

    /**
     * Equals the hour of the day that the server closes, in America/New_York time.
     */
    public static final int CLOSE_HOUR = 21;

    /**
     * Equals the minute of the day that the server closes.
     */
    public static final int CLOSE_MINUTE = 0;

    public static int curH = 0;
    public static int curM = 0;
    public static int curS = 0;

    public static boolean isOpen = false;
    private static boolean started = false;

    public static void startScheduler() {
        if (started) return;
        started = true;

        var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        var open = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0);
        var close = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0);
        isOpen = now.compareTo(open) > 0 && now.compareTo(close) < 0;
        tickDay();

        // 12 hours before open
        var nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusHours(12);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        Duration duration = Duration.between(now, nextRun);
        long initalDelay = duration.getSeconds();
        var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 12 hours."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 6 hours before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusHours(6);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 6 hours."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 1 hour before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusHours(1);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 1 hour."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 30 minutes before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusMinutes(30);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 30 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 20 minutes before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusMinutes(20);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 20 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 10 minutes before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusMinutes(10);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 10 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 5 minutes before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusMinutes(5);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 5 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 1 minute before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusMinutes(1);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in 1 minute."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 10 seconds before open
        nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusSeconds(10);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(HubTimer::openServer,
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 20 minutes before close
        nextRun = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0).minusMinutes(20);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in 20 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 10 minutes before close
        nextRun = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0).minusMinutes(10);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in 10 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 5 minutes before close
        nextRun = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0).minusMinutes(5);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in 5 minutes."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 1 minute before close
        nextRun = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0).minusMinutes(1);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler.scheduleAtFixedRate(() -> Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in 1 minute."),
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        // 10 seconds before
        now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        nextRun = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0).minusSeconds(10);
        boolean afterClose = now.compareTo(nextRun) > 0;
        if (afterClose) nextRun = nextRun.plusDays(1);
        duration = Duration.between(now, nextRun);
        initalDelay = duration.getSeconds();
        scheduler.scheduleAtFixedRate(HubTimer::closeServer,
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    private static void openServer() {
        openServer(10);
    }

    private static void tickDay() {
        var total = 24000;

        // Day: 1000 to 9000
        //
        var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        var open = now.withHour(0).withMinute(0).withSecond(0);
        var close = now.withHour(23).withMinute(59).withSecond(59);
        curH = now.getHour();
        curM = now.getMinute();
        curS = now.getSecond();

        var openUnix = open.toEpochSecond();
        var closeUnix = close.toEpochSecond();
        var duration = closeUnix - openUnix;
        var currentUnix = now.toEpochSecond();
        var remaining = closeUnix - currentUnix;
        var percentRemaining = ((double) remaining) / duration;

        var tick = 24000L - (long) (total * percentRemaining);
        if (tick >= 24000L) tick -= 24000L;
        tick -= 6000L;
        if (tick < 6000L) tick += 24000L;
        HubWorld.getInstance().getHub().setTime(tick);

        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), HubTimer::tickDay, 20L);
    }

    private static void openServer(int seconds) {
        if (seconds == 0) {
            isOpen = true;
            Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server is now open.");
//            Worlds.getInstance().getWorld().setTime(0);
//            tickDay();
        } else {
            Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server opens in " + seconds + " seconds.");
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> openServer(seconds - 1), 20L);
        }
    }


    private static void closeServer() {
        closeServer(10);
    }
    private static void closeServer(int seconds) {
        if (seconds == 0) {
            isOpen = false;
            var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
            var nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0);
            if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
            Duration duration = Duration.between(now, nextRun);
            var diff = duration.getSeconds();
            var h = diff / 3600;
            diff = diff % 3600;
            var m = diff / 60;
            var s = diff % 60;

            Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server is now closed.");
            Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server will reopen in " + h + "h " + m + "m " + s + "s ");
//            for (var player : Bukkit.getOnlinePlayers()) {
//                // save last location
//                // save health
//                // save hunger
//                // save experience
//                var hub = Worlds.getInstance().getHub();
//                if (player.getWorld().equals(hub)) continue;
//                player.closeInventory();
//                PlayerUtil.saveInventory(player);
//                player.getInventory().clear();
//                player.teleport(hub.getSpawnLocation());
//            }
        } else {
            Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in " + seconds + " seconds.");
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> closeServer(seconds - 1), 20L);
        }
    }
}
