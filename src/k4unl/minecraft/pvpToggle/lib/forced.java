package k4unl.minecraft.pvpToggle.lib;

public enum Forced {
    FORCEDON(0),FORCEDOFF(1), NOTFORCED(-1);

    private final int tier;

    public static final Forced[] VALID_TIERS = {FORCEDON, FORCEDOFF, NOTFORCED};

    private Forced(int _tier){
        this.tier = _tier;
    }


}
