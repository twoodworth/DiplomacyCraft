package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.database.ConnectionManager;
import me.tedwoodworth.diplomacy.database.DBManager;
import me.tedwoodworth.diplomacy.time.TimeManager;
import me.tedwoodworth.diplomacy.world.Worlds;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;


public class Diplomacy extends JavaPlugin {
    private static Diplomacy instance;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;
        DBManager.initialize();
        DBManager.registerEvents();
        Worlds.getInstance().registerEvents();
        TimeManager.startScheduler();

        //noinspection ConstantConditions
//        PlotCommand.register(getCommand("plot"));
//        NationCommand.register(getCommand("nation"));
//        GuardCommand.register(getCommand("guard"));
//        GroupCommand.register(getCommand("group"));
//        MenuCommand.register(getCommand("menu"));
//        ClassCommand.register(getCommand("class"));
//        ChatCommand.register(getCommand("nc"));
//        ChatCommand.register(getCommand("gc"));
//        ChatCommand.register(getCommand("lc"));
//        ChatCommand.register(getCommand("ac"));
//        LivesCommand.register(getCommand("lives"));
//        TeleportCommand.register(getCommand("ott"));
//        TeleportCommand.register(getCommand("ottConfirm"));
//        TeleportCommand.register(getCommand("ottCancel"));
//        TeleportCommand.register(getCommand("ottAccept"));
//        TeleportCommand.register(getCommand("ottDecline"));
//        TeleportCommand.register(getCommand("spawn"));
//        LinkCommand.register(getCommand("map"));
//        LinkCommand.register(getCommand("discord"));
//        PlayerCommand.register(getCommand("player"));
//        TogglePickupCommand.register(getCommand("ta"));
//        RecipeCommand.register(getCommand("recipes"));
//        AdminCommand.register(getCommand("admin"));
//        GeoCommand.register(getCommand("geo"));
//        System.out.println("[Diplomacy] Loaded commands");
//        DiplomacyConfig.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded config events");
//        CustomItemGenerator.getInstance();
//        CustomItemRecipes.getInstance().registerEvents();
//        CustomItems.getInstance();
//        System.out.println("[Diplomacy] Loaded custom recipe events");
//        Items.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded item events");
//        LivesManager.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded life events");
//        Nations.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded nation events");
//        ChatManager.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded chat events");
//        DiplomacyPlayers.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded player events");
//        ContestManager.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded contest events");
//        System.out.println("[Diplomacy] Loaded group events");
//        Guis.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded Gui events");
//        PlotManager.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded Gui events");
//        Guis.getInstance().loadNationMenus();
//        System.out.println("[Diplomacy] Loaded nation menus");
//        RecipeGuis.getInstance().loadRecipeMenus();
//        System.out.println("[Diplomacy] Loaded recipe menus");
//        LivesManager.getInstance().startScheduler();
//        ChatNotifications.getInstance().startScheduler();
//        System.out.println("[Diplomacy] Loaded schedulers");
//        Recipes.getInstance().loadRecipes();
//        System.out.println("[Diplomacy] Loaded recipes");
//        OurServerListener.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded dynmap events");
//        DiplomacyDynmap.getInstance().load();
//        System.out.println("[Diplomacy] Loaded Diplomacy-dynmap");
    }

    @Override
    public void onDisable() {
        ConnectionManager.close();
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


}
