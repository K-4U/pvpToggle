package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;

public class User {

    private String  userName;
    private boolean hasPVP;
    private PvPStatus isPvPForced = PvPStatus.NOTFORCED;
    private int coolDown;
    private String isInArea = "";

    public User(String _username, boolean _hasPVP) {

        userName = _username;
        hasPVP = _hasPVP;
        isPvPForced = PvPStatus.NOTFORCED;
        isInArea = "";
	}

	public User(String _username) {
		userName = _username;
        hasPVP = false;
        isPvPForced = PvPStatus.NOTFORCED;
        isInArea = "";
	}

	public String getUserName() {
		return userName;
	}

    public boolean getPVP(){
        return hasPVP;
    }

    public void setPVP(boolean newPVP){
        hasPVP = newPVP;
        coolDown = PvPConfig.INSTANCE.getInt("coolDownInSeconds") * 20;
    }

    public int getCoolDown(){
        return coolDown;
    }

    public void tickCoolDown() {
        if(coolDown > 0){
            coolDown --;
        }
    }

    public void setIsPvPForced(PvPStatus isIt){
        isPvPForced = isIt;
    }

    public PvPStatus getIsPvPForced(){
        return isPvPForced;
    }

    public String getIsInArea(){
        return isInArea;
    }

    public void setIsInArea(String areaName){
        if(areaName != null)
            isInArea = areaName;
    }

    public PvPStatus getPvpStatus(){
        if(hasPVP && getIsPvPForced() == PvPStatus.NOTFORCED){
            return PvPStatus.ON;
        }else if(!hasPVP && getIsPvPForced() == PvPStatus.NOTFORCED){
            return PvPStatus.OFF;
        }else{
            return getIsPvPForced();
        }
    }

    public void setPvpStatus(PvPStatus newStatus) {
        hasPVP = newStatus == PvPStatus.ON;
        if(newStatus != PvPStatus.ON && newStatus != PvPStatus.OFF){
            isPvPForced = newStatus;
        }else{
            isPvPForced = PvPStatus.NOTFORCED;
        }
    }
}
