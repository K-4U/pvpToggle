package k4unl.minecraft.pvpToggle;


import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.api.PvpAPI;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.server.Users;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.proxy.CommonProxy;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
        modid = ModInfo.ID,
        name = ModInfo.NAME,
        version = ModInfo.VERSION,
        acceptableRemoteVersions = "*"
)

public class PvpToggle {
    
    @Mod.Instance(value = ModInfo.ID)
    public static PvpToggle instance;
    
    @SidedProxy(
            clientSide = "k4unl.minecraft.pvpToggle.proxy.ClientProxy",
            serverSide = "k4unl.minecraft.pvpToggle.proxy.CommonProxy"
    )
    public static CommonProxy proxy;
    
    public static NetworkHandler networkHandler = new NetworkHandler();
    
    private ConfigHandler PvPConfigHandler = new ConfigHandler();
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        
        Log.init();
        PvPConfig.INSTANCE.init(event.getSide());
        PvPConfigHandler.init(PvPConfig.INSTANCE, event.getSuggestedConfigurationFile());
        
        PvpAPI.init(new PvpToggleAPI());
        
        if (event.getSide().equals(Side.SERVER)) {
            Areas.init();
            Users.init();
        }
        
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        
        networkHandler.init();
        proxy.init();
        
        if (event.getSide().equals(Side.SERVER)) {
            if (Functions.getServer().isPVPEnabled()) {
                EventHelper.init();
            } else {
                Log.error("PVP is not enabled on your server, PVPToggle will NOT function!");
            }
        }
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
        proxy.postInit(event);
    }
    
    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        
        Commands.init(event);
    }
    
    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        
        if (event.getSide().equals(Side.SERVER)) {
            Users.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
            Areas.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
            ServerHandler.readDimensionSettingsFromFile(DimensionManager.getCurrentSaveRootDirectory());
        }
    }
    
    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        
        if (event.getSide().equals(Side.SERVER)) {
            Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
            Areas.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
            ServerHandler.saveDimensionSettingsToFile(DimensionManager.getCurrentSaveRootDirectory());
        }
    }
}
