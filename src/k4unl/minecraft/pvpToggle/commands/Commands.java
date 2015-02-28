package k4unl.minecraft.pvpToggle.commands;


import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Commands {

	
	public static void init(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandPVP());
	}
}
