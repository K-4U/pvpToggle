package k4unl.minecraft.pvpToggle.client;

import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.client.gui.GuiPvpAreaAddEdit;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.lib.PvPArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Koen Beckers (K-4U)
 * Class that holds several client variables and functionality used for the GUI-system
 */
public class ClientHandler {


    private static final HashMap<UUID, PvPStatus> clientPvPStatus = new HashMap<>();
    private static List<PvPArea> areas = new ArrayList<>();
    private static       boolean                    openGui         = false;
    private static       boolean                    openHolder      = false;
    private static       boolean                    isPicking       = false;
    private static       Location                   pickedLocation  = null;
    private static       GuiPvpAreaAddEdit          holder          = null; //Holds the gui instance for when we start picking
    private static       Location                   lookingAt       = new Location(0, 0, 0);
    private static       int                        locationPicking = 0;// Either 1 or 2;
    private static       List<DimensionDTO>         dimensions      = new ArrayList<>();
    private static       List<DimensionDTO>         allDimensions      = new ArrayList<>();
    
    
    public static Location getLookingAt() {
        
        return lookingAt;
    }
    
    public static void setLookingAt(Location lookingAt) {
        
        ClientHandler.lookingAt = lookingAt;
    }
    
    public static Location getPickedLocation() {
        
        return pickedLocation;
    }
    
    public static void setPickedLocation(Location pickedLocation) {
        
        ClientHandler.pickedLocation = pickedLocation;
    }

    public static HashMap<UUID, PvPStatus> getClientPvPStatus() {

        return clientPvPStatus;
    }
    
    public static List<PvPArea> getAreas() {
        
        return areas;
    }
    
    public static void setAreas(List<PvPArea> areas) {
        
        ClientHandler.areas = areas;
    }
    
    public static boolean isOpenGui() {
        
        return openGui;
    }
    
    public static void setOpenGui(boolean openGui) {
        
        ClientHandler.openGui = openGui;
    }
    
    public static boolean isPicking() {
        
        return isPicking;
    }
    
    public static void setIsPicking(boolean isPicking) {
        
        ClientHandler.isPicking = isPicking;
    }
    
    public static GuiPvpAreaAddEdit getHolder() {
        
        return holder;
    }
    
    public static void setHolder(GuiPvpAreaAddEdit holder) {
        
        ClientHandler.holder = holder;
    }
    
    public static int getLocationPicking() {
        
        return locationPicking;
    }
    
    public static void setLocationPicking(int locationPicking) {
        
        ClientHandler.locationPicking = locationPicking;
    }
    
    public static boolean isOpenHolder() {
        
        return openHolder;
    }
    
    public static void setOpenHolder(boolean openHolder) {
        
        ClientHandler.openHolder = openHolder;
    }
    
    public static List<DimensionDTO> getDimensions() {
        
        return dimensions;
    }
    
    public static void setDimensions(List<DimensionDTO> dimensions) {
        
        ClientHandler.dimensions = dimensions;
    }
    
    public static List<DimensionDTO> getAllDimensions() {
    
        return allDimensions;
    }
    
    public static void setAllDimensions(List<DimensionDTO> dimensions) {
        
        ClientHandler.allDimensions = dimensions;
    }
}
