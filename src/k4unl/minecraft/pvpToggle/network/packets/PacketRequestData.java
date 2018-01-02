package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketRequestData extends AbstractPacket<PacketRequestData> {
    
    boolean areas         = false;
    boolean dimensions    = false;
    boolean allDimensions = false;
    
    public PacketRequestData() {
    
    }
    
    public PacketRequestData(boolean areas, boolean dimensions, boolean allDimensions) {
        
        this.areas = areas;
        this.dimensions = dimensions;
        this.allDimensions = allDimensions;
    }
    
    @Override
    public void handleClientSide(PacketRequestData message, EntityPlayer player) {
        
    
    }
    
    @Override
    public void handleServerSide(PacketRequestData message, EntityPlayer player) {
        
        
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            
            PacketDataToClient packet = new PacketDataToClient();
            if (message.areas) {
                packet.areasSet = true;
                packet.areaList = Areas.getAreas();
            }
            if (message.dimensions) {
                packet.dimensionsSet = true;
                packet.dimensions = ServerHandler.createDimensionDTOList();
                ;
            }
            if (message.allDimensions) {
                packet.allDimensionsSet = true;
                packet.allDimensions = ServerHandler.createAllDimensionsDTOList();
            }
            
            PvpToggle.networkHandler.sendTo(packet, (EntityPlayerMP) player);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
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
}
