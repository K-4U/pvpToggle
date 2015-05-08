package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;

public class User {

    private String  userName;
    private boolean hasPVP;
    private PvPForced isPvPForced = PvPForced.NOTFORCED;
    private int coolDown;
    private String isInArea = "";

    public User(String _username, boolean _hasPVP) {

        userName = _username;
        hasPVP = _hasPVP;
        isPvPForced = PvPForced.NOTFORCED;
        isInArea = "";
	}

	public User(String _username) {
		userName = _username;
        hasPVP = false;
        isPvPForced = PvPForced.NOTFORCED;
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

    public void setIsPvPForced(PvPForced isIt){
        isPvPForced = isIt;
    }

    public PvPForced getIsPvPForced(){
        return isPvPForced;
    }

    public String getIsInArea(){
        return isInArea;
    }

    public void setIsInArea(String areaName){
        isInArea = areaName;
    }
}
