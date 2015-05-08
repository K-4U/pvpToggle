package k4unl.minecraft.pvpToggle.lib;

public enum PvPForced {
    FORCEDON(0),FORCEDOFF(1), NOTFORCED(-1);

    private final int tier;

    public static final PvPForced[] VALID_TIERS = { FORCEDON, FORCEDOFF, NOTFORCED };

    private PvPForced(int _tier) {

        this.tier = _tier;
    }

}
