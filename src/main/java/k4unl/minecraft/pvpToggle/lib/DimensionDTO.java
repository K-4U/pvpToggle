package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.api.PvPStatus;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author Koen Beckers (K-4U)
 */
public class DimensionDTO {

    public DimensionType dimension;
    public String dimensionName;
    public PvPStatus status;

    public DimensionDTO(DimensionType dimension, String dimensionName, PvPStatus status) {

        this.dimension = dimension;
        this.dimensionName = dimensionName;
        this.status = status;
    }

    public DimensionDTO() {


    }
}
