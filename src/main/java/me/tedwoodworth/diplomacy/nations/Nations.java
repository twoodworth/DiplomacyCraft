package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.NationCreateEvent;
import me.tedwoodworth.diplomacy.events.NationDisbandEvent;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class Nations {

    private static Nations instance = null;
    private final File nationConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "nations.yml");
    private final List<Nation> nations = new ArrayList<>();
    private final YamlConfiguration nationConfig;

    public List<Nation> getNations() {
        return nations;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void createNation(String name, DiplomacyPlayer leader) {
        var founder = getOfflinePlayer(leader.getUUID());
        var nextNationID = nationConfig.getString("NextNationID");
        if (nextNationID == null) {
            nationConfig.set("NextNationID", "0");
            nextNationID = "0";
        }

        var nationID = nextNationID;
        nextNationID = String.valueOf(Integer.parseInt(nextNationID) + 1);

        Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Nation.yml")));
        ConfigurationSection nationSection = YamlConfiguration.loadConfiguration(reader);

        nationConfig.set("Nations." + nationID, nationSection);
        nationConfig.set("NextNationID", nextNationID);
        var initializedNationSection = Nation.initializeNation(nationSection, founder, name);
        var nation = new Nation(nationID, initializedNationSection);
        nations.add(nation);
        ScoreboardManager.getInstance().updateScoreboards();
        Bukkit.getPluginManager().callEvent(new NationCreateEvent(nation));
    }

    public static Nations getInstance() {
        if (instance == null) {
            instance = new Nations();
        }
        return instance;
    }

    private Nations() {
        nationConfig = YamlConfiguration.loadConfiguration(nationConfigFile);
        var nationsSection = nationConfig.getConfigurationSection("Nations");
        if (nationsSection == null) {
            nationsSection = nationConfig.createSection("Nations");
        }
        for (var nationID : nationsSection.getKeys(false)) {
            var nationSection = nationConfig.getConfigurationSection("Nations." + nationID);
            if (nationSection == null) {
                nationConfig.set("Nations." + nationID, null);
            } else {
                var nation = new Nation(nationID, nationSection);
                nations.add(nation);
            }
        }

    }

    @Nullable
    public Nation get(DiplomacyPlayer player) {
        for (var nation : nations) {
            for (var testPlayer : nation.getMembers()) {
                if (player.equals(testPlayer)) {
                    return nation;
                }
            }
        }
        return null;
    }

    @Nullable
    public Nation get(String name) {
        for (var nation : nations) {
            if (name.equalsIgnoreCase(nation.getName())) {
                return nation;
            }
        }
        return null;
    }

    @Nullable
    public Nation get(int id) {
        for (var nation : nations) {
            if (id == Integer.parseInt(nation.getNationID())) {
                return nation;
            }
        }
        return null;
    }

    @Nullable
    public Nation getFromID(String id) {
        for (var nation : nations) {
            if (id.equalsIgnoreCase(nation.getNationID())) {
                return nation;
            }
        }
        return null;
    }

    public void rename(Nation nation, String name) {
        nationConfig.set("Nations." + nation.getNationID() + ".Name", name);
        nation.setName(name);
    }

    public void removeNation(Nation nation) {
        nationConfig.set("Nations." + nation.getNationID(), null);
        var nationID = nation.getNationID();
        nations.remove(nation);
        ScoreboardManager.getInstance().updateScoreboards();
        Bukkit.getPluginManager().callEvent(new NationDisbandEvent(nationID));
    }

    public void listNations(Player player, int page) {
        var dPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(dPlayer);
        var nations = Nations.getInstance().getNations();
        if (nations.size() == 0) {
            player.sendMessage(ChatColor.RED + "There are currently no nations on the server to list.");
            return;
        }
        nations.sort(Comparator.comparingInt(p -> -p.getMembers().size()));
        var pages = nations.size() / 10;
        if (nations.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\nNation List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= nations.size()) break;

            var testNation = nations.get(i);
            net.md_5.bungee.api.ChatColor color;
            if (nation == null) {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            } else if (nation.equals(testNation)) {
                color = net.md_5.bungee.api.ChatColor.GREEN;
            } else if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.RED;
            } else {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            }

            var hoverText = new ComponentBuilder()
                    .append("[View Info]")
                    .color(color)
                    .bold(true)
                    .create();


            var nationText = new ComponentBuilder();
            nationText
                    .append("\n - ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY)
                    .bold(false)
                    .append(testNation.getName())
                    .color(color)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation info " + testNation.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message
                    .append(nationText.create())
                    .append(" (pop: " + testNation.getMembers().size() + ")")
                    .color(color)
                    .bold(false);
        }

        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation list " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }
        player.spigot().sendMessage(message.create());
    }

    public void listAllyNations(Player player, Nation otherNation, int page) {
        var dPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(dPlayer);
        var nationIDs = otherNation.getAllyNationIDs();
        var nations = new ArrayList<Nation>();
        for (var id : nationIDs) {
            nations.add(Nations.getInstance().getFromID(id));
        }
        if (nations.size() == 0) {
            player.sendMessage(ChatColor.RED + "This nation does not have any allies.");
            return;
        }
        nations.sort(Comparator.comparingInt(p -> -p.getMembers().size()));
        var pages = nations.size() / 10;
        if (nations.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\n" + otherNation.getName() + " Ally List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= nations.size()) break;
            var testNation = nations.get(i);

            net.md_5.bungee.api.ChatColor color;
            if (nation == null) {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            } else if (nation.equals(testNation)) {
                color = net.md_5.bungee.api.ChatColor.GREEN;
            } else if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.RED;
            } else {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            }

            var hoverText = new ComponentBuilder()
                    .append("[View Info]")
                    .color(color)
                    .bold(true)
                    .create();


            var nationText = new ComponentBuilder();
            nationText
                    .append("\n - ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY)
                    .bold(false)
                    .append(testNation.getName())
                    .color(color)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation info " + testNation.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message
                    .append(nationText.create())
                    .append(" (pop: " + testNation.getMembers().size() + ")")
                    .color(color)
                    .bold(false);
        }

        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation allies " + otherNation.getName() + " " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }

        player.spigot().sendMessage(message.create());
    }

    public void listMembers(Player player, Nation otherNation, int page) {
        var members = new ArrayList<>(otherNation.getMembers());
        if (members.size() == 0) {
            player.sendMessage(ChatColor.RED + "This nation does not have any members.");
            return;
        }
        members.sort(Comparator.comparingInt(p -> -Integer.parseInt(otherNation.getMemberClass(p).getClassID())));
        var pages = members.size() / 10;
        if (members.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\n" + otherNation.getName() + " Member List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= members.size()) break;
            var testPlayer = members.get(i);

            var hoverText = new ComponentBuilder()
                    .append("[View Player Info]")
                    .color(net.md_5.bungee.api.ChatColor.GOLD)
                    .bold(true)
                    .create();


            var rank = otherNation.getMemberClass(testPlayer).getPrefix();
            var playerText = new ComponentBuilder();
            playerText
                    .append("\n - ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY)
                    .bold(false);
            if (rank != null && !rank.equals("")) {
                playerText
                        .append(rank + " ")
                        .color(net.md_5.bungee.api.ChatColor.GOLD)
                        .bold(true);
            }

            playerText
                    .append(testPlayer.getOfflinePlayer().getName())
                    .color(net.md_5.bungee.api.ChatColor.GOLD)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/player info " + testPlayer.getOfflinePlayer().getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message.append(playerText.create());
        }

        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation members " + otherNation.getName() + " " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }

        player.spigot().sendMessage(message.create());
    }


    public void listOutlaws(Player player, Nation otherNation, int page) {
        var outlaws = new ArrayList<>(otherNation.getOutlaws());
        if (outlaws.size() == 0) {
            player.sendMessage(ChatColor.RED + "This nation does not have any outlaws.");
            return;
        }
        var pages = outlaws.size() / 10;
        if (outlaws.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\n" + otherNation.getName() + " Member List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= outlaws.size()) break;
            var testPlayer = Bukkit.getOfflinePlayer(outlaws.get(i));

            var hoverText = new ComponentBuilder()
                    .append("[View Player Info]")
                    .color(net.md_5.bungee.api.ChatColor.RED)
                    .bold(true)
                    .create();

            var playerText = new ComponentBuilder();
            playerText
                    .append("\n - ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY)
                    .bold(false)
                    .append(testPlayer.getName())
                    .color(net.md_5.bungee.api.ChatColor.RED)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/player info " + testPlayer.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message.append(playerText.create());
        }

        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation outlaws " + otherNation.getName() + " " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }

        player.spigot().sendMessage(message.create());
    }

    public void listEnemyNations(Player player, Nation otherNation, int page) {
        var dPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(dPlayer);
        var nationIDs = otherNation.getEnemyNationIDs();
        var nations = new ArrayList<Nation>();
        for (var id : nationIDs) {
            nations.add(Nations.getInstance().getFromID(id));
        }
        if (nations.size() == 0) {
            player.sendMessage(ChatColor.RED + "This nation does not have any enemies.");
            return;
        }
        nations.sort(Comparator.comparingInt(p -> -p.getMembers().size()));
        var pages = nations.size() / 10;
        if (nations.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\n" + otherNation.getName() + " Enemy List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= nations.size()) break;
            var testNation = nations.get(i);

            net.md_5.bungee.api.ChatColor color;
            if (nation == null) {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            } else if (nation.equals(testNation)) {
                color = net.md_5.bungee.api.ChatColor.GREEN;
            } else if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                color = net.md_5.bungee.api.ChatColor.RED;
            } else {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            }

            var hoverText = new ComponentBuilder()
                    .append("[View Info]")
                    .color(color)
                    .bold(true)
                    .create();


            var nationText = new ComponentBuilder();
            nationText
                    .append("\n - ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY)
                    .bold(false)
                    .append(testNation.getName())
                    .color(color)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation info " + testNation.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message
                    .append(nationText.create())
                    .append(" (pop: " + testNation.getMembers().size() + ")")
                    .color(color)
                    .bold(false);
        }

        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation enemies " + otherNation.getName() + " " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }
        player.spigot().sendMessage(message.create());
    }


    public void listGroups(Nation nation, Player player, int page) {
        var dPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var groups = nation.getGroups();
        if (groups.size() == 0) {
            player.sendMessage(ChatColor.RED + "This nation does not have any groups");
            return;
        }
        var pages = groups.size() / 10;
        if (groups.size() % 10 != 0) pages++;
        var message = new ComponentBuilder();
        message.append("\n" + nation.getName() + " Group List (page " + page + "/" + pages + ")")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true);
        for (int i = (page - 1) * 10; i < page * 10; i++) {
            if (i >= groups.size()) break;
            message.append("\n- ")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true);

            var testGroup = groups.get(i);

            net.md_5.bungee.api.ChatColor color;
            if (testGroup.getLeaders().contains(dPlayer)) {
                color = net.md_5.bungee.api.ChatColor.GREEN;
            } else if (testGroup.getMembers().contains(dPlayer)) {
                color = net.md_5.bungee.api.ChatColor.DARK_GREEN;
            } else {
                color = net.md_5.bungee.api.ChatColor.AQUA;
            }

            var hoverText = new ComponentBuilder()
                    .append("[View Info]")
                    .color(color)
                    .bold(true)
                    .create();


            var groupText = new ComponentBuilder();
            groupText
                    .append("\n" + testGroup.getName())
                    .color(color)
                    .bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group info " + testGroup.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

            message
                    .append(groupText.create())
                    .append(" (chunks: " + testGroup.getChunks().size() + ")")
                    .color(color)
                    .bold(false);
        }
        if (page < pages) {
            var nextText = new ComponentBuilder()
                    .append("[View Page " + (page + 1) + "]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .create();

            message.append("\nGo to page " + (page + 1))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation groups " + nation.getName() + " " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nextText)));

        }
        player.spigot().sendMessage(message.create());
    }

    public static boolean isWilderness(Nation nation) {
        return nation == null;
    }

    public void save() {
        try {
            nationConfig.save(nationConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }
    }
}
