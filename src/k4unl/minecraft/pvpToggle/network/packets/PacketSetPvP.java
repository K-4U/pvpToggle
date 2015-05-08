package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.PvPForced;
import net.minecraft.entity.player.EntityPlayer;


public class PacketSetPvP extends AbstractPacket<PacketSetPvP> {

    private boolean isPvPOn;
    private boolean isForced;

    public PacketSetPvP(){
        this.isPvPOn = false;
        this.isForced = false;
    }

    public PacketSetPvP(boolean pvp, PvPForced isPvPForced){
        if(isPvPForced == PvPForced.NOTFORCED) {
            this.isPvPOn = pvp;
            this.isForced = false;
        }else{
            this.isPvPOn = isPvPForced == PvPForced.FORCEDON;
            this.isForced = true;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isPvPOn = buf.readBoolean();
        this.isForced = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isPvPOn);
        buf.writeBoolean(this.isForced);
    }

    @Override
    public void handleClientSide(PacketSetPvP message, EntityPlayer player) {

        PvpToggle.instance.isPvPEnabled = message.isPvPOn;
        PvpToggle.instance.isPvPForced = message.isForced;
        //Log.info("Hey! We received a packet on the client. It's data is " + message.isPvPOn);
    }

    @Override
    public void handleServerSide(PacketSetPvP message, EntityPlayer player) {

    }
}
