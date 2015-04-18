package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.commands.CommandK4OpOnly;
import k4unl.minecraft.k4lib.lib.Area;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.config.ModInfo;
import k4unl.minecraft.k4lib.lib.SpecialChars;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandPvpToggle extends CommandK4OpOnly {

    @Override
    public String getCommandName() {

        return "pvptoggle";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length >= 1){
            if(args[0].toLowerCase().equals("version")) {
                sender.addChatMessage(new ChatComponentText("PvPToggle version " + ModInfo.VERSION));
            }
        }
        //Area
        if (args.length > 2){
            if(args[0].toLowerCase().equals("area")){
                handleAreaCommand(sender, args);
            }
        }
    }


    private void handleAreaCommand(ICommandSender sender, String[] args){
        if(args[1].toLowerCase().equals("new")){
            //Going to need a lot of arguments here.
            //<name> <x1> <y1> <z1> <x2> <y2> <z2>
            if(args.length < 9){
                sender.addChatMessage(new ChatComponentText(SpecialChars.RED + "Usage: /pvptoggle area new <name> <x1> <y1> <z1> <x2> <y2> <z2>"));
                return;
            }else{
                //We assume that we have enough arguments.
                Location loc1 = new Location(args[3], args[4], args[5]);
                Location loc2 = new Location(args[6], args[7], args[8]);
                Area newArea = new Area(args[2].toLowerCase(), loc1, loc2);
                sender.addChatMessage(new ChatComponentText("Creating a new area called " + newArea.getName()));
                sender.addChatMessage(new ChatComponentText(newArea.getLoc1().printLocation()));
                sender.addChatMessage(new ChatComponentText(newArea.getLoc2().printLocation()));
            }
        }

        if(args[1].toLowerCase().equals("option")){
            if(args.length >= 5) {
                if (args[2].toLowerCase().equals("get")) {
                    //Args[3] should be the name of the area
                    PvPArea theArea = Areas.getAreaByName(args[3].toLowerCase());
                    if(theArea == null){
                        sender.addChatMessage(new ChatComponentText(SpecialChars.RED + "This area cannot be found!"));
                        return;
                    }
                    if(args[4].toLowerCase().equals("announce")) {
                        sender.addChatMessage(new ChatComponentText(theArea.getName() + " announce = " + theArea.getAnnounce()));
                    }else if(args[4].toLowerCase().equals("forced")){
                        sender.addChatMessage(new ChatComponentText(theArea.getName() + " forced = " + theArea.getForced()));
                    }else{
                        sender.addChatMessage(new ChatComponentText(SpecialChars.RED + "Valid options: announce or forced"));
                    }
                }
            }else{
                sender.addChatMessage(new ChatComponentText(SpecialChars.RED + "Usage: /pvptoggle area options <name> [get/set] <optionName> "
                  + "[newValue]"));
            }
        }
    }
}