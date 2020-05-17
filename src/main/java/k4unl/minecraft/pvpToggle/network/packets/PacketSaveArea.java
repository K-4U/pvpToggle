package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.server.Areas;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;

import java.util.Optional;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSaveArea extends AbstractPacket {

    private PvPArea toSave;

    public PacketSaveArea() {

    }

    public PacketSaveArea(PvPArea toSave) {

        this.toSave = toSave;
    }

    public PacketSaveArea(ByteBuf buf) {

        String name = readString(buf);
        int minX = buf.readInt();
        int minY = buf.readInt();
        int minZ = buf.readInt();
        int maxX = buf.readInt();
        int maxY = buf.readInt();
        int maxZ = buf.readInt();
        Optional<DimensionType> dimensionTypeForString = Functions.getDimensionTypeForString(readString(buf));
        DimensionType dimensionType = dimensionTypeForString.get();
        boolean announce = buf.readBoolean();
        boolean forced = buf.readBoolean();
        Location l1 = new Location(minX, minY, minZ, dimensionType.getId());
        Location l2 = new Location(maxX, maxY, maxZ, dimensionType.getId());

        PvPArea toAdd = new PvPArea(name, l1, l2, dimensionType);
        toAdd.setAnnounce(announce);
        toAdd.setForced(forced);
        this.toSave = toAdd;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        writeString(toSave.getName(), buf);
        buf.writeInt(toSave.getMinX());
        buf.writeInt(toSave.getMinY());
        buf.writeInt(toSave.getMinZ());
        buf.writeInt(toSave.getMaxX());
        buf.writeInt(toSave.getMaxY());
        buf.writeInt(toSave.getMaxZ());
        writeString(toSave.getDimension().getRegistryName().toString(), buf);
        buf.writeBoolean(toSave.getAnnounce());
        buf.writeBoolean(toSave.getForced());
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            Areas.removeAreaByName(this.toSave.getName());
            Areas.addToList(this.toSave);
        }
    }
}
