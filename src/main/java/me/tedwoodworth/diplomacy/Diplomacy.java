package me.tedwoodworth.diplomacy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.tedwoodworth.diplomacy.commands.HubCommand;
import me.tedwoodworth.diplomacy.database.DBConnectionManager;
import me.tedwoodworth.diplomacy.database.DBManager;
import me.tedwoodworth.diplomacy.hub.HubListener;
import me.tedwoodworth.diplomacy.hub.HubTimer;
import me.tedwoodworth.diplomacy.survival.SurvivalWorld;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class Diplomacy extends JavaPlugin implements PluginMessageListener {
    private static Diplomacy instance;
    private static Economy economy = null;
    private static boolean isHubServer;

    @Override
    public void onEnable() {
        instance = this;
        var port = Bukkit.getServer().getPort();
        if (port == 25566) { // hub server
            isHubServer = true;
            HubTimer.startScheduler();
            Bukkit.getPluginManager().registerEvents(new HubListener(), this);
        } else if (port == 25567) {
            isHubServer = false;
            DBManager.initialize();
            DBManager.registerEvents();
            SurvivalWorld.getInstance().registerEvents();
            HubTimer.startScheduler();
            HubCommand.register(this.getCommand("hub"));
        } else {
            instance.getLogger().log(Level.SEVERE, "Unknown server, disabling.");
            instance.getPluginLoader().disablePlugin(instance);
            return;
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        //noinspection ConstantConditions
    }

    @Override
    public void onDisable() {
        if (!isHubServer) DBConnectionManager.close();
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

//        for (var grenade : Items.getInstance().grenadeThrowerMap.keySet()) {
//            grenade.remove();
//        }
//        for (var block : DiplomacyPlayers.getInstance().griefedBlocks.keySet()) {
//            var state = DiplomacyPlayers.getInstance().griefedBlocks.get(block);
//            state.update(true, false);
//        }
//        for (var map : DiplomacyPlayers.getInstance().explodedBlocks) {
//            for (var block : map.keySet()) {
//                var state = map.get(block);
//                state.update(true, false);
//            }
//        }
    }

    public static Diplomacy getInstance() {
        return instance;
    }

    public boolean isHubServer() {
        return isHubServer;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }
}

