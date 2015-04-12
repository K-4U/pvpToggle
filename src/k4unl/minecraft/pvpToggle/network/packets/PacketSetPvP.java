package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.Log;
import net.minecraft.entity.player.EntityPlayer;


public class PacketSetPvP extends AbstractPacket<PacketSetPvP> {

    private boolean isPvPOn;

    public PacketSetPvP(){
        this.isPvPOn = false;
    }

    public PacketSetPvP(boolean pvp){
        this.isPvPOn = pvp;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isPvPOn = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isPvPOn);
    }

    @Override
    public void handleClientSide(PacketSetPvP message, EntityPlayer player) {

        PvpToggle.instance.isPvPEnabled = message.isPvPOn;
        Log.info("Hey! We received a packet on the client. It's data is " + message.isPvPOn);
    }

    @Override
    public void handleServerSide(PacketSetPvP message, EntityPlayer player) {

    }
}
