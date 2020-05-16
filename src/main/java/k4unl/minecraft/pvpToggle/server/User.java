package k4unl.minecraft.pvpToggle.server;

import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private boolean hasPVP;
    private PvPStatus isPvPForced = PvPStatus.NOTFORCED;
    private int coolDown;
    private String isInArea = "";

    public User(UUID userUUID, boolean _hasPVP) {

        uuid = userUUID;
        hasPVP = _hasPVP;
        isPvPForced = PvPStatus.NOTFORCED;
        isInArea = "";
    }

    public User(UUID userUUID) {
        uuid = userUUID;
        hasPVP = false;
        isPvPForced = PvPStatus.NOTFORCED;
        isInArea = "";
    }

    public User(CompoundNBT compoundnbt) {
        this.uuid = compoundnbt.getUniqueId("username");
        this.hasPVP = compoundnbt.getBoolean("hasPVP");
        this.isInArea = compoundnbt.getString("isInArea");
        this.coolDown = compoundnbt.getInt("cooldown");
    }

    public CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("username", this.uuid);
        nbt.putBoolean("hasPVP", this.hasPVP);
        nbt.putString("isInArea", this.isInArea);
        nbt.putInt("cooldown", this.coolDown);

        return nbt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean getPVP() {
        return hasPVP;
    }

    public void setPVP(boolean newPVP) {
        hasPVP = newPVP;
        coolDown = PvPConfig.cooldownInSeconds.get() * 20;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void tickCoolDown() {
        if (coolDown > 0) {
            coolDown--;
        }
    }

    public PvPStatus getIsPvPForced() {
        return isPvPForced;
    }

    public void setIsPvPForced(PvPStatus isIt) {
        isPvPForced = isIt;
    }

    public String getIsInArea() {
        return isInArea;
    }

    public void setIsInArea(String areaName) {
        if (areaName != null)
            isInArea = areaName;
    }

    public PvPStatus getPvpStatus() {
        if (hasPVP && getIsPvPForced() == PvPStatus.NOTFORCED) {
            return PvPStatus.ON;
        } else if (!hasPVP && getIsPvPForced() == PvPStatus.NOTFORCED) {
            return PvPStatus.OFF;
        } else {
            return getIsPvPForced();
        }
    }

    public void setPvpStatus(PvPStatus newStatus) {
        hasPVP = newStatus == PvPStatus.ON;
        if (newStatus != PvPStatus.ON && newStatus != PvPStatus.OFF) {
            isPvPForced = newStatus;
        } else {
            isPvPForced = PvPStatus.NOTFORCED;
        }
    }
}
