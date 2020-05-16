package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPvPList extends AbstractPacket {

    private List<usr> users = new ArrayList<usr>();

    public PacketPvPList() {
        users = new ArrayList<usr>();
    }

    public PacketPvPList(ByteBuf buf) {
        int length = buf.readInt();

        for (int a = 0; a < length; a++) {
            usr user = new usr();
            user.pvpStatus = PvPStatus.fromInt(buf.readInt());

            user.uuid = UUID.fromString(readString(buf));
            users.add(user);
        }
    }

    public void addToList(PvPStatus newPvPStatus, UUID userUUID) {
        usr user = new usr();
        user.pvpStatus = newPvPStatus;
        user.uuid = userUUID;
        users.add(user);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(users.size());
        for (usr user : users) {
            buf.writeInt(user.pvpStatus.ordinal());
            writeString(user.uuid.toString(), buf);
        }
    }

    @Override
    public void handleClientSide(PlayerEntity player) {
        for (usr user : this.users) {
            ClientHandler.getClientPvPStatus().remove(user.uuid);
            ClientHandler.getClientPvPStatus().put(user.uuid, user.pvpStatus);
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player) {

    }

    private static class usr {
        public PvPStatus pvpStatus;
        public UUID uuid;
    }
}
