package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;

public class User {
	private String userName;
	private boolean hasPVP;
    private Forced isForced = Forced.NOTFORCED;
    private int coolDown;
    private String isInArea = "";
	
	public User(String _username, boolean _hasPVP){
		userName = _username;
        hasPVP = _hasPVP;
        isForced = Forced.NOTFORCED;
        isInArea = "";
	}

	public User(String _username) {
		userName = _username;
        hasPVP = false;
        isForced = Forced.NOTFORCED;
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

    public void setIsForced(Forced isIt){
        isForced = isIt;
    }

    public Forced getIsForced(){
        return isForced;
    }

    public String getIsInArea(){
        return isInArea;
    }

    public void setIsInArea(String areaName){
        isInArea = areaName;
    }
}
