package me.tedwoodworth.diplomacy.dynmap;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.dynmap.area.AreaCommon;
import me.tedwoodworth.diplomacy.dynmap.area.AreaStyle;
import me.tedwoodworth.diplomacy.dynmap.blocks.NationChunk;
import me.tedwoodworth.diplomacy.dynmap.blocks.NationChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.awt.*;
import java.util.*;

public class DiplomacyDynmap {
    private Map<String, AreaMarker> resareas = new HashMap<>();
    private Map<String, Marker> resmark = new HashMap<>();
    private boolean reload = false;
    private Plugin dynmap;
    private Plugin diplomacy = Bukkit.getPluginManager().getPlugin("Diplomacy");
    private DynmapAPI dynmapAPI;
    private boolean displayNationName;
    private Diplomacy diplomacyAPI;
    private int blocksize;

    private MarkerSet set;
    private MarkerAPI markerAPI;
    private boolean use3d;
    private String infoWindow;
    private Map<String, AreaStyle> cusstyle;
    private Set<String> visible;
    private Set<String> hidden;
    private boolean playersets;
    private long updatePeriod;
    private boolean stop;


    private static DiplomacyDynmap instance = null;

    public static DiplomacyDynmap getInstance() {
        if (instance == null) {
            instance = new DiplomacyDynmap();
        }
        return instance;
    }

    public void load() {
        var pluginManager = Bukkit.getPluginManager();

        dynmap = pluginManager.getPlugin("dynmap");
        if (dynmap == null) {
            return;
        }

        dynmapAPI = (DynmapAPI) dynmap;

        if (dynmap.isEnabled()) {
            activate();
        }
    }

    public void activate() {
        markerAPI = ((DynmapAPI) dynmap).getMarkerAPI();
        if (markerAPI == null) {
            return;
        }

        diplomacyAPI = Diplomacy.getInstance();

        blocksize = 16;

        if (reload) {
            if (set != null) {
                set.deleteMarkerSet();
                set = null;
            }
        } else {
            reload = true;
        }

        if (set == null) {
            set = markerAPI.createMarkerSet("diplomacy.markerset", "Diplomacy", null, false);
        } else {
            set.setMarkerSetLabel("Diplomacy");
        }

        if (set == null) {
            return;
        }
        resareas.clear();
        resmark.clear();

        set.setMinZoom(0);
        set.setLayerPriority(10);
        set.setHideByDefault(false);
        use3d = false;
        infoWindow = "<div class=\"infoWindow\"><span style=\"font-size:120%;\">%nation%</span>" +
                "<br /><span style=\"font-weight:bold;\">%Leader%: %leaders%</span>" +
                "<br /><span style=\"font-weight:bold;\">%Assistant%: %assistants%</span></div>" +
                "<br /><span style=\"font-weight:bold;\">%Diplomat%: %diplomats%</span></div>";
        displayNationName = true;

        var color = Color.decode("#FF0000");
        cusstyle = new HashMap<>();

        playersets = false;

        updatePeriod = 6000L;
        stop = false;

        scheduleSyncDelayedTask(new DiplomacyUpdate(this), 40L);
        Bukkit.getServer().getPluginManager().registerEvents(new OurServerListener(), diplomacy);
    }

