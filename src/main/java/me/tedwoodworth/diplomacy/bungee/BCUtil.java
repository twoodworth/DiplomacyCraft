package me.tedwoodworth.diplomacy.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.entity.Player;

public class BCUtil {

    public static void setServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Diplomacy.getInstance(), "BungeeCord", out.toByteArray());
    }
}
