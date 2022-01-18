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

public class ClassCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String classUsage = "/class";
    private static final String classRenameUsage = "/class rename <class> <new name>";
    private static final String classPrefixUsage = "/class prefix <class> <new prefix>";
    private static final String classClearPrefixUsage = "/class clearPrefix <class>";

    /**
     * Used for formatting a number as a dollar value
     */
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    /**
     * Registers ClassCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var classCommand = new ClassCommand();

        pluginCommand.setExecutor(classCommand);
        pluginCommand.setTabCompleter(classCommand);
    }

    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a class command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param label:   Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
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
        } else {
            sender.sendMessage(incorrectUsage + classUsage);
        }
        return true;
    }

    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param alias:   Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                var list = Arrays.asList(
                        "rename",
                        "prefix",
                        "clearPrefix"
                );
                var list1 = new ArrayList<String>();
                for (var val : list) {
                    if (val.toLowerCase().contains(args[0].toLowerCase()))
                        list1.add(val);
                }
                return list1;
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length == 2) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        var list = new ArrayList<String>();
                        var classes = nation.getClasses();
                        for (var nationClass : classes) {
                            if (nationClass.getName().toLowerCase().contains(args[1].toLowerCase()))
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
                            if (nationClass.getName().toLowerCase().contains(args[1].toLowerCase()))
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
            }
        }
        return null;
    }


    /**
     * Sends a player a list of class commands
     * @param sender
     */
    private void classCommand(CommandSender sender) {
        // return if not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Classes " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "1" + ChatColor.GOLD + "/" + ChatColor.RED + "1" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/class rename" + ChatColor.WHITE + " Rename a class");
        sender.sendMessage(ChatColor.GOLD + "/class prefix" + ChatColor.WHITE + " Change a class's prefix");
        sender.sendMessage(ChatColor.GOLD + "/class clearPrefix" + ChatColor.WHITE + " Remove a class's prefix");
    }

    /**
     * Renames a specified class to a new name
     * @param sender: Sender of command
     * @param className: Name of class to rename
     * @param newClassName: New name of class
     */
    private void classRename(CommandSender sender, String className, String newClassName) {
        // check if not player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        // check if player does not belong to nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        // check if player does not have permission to rename
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

        // check if class does not exist
        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        // check if class already has the new name
        if (newClassName.equalsIgnoreCase(className)) {
            sender.sendMessage(ChatColor.RED + "The class is already named that.");
            return;
        }

        // check if a different class already has that name
        for (var testClass : nation.getClasses()) {
            var testClassName = testClass.getName();
            if (newClassName.equalsIgnoreCase(testClassName)) {
                sender.sendMessage(ChatColor.RED + "Another class is already named that.");
                return;
            }
        }

        // check if new name is too long
        if (newClassName.length() > 16) {
            sender.sendMessage(ChatColor.RED + "That name is too long.");
            return;
        }

        // send notification to nation members
        for (var member : nation.getMembers()) {
            var player = Bukkit.getPlayer(member.getUUID());
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The class '" + nationClass.getName() + "' has been renamed to '" + newClassName + "'.");
            }
        }

        // rename class
        nation.renameClass(nationClass, newClassName);

    }

    /**
     * Sets the prefix of a given class
     * @param sender: Sender of command
     * @param className: Name of class
     * @param newPrefix: New prefix of class
     */
    private void classPrefix(CommandSender sender, String className, String newPrefix) {
        // check if not player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        // check if not in a nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        // check if insufficient permission
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

        // check if class does not exist
        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        // check if class prefix is already set to new prefix
        if (newPrefix.equalsIgnoreCase(nationClass.getPrefix())) {
            sender.sendMessage(ChatColor.RED + "The class prefix is already set to that.");
            return;
        }

        // check if prefix is too long
        if (newPrefix.length() > 16) {
            sender.sendMessage(ChatColor.RED + "That prefix is too long.");
            return;
        }

        // send notification to nation members
        for (var member : nation.getMembers()) {
            var player = Bukkit.getPlayer(member.getUUID());
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The prefix of the class '" + nationClass.getName() + "' has been set to '" + newPrefix + "'.");
            }
        }

        // update prefix
        nation.setClassPrefix(nationClass, newPrefix);
    }

    /**
     * Removes prefix from a given class
     * @param sender: Sender of command
     * @param className: Name of class
     */
    private void classClearPrefix(CommandSender sender, String className) {
        // check if not player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        // check if not in nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canManageClasses = permissions.get("CanManageClasses");

        // check if insufficient permission
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

        // check if class does not exist
        if (nationClass == null) {
            sender.sendMessage(ChatColor.RED + "Class not found.");
            return;
        }

        // check if class already has no prefix
        if (nationClass.getPrefix() == null) {
            sender.sendMessage(ChatColor.RED + "That class doesn't have a prefix.");
            return;
        }

        // send notification to nation members
        for (var member : nation.getMembers()) {
            var player = Bukkit.getPlayer(member.getUUID());
            if (player != null) {
                player.sendMessage(ChatColor.AQUA + "The prefix of the class '" + nationClass.getName() + "' has been removed.");
            }
        }

        // remove prefix
        nation.clearClassPrefix(nationClass);
    }
}
