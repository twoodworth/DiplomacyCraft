package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.chat.ChatCommand;
import me.tedwoodworth.diplomacy.chat.ChatManager;
import me.tedwoodworth.diplomacy.chat.ChatNotifications;
import me.tedwoodworth.diplomacy.commands.*;
import me.tedwoodworth.diplomacy.dynmap.DiplomacyDynmap;
import me.tedwoodworth.diplomacy.dynmap.OurServerListener;
import me.tedwoodworth.diplomacy.enchanting.EnchantingTables;
import me.tedwoodworth.diplomacy.enchanting.EnchantingManager;
import me.tedwoodworth.diplomacy.enchanting.EnchantingTomes;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.lives_and_tax.LivesCommand;
import me.tedwoodworth.diplomacy.lives_and_tax.LivesManager;
import me.tedwoodworth.diplomacy.nations.Guis;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import me.tedwoodworth.diplomacy.spawning.SpawnManager;
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
        ClassCommand.register(getCommand("class"));
        ChatCommand.register(getCommand("nc"));
        ChatCommand.register(getCommand("gc"));
        ChatCommand.register(getCommand("lc"));
        ChatCommand.register(getCommand("ac"));
        LivesCommand.register(getCommand("lives"));
        LivesCommand.register(getCommand("giveLives"));
        LivesCommand.register(getCommand("addLife"));
        TeleportCommand.register(getCommand("ott"));
        TeleportCommand.register(getCommand("ottConfirm"));
        TeleportCommand.register(getCommand("ottCancel"));
        TeleportCommand.register(getCommand("ottAccept"));
        TeleportCommand.register(getCommand("ottDecline"));
        CombatLogCommand.register(getCommand("tag"));
        CombatLogCommand.register(getCommand("untag"));
        LinkCommand.register(getCommand("map"));
        LinkCommand.register(getCommand("discord"));
        PlayerCommand.register(getCommand("player"));
        GuideCommand.register(getCommand("guide"));
        System.out.println("[Diplomacy] Loaded commands");
        EnchantingManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded enchanting manager events");
        EnchantingTomes.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded enchanting tome events");
        EnchantingTables.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded enchanting table events");
        DiplomacyConfig.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded config events");
        SpawnManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded spawning events");
        LivesManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded life events");
        OurServerListener.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded dynmap events");
        Nations.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded nation events");
        ChatManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded chat events");
        DiplomacyPlayers.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded player events");
        ContestManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded contest events");
        DiplomacyGroups.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded group events");
        Guis.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded Gui events");
        DiplomacyDynmap.getInstance().load();
        System.out.println("[Diplomacy] Loaded Diplomacy-dynmap");
        LivesManager.getInstance().startScheduler();
        ChatNotifications.getInstance().startScheduler();
        System.out.println("[Diplomacy] Loaded schedulers");
        DiplomacyRecipes.getInstance().loadRecipes();
        System.out.println("[Diplomacy] Loaded recipes");


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
