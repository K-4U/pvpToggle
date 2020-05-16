package k4unl.minecraft.pvpToggle.server;

import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.network.packets.PacketPvPList;
import k4unl.minecraft.pvpToggle.network.packets.PacketSetPvP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Users {

    private static List<User> userList;

    public static void init() {

        userList = new ArrayList<User>();
    }

    public static User getUserByUUID(UUID userUUID) {

        for (User u : userList) {
            if (u.getUuid().equals(userUUID)) {
                return u;
            }
        }
        User nUser = new User(userUUID);
        userList.add(nUser);
        return nUser;
    }

    public static List<User> getUserList() {

        return userList;
    }

    public static boolean hasPVPEnabled(UUID username) {

        PvPStatus playerStatus = getUserByUUID(username).getPvpStatus();
        if (playerStatus == PvPStatus.NOTFORCED) {
            return getUserByUUID(username).getPVP();
        } else {

            return (playerStatus == PvPStatus.FORCEDON || playerStatus == PvPStatus.ON);
        }
    }

    public static PacketSetPvP createPacket(UUID userUUID) {

        return new PacketSetPvP(getUserByUUID(userUUID).getPvpStatus(), userUUID);
    }

    public static void addToPvpList(PacketPvPList list, UUID userUUID) {

        list.addToList(getUserByUUID(userUUID).getPvpStatus(), userUUID);
    }

    public static boolean isInCoolDown(UUID userUUID) {

        return getUserByUUID(userUUID).getCoolDown() > 0;
    }

    public static void tickCoolDown() {

        for (User u : userList) {
            u.tickCoolDown();
        }
    }

    public static void addUser(User user) {
        getUserList().add(user);
    }
}
