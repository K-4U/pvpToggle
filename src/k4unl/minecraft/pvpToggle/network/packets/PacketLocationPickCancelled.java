package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketLocationPickCancelled extends AbstractPacket<PacketLocationPickCancelled> {
    
    @Override
    public void handleClientSide(PacketLocationPickCancelled message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(PacketLocationPickCancelled message, EntityPlayer player) {
    
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
    }
}
