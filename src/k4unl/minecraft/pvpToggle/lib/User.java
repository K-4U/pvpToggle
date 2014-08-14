package k4unl.minecraft.pvpToggle.lib;


public class User {
	private String userName;
	private boolean hasPVP;
	
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
    }
}
