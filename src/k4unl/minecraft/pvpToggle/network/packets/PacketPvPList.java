package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.PvPForced;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketPvPList extends AbstractPacket<PacketPvPList> {

    private static class usr {
        public boolean isPvPOn;
        public boolean isForced;
        public String user;
    }

    private List<usr> users = new ArrayList<usr>();

    public PacketPvPList(){
        users = new ArrayList<usr>();
    }

    public void addToList(boolean pvp, PvPForced isPvPForced, String username){
        usr user = new usr();
        if(isPvPForced == PvPForced.NOTFORCED) {
            user.isPvPOn = pvp;
            user.isForced = false;
        }else{
            user.isPvPOn = isPvPForced == PvPForced.FORCEDON;
            user.isForced = true;
        }
        user.user = username;
        users.add(user);
    }

    @Override
    public void handleClientSide(PacketPvPList message, EntityPlayer player) {
        for(usr user : message.users) {
            if (PvpToggle.clientPvPEnabled.containsKey(user.user)) {
                PvpToggle.clientPvPEnabled.remove(user.user);
            }
            PvpToggle.clientPvPEnabled.put(user.user, user.isPvPOn);

            if (PvpToggle.clientPvPForced.containsKey(user.user)) {
                PvpToggle.clientPvPForced.remove(user.user);
            }
            PvpToggle.clientPvPForced.put(user.user, user.isForced);
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
            user.isPvPOn = buf.readBoolean();
            user.isForced = buf.readBoolean();

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
            buf.writeBoolean(user.isPvPOn);
            buf.writeBoolean(user.isForced);
            buf.writeByte(user.user.length());
            for (int i = 0; i < user.user.length(); i++)
                buf.writeChar(user.user.charAt(i));
        }
    }
}
