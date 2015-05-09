package k4unl.minecraft.pvpToggle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPForced;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.proxy.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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

    @SideOnly(Side.CLIENT)
    public boolean isPvPForced;

    private ConfigHandler PvPConfigHandler = new ConfigHandler();

    public Map<Integer, PvPForced> dimensionSettings = new HashMap<Integer, PvPForced>();

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
            if (MinecraftServer.getServer().isPVPEnabled()) {
                EventHelper.init();
            }
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
            readDimensionSettingsFromFile(DimensionManager.getCurrentSaveRootDirectory());
        }
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

        if (event.getSide().equals(Side.SERVER)) {
            Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
            Areas.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
            readDimensionSettingsFromFile(DimensionManager.getCurrentSaveRootDirectory());
        }
	}



    public void readDimensionSettingsFromFile(File dir){
        dimensionSettings.clear();
        if(dir != null){
            Gson gson = new Gson();
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.dimensions.json";
            File f = new File(p);
            if(!f.exists()){
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileInputStream ipStream = new FileInputStream(f);
                InputStreamReader reader = new InputStreamReader(ipStream);
                BufferedReader bReader = new BufferedReader(reader);
                String json = bReader.readLine();
                reader.close();
                ipStream.close();
                bReader.close();

                Type myTypeMap = new TypeToken<Map<Integer,PvPForced>>(){}.getType();
                dimensionSettings = gson.fromJson(json, myTypeMap);
                if(dimensionSettings== null){
                    dimensionSettings = new HashMap<Integer, PvPForced>();
                }

                //Log.info("Read from file: " + json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void saveDimensionSettingsToFile(File dir){
        if(dir != null){
            Gson gson = new Gson();
            String json = gson.toJson(dimensionSettings);
            //Log.info("Saving: " + json);
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.dimensions.json";
            File f = new File(p);
            if(!f.exists()){
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                PrintWriter opStream = new PrintWriter(f);
                opStream.write(json);
                opStream.flush();
                opStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
