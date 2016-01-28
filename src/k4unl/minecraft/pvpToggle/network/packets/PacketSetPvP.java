package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import net.minecraft.entity.player.EntityPlayer;


public class PacketSetPvP extends AbstractPacket<PacketSetPvP> {

    private PvPStatus pvpStatus;
    private String user;

    public PacketSetPvP(){
        pvpStatus = PvPStatus.NOTFORCED;
        this.user = "";
    }

    public PacketSetPvP(PvPStatus newStatus, String username){
        pvpStatus = newStatus;
        this.user = username;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pvpStatus = PvPStatus.fromInt(buf.readInt());

        user = "";
        int l = buf.readByte();
        for(int i = 0; i < l; i++)
        	user += buf.readChar();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pvpStatus.ordinal());
        buf.writeByte(user.length());
        for(int i = 0; i < user.length(); i++)
        	buf.writeChar(user.charAt(i));
    }

    @Override
    public void handleClientSide(PacketSetPvP message, EntityPlayer player) {

        if(PvpToggle.clientPvPStatus.containsKey(message.user)){
            PvpToggle.clientPvPStatus.remove(message.user);
        }
        PvpToggle.clientPvPStatus.put(message.user, message.pvpStatus);
    }

    @Override
    public void handleServerSide(PacketSetPvP message, EntityPlayer player) {

    }
}
