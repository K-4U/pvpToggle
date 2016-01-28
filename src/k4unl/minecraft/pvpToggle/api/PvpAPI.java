package k4unl.minecraft.pvpToggle.api;

import k4unl.minecraft.pvpToggle.api.exceptions.*;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PvpAPI {
    private static IPvpToggleApi instance;

    public static IPvpToggleApi getInstance() {

        return instance;
    }

    public interface IPvpToggleApi {

        /**
         * Gets the PVPStatus of a given userareaName
         * Make sure to use the userareaName, NOT the displayareaName or the UUID!
         * @param username
         * @return
         */
        PvPStatus getPvpStatus(String username) throws NoSuchUserException;

        /**
         * Sets the PVP Status of a given userareaName.
         * Keep in mind that as soon as a player travels trough an area or a dimension, the forced pvp will drop!
         * @param userame
         * @param newStatus
         */
        void setPvpStatus(String userame, PvPStatus newStatus) throws NoSuchUserException;

        /**
         * Gets the setting for a given dimension id.
         * The returned variable can only have the following values:
         * - FORCEDON
         * - FORCEDOFF
         * - NOTFORCED
         * @param dimensionID
         * @return
         */
        PvPStatus getDimensionSetting(int dimensionID) throws NoSuchDimensionSavedException;


        /**
         * Sets the setting for a given dimension id.
         * Please only use one of the following values for the toSet argument:
         * - FORCEDON
         * - FORCEDOFF
         * - NOTFORCED
         *
         * This will also save it to the config.
         * @param dimensionId
         * @param toSet
         */
        void setDimensionSetting(int dimensionId, PvPStatus toSet) throws IncorrectStatusException;


        /**
         * Checks if a BlockPos is in an area, doesn't tell you which.
         * Use this method before getAreaName to avoid exceptions.
         * @param pos The blockpos to set
         * @param dimensionId
         */
        boolean isInAnArea(BlockPos pos, int dimensionId);

        /**
         * Gets the area areaName for a given blockpos
         * @param pos
         * @param dimensionId
         * @return
         * @throws NoAreaFoundException
         */
        String getAreaName(BlockPos pos, int dimensionId) throws NoAreaFoundException;

        /**
         * Gets the forced status for a given areaName.
         * The returned variable can only have the following values:
         * - FORCEDON
         * - FORCEDOFF
         * @param areaName
         * @return
         * @throws NoAreaFoundException
         */
        PvPStatus getAreaSetting(String areaName) throws NoAreaFoundException;

        /**
         * Gets whether or not the mod announces that the player enters or leaves this area.
         * @param areaName
         * @return
         * @throws NoAreaFoundException
         */
        boolean getAreaAnnouncesEntry(String areaName) throws NoAreaFoundException;

        /**
         * Sets an area given by areaName to the given status
         Please only use one of the following values for the toSet argument:
         * - FORCEDON
         * - FORCEDOFF
         * @param areaName
         * @param toSet
         * @throws NoAreaFoundException
         */
        void setAreaSetting(String areaName, PvPStatus toSet) throws NoAreaFoundException, IncorrectStatusException;

        /**
         * Sets whether or not the mod announces that the player enters or leaves this area.
         * @param areaName
         * @param toSet
         * @throws NoAreaFoundException
         */
        void setAreaAnnouncesEntry(String areaName, boolean toSet) throws NoAreaFoundException;

        /**
         * Creates a new area
         * Please only use one of the following values for the toSet argument:
         * - FORCEDON
         * - FORCEDOFF
         * @param areaName
         * @param location1
         * @param location2
         * @param dimensionId
         * @param toSet
         * @param announce
         */
        void createNewArea(String areaName, BlockPos location1, BlockPos location2, int dimensionId, PvPStatus toSet, boolean announce) throws
          AreaNameAlreadyExistsException, IncorrectStatusException;


        /**
         * Returns a list of two blockpos, containing the x, y and z coordinates of two opposite corners of the area.
         * @param areaName
         * @return
         * @throws NoAreaFoundException
         */
        List<BlockPos> getAreaBoundaries(String areaName) throws NoAreaFoundException;

        /**
         * Gets the dimension of the given area.
         * @param areaName
         * @return
         * @throws NoAreaFoundException
         */
        int getDimensionIdFromArea(String areaName) throws NoAreaFoundException;

        /**
         * Deletes an area given by areaName.
         * @param areaName
         * @throws NoAreaFoundException
         */
        void deleteArea(String areaName) throws NoAreaFoundException;
    }

    /**
     * For internal use only, don't call it.
     *
     * @param inst
     */
    public static void init(IPvpToggleApi inst) {

        if (instance == null && Loader.instance().activeModContainer().getModId().equals(ModInfo.ID)) {
            instance = inst;
            Log.info("Initialized PVPToggle API");
        } else {
            throw new IllegalStateException("This method should be called from PVPToggle only!");
        }
    }
}
