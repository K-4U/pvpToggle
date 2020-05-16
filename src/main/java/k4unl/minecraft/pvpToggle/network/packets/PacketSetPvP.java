package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import k4unl.minecraft.pvpToggle.lib.Log;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;


public class PacketSetPvP extends AbstractPacket {

    private PvPStatus pvpStatus;
    private UUID user;

    public PacketSetPvP() {

        pvpStatus = PvPStatus.NOTFORCED;
        this.user = null;
    }

    public PacketSetPvP(PvPStatus newStatus, UUID userUUID) {

        pvpStatus = newStatus;
        this.user = userUUID;
    }

    public PacketSetPvP(ByteBuf buf) {

        this.pvpStatus = PvPStatus.fromInt(buf.readInt());
        this.user = UUID.fromString(readString(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(pvpStatus.toInt());
        writeString(user.toString(), buf);
    }

    @Override
    public void handleClientSide(PlayerEntity player) {
        Log.info("Received msg" + this.user + "=" + this.pvpStatus);
        ClientHandler.getClientPvPStatus().remove(this.user);
        ClientHandler.getClientPvPStatus().put(this.user, this.pvpStatus);
    }

    @Override
    public void handleServerSide(PlayerEntity player) {

    }

}
