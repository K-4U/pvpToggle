package k4unl.minecraft.pvpToggle.proxy;


import k4unl.minecraft.pvpToggle.client.ClientEventHandler;
import k4unl.minecraft.pvpToggle.commands.CommandPvpToggleClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public void init(){
        ClientCommandHandler.instance.registerCommand(new CommandPvpToggleClient());
	}

    @Override
    public EntityPlayer getPlayer() {

        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    public void postInit(FMLPostInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.instance);
    }
}
