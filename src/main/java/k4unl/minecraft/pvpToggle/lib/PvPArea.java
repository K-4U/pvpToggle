package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.k4lib.lib.Area;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.DimensionType;


public class PvPArea extends Area {
    private boolean announce;
    private boolean forced; //Forced will be the value that PVP has.

    public PvPArea(String name_, Location loc1_, Location loc2_, DimensionType dimensionId) {

        super(name_, loc1_, loc2_, dimensionId);
    }

    public PvPArea(CompoundNBT compoundNBT) {
        super(compoundNBT);
        this.announce = compoundNBT.getBoolean("announce");
        this.forced = compoundNBT.getBoolean("forced");
    }

    public PvPArea() {
        super(null, null, null, null);
    }

    @Override
    public CompoundNBT save() {
        CompoundNBT nbt = super.save();
        nbt.putBoolean("announce", this.announce);
        nbt.putBoolean("forced", this.forced);
        return nbt;
    }

    public boolean getAnnounce() {
        return announce;
    }

    public void setAnnounce(boolean newValue) {
        announce = newValue;
    }

    public boolean getForced() {
        return forced;
    }

    public void setForced(boolean newValue) {
        forced = newValue;
    }
}
