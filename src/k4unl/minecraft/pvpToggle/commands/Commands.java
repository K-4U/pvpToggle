package k4unl.minecraft.pvpToggle.commands;


import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Commands {
	public static void init(FMLServerStartingEvent event){
        //if(event.getSide().isServer()) {
            event.registerServerCommand(new CommandPVP());
            event.registerServerCommand(new CommandPvpToggle());
        //}
	}
}
