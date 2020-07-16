package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.commands.GroupCommand;
import me.tedwoodworth.diplomacy.commands.NationCommand;
import me.tedwoodworth.diplomacy.commands.PlotCommand;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.plugin.java.JavaPlugin;

public class Diplomacy extends JavaPlugin {
    private static Diplomacy instance;

    @Override
    public void onEnable() {
        instance = this;
        //noinspection ConstantConditions
        PlotCommand.register(getCommand("plot"));
        NationCommand.register(getCommand("nation"));
        GroupCommand.register(getCommand("group"));
        Nations.getInstance().registerEvents();
        DiplomacyPlayers.getInstance().registerEvents();
        ContestManager.getInstance().registerEvents();
        DiplomacyGroups.getInstance().registerEvents();
    }

    @Override
    public void onDisable() {
    }

    public static Diplomacy getInstance() {
        return instance;
    }


}
