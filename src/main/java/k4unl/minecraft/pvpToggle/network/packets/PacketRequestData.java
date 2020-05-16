package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketRequestData extends AbstractPacket {

    boolean areas = false;
    boolean dimensions = false;
    boolean allDimensions = false;

    public PacketRequestData() {

    }

    public PacketRequestData(boolean areas, boolean dimensions, boolean allDimensions) {

        this.areas = areas;
        this.dimensions = dimensions;
        this.allDimensions = allDimensions;
    }

    public PacketRequestData(ByteBuf buf) {
        areas = buf.readBoolean();
        dimensions = buf.readBoolean();
        allDimensions = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeBoolean(areas);
        buf.writeBoolean(dimensions);
        buf.writeBoolean(allDimensions);
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {

        if (Functions.isPlayerOpped(player.getGameProfile())) {

            PacketDataToClient packet = new PacketDataToClient();
            if (this.areas) {
                packet.areasSet = true;
                packet.areaList = Areas.getAreas();
            }
            if (this.dimensions) {
                packet.dimensionsSet = true;
                packet.dimensions = ServerHandler.createDimensionDTOList();
            }
            if (this.allDimensions) {
                packet.allDimensionsSet = true;
                packet.allDimensions = ServerHandler.createAllDimensionsDTOList();
            }

            PvpToggle.networkHandler.sendTo(packet, (ServerPlayerEntity) player);
        }
    }
}
