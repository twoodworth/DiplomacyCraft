package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.NationClass;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClassCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String classUsage = "/class";
    private static final String classRenameUsage = "/class rename <class> <new name>";
    private static final String classPrefixUsage = "/class prefix <class> <new prefix>";
    private static final String classClearPrefixUsage = "/class clearPrefix <class>";
    private static final String classTaxUsage = "/class tax <class> <amount>";

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public static void register(PluginCommand pluginCommand) {
        var classCommand = new ClassCommand();

        pluginCommand.setExecutor(classCommand);
        pluginCommand.setTabCompleter(classCommand);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            classCommand(sender);
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 3) {
                classRename(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + classRenameUsage);
            }
        } else if (args[0].equalsIgnoreCase("prefix")) {
            if (args.length == 3) {
                classPrefix(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + classPrefixUsage);
            }
        } else if (args[0].equalsIgnoreCase("clearPrefix")) {
            if (args.length == 2) {
                classClearPrefix(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + classClearPrefixUsage);
            }
        } else if (args[0].equalsIgnoreCase("tax")) {
            if (args.length == 3) {
                classTax(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + classTaxUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + classUsage);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                return Arrays.asList(
                        "rename",
                        "prefix",
                        "clearPrefix",
                        "tax"
                );
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length == 2) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        var list = new ArrayList<String>();
                        var classes = nation.getClasses();
                        for (var nationClass : classes) {
                            list.add(nationClass.getName());
                        }
                        return list;
                    }
                }
                return null;
            } else if (args[0].equalsIgnoreCase("prefix")) {
                if (args.length == 2) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        var list = new ArrayList<String>();
                        var classes = nation.getClasses();
                        for (var nationClass : classes) {
                            list.add(nationClass.getName());
                        }
                        return list;
                    }
                }
            } else if (args[0].equalsIgnoreCase("clearPrefix")) {
                if (args.length == 2) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        var list = new ArrayList<String>();
                        var classes = nation.getClasses();
                        for (var nationClass : classes) {
                            list.add(nationClass.getName());
                        }
                        return list;
                    }
                }
            } else if (args[0].equalsIgnoreCase("tax")) {
                if (args.length == 2) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        var list = new ArrayList<String>();
                        var classes = nation.getClasses();
                        for (var nationClass : classes) {
                            list.add(nationClass.getName());
                        }
                        return list;
                    }
                }
            }
        }
        return null;
    }


    private void classCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Manage Classes:");
        sender.sendMessage(ChatColor.AQUA + classRenameUsage + ChatColor.GRAY + " Rename a class");
        sender.sendMessage(ChatColor.AQUA + classPrefixUsage + ChatColor.GRAY + " Change a class's prefix");
        sender.sendMessage(ChatColor.AQUA + classClearPrefixUsage + ChatColor.GRAY + " Remove a class's prefix");
        sender.sendMessage(ChatColor.AQUA + classTaxUsage + ChatColor.GRAY + " Set a class's tax");
    }

    private void classRename(CommandSender sender, String className, String newClassName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        if (!canManageClasses) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to rename classes.");
            return;
        }

        NationClass nationClass = null;

        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (className.equalsIgnoreCase(testClassName)) {
                nationClass = testClass;
            }
        }

        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        if (newClassName.equalsIgnoreCase(className)) {
            sender.sendMessage(ChatColor.RED + "The class is already named that.");
            return;
        }

        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (newClassName.equalsIgnoreCase(testClassName)) {
                sender.sendMessage(ChatColor.RED + "Another class is already named that.");
                return;
            }
        }

        for (var strUUID : nation.getMembers()) {
            var player = Bukkit.getPlayer(UUID.fromString(strUUID));
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The class '" + nationClass.getName() + "' has been renamed to '" + newClassName + "'.");
            }
        }

        nation.renameClass(nationClass, newClassName);

    }

    private void classPrefix(CommandSender sender, String className, String newPrefix) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        if (!canManageClasses) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to set class prefixes.");
            return;
        }

        NationClass nationClass = null;

        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (className.equalsIgnoreCase(testClassName)) {
                nationClass = testClass;
            }
        }

        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        if (newPrefix.equalsIgnoreCase(nationClass.getPrefix())) {
            sender.sendMessage(ChatColor.RED + "The class prefix is already set to that.");
            return;
        }

        for (var strUUID : nation.getMembers()) {
            var player = Bukkit.getPlayer(UUID.fromString(strUUID));
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The prefix of the class '" + nationClass.getName() + "' has been set to '" + newPrefix + "'.");
            }
        }

        nation.setClassPrefix(nationClass, newPrefix);
    }

    private void classClearPrefix(CommandSender sender, String className) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        if (!canManageClasses) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to remove class prefixes.");
            return;
        }

        NationClass nationClass = null;

        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (className.equalsIgnoreCase(testClassName)) {
                nationClass = testClass;
            }
        }

        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        if (nationClass.getPrefix() == null) {
            sender.sendMessage(ChatColor.RED + "That class doesn't have a prefix.");
            return;
        }


        for (var strUUID : nation.getMembers()) {
            var player = Bukkit.getPlayer(UUID.fromString(strUUID));
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The prefix of the class '" + nationClass.getName() + "' has been removed.");
            }
        }

        nation.clearClassPrefix(nationClass);
    }

    private void classTax(CommandSender sender, String className, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        if (!canManageClasses) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to set class taxes.");
            return;
        }

        NationClass nationClass = null;

        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (className.equalsIgnoreCase(testClassName)) {
                nationClass = testClass;
            }
        }

        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        var amount = 0.0;

        try {
            amount = Double.parseDouble(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be a number.");
            return;
        }


        if (nationClass.getTax() == amount) {
            sender.sendMessage(ChatColor.RED + "The tax is already set to that.");
            return;
        }


        for (var strUUID : nation.getMembers()) {
            var player = Bukkit.getPlayer(UUID.fromString(strUUID));
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The tax for the class '" + nationClass.getName() + "' has been set to \u00A4" + formatter.format(amount));
            }
        }

        nation.setClassTax(nationClass, amount);
    }
}
