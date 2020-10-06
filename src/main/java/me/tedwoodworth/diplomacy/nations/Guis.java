package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.*;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Guis {
    private static Guis instance = null;
    private int updateTaskID = -1;

    private final Map<String, InventoryGui> nationMenus;
    private Map<String, StaticGuiElement> nationElements;
    private final List<String> alphabetically;
    private final List<String> balance;
    private final List<String> territory;
    private final List<String> population;
    private final List<String> age;
    private StaticGuiElement timeElement;

    private final GuiElementGroup alpGroup = new GuiElementGroup('g');
    private final GuiElementGroup balGroup = new GuiElementGroup('g');
    private final GuiElementGroup terGroup = new GuiElementGroup('g');
    private final GuiElementGroup popGroup = new GuiElementGroup('g');
    private final GuiElementGroup ageGroup = new GuiElementGroup('g');
    private final GuiElementGroup rAlpGroup = new GuiElementGroup('g');
    private final GuiElementGroup rBalGroup = new GuiElementGroup('g');
    private final GuiElementGroup rTerGroup = new GuiElementGroup('g');
    private final GuiElementGroup rPopGroup = new GuiElementGroup('g');
    private final GuiElementGroup rAgeGroup = new GuiElementGroup('g');

    private InventoryGui alpNations = null;
    private InventoryGui rAlpNations = null;
    private InventoryGui balNations = null;
    private InventoryGui rBalNations = null;
    private InventoryGui terNations = null;
    private InventoryGui rTerNations = null;
    private InventoryGui popNations = null;
    private InventoryGui rPopNations = null;
    private InventoryGui ageNations = null;
    private InventoryGui rAgeNations = null;

    public static Guis getInstance() {
        if (instance == null) {
            instance = new Guis();
        }
        return instance;
    }

    public Guis() {
        nationMenus = new HashMap<>();

        // Nation sorting lists
        alphabetically = new ArrayList<>() {
            public boolean add(String id) {
                if (super.size() == 0) {
                    super.add(id);
                    return true;
                }
                int index = Collections.binarySearch(this, id, ((p1, p2) -> Objects.requireNonNull(Nations.getInstance().get(Integer.parseInt(p1))).getName().compareToIgnoreCase(Objects.requireNonNull(Nations.getInstance().get(Integer.parseInt(p2))).getName())));
                if (index < 0) index = ~index;
                super.add(index, id);
                return true;
            }
        };
        balance = new ArrayList<>() {
            public boolean add(String id) {
                if (super.size() == 0) {
                    super.add(id);
                    return true;
                }
                int index = Collections.binarySearch(this, id, Comparator.comparingDouble((p) -> -Objects.requireNonNull(Nations.getInstance().get(Integer.parseInt(p))).getBalance()));
                if (index < 0) index = ~index;
                super.add(index, id);
                return true;
            }
        };
        territory = new ArrayList<>() {
            public boolean add(String id) {
                if (super.size() == 0) {
                    super.add(id);
                    return true;
                }
                int index = Collections.binarySearch(this, id, Comparator.comparingInt((p) -> -Objects.requireNonNull(Nations.getInstance().get(Integer.parseInt(p))).getChunks().size()));
                if (index < 0) index = ~index;
                super.add(index, id);
                return true;
            }
        };
        population = new ArrayList<>() {
            public boolean add(String id) {
                if (super.size() == 0) {
                    super.add(id);
                    return true;
                }
                int index = Collections.binarySearch(this, id, Comparator.comparingInt((p) -> -Objects.requireNonNull(Nations.getInstance().get(Integer.parseInt(p))).getMembers().size()));
                if (index < 0) index = ~index;
                super.add(index, id);
                return true;
            }
        };
        age = new ArrayList<>() {
            public boolean add(String id) {
                if (super.size() == 0) {
                    super.add(id);
                    return true;
                }
                int index = Collections.binarySearch(this, id, String::compareTo);
                if (index < 0) index = ~index;
                super.add(index, id);
                return true;
            }
        };
        for (var nation : Nations.getInstance().getNations()) {
            var id = nation.getNationID();
            alphabetically.add(id);
            balance.add(id);
            territory.add(id);
            population.add(id);
            age.add(id);
        }

        // Create nation elements
        loadNationElements();

        if (updateTaskID == -1) {
            updateTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::updateNationOrder, 1L, 6000L);
        }

    }

    public void loadNationMenus() {
        for (var nation : new ArrayList<>(Nations.getInstance().getNations())) {
            getInstance().getNationMenu(nation);
        }
    }

    public void loadNationElements() {
        nationElements = new HashMap<>();
        for (var nation : Nations.getInstance().getNations()) {
            nationElements.put(nation.getNationID(), getNationElement(nation));
        }
    }

    public @Nullable GuiElementGroup getNationsGroup(String type) {
        return switch (type) {
            case "alphabetically" -> alpGroup;
            case "territory" -> terGroup;
            case "balance" -> balGroup;
            case "population" -> popGroup;
            case "age" -> ageGroup;
            case "reverseAlphabetically" -> rAlpGroup;
            case "reverseTerritory" -> rTerGroup;
            case "reverseBalance" -> rBalGroup;
            case "reversePopulation" -> rPopGroup;
            case "reverseAge" -> rAgeGroup;
            default -> null;
        };
    }

    public InventoryGui getNations(String type) {
        switch (type) {
            case "reverseAlphabetically" -> {
                if (rAlpNations == null) {
                    rAlpNations = NationGuiFactory.createNations("reverseAlphabetically");
                }
                return rAlpNations;
            }
            case "balance" -> {
                if (balNations == null) {
                    balNations = NationGuiFactory.createNations("balance");
                }
                return balNations;
            }
            case "reverseBalance" -> {
                if (rBalNations == null) {
                    rBalNations = NationGuiFactory.createNations("reverseBalance");
                }
                return rBalNations;
            }
            case "territory" -> {
                if (terNations == null) {
                    terNations = NationGuiFactory.createNations("territory");
                }
                return terNations;
            }
            case "reverseTerritory" -> {
                if (rTerNations == null) {
                    rTerNations = NationGuiFactory.createNations("reverseTerritory");
                }
                return rTerNations;
            }
            case "population" -> {
                if (popNations == null) {
                    popNations = NationGuiFactory.createNations("population");
                }
                return popNations;
            }
            case "reversePopulation" -> {
                if (rPopNations == null) {
                    rPopNations = NationGuiFactory.createNations("reversePopulation");
                }
                return rPopNations;
            }
            case "age" -> {
                if (ageNations == null) {
                    ageNations = NationGuiFactory.createNations("age");
                }
                return ageNations;
            }
            case "reverseAge" -> {
                if (rAgeNations == null) {
                    rAgeNations = NationGuiFactory.createNations("reverseAge");
                }
                return rAgeNations;
            }
            default -> { // Includes "alphabetically"
                if (alpNations == null) {
                    alpNations = NationGuiFactory.createNations("alphabetically");
                }
                return alpNations;
            }
        }
    }

    public StaticGuiElement getTimeElement() {
            if (timeElement == null) {
                updateTimeElement();
            }
            return timeElement;
    }

    public void updateTimeElement() {
        var zone = TimeZone.getTimeZone("America/New_York");
        var zoneTime = Instant.now().atZone(zone.toZoneId());
        var formatter = new DecimalFormat("00");
        String label;
        if (zone.inDaylightTime(new Date(Instant.now().getEpochSecond()))) {
            label = "EST";
        } else {
            label = "EDT";
        }
        timeElement = new StaticGuiElement('X',
                new ItemStack(Material.PAPER),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Last Updated:",
                "" + ChatColor.GRAY + zoneTime.getHour() + ":" + formatter.format(zoneTime.getMinute()) + " " + label
        );
    }

    public void updateNationOrder() {
        updateTimeElement();
        alpGroup.clearElements();
        for (var id : alphabetically) {
            alpGroup.addElement(nationElements.get(id));
            getNations("alphabetically").addElement(getTimeElement());
        }
        rAlpGroup.clearElements();
        for (int i = alphabetically.size() - 1; i >= 0; i--) {
            rAlpGroup.addElement(nationElements.get(alphabetically.get(i)));
            getNations("reverseAlphabetically").addElement(getTimeElement());
        }
        terGroup.clearElements();
        for (var id : territory) {
            terGroup.addElement(nationElements.get(id));
            getNations("territory").addElement(getTimeElement());
        }
        rTerGroup.clearElements();
        for (int i = territory.size() - 1; i >= 0; i--) {
            rTerGroup.addElement(nationElements.get(territory.get(i)));
            getNations("reverseTerritory").addElement(getTimeElement());
        }
        balGroup.clearElements();
        for (var id : balance) {
            balGroup.addElement(nationElements.get(id));
            getNations("balance").addElement(getTimeElement());
        }
        rBalGroup.clearElements();
        for (int i = balance.size() - 1; i >= 0; i--) {
            rBalGroup.addElement(nationElements.get(balance.get(i)));
            getNations("reverseBalance").addElement(getTimeElement());
        }
        popGroup.clearElements();
        for (var id : population) {
            popGroup.addElement(nationElements.get(id));
            getNations("population").addElement(getTimeElement());
        }
        rPopGroup.clearElements();
        for (int i = population.size() - 1; i >= 0; i--) {
            rPopGroup.addElement(nationElements.get(population.get(i)));
            getNations("reversePopulation").addElement(getTimeElement());
        }
        ageGroup.clearElements();
        for (var id : age) {
            ageGroup.addElement(nationElements.get(id));
            getNations("age").addElement(getTimeElement());
        }
        rAgeGroup.clearElements();
        for (int i = age.size() - 1; i >= 0; i--) {
            rAgeGroup.addElement(nationElements.get(age.get(i)));
            getNations("reverseAge").addElement(getTimeElement());
        }
    }

    public StaticGuiElement getNationElement(Nation nation) {
        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        Objects.requireNonNull(bannerMeta).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        var plots = nation.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }

        String status;
        if (nation.getIsOpen()) {
            status = ChatColor.GREEN + "Open";
        } else {
            status = ChatColor.RED + "Closed";
        }

        return new StaticGuiElement('g',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(nation);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + nation.getName(),
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + nation.getMembers().size(),
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + NationGuiFactory.formatter.format(nation.getBalance()),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Borders: " + status,
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + nation.getDateCreated()
        );
    }

    public InventoryGui getNationMenu(Nation nation) {
        if (!nationMenus.containsKey(nation.getNationID())) {
            var menu = NationGuiFactory.create(nation);
            nationMenus.put(nation.getNationID(), menu);
        }
        return nationMenus.get(nation.getNationID());
    }

    public StaticGuiElement getStatusElement(Nation nation) {
        var isOpen = nation.getIsOpen();
        // Create strStatus & ItemStack
        String status;
        ItemStack itemStack;
        if (isOpen) {
            status = "Open";
            itemStack = new ItemStack(Material.OAK_DOOR); //TODO figure out why door doesn't change when clicking from player menu
        } else {
            status = "Closed";
            itemStack = new ItemStack(Material.IRON_DOOR);
        }

        // Border status element
        return new StaticGuiElement('k',
                itemStack,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var diplomacyClicker = DiplomacyPlayers.getInstance().get(clicker.getUniqueId());
                    var testNation = Nations.getInstance().get(diplomacyClicker);
                    if (!Objects.equals(testNation, nation)) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                    try {
                        var nationClass = Objects.requireNonNull(testNation).getMemberClass(diplomacyClicker);
                        var permissions = Objects.requireNonNull(nationClass).getPermissions();
                        if (!permissions.get("CanToggleBorder")) {
                            clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        } else {
                            nation.setIsOpen(!nation.getIsOpen());
                        }
                        return true;
                    } catch (NullPointerException e) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                ChatColor.GRAY + status,
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Toggle border status",
                " ",
                ChatColor.BLUE + "Open Borders: " + ChatColor.GRAY + "/nation open",
                ChatColor.BLUE + "Close Borders: " + ChatColor.GRAY + "/nation close"
        );
    }

    public StaticGuiElement getBannerElement(Nation nation) {
        var banner = nation.getBanner();
        return new StaticGuiElement('b',
                banner,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:",
                " ",
                ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/nation banner"
        );
    }

    public StaticGuiElement getBalanceElement(Nation nation) {
        var nationWealth = nation.getBalance();
        var strNationWealth = "\u00A4" + NationGuiFactory.formatter.format(nationWealth);
        return new StaticGuiElement('e',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                " ",
                ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>",
                ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/nation withdraw <amount>"

        );
    }

    public StaticGuiElement getPlotElement(Nation nation) {
        var plots = nation.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }
        return new StaticGuiElement('l',
                new ItemStack(Material.GRASS_BLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                ChatColor.GRAY + String.valueOf(plots) + label
        );
    }

    public StaticGuiElement getNameElement(Nation nation) {
        return new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                ChatColor.GRAY + nation.getName(),
                " ",
                ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/nation rename <name>"
        );
    }

    public StaticGuiElement getMembersElement(Nation nation) {
        return new StaticGuiElement('c',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = NationGuiFactory.createMembers(nation, (Player) clicker, "alphabet", 0);//TODO change to getMembers
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + nation.getMembers().size(),
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
        );
    }

    public void addNation(Nation nation) {
        var id = nation.getNationID();

        alphabetically.add(id);
        balance.add(id);
        territory.add(id);
        age.add(id);
        population.add(id);
        nationElements.put(id, getNationElement(nation));
        nationMenus.put(id, getNationMenu(nation));
    }

    public void removeNation(String id) {
        alphabetically.remove(id);
        balance.remove(id);
        territory.remove(id);
        age.remove(id);
        population.remove(id);
        nationElements.remove(id);
        nationMenus.remove(id);
    }


    public void updateNation(Nation nation, String type) {
        var id = nation.getNationID();
        var gui = getNationMenu(nation);
        switch (type) {
            case "name" -> {
                alphabetically.remove(id);
                alphabetically.add(id);
                gui.setTitle("" + ChatColor.DARK_GRAY + ChatColor.BOLD + nation.getName() + " Main");
                gui.addElement(getNameElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
            case "balance" -> {
                balance.remove(id);
                balance.add(id);
                gui.addElement(getBalanceElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
            case "territory" -> {
                territory.remove(id);
                territory.add(id);
                gui.addElement(getPlotElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
            case "population" -> {
                population.remove(id);
                population.add(id);
                gui.addElement(getMembersElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
            case "banner" -> {
                gui.addElement(getBannerElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
            case "border" -> {
                gui.addElement(getStatusElement(nation));
                nationElements.replace(id, getNationElement(nation));
            }
        }
        gui.draw();
        nationElements.replace(id, getNationElement(nation));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Guis.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onNationToggleBorder(NationToggleBorderEvent event) {
            updateNation(event.getNation(), "border");
        }

        @EventHandler
        private void onNationChangeBanner(NationChangeBannerEvent event) {
            updateNation(event.getNation(), "banner");
        }

        @EventHandler
        private void onNationChangeBalance(NationChangeBalanceEvent event) {
            updateNation(event.getNation(), "balance");
        }

        @EventHandler
        private void onNationAddChunk(NationAddChunkEvent event) {
            updateNation(event.getNation(), "territory");
        }

        @EventHandler
        private void onNationRemoveChunk(NationRemoveChunkEvent event) {
            updateNation(event.getNation(), "territory");
        }

        @EventHandler
        private void onNationRename(NationRenameEvent event) {
            updateNation(event.getNation(), "name");
        }

        @EventHandler
        private void onNationJoin(NationJoinEvent event) {
            updateNation(event.getNation(), "population");
        }

        @EventHandler
        private void onNationLeave(NationLeaveEvent event) {
            updateNation(event.getNation(), "population");
        }

        @EventHandler
        private void onNationDisband(NationDisbandEvent event) {
            removeNation(event.getNationID());
        }

        @EventHandler
        private void onNationCreate(NationCreateEvent event) {
            addNation(event.getNation());
        }
    }
}
