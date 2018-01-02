package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPickedLocation extends AbstractPacket<PacketPickedLocation> {
    
    private Location pickedLocation;
    
    public PacketPickedLocation() {
    
    }
    
    public PacketPickedLocation(Location pickedLocation) {
        
        this.pickedLocation = pickedLocation;
    }
    
    @Override
    public void handleClientSide(PacketPickedLocation message, EntityPlayer player) {
    
        ClientHandler.setPickedLocation(message.pickedLocation);
        ClientHandler.setIsPicking(false);
        ClientHandler.getHolder().handlePicked();
        ClientHandler.setOpenHolder(true);
    }
    
    @Override
    public void handleServerSide(PacketPickedLocation message, EntityPlayer player) {
    
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        this.pickedLocation = new Location(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(this.pickedLocation.getX());
        buf.writeInt(this.pickedLocation.getY());
        buf.writeInt(this.pickedLocation.getZ());
        buf.writeInt(this.pickedLocation.getDimension());
    }
}
