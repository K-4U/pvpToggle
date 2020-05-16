package k4unl.minecraft.pvpToggle.network;

import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.network.packets.*;

public class NetworkHandler extends k4unl.minecraft.k4lib.network.NetworkHandler {
    public static NetworkHandler INSTANCE;

    public NetworkHandler() {
        INSTANCE = this;
    }

    @Override
    public String getModId() {

        return ModInfo.LID;
    }

    @Override
    public String getProtocolVersion() {
        return "1";
    }

    /*
     * The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    @Override
    public void init() {
        int i = 0;
//		getChannel().registerMessage(i++, PackageAllDataToClient.class, PackageAllDataToClient::toBytes, PackageAllDataToClient::new, PackageAllDataToClient::handle);
        getChannel().registerMessage(i++, PacketSetPvP.class, PacketSetPvP::toBytes, PacketSetPvP::new, PacketSetPvP::handle);
        getChannel().registerMessage(i++, PacketPvPList.class, PacketPvPList::toBytes, PacketPvPList::new, PacketPvPList::handle);
        getChannel().registerMessage(i++, PacketDataToClient.class, PacketDataToClient::toBytes, PacketDataToClient::new, PacketDataToClient::handle);
        getChannel().registerMessage(i++, PacketSaveArea.class, PacketSaveArea::toBytes, PacketSaveArea::new, PacketSaveArea::handle);
        getChannel().registerMessage(i++, PacketSaveDimension.class, PacketSaveDimension::toBytes, PacketSaveDimension::new, PacketSaveDimension::handle);
        getChannel().registerMessage(i++, PacketRequestData.class, PacketRequestData::toBytes, PacketRequestData::new, PacketRequestData::handle);

        getChannel().registerMessage(i++, PacketPlayerPicksLocation.class, PacketPlayerPicksLocation::toBytes, PacketPlayerPicksLocation::new, PacketPlayerPicksLocation::handle);
        //getChannel().registerMessage(PacketLocationPickCancelled.class, PacketLocationPickCancelled.class, discriminant++, Side.SERVER); //TODO: Figure out a way to cancel..
        getChannel().registerMessage(i++, PacketPickedLocation.class, PacketPickedLocation::toBytes, PacketPickedLocation::new, PacketPickedLocation::handle);

        getChannel().registerMessage(i++, PacketRemoveArea.class, PacketRemoveArea::toBytes, PacketRemoveArea::new, PacketRemoveArea::handle);
    }
}
