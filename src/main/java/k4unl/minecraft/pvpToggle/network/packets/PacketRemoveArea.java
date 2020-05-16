package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketRemoveArea extends AbstractPacket {

    private String toRemove;

    public PacketRemoveArea() {

    }

    public PacketRemoveArea(String toRemove) {

        this.toRemove = toRemove;
    }

    public PacketRemoveArea(ByteBuf buf) {

        toRemove = readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(toRemove.length());
        buf.writeCharSequence(toRemove, Charset.defaultCharset());
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            Areas.removeAreaByName(this.toRemove);
            List<DimensionDTO> dtoList = ServerHandler.createDimensionDTOList();
            PacketDataToClient packetDataToClient = new PacketDataToClient();
            packetDataToClient.dimensionsSet = true;
            packetDataToClient.areasSet = true;
            packetDataToClient.dimensions = dtoList;
            packetDataToClient.areaList = Areas.getAreas();
            PvpToggle.networkHandler.sendTo(packetDataToClient, (ServerPlayerEntity) player);
        }
    }
}
