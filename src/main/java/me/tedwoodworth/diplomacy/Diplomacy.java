package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItemRecipes;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.chat.ChatCommand;
import me.tedwoodworth.diplomacy.chat.ChatManager;
import me.tedwoodworth.diplomacy.chat.ChatNotifications;
import me.tedwoodworth.diplomacy.commands.*;
import me.tedwoodworth.diplomacy.Items.Items;
import me.tedwoodworth.diplomacy.entities.Entities;
//import me.tedwoodworth.diplomacy.geology.*;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.lives_and_tax.LivesCommand;
import me.tedwoodworth.diplomacy.lives_and_tax.LivesManager;
import me.tedwoodworth.diplomacy.nations.Guis;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import me.tedwoodworth.diplomacy.spawning.SpawnManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
        TogglePickupCommand.register(getCommand("ta"));
        RecipeCommand.register(getCommand("recipes"));
        ResetWorldCommand.register(getCommand("resetWorld"));
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
        Guis.getInstance().loadRecipeMenus();
        System.out.println("[Diplomacy] Loaded recipe menus");
        LivesManager.getInstance().startScheduler();
        ChatNotifications.getInstance().startScheduler();
        System.out.println("[Diplomacy] Loaded schedulers");
        DiplomacyRecipes.getInstance().loadRecipes();
        System.out.println("[Diplomacy] Loaded recipes");
        Entities.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded entity events");
        GuardManager.getInstance().registerEvents();
        System.out.println("[Diplomacy] Loaded guard events");
//        FluidDynamics.getInstance().registerEvents();
//        System.out.println("[Diplomacy] Loaded fluid dynamics events");


        // check if world is new
        var world = Bukkit.getWorld("world");
        var border = world.getWorldBorder();
        if (border.getSize() < 16) {
            var size = DiplomacyConfig.getInstance().getWorldSize();
            border.setSize(size);
            var chunks = (int) (size / 16.0);
//            fillWorld(Bukkit.getWorld("world"), chunks / 2, chunks / 2 * (-1), chunks / 2 * (-1), 0, Math.pow(chunks, 2));
        }

        // setup economy
        if (!setupEconomy()) {
            throw new RuntimeException("Unable to set up vault economy.");
        }

//        RandomBlockTicker.getInstance();
//        System.out.println("[Diplomacy] Initialized random block ticker");

//        RandomSubchunkTicker.getInstance();
//        System.out.println("[Diplomacy] Initialized random subchunk ticker");
    }

//    private void fillWorld(World world, int radius, int currentX, int currentZ, int count, double total) {
//        var percent = (100 * count) / total;
//        System.out.println("Generating new world: " + String.format("%.2f%%", percent));
//        var chunk = world.getChunkAt(currentX, currentZ);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 0), 900);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 1), 850);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 2), 800);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 3), 750);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 4), 700);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 5), 650);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 6), 600);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 7), 550);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 8), 500);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 9), 480);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 10), 460);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 11), 440);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 12), 420);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 13), 400);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 14), 380);
//        GeoData.getInstance().setSubchunkTemperature(new SubChunk(chunk, 15), 360);
//        for (int y = 0; y < 128; y++) {
//            for (int x = 0; x < 16; x++) {
//                for (int z = 0; z < 16; z++) {
//                    var block = chunk.getBlock(x, y, z);
//                    block.setType(Material.LAVA);
//                }
//            }
//        }
//
//        for (int y = 128; y < world.getMaxHeight(); y++) {
//            for (int x = 0; x < 16; x++) {
//                for (int z = 0; z < 16; z++) {
//                    var block = chunk.getBlock(x, y, z);
//                    block.setType(Material.AIR);
//                }
//            }
//        }
//        currentZ++;
//        if (currentZ == radius) {
//            currentZ = radius * (-1);
//            currentX++;
//        }
//        if (currentX < radius) {
//            int finalCurrentX = currentX;
//            int finalCurrentZ = currentZ;
//            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> fillWorld(world, radius, finalCurrentX, finalCurrentZ, count + 1, total), 1L);
//
//        } else {
//            System.out.println("Generating new world: 100%");
//        }
//    }


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
