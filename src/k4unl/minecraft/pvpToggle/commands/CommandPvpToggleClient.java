package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.lib.SpecialChars;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandPvpToggleClient implements ICommand {

    @Override
    public String getCommandName() {

        return "pvpui";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return "pvpui [location <x> <y>] [own enable/disable] [other enable/disable]";
    }

    @Override
    public List getCommandAliases() {

        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 2) {
            if (args[0].toLowerCase().equals("own")) {
                if(args[1].toLowerCase().equals("enable")){
                    PvPConfig.INSTANCE.setBool("renderOwnIcon", "ui", true);
                    sender.addChatMessage(new ChatComponentText("Own rendering is now " + SpecialChars.GREEN + "enabled."));
                }else if(args[1].toLowerCase().equals("disable")){
                    PvPConfig.INSTANCE.setBool("renderOwnIcon", "ui", false);
                    sender.addChatMessage(new ChatComponentText("Own rendering is now " + SpecialChars.RED + "disabled."));
                }
            }else if(args[0].toLowerCase().equals("other")){
                if(args[1].toLowerCase().equals("enable")){
                    PvPConfig.INSTANCE.setBool("renderOtherIcon", "ui", true);
                    sender.addChatMessage(new ChatComponentText("Other person rendering is now " + SpecialChars.GREEN + "enabled."));
                }else if(args[1].toLowerCase().equals("disable")){
                    PvPConfig.INSTANCE.setBool("renderOtherIcon", "ui", false);
                    sender.addChatMessage(new ChatComponentText("Other person rendering is now " + SpecialChars.RED + "disabled."));
                }
            }
        }else if(args.length == 3){
            if(args[0].toLowerCase().equals("location")){
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                PvPConfig.INSTANCE.setInt("x", "ui", x);
                PvPConfig.INSTANCE.setInt("y", "ui", y);
            }
        }else{
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
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
