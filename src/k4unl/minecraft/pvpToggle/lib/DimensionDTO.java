package k4unl.minecraft.pvpToggle.lib;

import k4unl.minecraft.pvpToggle.api.PvPStatus;

/**
 * @author Koen Beckers (K-4U)
 */
public class DimensionDTO {

    public int dimensionId;
    public String dimensionName;
    public PvPStatus status;
    
    public DimensionDTO(int dimensionId, String dimensionName, PvPStatus status) {
        
        this.dimensionId = dimensionId;
        this.dimensionName = dimensionName;
        this.status = status;
    }
    
    public DimensionDTO() {
    
    
    }
}
