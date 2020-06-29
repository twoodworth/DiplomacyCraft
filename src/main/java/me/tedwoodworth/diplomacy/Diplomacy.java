package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.commands.NationCommand;
import me.tedwoodworth.diplomacy.commands.PlotCommand;
import me.tedwoodworth.diplomacy.nations.NationManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Diplomacy extends JavaPlugin {
    private static Diplomacy instance;

    @Override
    public void onEnable() {
        instance = this;
        //noinspection ConstantConditions
        PlotCommand.register(getCommand("plot"));
        NationCommand.register(getCommand("nation"));
        NationManager.load();
    }

    @Override
    public void onDisable() {
    }

    public static Diplomacy getInstance() {
        return instance;
    }


}
