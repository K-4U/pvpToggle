package k4unl.minecraft.pvpToggle.lib;

public enum PvPForced {
    FORCEDON(1),FORCEDOFF(0), NOTFORCED(-1);

    private final int tier;

    public static final PvPForced[] VALID_TIERS = { FORCEDON, FORCEDOFF, NOTFORCED };

    private PvPForced(int _tier) {

        this.tier = _tier;
    }

    public static PvPForced fromInt(int v) {
        switch (v){
            case -1:
                return NOTFORCED;
            case 0:
                return FORCEDOFF;
            case 1:
                return FORCEDON;
        }
        return NOTFORCED;
    }
}
