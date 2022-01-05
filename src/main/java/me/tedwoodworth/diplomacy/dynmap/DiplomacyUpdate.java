//package me.tedwoodworth.diplomacy.dynmap;
//
//public class DiplomacyUpdate implements Runnable {
//    private final DiplomacyDynmap kernel;
//    private boolean runOnce;
//
//    public DiplomacyUpdate(final DiplomacyDynmap kernel) {
//        this.kernel = kernel;
//    }
//
//    public boolean isRunOnce() {
//        return runOnce;
//    }
//
//    @Override
//    public synchronized void run() {
//        if (!kernel.isStop()) {
//            kernel.updateClaimedChunk();
//            if (!isRunOnce()) {
//                kernel.scheduleSyncDelayedTask(this, kernel.getUpdatePeriod());
//            }
//        }
//    }
//
//    public void setRunOnce(boolean runOnce) {
//        this.runOnce = runOnce;
//    }
//}