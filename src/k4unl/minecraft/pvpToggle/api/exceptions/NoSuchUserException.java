package k4unl.minecraft.pvpToggle.api.exceptions;

/**
 * @author Koen Beckers (K-4U)
 */
public class NoSuchUserException extends Exception {
    public String username;

    public NoSuchUserException(String username) {
        this.username = username;
    }
}
