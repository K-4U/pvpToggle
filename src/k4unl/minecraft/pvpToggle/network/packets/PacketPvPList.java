package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPvPList extends AbstractPacket<PacketPvPList> {

    private static class usr {
        public PvPStatus pvpStatus;
        public String    user;
    }

    private List<usr> users = new ArrayList<usr>();

    public PacketPvPList(){
        users = new ArrayList<usr>();
    }

    public void addToList(PvPStatus newPvPStatus, String username){
        usr user = new usr();
        user.pvpStatus = newPvPStatus;
        user.user = username;
        users.add(user);
    }

    @Override
    public void handleClientSide(PacketPvPList message, EntityPlayer player) {
        for(usr user : message.users) {

            if (PvpToggle.clientPvPStatus.containsKey(user.user)) {
                PvpToggle.clientPvPStatus.remove(user.user);
            }
            PvpToggle.clientPvPStatus.put(user.user, user.pvpStatus);
        }
    }

    @Override
    public void handleServerSide(PacketPvPList message, EntityPlayer player) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();

        for(int a = 0; a < length; a++) {
            usr user = new usr();
            user.pvpStatus = PvPStatus.fromInt(buf.readInt());

            user.user = "";
            int l = buf.readByte();
            for (int i = 0; i < l; i++)
                user.user += buf.readChar();

            users.add(user);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(users.size());
        for(usr user : users) {
            buf.writeInt(user.pvpStatus.ordinal());
            buf.writeByte(user.user.length());
            for (int i = 0; i < user.user.length(); i++)
                buf.writeChar(user.user.charAt(i));
        }
    }
}
