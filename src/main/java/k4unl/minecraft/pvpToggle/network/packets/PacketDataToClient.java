package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketDataToClient extends AbstractPacket {

    public boolean areasSet = false;
    public List<PvPArea> areaList = new ArrayList<>();
    public boolean dimensionsSet = false;
    public List<DimensionDTO> dimensions = new ArrayList<>();
    public boolean allDimensionsSet = false;
    public List<DimensionDTO> allDimensions = new ArrayList<>();

    public PacketDataToClient() {

    }

    public PacketDataToClient(ByteBuf buf) {

        Log.info("Yes, received package");
        areasSet = buf.readBoolean();
        dimensionsSet = buf.readBoolean();
        allDimensionsSet = buf.readBoolean();
        if (areasSet) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                String name = readString(buf);
                int minX = buf.readInt();
                int minY = buf.readInt();
                int minZ = buf.readInt();
                int maxX = buf.readInt();
                int maxY = buf.readInt();
                int maxZ = buf.readInt();
                Optional<DimensionType> dimensionTypeForString = Functions.getDimensionTypeForString(readString(buf));
                DimensionType dimensionType = dimensionTypeForString.get();
                boolean announce = buf.readBoolean();
                boolean forced = buf.readBoolean();
                Location l1 = new Location(minX, minY, minZ, dimensionType.getId());
                Location l2 = new Location(maxX, maxY, maxZ, dimensionType.getId());

                PvPArea toAdd = new PvPArea(name, l1, l2, dimensionType);
                toAdd.setAnnounce(announce);
                toAdd.setForced(forced);
                areaList.add(toAdd);
            }
        }
        if (dimensionsSet) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                DimensionDTO dimdto = new DimensionDTO();
                dimdto.dimensionName = readString(buf);
                Optional<DimensionType> dimensionTypeForString = Functions.getDimensionTypeForString(readString(buf));
                dimensionTypeForString.ifPresent(dimensionType -> dimdto.dimension = dimensionType);
                dimdto.status = PvPStatus.fromInt(buf.readInt());
                dimensions.add(dimdto);
            }
        }
        if (allDimensionsSet) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                DimensionDTO dimdto = new DimensionDTO();
                dimdto.dimensionName = readString(buf);
                Optional<DimensionType> dimensionTypeForString = Functions.getDimensionTypeForString(readString(buf));
                dimensionTypeForString.ifPresent(dimensionType -> dimdto.dimension = dimensionType);
                allDimensions.add(dimdto);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeBoolean(areasSet);
        buf.writeBoolean(dimensionsSet);
        buf.writeBoolean(allDimensionsSet);

        if (areasSet) {
            buf.writeInt(areaList.size());
            for (PvPArea area : areaList) {
                writeString(area.getName(), buf);
                buf.writeInt(area.getMinX());
                buf.writeInt(area.getMinY());
                buf.writeInt(area.getMinZ());
                buf.writeInt(area.getMaxX());
                buf.writeInt(area.getMaxY());
                buf.writeInt(area.getMaxZ());
                writeString(area.getDimension().getRegistryName().toString(), buf);
                buf.writeBoolean(area.getAnnounce());
                buf.writeBoolean(area.getForced());
            }
        }
        if (dimensionsSet) {

            buf.writeInt(dimensions.size());
            for (DimensionDTO dto : dimensions) {
                writeString(dto.dimensionName, buf);
                writeString(dto.dimension.getRegistryName().toString(), buf);
                buf.writeInt(dto.status.toInt());
            }
        }
        if (allDimensionsSet) {

            buf.writeInt(allDimensions.size());
            for (DimensionDTO dto : allDimensions) {
                writeString(dto.dimensionName, buf);
                writeString(dto.dimension.getRegistryName().toString(), buf);
            }
        }
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

        if (this.areasSet) {
            ClientHandler.setAreas(this.areaList);
        }
        if (this.dimensionsSet) {
            ClientHandler.setDimensions(this.dimensions);
        }
        if (this.allDimensionsSet) {
            ClientHandler.setAllDimensions(this.allDimensions);
        }
        ClientHandler.setOpenGui(true);

        //Because it's only here we actually open the GUI.
        //This means the server has checked if the requester is actually op.
    }

    @Override
    public void handleServerSide(PlayerEntity player) {

    }
}
