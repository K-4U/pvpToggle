package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketRemoveArea extends AbstractPacket<PacketRemoveArea> {
    
    private String toRemove;
    
    public PacketRemoveArea() {
    
    }
    
    public PacketRemoveArea(String toRemove) {
        
        this.toRemove = toRemove;
    }
    
    @Override
    public void handleClientSide(PacketRemoveArea message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(PacketRemoveArea message, EntityPlayer player) {
        
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            Areas.removeAreaByName(message.toRemove);
            List<DimensionDTO> dtoList = ServerHandler.createDimensionDTOList();
            PacketDataToClient packetDataToClient = new PacketDataToClient();
            packetDataToClient.dimensionsSet = true;
            packetDataToClient.areasSet = true;
            packetDataToClient.dimensions = dtoList;
            packetDataToClient.areaList = Areas.getAreas();
            PvpToggle.networkHandler.sendTo(packetDataToClient, (EntityPlayerMP) player);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        int stringSize = buf.readInt();
        toRemove = buf.readCharSequence(stringSize, Charset.defaultCharset()).toString();
        
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(toRemove.length());
        buf.writeCharSequence(toRemove, Charset.defaultCharset());
    }
}