    public void updateClaimedChunk() {//TODO optimize
        Map<String, AreaMarker> newmap = new HashMap<>();
        Map<String, Marker> newmark = new HashMap<>();

        Map<String, NationChunks> chunks_by_nation = new HashMap<>();

        var nations = Nations.getInstance().getNations();
        for (var nation : nations) {
            var diplomacyChunks = nation.getChunks();
            var nationID = nation.getNationID();
            var nationChunks = chunks_by_nation.get(nationID);
            if (nationChunks == null) {
                nationChunks = new NationChunks();
            }
            chunks_by_nation.put(nationID, nationChunks);

            for (final var diplomacyChunk : diplomacyChunks) {
                final var chunk = diplomacyChunk.getChunk();
                final var world = chunk.getWorld().getName();

                var blocks = nationChunks.getChunks().computeIfAbsent(world, k -> new LinkedList<>());

                blocks.add(new NationChunk(chunk.getX(), chunk.getZ()));
            }
        }

        for (final var nation : nations) {
            final String nationName = nation.getName();
            final String nationID = nation.getNationID();
            final NationChunks nationChunks = chunks_by_nation.get(nationID);
            if (nationChunks == null) continue;

            for (var worldChunks : nationChunks.getChunks().entrySet()) {
                handleNationOnWorld(nationName, nation, worldChunks.getKey(), worldChunks.getValue(), newmap, newmark);
            }
            nationChunks.clear();
        }
        chunks_by_nation.clear();

        /* Now, review old map - anything left is gone */
        for (final AreaMarker oldm : resareas.values()) {
            oldm.deleteMarker();
        }

        for (final Marker oldm : resmark.values()) {
            oldm.deleteMarker();
        }
        /* And replace with new map */
        resareas = newmap;
        resmark = newmark;
    }

