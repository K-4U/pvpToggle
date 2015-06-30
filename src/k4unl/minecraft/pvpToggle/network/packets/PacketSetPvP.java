package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.PvPForced;
import net.minecraft.entity.player.EntityPlayer;


public class PacketSetPvP extends AbstractPacket<PacketSetPvP> {

    private boolean isPvPOn;
    private boolean isForced;
    private String user;

    public PacketSetPvP(){
        this.isPvPOn = false;
        this.isForced = false;
        this.user = "";
    }

    public PacketSetPvP(boolean pvp, PvPForced isPvPForced, String username){
        if(isPvPForced == PvPForced.NOTFORCED) {
            this.isPvPOn = pvp;
            this.isForced = false;
        }else{
            this.isPvPOn = isPvPForced == PvPForced.FORCEDON;
            this.isForced = true;
        }
        this.user = username;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isPvPOn = buf.readBoolean();
        this.isForced = buf.readBoolean();

        user = "";
        int l = buf.readByte();
        for(int i = 0; i < l; i++)
        	user += buf.readChar();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isPvPOn);
        buf.writeBoolean(this.isForced);
        buf.writeByte(user.length());
        for(int i = 0; i < user.length(); i++)
        	buf.writeChar(user.charAt(i));
    }

    @Override
    public void handleClientSide(PacketSetPvP message, EntityPlayer player) {

        if(PvpToggle.clientPvPEnabled.containsKey(message.user)){
            PvpToggle.clientPvPEnabled.remove(message.user);
        }
        PvpToggle.clientPvPEnabled.put(message.user, message.isPvPOn);

        if(PvpToggle.clientPvPForced.containsKey(message.user)){
            PvpToggle.clientPvPForced.remove(message.user);
        }
        PvpToggle.clientPvPForced.put(message.user, message.isForced);
    }

    @Override
    public void handleServerSide(PacketSetPvP message, EntityPlayer player) {

    }
}
