package k4unl.minecraft.pvpToggle.commands;


import com.mojang.brigadier.CommandDispatcher;
import k4unl.minecraft.k4lib.commands.CommandsRegistry;
import net.minecraft.command.CommandSource;

public class Commands extends CommandsRegistry {


    public Commands(boolean isDedicatedServer, CommandDispatcher<CommandSource> dispatcher) {
        if (isDedicatedServer) {
            register(dispatcher, new CommandPVP());
            register(dispatcher, new CommandPvpToggle());
        } else {
//            register(dispatcher, new CommandPvpToggleClient());
        }
    }


}
