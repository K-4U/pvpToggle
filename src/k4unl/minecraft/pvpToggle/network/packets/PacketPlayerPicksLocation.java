package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPlayerPicksLocation extends AbstractPacket<PacketPlayerPicksLocation> {
    
    @Override
    public void handleClientSide(PacketPlayerPicksLocation message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(PacketPlayerPicksLocation message, EntityPlayer player) {
        if(!ServerHandler.getPlayersPicking().contains(player.getName())){
            ServerHandler.getPlayersPicking().add(player.getName());
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
    }
}
