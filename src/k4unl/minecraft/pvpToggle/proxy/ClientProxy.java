package k4unl.minecraft.pvpToggle.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public void init(){

	}

    @Override
    public EntityPlayer getPlayer() {

        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    public void postInit(FMLPostInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.instance);
    }
}
