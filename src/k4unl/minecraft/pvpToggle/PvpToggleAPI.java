package k4unl.minecraft.pvpToggle;

import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.api.PvpAPI.IPvpToggleApi;
import k4unl.minecraft.pvpToggle.api.exceptions.*;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.server.User;
import k4unl.minecraft.pvpToggle.server.Users;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PvpToggleAPI implements IPvpToggleApi {

    @Override
    public PvPStatus getPvpStatus(String username) throws NoSuchUserException {

        for (User u : Users.getUserList()) {
            if (u.getUserName().equals(username)) {
                return u.getPvpStatus();
            }
        }
        throw new NoSuchUserException(username);
    }

    @Override
    public void setPvpStatus(String username, PvPStatus newStatus) throws NoSuchUserException {

        for (User u : Users.getUserList()) {
            if (u.getUserName().equals(username)) {
                u.setPvpStatus(newStatus);
            }
        }
        throw new NoSuchUserException(username);
    }

    @Override
    public PvPStatus getDimensionSetting(int dimensionID) throws NoSuchDimensionSavedException {

        if (ServerHandler.getDimensionSettings().containsKey(dimensionID)) {
            return ServerHandler.getDimensionSettings().get(dimensionID);
        } else {
            throw new NoSuchDimensionSavedException();
        }
    }

    @Override
    public void setDimensionSetting(int dimensionId, PvPStatus toSet) throws IncorrectStatusException {

        if (toSet == PvPStatus.OFF || toSet == PvPStatus.ON) {
            throw new IncorrectStatusException();
        }
        if (ServerHandler.getDimensionSettings().containsKey(dimensionId)) {
            ServerHandler.getDimensionSettings().remove(dimensionId);
        }
        ServerHandler.getDimensionSettings().put(dimensionId, toSet);
    }

    @Override
    public boolean isInAnArea(BlockPos pos, int dimensionId) {

        for (PvPArea area : Areas.getAreas()) {
            if (area.contains(pos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getAreaName(BlockPos pos, int dimensionId) throws NoAreaFoundException {

        for (PvPArea area : Areas.getAreas()) {
            if (area.contains(pos)) {
                return area.getName();
            }
        }
        throw new NoAreaFoundException();
    }

    @Override
    public PvPStatus getAreaSetting(String name) throws NoAreaFoundException {

        PvPArea area = Areas.getAreaByName(name);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            return area.getForced() ? PvPStatus.FORCEDON : PvPStatus.FORCEDOFF;
        }
    }

    @Override
    public boolean getAreaAnnouncesEntry(String name) throws NoAreaFoundException {

        PvPArea area = Areas.getAreaByName(name);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            return area.getAnnounce();
        }
    }

    @Override
    public void setAreaSetting(String name, PvPStatus toSet) throws NoAreaFoundException, IncorrectStatusException {

        if (toSet != PvPStatus.FORCEDON && toSet != PvPStatus.FORCEDOFF) {
            throw new IncorrectStatusException();
        }
        PvPArea area = Areas.getAreaByName(name);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            area.setForced(toSet == PvPStatus.FORCEDON);
        }
    }

    @Override
    public void setAreaAnnouncesEntry(String name, boolean toSet) throws NoAreaFoundException {

        PvPArea area = Areas.getAreaByName(name);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            area.setAnnounce(toSet);
        }
    }

    @Override
    public void createNewArea(String name, BlockPos location1, BlockPos location2, int dimensionId, PvPStatus toSet, boolean announce) throws AreaNameAlreadyExistsException, IncorrectStatusException {

        if(Areas.getAreaByName(name) != null){
            throw new AreaNameAlreadyExistsException();
        }

        if (toSet != PvPStatus.FORCEDON && toSet != PvPStatus.FORCEDOFF) {
            throw new IncorrectStatusException();
        }
        Location loc1 = new Location(location1);
        Location loc2 = new Location(location2);
        PvPArea newArea = new PvPArea(name, loc1, loc2, dimensionId);
        newArea.setForced(toSet == PvPStatus.FORCEDON);
        newArea.setAnnounce(announce);
        Areas.addToList(newArea);

    }

    @Override
    public List<BlockPos> getAreaBoundaries(String areaName) throws NoAreaFoundException {

        PvPArea area = Areas.getAreaByName(areaName);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            List<BlockPos> ret = new ArrayList<BlockPos>();
            ret.add(area.getLoc1().toBlockPos());
            ret.add(area.getLoc2().toBlockPos());
            return ret;
        }
    }

    @Override
    public int getDimensionIdFromArea(String areaName) throws NoAreaFoundException {
        PvPArea area = Areas.getAreaByName(areaName);
        if (area == null) {
            throw new NoAreaFoundException();
        } else {
            return area.getDimensionId();
        }
    }

    @Override
    public void deleteArea(String name) throws NoAreaFoundException {
        if(Areas.getAreaByName(name) == null){
            throw new NoAreaFoundException();
        }
        Areas.removeAreaByName(name);
    }
}
