package k4unl.minecraft.pvpToggle.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class CommandPvpToggleClient implements ICommand {

    @Override
    public String getCommandName() {

        return "pvpui";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return "pvpui [location <x> <y>] [enable/disable]";
    }

    @Override
    public List getCommandAliases() {

        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {

        return false;
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }
}
