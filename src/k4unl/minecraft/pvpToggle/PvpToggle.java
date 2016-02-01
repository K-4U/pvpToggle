package k4unl.minecraft.pvpToggle;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.api.PvpAPI;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.proxy.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
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
    @Mod.Instance(value = ModInfo.ID)
    public static PvpToggle instance;

    @SidedProxy(
            clientSide = "k4unl.minecraft.pvpToggle.proxy.ClientProxy",
            serverSide = "k4unl.minecraft.pvpToggle.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public static NetworkHandler networkHandler = new NetworkHandler();

    //public static final HashMap<String, Boolean> clientPvPEnabled = new HashMap<String, Boolean>();
    public static final HashMap<String, PvPStatus> clientPvPStatus = new HashMap<String, PvPStatus>();

    private ConfigHandler PvPConfigHandler = new ConfigHandler();

    public Map<Integer, PvPStatus> dimensionSettings = new HashMap<Integer, PvPStatus>();

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
            if (MinecraftServer.getServer().isPVPEnabled()) {
                EventHelper.init();
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
            readDimensionSettingsFromFile(DimensionManager.getCurrentSaveRootDirectory());
        }
    }

    @Mod.EventHandler
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

                Type myTypeMap = new TypeToken<Map<Integer,PvPStatus>>(){}.getType();
                dimensionSettings = gson.fromJson(json, myTypeMap);
                if(dimensionSettings== null){
                    dimensionSettings = new HashMap<Integer, PvPStatus>();
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
