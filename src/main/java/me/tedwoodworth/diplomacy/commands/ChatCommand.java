//package me.tedwoodworth.diplomacy.commands;
//
//import me.tedwoodworth.diplomacy.chat.ChatManager;
//import me.tedwoodworth.diplomacy.nations.Nations;
//import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
//import org.bukkit.ChatColor;
//import org.bukkit.command.*;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//public class ChatCommand implements CommandExecutor, TabCompleter {
//    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
//    private static final String gcUsage = "/gc";
//    private static final String acUsage = "/ac";
//    private static final String ncUsage = "/nc";
//    private static final String lcUsage = "/lc";
//
//    public static void register(PluginCommand pluginCommand) {
//        var chatCommand = new ChatCommand();
//
//        pluginCommand.setExecutor(chatCommand);
//        pluginCommand.setTabCompleter(chatCommand);
//    }
//
//    @Override
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        if (command.getName().equalsIgnoreCase("gc")) {
//            if (args.length == 0) {
//                gc(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + gcUsage);
//            }
//        } else if (command.getName().equalsIgnoreCase("ac")) {
//            if (args.length == 0) {
//                ac(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + acUsage);
//            }
//        } else if (command.getName().equalsIgnoreCase("nc")) {
//            if (args.length == 0) {
//                nc(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + ncUsage);
//            }
//        } else if (command.getName().equalsIgnoreCase("lc")) {
//            if (args.length == 0) {
//                lc(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + lcUsage);
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
//        return null;
//    }
//
//    private void gc(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        if (ChatManager.getInstance().getChatMode(player) == null) {
//            ChatManager.getInstance().setChatMode(player, "0");
//        }
//
//
//        if (ChatManager.getInstance().getChatMode(player).equals("0")) {
//            sender.sendMessage(ChatColor.DARK_RED + "You are already in global chat.");
//            return;
//        }
//
//        ChatManager.getInstance().setChatMode(player, "0");
//        player.sendMessage(ChatColor.AQUA + "You have joined global chat.");
//    }
//
//    private void ac(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        var nation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(player.getUniqueId()));
//        if (nation == null) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to enter ally chat.");
//            return;
//        }
//
//        if (ChatManager.getInstance().getChatMode(player) == null) {
//            ChatManager.getInstance().setChatMode(player, "0");
//        }
//
//        if (ChatManager.getInstance().getChatMode(player).equals("2")) {
//            sender.sendMessage(ChatColor.DARK_RED + "You are already in ally chat.");
//            return;
//        }
//
//        ChatManager.getInstance().setChatMode(player, "2");
//        player.sendMessage(ChatColor.AQUA + "You have joined ally chat.");
//    }
//
//    private void nc(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        var nation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(player.getUniqueId()));
//        if (nation == null) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to enter nation chat.");
//            return;
//        }
//
//        if (ChatManager.getInstance().getChatMode(player) == null) {
//            ChatManager.getInstance().setChatMode(player, "0");
//        }
//
//        if (ChatManager.getInstance().getChatMode(player).equals("3")) {
//            sender.sendMessage(ChatColor.DARK_RED + "You are already in nation chat.");
//            return;
//        }
//
//        ChatManager.getInstance().setChatMode(player, "3");
//        player.sendMessage(ChatColor.AQUA + "You have joined nation chat.");
//    }
//
//    private void lc(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//        var player = (Player) sender;
//
//        if (ChatManager.getInstance().getChatMode(player) == null) {
//            ChatManager.getInstance().setChatMode(player, "0");
//        }
//
//        if (ChatManager.getInstance().getChatMode(player).equals("1")) {
//            sender.sendMessage(ChatColor.DARK_RED + "You are already in local chat.");
//            return;
//        }
//
//        ChatManager.getInstance().setChatMode(player, "1");
//        player.sendMessage(ChatColor.AQUA + "You have joined local chat.");
//    }
//}
