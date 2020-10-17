package me.tedwoodworth.diplomacy.players;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.*;

public class AccountManager {
    private static AccountManager instance = null;
    private final File accountManagerFile = new File(Diplomacy.getInstance().getDataFolder(), "accounts.yml");
    private final YamlConfiguration config;
    private final Set<Account> accounts = new HashSet<>();

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    private AccountManager() {
        config = YamlConfiguration.loadConfiguration(accountManagerFile);
        save();
    }

    public void save() {
        try {
            for (var account : accounts) {
                config.set("Accounts." + account.getId(), account.getConfigSection());
            }
            config.save(accountManagerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @Nullable Account getAccount(UUID uuid) {
        for (var account : accounts) {
            if (account.getPlayerIDs().contains(uuid)) return account;
        }
        return null;
    }

    public Account getAccount(UUID uuid, InetAddress ip) {
        Account playerAccount = null;
        Account ipAccount = null;
        var count = 0;

        // Find accounts
        for (var testAccount : accounts) {
            if (testAccount.getPlayerIDs().contains(uuid)) {
                playerAccount = testAccount;
                count++;
            }
            if (testAccount.getAddresses().contains(ip)) {
                ipAccount = testAccount;
                count++;
            }
            if (count == 2) {
                break;
            }
        }

        // Add new uuid / ip if missing
        if (playerAccount != null && ipAccount == null) {
            playerAccount.addAddress(ip);
            return playerAccount;
        } else if (playerAccount == null && ipAccount != null) {
            ipAccount.addPlayerID(uuid);
            return ipAccount;
        } else if (playerAccount != null && ipAccount != null && !playerAccount.equals(ipAccount)) {
            mergeAccounts(playerAccount, ipAccount);
            ScoreboardManager.getInstance().updateScoreboards();
            return playerAccount;
        } else if (playerAccount == null && ipAccount == null) {

            String nextAccountID = config.getString("NextAccountID");
            if (nextAccountID == null) {
                config.set("NextAccountID", "0");
                nextAccountID = "0";
            }

            var id = nextAccountID;
            nextAccountID = String.valueOf(Integer.parseInt(nextAccountID) + 1);
            config.set("NextAccountID", nextAccountID);

            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Account.yml")));
            ConfigurationSection accountSection = YamlConfiguration.loadConfiguration(reader);
            var strAddresses = new ArrayList<String>();
            var strUUIDs = new ArrayList<String>();
            strAddresses.add(ip.getHostAddress());
            strUUIDs.add(uuid.toString());
            accountSection.set("UUIDs", strUUIDs);
            accountSection.set("Lives", DiplomacyPlayers.getInstance().get(uuid).getLives());
            accountSection.set("Addresses", strAddresses);
            accountSection.set("Main", uuid.toString());
            config.set("Accounts." + id, accountSection);
            var account = new Account(accountSection, id);
            accounts.add(account);
            ScoreboardManager.getInstance().updateScoreboards();
            return account;
        } else {
            return playerAccount;
        }

    }

    public void mergeAccounts(Account playerAccount, Account ipAccount) {
        playerAccount.setLives(Math.max(playerAccount.getLives() + ipAccount.getLives() - 20, 1));
        for (var uuid : new HashSet<>(ipAccount.getPlayerIDs())) {
            playerAccount.addPlayerID(uuid);
            ipAccount.removePlayerID(uuid);
        }
        for (var address : new HashSet<>(ipAccount.getAddresses())) {
            playerAccount.addAddress(address);
            ipAccount.removeAddress(address);
        }
        for (var uuid : playerAccount.getPlayerIDs()) {
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
            diplomacyPlayer.setLives(playerAccount.getLives());
            var player = diplomacyPlayer.getPlayer().getPlayer();
            if (player != null) {
                player.sendMessage(ChatColor.BLUE + "Account has been merged with " + Bukkit.getOfflinePlayer(ipAccount.getMain()).getName() + ", you now have " + ipAccount.getLives() + " lives.");
            }
        }
        accounts.remove(ipAccount);
    }


}
