package k4unl.minecraft.pvpToggle.common;

import k4unl.minecraft.pvpToggle.client.gui.GuiPvpToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiHandler implements IGuiHandler {
    
    public enum GuiIDs {
        PVPTOGGLE_MAIN;
        
        public static GuiIDs fromOrdinal(int id) {
            
            for (GuiIDs guiID : GuiIDs.values()) {
                if (guiID.ordinal() == id) {
                    return guiID;
                }
            }
            return null;
        }
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        
        return null;// new MainContainer();
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        
        switch (GuiIDs.fromOrdinal(ID)){
    
            case PVPTOGGLE_MAIN:
                return new GuiPvpToggle();
        }
        return null;
    }
}
