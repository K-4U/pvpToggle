package k4unl.minecraft.pvpToggle.world;

import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import k4unl.minecraft.pvpToggle.server.User;
import k4unl.minecraft.pvpToggle.server.Users;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.Optional;

public class PvPWorldData extends WorldSavedData {

    public PvPWorldData() {
        super(ModInfo.ID);
    }

    public static PvPWorldData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(PvPWorldData::new, ModInfo.ID);
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT users = nbt.getList("users", 10);
        for (int i = 0; i < users.size(); ++i) {
            CompoundNBT compoundnbt = users.getCompound(i);
            User user = new User(compoundnbt);
            Users.addUser(user);
        }

        ListNBT areas = nbt.getList("areas", 10);
        for (int i = 0; i < areas.size(); i++) {
            CompoundNBT compoundNBT = areas.getCompound(i);
            PvPArea area = new PvPArea(compoundNBT);
            Areas.addToList(area);
        }

        ListNBT dimensionSettings = nbt.getList("dimensions", 10);
        for (int i = 0; i < dimensionSettings.size(); i++) {
            CompoundNBT compoundNBT = dimensionSettings.getCompound(i);

            PvPStatus status = PvPStatus.fromInt(compoundNBT.getInt("status"));
            Optional<DimensionType> value = Functions.getDimensionTypeForString(compoundNBT.getString("dimension"));
            value.ifPresent(dimensionType -> ServerHandler.getDimensionSettings().put(dimensionType, status));

        }
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT users = new ListNBT();
        for (User user : Users.getUserList()) {
            CompoundNBT nbt = user.save();
            users.add(nbt);
        }
        compound.put("users", users);

        ListNBT areas = new ListNBT();
        for (PvPArea area : Areas.getAreas()) {
            CompoundNBT nbt = area.save();
            areas.add(nbt);
        }
        compound.put("areas", areas);

        ListNBT dimensions = new ListNBT();
        for (Map.Entry<DimensionType, PvPStatus> dimensionEntry : ServerHandler.getDimensionSettings().entrySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();

            compoundNBT.putString("dimension", dimensionEntry.getKey().getRegistryName().toString());
            compoundNBT.putInt("status", dimensionEntry.getValue().toInt());
            dimensions.add(compoundNBT);
        }
        compound.put("dimensions", dimensions);


        return compound;
    }
}
