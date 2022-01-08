package me.tedwoodworth.diplomacy.time;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.hub.HubTimer;
import me.tedwoodworth.diplomacy.survival.SurvivalWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SurvivalTimer {
    /**
     * Equals the hour of the day that the server opens, in America/New_York time.
     */
    public static final int OPEN_HOUR = HubTimer.OPEN_HOUR;
    /**
     * Equals the minute of the day that the server opens.
     */
    public static final int OPEN_MINUTE = HubTimer.OPEN_MINUTE;

    /**
     * Equals the hour of the day that the server closes, in America/New_York time.
     */
    public static final int CLOSE_HOUR = HubTimer.CLOSE_HOUR;

    /**
     * Equals the minute of the day that the server closes.
     */
    public static final int CLOSE_MINUTE = HubTimer.CLOSE_MINUTE;

    public static boolean isOpen = false;
    private static boolean started = false;

    public static void startScheduler() {
        if (started) return;
        started = true;

        var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        var open = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0);
        var close = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0);
        isOpen = now.compareTo(open) > 0 && now.compareTo(close) < 0;
        if (isOpen) {
            tickDay();
            Bukkit.setWhitelist(false);
        } else {
            Bukkit.setWhitelist(true);
        }

        // 10 seconds before open
        var nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0).minusSeconds(0);
        if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
        var duration = Duration.between(now, nextRun);
        var initalDelay = duration.getSeconds();
        var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(SurvivalTimer::openServer,
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
        scheduler.scheduleAtFixedRate(SurvivalTimer::closeServer,
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    private static void openServer() {
        openServer(10);
    }

    private static void tickDay() {
        var total = 24000;

        // Dawn: 23000 to 999 (2/24) (adjustment: 3/24) 21,000 to 23,999
        // Day: 1000 to 10,999 (10/24) (adjustment: 12/24) 0 or 24,000 to 11,999
        // Dusk: 11000 to 12,999 (2/24) (adjustment: 3/24) 12,000 to 14,999
        // Night: 13000 to 22999 (10/24) (adjustment: 6/24) 15,000 to 20,999

        // Day: 1000 to 9000
        //
        var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        var open = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0);
        var close = now.withHour(CLOSE_HOUR).withMinute(CLOSE_MINUTE).withSecond(0);

        var openUnix = open.toEpochSecond();
        var closeUnix = close.toEpochSecond();
        var duration = closeUnix - openUnix;
        var currentUnix = now.toEpochSecond();
        var remaining = closeUnix - currentUnix;
        var percentRemaining = ((double) remaining) / duration;

        var tick = 24000L - (long) (total * percentRemaining);
        if (tick >= 24000L) tick -= 24000L;
        long time;
        if (tick < 12000) {
            time = (long) (((double) tick) / 11999 * 9999 + 1000);
        } else if (tick < 15000) {
            time = (long) (((double) tick - 12000.0) / 2999 * 1999 + 11000);
        } else if (tick < 21000) {
            time = (long) (((double) tick - 15000) / 5999 * 9999 + 13000);
        } else {
            time = (long) (((double) tick - 21000) / 2999 * 1999  + 23000);
            if (time >= 24000L) time -= 24000L;
        }
        SurvivalWorld.getInstance().getWorld().setTime(time);

        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                () -> {
                    if (isOpen) tickDay();
                },
                20L);
    }

    private static void openServer(int seconds) {
        if (seconds == 0) {
            Bukkit.setWhitelist(false);
            isOpen = true;
            Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Server is now open.");
            tickDay();
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
            Bukkit.setWhitelist(true);
            for (var player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer("" + ChatColor.RED + ChatColor.BOLD + "Server is closed and will reopen in " + h + "h " + m + "m " + s + "s ");
            }
        } else {
            Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "Server closes in " + seconds + " seconds.");
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> closeServer(seconds - 1), 20L);
        }
    }
}
