package k4unl.minecraft.pvpToggle.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.DimensionDTO;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;

import java.util.Optional;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSaveDimension extends AbstractPacket {

    private DimensionDTO toSave;

    public PacketSaveDimension() {

    }

    public PacketSaveDimension(DimensionDTO toSave) {

        this.toSave = toSave;
    }


    public PacketSaveDimension(ByteBuf buf) {
        toSave = new DimensionDTO();
        Optional<DimensionType> dimensionTypeForString = Functions.getDimensionTypeForString(readString(buf));
        dimensionTypeForString.ifPresent(dimensionType -> toSave.dimension = dimensionType);
        toSave.status = PvPStatus.fromInt(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeString(toSave.dimension.getRegistryName().toString(), buf);
        buf.writeInt(toSave.status.toInt());
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {
        if (Functions.isPlayerOpped(player.getGameProfile())) {
            ServerHandler.saveDimension(this.toSave);
        }
    }
}