    private void handleNationOnWorld(String nationName, Nation nation, String world, LinkedList<NationChunk> chunks, Map<String, AreaMarker> newmap, Map<String, Marker> newmark) {
        var poly_index = 0;

        final var desc = AreaCommon.formatInfoWindow(infoWindow, nation);

        if (isVisible(nation.getNationID(), world)) {
            if (chunks.isEmpty()) {
                return;
            }
            var nodeVals = new LinkedList<NationChunk>();
            var curChunks = new TileFlags();

            for (final var chunk : chunks) {
                curChunks.setFlag(chunk.getX(), chunk.getZ(), true);
                nodeVals.addLast(chunk);
            }

            while (nodeVals != null) {
                LinkedList<NationChunk> ournodes = null;
                LinkedList<NationChunk> newlist = null;
                TileFlags ourChunks = null;
                int minx = Integer.MAX_VALUE;
                int minz = Integer.MAX_VALUE;
                for (var node : nodeVals) {
                    final int nodeX = node.getX();
                    final int nodeZ = node.getZ();
                    /* If we need to start shape, and this block is not part of one yet */
                    if ((ourChunks == null) && curChunks.getFlag(nodeX, nodeZ)) {
                        ourChunks = new TileFlags();  /* Create map for shape */
                        ournodes = new LinkedList<>();
                        floodFillTarget(curChunks, ourChunks, nodeX, nodeZ);   /* Copy shape */
                        ournodes.add(node); /* Add it to our node list */
                        minx = nodeX;
                        minz = nodeZ;
                    }
                    /* If shape found, and we're in it, add to our node list */
                    else if ((ourChunks != null) && ourChunks.getFlag(nodeX, nodeZ)) {
                        ournodes.add(node);
                        if (nodeX < minx) {
                            minx = nodeX;
                            minz = nodeZ;
                        } else if ((nodeX == minx) && (nodeZ < minz)) {
                            minz = nodeZ;
                        }
                    } else {  /* Else, keep it in the list for the next polygon */
                        if (newlist == null) newlist = new LinkedList<>();
                        newlist.add(node);
                    }
                }
                nodeVals = newlist; /* Replace list (null if no more to process) */
                if (ourChunks != null) {
                    /* Trace outline of blocks - start from minx, minz going to x+ */
                    int cur_x = minx;
                    int cur_z = minz;
                    Direction dir = Direction.XPLUS;
                    ArrayList<int[]> linelist = new ArrayList<>();
                    linelist.add(new int[]{minx, minz}); // Add start point
                    while ((cur_x != minx) || (cur_z != minz) || (dir != Direction.ZMINUS)) {
                        switch (dir) {
                            case XPLUS: /* Segment in X+ Direction */
                                if (!ourChunks.getFlag(cur_x + 1, cur_z)) { /* Right turn? */
                                    linelist.add(new int[]{cur_x + 1, cur_z}); /* Finish line */
                                    dir = Direction.ZPLUS;  /* Change Direction */
                                } else if (!ourChunks.getFlag(cur_x + 1, cur_z - 1)) {  /* Straight? */
                                    cur_x++;
                                } else {  /* Left turn */
                                    linelist.add(new int[]{cur_x + 1, cur_z}); /* Finish line */
                                    dir = Direction.ZMINUS;
                                    cur_x++;
                                    cur_z--;
                                }
                                break;
                            case ZPLUS: /* Segment in Z+ Direction */
                                if (!ourChunks.getFlag(cur_x, cur_z + 1)) { /* Right turn? */
                                    linelist.add(new int[]{cur_x + 1, cur_z + 1}); /* Finish line */
                                    dir = Direction.XMINUS;  /* Change Direction */
                                } else if (!ourChunks.getFlag(cur_x + 1, cur_z + 1)) {  /* Straight? */
                                    cur_z++;
                                } else {  /* Left turn */
                                    linelist.add(new int[]{cur_x + 1, cur_z + 1}); /* Finish line */
                                    dir = Direction.XPLUS;
                                    cur_x++;
                                    cur_z++;
                                }
                                break;
                            case XMINUS: /* Segment in X- Direction */
                                if (!ourChunks.getFlag(cur_x - 1, cur_z)) { /* Right turn? */
                                    linelist.add(new int[]{cur_x, cur_z + 1}); /* Finish line */
                                    dir = Direction.ZMINUS;  /* Change Direction */
                                } else if (!ourChunks.getFlag(cur_x - 1, cur_z + 1)) {  /* Straight? */
                                    cur_x--;
                                } else {  /* Left turn */
                                    linelist.add(new int[]{cur_x, cur_z + 1}); /* Finish line */
                                    dir = Direction.ZPLUS;
                                    cur_x--;
                                    cur_z++;
                                }
                                break;
                            case ZMINUS: /* Segment in Z- Direction */
                                if (!ourChunks.getFlag(cur_x, cur_z - 1)) { /* Right turn? */
                                    linelist.add(new int[]{cur_x, cur_z}); /* Finish line */
                                    dir = Direction.XPLUS;  /* Change Direction */
                                } else if (!ourChunks.getFlag(cur_x - 1, cur_z - 1)) {  /* Straight? */
                                    cur_z--;
                                } else {  /* Left turn */
                                    linelist.add(new int[]{cur_x, cur_z}); /* Finish line */
                                    dir = Direction.XMINUS;
                                    cur_x--;
                                    cur_z--;
                                }
                                break;
                        }
                    }
                    /* Build information for specific area */
                    final String polyId = new StringBuilder().append(nationName).append("__").append(world).append("__").append(poly_index).toString();

                    final int sz = linelist.size();
                    final double[] x = new double[sz];
                    final double[] z = new double[sz];
                    for (int i = 0; i < sz; i++) {
                        final int[] line = linelist.get(i);
                        x[i] = (double) line[0] * (double) blocksize;
                        z[i] = (double) line[1] * (double) blocksize;
                    }
                    /* Find existing one */
                    AreaMarker areaMarker = resareas.remove(polyId); /* Existing area? */
                    if (areaMarker == null) {
                        areaMarker = set.createAreaMarker(polyId, nationName, false, world, x, z, false);
                        if (areaMarker == null) {
                            return;
                        }
                    } else {
                        areaMarker.setCornerLocations(x, z); /* Replace corner locations */
                        areaMarker.setLabel(nationName);   /* Update label */
                    }
                    areaMarker.setDescription(desc); /* Set popup */

                    /* Set line and fill properties */
                    var defstyle = new AreaStyle(nation.getColor());
                    addStyle(cusstyle, defstyle, nationName, areaMarker);

                    /* Set the nation name */
                    if (displayNationName) {
                        /* TODO: SHOW THE NATION NAME */
                    }

                    /* Add to map */
                    newmap.put(polyId, areaMarker);
                    poly_index++;
                }
            }
        }
    }

