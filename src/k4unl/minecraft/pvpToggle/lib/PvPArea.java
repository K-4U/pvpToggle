package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.k4lib.lib.Area;
import k4unl.minecraft.k4lib.lib.Location;


public class PvPArea extends Area {
    private boolean announce;
    private boolean forced; //Forced will be the value that PVP has.

    public PvPArea(String name_, Location loc1_, Location loc2_) {

        super(name_, loc1_, loc2_);
    }

    public void setAnnounce(boolean newValue){
        announce = newValue;
    }

    public boolean getAnnounce(){
        return announce;
    }

    public void setForced(boolean newValue){
        forced = newValue;
    }
    
    public boolean getForced(){
        return forced;
    }
}
