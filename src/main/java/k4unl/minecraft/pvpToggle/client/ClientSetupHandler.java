package k4unl.minecraft.pvpToggle.client;

import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModInfo.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupHandler {

    public static void init(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::onRenderGameOverlay);
        MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::onPlayerRender);
        MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::tickPlayer);
    }

}
