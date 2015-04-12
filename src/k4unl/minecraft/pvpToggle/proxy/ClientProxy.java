package k4unl.minecraft.pvpToggle.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.k4lib.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {
	
	public void init(){

	}

    @Override
    public EntityPlayer getPlayer() {

        return FMLClientHandler.instance().getClientPlayerEntity();
    }
}
