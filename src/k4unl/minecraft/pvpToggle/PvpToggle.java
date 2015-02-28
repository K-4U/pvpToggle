package k4unl.minecraft.pvpToggle;



import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;


@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	acceptableRemoteVersions="*"
)


public class PvpToggle {
    @Mod.Instance(value = ModInfo.ID)
    public static PvpToggle instance;

    private ConfigHandler PvPConfigHandler = new ConfigHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();
        PvPConfig.INSTANCE.init();
        PvPConfigHandler.init(PvPConfig.INSTANCE, event.getSuggestedConfigurationFile());
        Users.init();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {

        EventHelper.init();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

        Commands.init(event);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        Users.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
	}
	
	@Mod.EventHandler
	public void serverStop(FMLServerStoppingEvent event){
		Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
	}
}
