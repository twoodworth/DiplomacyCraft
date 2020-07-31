package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.commands.*;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Diplomacy extends JavaPlugin {
    private static Diplomacy instance;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;
        //noinspection ConstantConditions
        PlotCommand.register(getCommand("plot"));
        NationCommand.register(getCommand("nation"));
        GroupCommand.register(getCommand("group"));
        EconomyCommand.register(getCommand("wallet"));
        EconomyCommand.register(getCommand("pay"));
        EconomyCommand.register(getCommand("deposit"));
        EconomyCommand.register(getCommand("withdraw"));
        MenuCommand.register(getCommand("menu"));
        Nations.getInstance().registerEvents();
        DiplomacyPlayers.getInstance().registerEvents();
        ContestManager.getInstance().registerEvents();
        DiplomacyGroups.getInstance().registerEvents();

        if (!setupEconomy()) {
            throw new RuntimeException("Unable to set up vault economy.");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public void onDisable() {
    }

    public static Diplomacy getInstance() {
        return instance;
    }


}
