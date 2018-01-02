package k4unl.minecraft.pvpToggle.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Koen Beckers (K-4U)
 */
public class ServerHandler {
    
    private static List<String> playersPicking = new ArrayList<>();
    
    private static Map<Integer, PvPStatus> dimensionSettings = new HashMap<>();
    
    public static void readDimensionSettingsFromFile(File dir) {
        
        ServerHandler.dimensionSettings.clear();
        if (dir != null) {
            Gson gson = new Gson();
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.dimensions.json";
            File f = new File(p);
            if (!f.exists()) {
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
                
                Type myTypeMap = new TypeToken<Map<Integer, PvPStatus>>() {
                }.getType();
                dimensionSettings = gson.fromJson(json, myTypeMap);
                if (dimensionSettings == null) {
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
    
    public static void saveDimensionSettingsToFile(File dir) {
        
        if (dir != null) {
            Gson gson = new Gson();
            String json = gson.toJson(dimensionSettings);
            //Log.info("Saving: " + json);
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.dimensions.json";
            File f = new File(p);
            if (!f.exists()) {
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
    
    public static Map<Integer, PvPStatus> getDimensionSettings() {
        
        return dimensionSettings;
    }
    
    public static List<String> getPlayersPicking() {
        
        return playersPicking;
    }
    
    public static void setPlayersPicking(List<String> playersPicking) {
        
        ServerHandler.playersPicking = playersPicking;
    }
    
    public static List<DimensionDTO> createDimensionDTOList() {
        
        List<DimensionDTO> dtoList = new ArrayList<>();
        for (Map.Entry<Integer, PvPStatus> entry : ServerHandler.getDimensionSettings().entrySet()) {
            DimensionDTO dto = new DimensionDTO();
            dto.status = entry.getValue();
            dto.dimensionId = entry.getKey();
            try {
                dto.dimensionName = Functions.getWorldServerForDimensionId(dto.dimensionId).provider.getDimensionType().getName();
            } catch (NullPointerException e) {
                dto.dimensionName = String.format("Dimension(%d) not found", dto.dimensionId);
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
    
    public static List<DimensionDTO> createAllDimensionsDTOList() {
        
        List<DimensionDTO> dtoList = new ArrayList<>();
        for (WorldServer world : Functions.getServer().worlds) {
            if(!ServerHandler.getDimensionSettings().keySet().contains(world.provider.getDimension())) {
                DimensionDTO dto = new DimensionDTO();
                dto.dimensionName = world.provider.getDimensionType().getName();
                dto.dimensionId = world.provider.getDimension();
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    
    public static void saveDimension(DimensionDTO toSave){
        ServerHandler.getDimensionSettings().remove(toSave.dimensionId);
        ServerHandler.getDimensionSettings().put(toSave.dimensionId, toSave.status);
        ServerHandler.saveDimensionSettingsToFile(DimensionManager.getCurrentSaveRootDirectory());
        
        //Now, trigger something that'll cause everybody in said dimension to be notified of the changes.
        for (EntityPlayer playerEntity : Functions.getWorldServerForDimensionId(toSave.dimensionId).playerEntities) {
            updatePlayerToDimension(toSave.dimensionId, playerEntity);
        }
        
        Log.info("Saving dimension #" + toSave.dimensionId);
    }
    
    
    public static void updatePlayerToDimension(int toDimension, EntityPlayer player){
        if (ServerHandler.getDimensionSettings().containsKey(toDimension)) {
            if (!ServerHandler.getDimensionSettings().get(toDimension).equals(PvPStatus.NOTFORCED)) {
                if (PvPConfig.INSTANCE.getBool("announceDimensionSetting")) {
                    player.sendStatusMessage(new TextComponentString("This dimension has PvP forced to " + (ServerHandler.getDimensionSettings().get(toDimension).equals(PvPStatus.FORCEDON) ? "On" : "Off")), true);
                }
            }
            Users.getUserByName(player.getGameProfile().getName()).setIsPvPForced(ServerHandler.getDimensionSettings().get(toDimension));
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(player.getGameProfile().getName()), toDimension);
        } else {
            //Remember his old setting.
            Users.getUserByName(player.getGameProfile().getName()).setIsPvPForced(PvPStatus.OFF);
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(player.getGameProfile().getName()), toDimension);
        }
    }
}
