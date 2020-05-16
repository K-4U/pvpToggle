package k4unl.minecraft.pvpToggle.api;


public enum PvPStatus {
    FORCEDON(1), FORCEDOFF(0), NOTFORCED(-1), ON(2), OFF(3);

    private final int tier;

    public static final PvPStatus[] FORCED = {FORCEDON, FORCEDOFF, NOTFORCED};

    private PvPStatus(int _tier) {

        this.tier = _tier;
    }

    public int toInt() {

        return this.tier;
    }

    public static PvPStatus fromInt(int v) {

        switch (v) {
            case -1:
                return NOTFORCED;
            case 0:
                return FORCEDOFF;
            case 1:
                return FORCEDON;
            case 2:
                return ON;
            case 3:
                return OFF;
        }
        return NOTFORCED;
    }
}
