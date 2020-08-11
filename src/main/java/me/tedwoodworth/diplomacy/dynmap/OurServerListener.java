package me.tedwoodworth.diplomacy.dynmap;

import me.tedwoodworth.diplomacy.events.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class OurServerListener implements Listener {

    private final DiplomacyDynmap kernel;

    public OurServerListener(final DiplomacyDynmap kernel) {
        this.kernel = kernel;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        final Plugin plugin = event.getPlugin();
        final String name = plugin.getDescription().getName();
        if (name.equals("dynmap") || name.equals("Diplomacy")) {
            if (kernel.getDynmap().isEnabled()) {
                kernel.activate();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationJoin(NationJoinEvent event) {
        if (kernel.isPlayersets()) {
            kernel.requestUpdatePlayerSet(event.getNation().getNationID());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationLeave(NationLeaveEvent event) {
        if (kernel.isPlayersets()) {
            kernel.requestUpdatePlayerSet(event.getNation().getNationID());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationCreate(NationCreateEvent event) {
        if (kernel.isPlayersets()) {
            kernel.requestUpdatePlayerSet(event.getNation().getNationID());
        }
        kernel.requestUpdateDiplomacy();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationDisband(NationDisbandEvent event) {
        if (kernel.isPlayersets()) {
            kernel.requestUpdatePlayerSet(event.getNationID());
        }
        kernel.requestUpdateDiplomacy();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationRename(NationRenameEvent event) {
        kernel.requestUpdateDiplomacy();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationAddChunk(NationAddChunkEvent event) {
        kernel.requestUpdateDiplomacy();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationRemoveChunk(NationRemoveChunkEvent event) {
        kernel.requestUpdateDiplomacy();
    }
}
