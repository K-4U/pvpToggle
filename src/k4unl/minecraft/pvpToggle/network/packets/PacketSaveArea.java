package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import net.minecraft.entity.player.EntityPlayer;

import java.nio.charset.Charset;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSaveArea extends AbstractPacket<PacketSaveArea> {
    
    private PvPArea toSave;
    
    public PacketSaveArea() {
    
    }
    
    public PacketSaveArea(PvPArea toSave) {
        
        this.toSave = toSave;
    }
    
    @Override
    public void handleClientSide(PacketSaveArea message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(PacketSaveArea message, EntityPlayer player) {
        
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            Areas.removeAreaByName(message.toSave.getName());
            Areas.addToList(message.toSave);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        int stringSize = buf.readInt();
        String name = buf.readCharSequence(stringSize, Charset.defaultCharset()).toString();
        int minX = buf.readInt();
        int minY = buf.readInt();
        int minZ = buf.readInt();
        int maxX = buf.readInt();
        int maxY = buf.readInt();
        int maxZ = buf.readInt();
        int dimensionId = buf.readInt();
        boolean announce = buf.readBoolean();
        boolean forced = buf.readBoolean();
        Location l1 = new Location(minX, minY, minZ, dimensionId);
        Location l2 = new Location(maxX, maxY, maxZ, dimensionId);
        
        toSave = new PvPArea(name, l1, l2, dimensionId);
        toSave.setAnnounce(announce);
        toSave.setForced(forced);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(toSave.getName().length());
        buf.writeCharSequence(toSave.getName(), Charset.defaultCharset());
        buf.writeInt(toSave.getMinX());
        buf.writeInt(toSave.getMinY());
        buf.writeInt(toSave.getMinZ());
        buf.writeInt(toSave.getMaxX());
        buf.writeInt(toSave.getMaxY());
        buf.writeInt(toSave.getMaxZ());
        buf.writeInt(toSave.getDimensionId());
        buf.writeBoolean(toSave.getAnnounce());
        buf.writeBoolean(toSave.getForced());
    }
}