    public static int decodeColor(String strColor) {
        var red = Integer.parseInt(strColor.substring(strColor.indexOf('=') + 1, strColor.indexOf(',')));
        strColor = strColor.substring(strColor.indexOf('g'));
        var green = Integer.parseInt(strColor.substring(strColor.indexOf('=') + 1, strColor.indexOf(',')));
        strColor = strColor.substring(strColor.indexOf('b'));
        var blue = Integer.parseInt(strColor.substring(strColor.indexOf('=') + 1, strColor.indexOf(']')));

        return (red << 16) | (green << 8) | blue;
    }

    public static void addStyle(final Map<String, AreaStyle> cusstyle, final AreaStyle defstyle, final String resid, final AreaMarker areaMarker) {
        AreaStyle as = cusstyle.get(resid);
        if (as == null) {
            as = defstyle;
        }
        int fc = decodeColor(as.getFillcolor());
        int sc = decodeColor(as.getStrokecolor());
        try {
            sc = Integer.parseInt(as.getStrokecolor().substring(1), 16);
            fc = Integer.parseInt(as.getFillcolor().substring(1), 16);
        } catch (NumberFormatException ignored) {

        }

        areaMarker.setLineStyle(as.getStrokeweight(), as.getStrokeopacity(), sc);
        areaMarker.setFillStyle(as.getFillopacity(), fc);
        areaMarker.setBoostFlag(as.isBoost());
    }

    private static void floodFillTarget(TileFlags src, TileFlags dest, int x, int y) {
        int cnt = 0;
        ArrayDeque<int[]> stack = new ArrayDeque<int[]>();
        stack.push(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] nxt = stack.pop();
            x = nxt[0];
            y = nxt[1];
            if (src.getFlag(x, y)) { /* Set in src */
                src.setFlag(x, y, false);   /* Clear source */
                dest.setFlag(x, y, true);   /* Set in destination */
                cnt++;
                if (src.getFlag(x + 1, y))
                    stack.push(new int[]{x + 1, y});
                if (src.getFlag(x - 1, y))
                    stack.push(new int[]{x - 1, y});
                if (src.getFlag(x, y + 1))
                    stack.push(new int[]{x, y + 1});
                if (src.getFlag(x, y - 1))
                    stack.push(new int[]{x, y - 1});
            }
        }
    }

    private boolean isVisible(String nationID, String world) {
        if (visible != null && visible.size() > 0) {
            if ((!visible.contains(nationID)) && (!visible.contains("world:" + world))) {
                return false;
            }
        }
        if (hidden != null && hidden.size() > 0) {
            return !(hidden.contains(nationID) || hidden.contains("world:" + world));
        }
        return true;
    }
    public void requestUpdateDiplomacy() {
        final DiplomacyUpdate diplomacyUpdate = new DiplomacyUpdate(this);
        diplomacyUpdate.setRunOnce(true);
        scheduleSyncDelayedTask(diplomacyUpdate, 20L);
    }

    public boolean isPlayersets() {
        return playersets;
    }

    public MarkerAPI getMarkerAPI() {
        return markerAPI;
    }

    public void requestUpdatePlayerSet(final String nationID) {
        if (playersets) {
            scheduleSyncDelayedTask(new PlayerSetUpdate(this, nationID), 40L);
        }
    }

    public Plugin getDynmap() {
        return dynmap;
    }

    public void scheduleSyncDelayedTask(final Runnable run, final long period) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(diplomacy, run, period);
    }

    public long getUpdatePeriod() {
        return updatePeriod;
    }

    public boolean isStop() {
        return this.stop;
    }
}
