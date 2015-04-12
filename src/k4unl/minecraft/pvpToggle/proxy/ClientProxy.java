package k4unl.minecraft.pvpToggle.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import k4unl.minecraft.pvpToggle.client.gui.GuiRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public void init(){

	}

    @Override
    public EntityPlayer getPlayer() {

        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    public void postInit(FMLPostInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new GuiRenderer());
    }
}
