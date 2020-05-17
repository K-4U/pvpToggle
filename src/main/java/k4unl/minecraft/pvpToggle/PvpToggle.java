package k4unl.minecraft.pvpToggle;


import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.config.Config;
import k4unl.minecraft.pvpToggle.api.PvpAPI;
import k4unl.minecraft.pvpToggle.client.ClientSetupHandler;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.Users;
import k4unl.minecraft.pvpToggle.world.PvPWorldData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModInfo.ID)
public class PvpToggle {

    public static PvpToggle instance;

    public static NetworkHandler networkHandler = new NetworkHandler();

    public PvpToggle() {
        Log.init();
        Config config = new PvPConfig();
        config.load(ModInfo.ID);

        PvpAPI.init(new PvpToggleAPI());

        Areas.init();
        Users.init();

        networkHandler.init();

        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetupHandler::init);


    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartedEvent event) {

        PvPWorldData pvPWorldData = PvPWorldData.get(event.getServer().func_71218_a(DimensionType.OVERWORLD)); //getWorld
        boolean b = event.getServer() instanceof DedicatedServer;
        new Commands(b, event.getServer().getCommandManager().getDispatcher());

        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            if (Functions.getServer().isPVPEnabled()) {

                EventHelper.init();
            } else {
                Log.error("PVP is not enabled on your server, PVPToggle will NOT function!");
            }
        });
    }
}
