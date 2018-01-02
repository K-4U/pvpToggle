package k4unl.minecraft.pvpToggle.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * @author Koen Beckers (K-4U)
 */
public class MainContainer extends Container {
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        
        return true;
    }
}
