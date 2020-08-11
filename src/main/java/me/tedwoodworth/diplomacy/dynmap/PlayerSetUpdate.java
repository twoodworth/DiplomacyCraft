package me.tedwoodworth.diplomacy.dynmap;

public class PlayerSetUpdate implements Runnable {
    private final DiplomacyDynmap kernel;
    private final String nationID;

    public PlayerSetUpdate(final DiplomacyDynmap kernel, final String nationID) {
        this.kernel = kernel;
        this.nationID = nationID;
    }

    @Override
    public void run() {
        if (!kernel.isStop()) {
            PlayerSetCommon.updatePlayerSet(kernel.getMarkerAPI(), nationID);
        }
    }
}
