package k4unl.minecraft.pvpToggle.server;

import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

/**
 * @author Koen Beckers (K-4U)
 */
public class ServerHandler {

    private static List<UUID> playersPicking = new ArrayList<>();

    private static Map<DimensionType, PvPStatus> dimensionSettings = new HashMap<>();

    public static Map<DimensionType, PvPStatus> getDimensionSettings() {

        return dimensionSettings;
    }

    public static List<UUID> getPlayersPicking() {

        return playersPicking;
    }

    public static void setPlayersPicking(List<UUID> playersPicking) {

        ServerHandler.playersPicking = playersPicking;
    }

    public static List<DimensionDTO> createDimensionDTOList() {

        List<DimensionDTO> dtoList = new ArrayList<>();
        for (Map.Entry<DimensionType, PvPStatus> entry : ServerHandler.getDimensionSettings().entrySet()) {
            DimensionDTO dto = new DimensionDTO();
            dto.status = entry.getValue();
            dto.dimension = entry.getKey();
            try {
                dto.dimensionName = dto.dimension.getRegistryName().toString();
            } catch (NullPointerException e) {
                dto.dimensionName = String.format("Dimension(%d) not found", dto.dimension.getId());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static List<DimensionDTO> createAllDimensionsDTOList() {

        List<DimensionDTO> dtoList = new ArrayList<>();
        for (ServerWorld world : Functions.getServer().getWorlds()) {
            if (!ServerHandler.getDimensionSettings().containsKey(world.getDimension().getType())) {
                DimensionDTO dto = new DimensionDTO();
                dto.dimensionName = world.getDimension().getType().getRegistryName().toString();
                dto.dimension = world.getDimension().getType();
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public static void saveDimension(DimensionDTO toSave) {
        ServerHandler.getDimensionSettings().remove(toSave.dimension);
        ServerHandler.getDimensionSettings().put(toSave.dimension, toSave.status);

        //Now, trigger something that'll cause everybody in said dimension to be notified of the changes.
        for (PlayerEntity playerEntity : Functions.getWorldServerForDimensionType(toSave.dimension).getPlayers()) {
            updatePlayerToDimension(toSave.dimension, playerEntity);
        }

        Log.info("Saving dimension #" + toSave.dimension);
    }


    public static void updatePlayerToDimension(DimensionType toDimension, PlayerEntity player) {
        if (ServerHandler.getDimensionSettings().containsKey(toDimension)) {
            if (!ServerHandler.getDimensionSettings().get(toDimension).equals(PvPStatus.NOTFORCED)) {
                if (PvPConfig.announceDimensionSettings.get()) {
                    player.sendStatusMessage(new StringTextComponent("This dimension has PvP forced to " + (ServerHandler.getDimensionSettings().get(toDimension).equals(PvPStatus.FORCEDON) ? "On" : "Off")), true);
                }
            }
            Users.getUserByUUID(player.getGameProfile().getId()).setIsPvPForced(ServerHandler.getDimensionSettings().get(toDimension));
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(player.getGameProfile().getId()), toDimension);
        } else {
            //Remember his old setting.
            Users.getUserByUUID(player.getGameProfile().getId()).setIsPvPForced(PvPStatus.OFF);
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(player.getGameProfile().getId()), toDimension);
        }
    }
}
