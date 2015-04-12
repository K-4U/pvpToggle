package k4unl.minecraft.pvpToggle;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.k4lib.lib.Area;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.proxy.CommonProxy;
import net.minecraftforge.common.DimensionManager;


@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	acceptableRemoteVersions="*"
)


public class PvpToggle {
    @Instance(value = ModInfo.ID)
    public static PvpToggle instance;

    @SidedProxy(
      clientSide = "k4unl.minecraft.pvpToggle.proxy.ClientProxy",
      serverSide = "k4unl.minecraft.pvpToggle.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @SideOnly(Side.CLIENT)
    public boolean isPvPEnabled;

    private ConfigHandler PvPConfigHandler = new ConfigHandler();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();
        if (event.getSide().equals(Side.SERVER)) {
            PvPConfig.INSTANCE.init();
            PvPConfigHandler.init(PvPConfig.INSTANCE, event.getSuggestedConfigurationFile());
            Areas.init();
            Users.init();
        }


    }

    @EventHandler
    public void load(FMLInitializationEvent event) {

        NetworkHandler.init();

        if (event.getSide().equals(Side.SERVER)) {
            EventHelper.init();
        }
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

        Commands.init(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        if (event.getSide().equals(Side.SERVER)) {
            Users.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
            Areas.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
        }
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

        if(event.getSide().equals(Side.SERVER)) {
            Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
            Areas.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
        }
	}
}
