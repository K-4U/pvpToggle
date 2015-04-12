package k4unl.minecraft.pvpToggle.network;

import cpw.mods.fml.relauncher.Side;
import k4unl.minecraft.k4lib.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.network.packets.PacketSetPvP;

public class NetworkHandler extends k4unl.minecraft.k4lib.network.NetworkHandler {

    public static String getModId() {

        return ModInfo.LID;
    }

    /*
     * The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    public static void init() {

        INSTANCE.registerMessage(PacketSetPvP.class, PacketSetPvP.class, discriminant++, Side.CLIENT);
    }
}
