package k4unl.minecraft.pvpToggle.proxy;


import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.common.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	public void init(){
        
        NetworkRegistry.INSTANCE.registerGuiHandler(PvpToggle.instance, new GuiHandler());
	}

    public EntityPlayer getPlayer() {
        return null;
    }


    public void postInit(FMLPostInitializationEvent event) {

    }
}
