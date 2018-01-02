package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSaveDimension extends AbstractPacket<PacketSaveDimension> {
    
    private DimensionDTO toSave;
    
    public PacketSaveDimension() {
    
    }
    
    public PacketSaveDimension(DimensionDTO toSave) {
        
        this.toSave = toSave;
    }
    
    @Override
    public void handleClientSide(PacketSaveDimension message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(PacketSaveDimension message, EntityPlayer player) {
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            ServerHandler.saveDimension(message.toSave);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        toSave = new DimensionDTO();
        toSave.dimensionId = buf.readInt();
        toSave.status = PvPStatus.fromInt(buf.readInt());
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(toSave.dimensionId);
        buf.writeInt(toSave.status.toInt());
    }
}
