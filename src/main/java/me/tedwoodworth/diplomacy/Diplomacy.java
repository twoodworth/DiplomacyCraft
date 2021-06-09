package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.Guis.RecipeGuis;
import me.tedwoodworth.diplomacy.Items.*;
import me.tedwoodworth.diplomacy.chat.ChatManager;
import me.tedwoodworth.diplomacy.chat.ChatNotifications;
import me.tedwoodworth.diplomacy.commands.*;
import me.tedwoodworth.diplomacy.dynmap.DiplomacyDynmap;
import me.tedwoodworth.diplomacy.dynmap.OurServerListener;
import me.tedwoodworth.diplomacy.entities.EntityManager;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.lives.LivesCommand;
import me.tedwoodworth.diplomacy.lives.LivesManager;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import me.tedwoodworth.diplomacy.spawning.SpawnManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

//import me.tedwoodworth.diplomacy.geology.*;

public class Diplomacy extends JavaPlugin {
    private static Diplomacy instance;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;
        //noinspection ConstantConditions
        WorldManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded worlds & registered world events");
        PlotCommand.register(getCommand("plot"));
        NationCommand.register(getCommand("nation"));
        GuardCommand.register(getCommand("guard"));
        GroupCommand.register(getCommand("group"));
        MenuCommand.register(getCommand("menu"));
        ClassCommand.register(getCommand("class"));
        ChatCommand.register(getCommand("nc"));
        ChatCommand.register(getCommand("gc"));
        ChatCommand.register(getCommand("lc"));
        ChatCommand.register(getCommand("ac"));
        LivesCommand.register(getCommand("lives"));
        TeleportCommand.register(getCommand("ott"));
        TeleportCommand.register(getCommand("ottConfirm"));
        TeleportCommand.register(getCommand("ottCancel"));
        TeleportCommand.register(getCommand("ottAccept"));
        TeleportCommand.register(getCommand("ottDecline"));
        TeleportCommand.register(getCommand("spawn"));
        LinkCommand.register(getCommand("map"));
        LinkCommand.register(getCommand("discord"));
        PlayerCommand.register(getCommand("player"));
        TogglePickupCommand.register(getCommand("ta"));
        RecipeCommand.register(getCommand("recipes"));
        AdminCommand.register(getCommand("admin"));
//        GeoCommand.register(getCommand("geo"));
        System.out.println("[Diplomacy] Loaded commands");
        DiplomacyConfig.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded config events");
        SpawnManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded spawning events");
        CustomItemGenerator.getInstance();
        CustomItemRecipes.getInstance().registerEvents();
        CustomItems.getInstance();
        System.out.println("[Diplomacy] Loaded custom recipe events");
        Items.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded item events");
        LivesManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded life events");
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
        Guis.getInstance().loadNationMenus();
        System.out.println("[Diplomacy] Loaded nation menus");
        RecipeGuis.getInstance().loadRecipeMenus();
        System.out.println("[Diplomacy] Loaded recipe menus");
        LivesManager.getInstance().startScheduler();
        ChatNotifications.getInstance().startScheduler();
        System.out.println("[Diplomacy] Loaded schedulers");
        Recipes.getInstance().loadRecipes();
        System.out.println("[Diplomacy] Loaded recipes");
        GuardManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded guard events");
        OurServerListener.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded dynmap events");
        DiplomacyDynmap.getInstance().load();
        System.out.println("[Diplomacy] Loaded Diplomacy-dynmap");
        EntityManager.getInstance();
        System.out.println("[Diplomacy] Loaded entity manager");
    }

    @Override
    public void onDisable() {
        for (var grenade : Items.getInstance().grenadeThrowerMap.keySet()) {
            grenade.remove();
        }
        for (var block : DiplomacyPlayers.getInstance().griefedBlocks.keySet()) {
            var state = DiplomacyPlayers.getInstance().griefedBlocks.get(block);
            state.update(true, false);
        }
        for (var map : DiplomacyPlayers.getInstance().explodedBlocks) {
            for (var block : map.keySet()) {
                var state = map.get(block);
                state.update(true, false);
            }
        }
    }

    public static Diplomacy getInstance() {
        return instance;
    }


}
