package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.commands.CommandK4Base;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.server.User;
import k4unl.minecraft.pvpToggle.server.Users;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CommandPVP extends CommandK4Base {


    @Override
    public String getName() {

        return "pvp";
    }

    @Override
    public String getUsage(ICommandSender sender) {

        return "/pvp on/off";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length < 1) {
            sender.sendMessage(new TextComponentString("Usage: /pvp on/off."));
            return;
        }

        User sndr = Users.getUserByName(sender.getName());
        if (Users.isInCoolDown(sender.getName())) {
            float coolDownInSeconds = sndr.getCoolDown() / 20.0F;
            sender.sendMessage(new TextComponentString("You're in cooldown! You have to wait " + coolDownInSeconds + "s before switching again."));
            return;
        }
        if (args[0].equals("on")) {
            if (sndr.getPVP()) {
                sender.sendMessage(new TextComponentString("PVP is already enabled for you"));
            } else {
                sndr.setPVP(true);
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Warning: PVP is now enabled. Players who also have PVP " +
                        TextFormatting.RED + "enabled " +
                        "can now hurt/kill you! To turn this off, type /pvp off"));
                PvpToggle.networkHandler.sendToDimension(Users.createPacket(sender.getName()), ((EntityPlayerMP) sender).dimension);
            }
        } else if (args[0].equals("off")) {
            if (sndr.getPVP()) {
                sndr.setPVP(false);
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "PVP is now disabled. Players can no longer" +
                        " hurt/kill " + TextFormatting.GREEN + "you! To turn PVP back on, type /pvp on"));
                PvpToggle.networkHandler.sendToDimension(Users.createPacket(sender.getName()), ((EntityPlayerMP) sender).dimension);
            } else {
                sender.sendMessage(new TextComponentString("PVP is already disabled for you"));
            }
        }
    }

    @Override
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {

        List<String> ret = new ArrayList<String>();
        ret.add("on");
        ret.add("off");
        return ret;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

        return true;
    }
}
