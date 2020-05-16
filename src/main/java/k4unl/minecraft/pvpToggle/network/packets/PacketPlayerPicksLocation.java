package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPlayerPicksLocation extends AbstractPacket {

    public PacketPlayerPicksLocation() {
    }

    public PacketPlayerPicksLocation(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {
        if (!ServerHandler.getPlayersPicking().contains(player.getUniqueID())) {
            ServerHandler.getPlayersPicking().add(player.getUniqueID());
        }
    }
}
