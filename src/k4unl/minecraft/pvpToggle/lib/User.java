package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;

public class User {
	private String userName;
	private boolean hasPVP;
    private int coolDown;
	
	public User(String _username, boolean _hasPVP){
		userName = _username;
        hasPVP = _hasPVP;
	}

	public User(String _username) {
		userName = _username;
        hasPVP = false;

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
}
