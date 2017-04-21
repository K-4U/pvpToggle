package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.commands.CommandK4OpOnly;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class CommandPvpToggle extends CommandK4OpOnly {

    @Override
    public String getName() {

        return "pvptoggle";
    }

    @Override
    public String getUsage(ICommandSender p_71518_1_) {

        return "pvptoggle version|save|load|area|dimension";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length >= 1) {
            if (args[0].toLowerCase().equals("version")) {
                sender.sendMessage(new TextComponentString("PvPToggle version " + ModInfo.VERSION));
            } else if (args[0].toLowerCase().equals("save")) {
                Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
                Areas.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
                PvpToggle.instance.saveDimensionSettingsToFile(DimensionManager.getCurrentSaveRootDirectory());

                sender.sendMessage(new TextComponentString("Areas, users and dimensions saved to world dir!"));
            } else if (args[0].toLowerCase().equals("load")) {
                Users.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
                Areas.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
                PvpToggle.instance.readDimensionSettingsFromFile(DimensionManager.getCurrentSaveRootDirectory());

                sender.sendMessage(new TextComponentString("Areas, users and dimensions loaded from world dir!"));
            }
        }
        //Area
        if (args.length > 2) {
            if (args[0].toLowerCase().equals("area")) {
                handleAreaCommand(sender, args);
            } else if (args[0].toLowerCase().equals("dimension")) {
                handleDimensionCommand(sender, args);
            }
        }
    }

    private void handleDimensionCommand(ICommandSender sender, String[] args) {
        //1 = set/get
        //2 = dimension id
        //3 = -1/0/1

        if (args[1].toLowerCase().equals("get")) {
            if (args.length >= 3) {
                if (PvpToggle.instance.dimensionSettings.containsKey(Integer.parseInt(args[2]))) {
                    PvPStatus f = PvpToggle.instance.dimensionSettings.get(Integer.parseInt(args[2]));
                    sender.sendMessage(new TextComponentString("Dimension " + args[2] + " = " + (f.equals(PvPStatus.NOTFORCED) ? "Not forced" : (f.equals(PvPStatus.FORCEDON) ? "Forced on" : "Forced off"))));
                } else {
                    sender.sendMessage(new TextComponentString("Dimension " + args[2] + " = Not forced"));
                }
            }
        } else {
            if (args.length >= 4) {
                if (PvpToggle.instance.dimensionSettings.containsKey(Integer.parseInt(args[2]))) {
                    PvpToggle.instance.dimensionSettings.remove(Integer.parseInt(args[2]));
                }
                int v = Integer.parseInt(args[3]);

                if (v < -1 || v > 1) {
                    sender.sendMessage(new TextComponentString("Please enter -1, 0 or 1 as a value"));
                    return;
                }
                PvpToggle.instance.dimensionSettings.put(Integer.parseInt(args[2]), PvPStatus.fromInt(v));
                PvPStatus f = PvPStatus.fromInt(v);
                sender.sendMessage(new TextComponentString("Dimension " + args[2] + " = " + (f.equals(PvPStatus.NOTFORCED) ? "Not forced" : (f.equals(PvPStatus.FORCEDON) ? "Forced on" : "Forced off"))));
            }
        }
    }

    private void handleAreaCommand(ICommandSender sender, String[] args) {

        if (args[1].toLowerCase().equals("new")) {
            //Going to need a lot of arguments here.
            //<name> <x1> <y1> <z1> <x2> <y2> <z2>
            if (args.length < 9) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /pvptoggle area new <name> <x1> <y1> <z1> <x2> <y2> <z2>"));
                return;
            } else {
                //We assume that we have enough arguments.
                Location loc1 = new Location(args[3], args[4], args[5]);
                Location loc2 = new Location(args[6], args[7], args[8]);
                PvPArea newArea = new PvPArea(args[2].toLowerCase(), loc1, loc2, sender.getEntityWorld().provider.getDimension());
                sender.sendMessage(new TextComponentString("Creating a new area called " + newArea.getName()));
                sender.sendMessage(new TextComponentString(newArea.getLoc1().printLocation()));
                sender.sendMessage(new TextComponentString(newArea.getLoc2().printLocation()));
                Areas.addToList(newArea);
            }
        }

        if (args[1].toLowerCase().equals("delete")) {
            if (args.length >= 3) {
                PvPArea theArea = Areas.getAreaByName(args[2].toLowerCase());
                if (theArea == null) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "This area cannot be found!"));
                    return;
                }
                Areas.removeAreaByName(args[2].toLowerCase());
            }
        }

        if (args[1].toLowerCase().equals("options")) {
            if (args.length >= 5) {
                if (args[2].toLowerCase().equals("get")) {
                    //Args[3] should be the name of the area
                    PvPArea theArea = Areas.getAreaByName(args[3].toLowerCase());
                    if (theArea == null) {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "This area cannot be found!"));
                        return;
                    }
                    if (args[4].toLowerCase().equals("announce")) {
                        sender.sendMessage(new TextComponentString(theArea.getName() + " announce = " + theArea.getAnnounce()));
                    } else if (args[4].toLowerCase().equals("forced")) {
                        sender.sendMessage(new TextComponentString(theArea.getName() + " forced = " + theArea.getForced()));
                    } else {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Valid options: announce or forced"));
                    }
                }
                if (args[2].toLowerCase().equals("set")) {
                    //Args[3] should be the name of the area
                    PvPArea theArea = Areas.getAreaByName(args[3].toLowerCase());
                    if (theArea == null) {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "This area cannot be found!"));
                        return;
                    }
                    if (args[4].toLowerCase().equals("announce")) {
                        theArea.setAnnounce((args[5].toLowerCase().equals("on") || args[5].toLowerCase().equals("true")));
                        sender.sendMessage(new TextComponentString(theArea.getName() + " announce = " + theArea.getAnnounce()));
                    } else if (args[4].toLowerCase().equals("forced")) {
                        theArea.setForced((args[5].toLowerCase().equals("on") || args[5].toLowerCase().equals("true")));
                        sender.sendMessage(new TextComponentString(theArea.getName() + " forced = " + theArea.getForced()));
                    } else {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Valid options: announce or forced"));
                    }
                }
            } else {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /pvptoggle area options <name> [get/set] <optionName> "
                        + "[newValue]"));
            }
        }

        if(args[1].toLowerCase().equals("help")){
            sender.sendMessage(new TextComponentString("Usage: /pvptoggle area new|options|delete"));
        }
    }


    @Override
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {

        List<String> ret = new ArrayList<String>();

        if (args.length == 1) {
            ret.add("area");
            ret.add("dimension");
            ret.add("load");
            ret.add("save");
            ret.add("version");
        } else {
            if (args[0].toLowerCase().equals("area")) {
                if (args.length == 2) {
                    ret.add("new");
                    ret.add("delete");
                    ret.add("option");
                } else {
                    if (args[1].toLowerCase().equals("option")) {
                        if (args.length == 3) {
                            ret.add("get");
                            ret.add("set");
                        } else {
                            ret.add("announce");
                            ret.add("forced");
                        }
                    }
                }
            }
        }

        return ret;
    }
}
