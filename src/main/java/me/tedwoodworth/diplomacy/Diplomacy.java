package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.commands.PlotCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Diplomacy extends JavaPlugin {
    @Override
    public void onEnable() {
        //noinspection ConstantConditions
        PlotCommand.register(getCommand("plot"));
    }

    @Override
    public void onDisable() {
    }
}
