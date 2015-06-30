package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.commands.CommandK4Base;
import k4unl.minecraft.pvpToggle.lib.User;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class CommandPVP extends CommandK4Base{

	
	@Override
	public String getCommandName() {
		return "pvp";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/pvp on/off";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] var2) {
        if(var2.length < 1){
            sender.addChatMessage(new ChatComponentText("Usage: /pvp on/off."));
            return;
        }

		User sndr = Users.getUserByName(sender.getName());
        if(Users.isInCoolDown(sender.getName())){
            float coolDownInSeconds = sndr.getCoolDown() / 20.0F;
            sender.addChatMessage(new ChatComponentText("You're in cooldown! You have to wait " + coolDownInSeconds + "s before switching again."));
            return;
        }
        if(var2[0].equals("on")){
            if(sndr.getPVP()){
                sender.addChatMessage(new ChatComponentText("PVP is already enabled for you"));
            }else{
                sndr.setPVP(true);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Warning: PVP is now enabled. Players who also have PVP " +
                  EnumChatFormatting.RED + "enabled " +
                  "can now hurt/kill you! To turn this off, type /pvp off"));
                NetworkHandler.sendToDimension(Users.createPacket(sender.getName()), ((EntityPlayerMP) sender).dimension);
            }
        }else if(var2[0].equals("off")){
            if(sndr.getPVP()){
                sndr.setPVP(false);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "PVP is now disabled. Players can no longer" +
                  " hurt/kill " + EnumChatFormatting.GREEN + "you! To turn PVP back on, type /pvp on"));
                NetworkHandler.sendToDimension(Users.createPacket(sender.getName()), ((EntityPlayerMP)sender).dimension);
            }else{
                sender.addChatMessage(new ChatComponentText("PVP is already disabled for you"));
            }
        }
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos){
		List<String> ret = new ArrayList<String>();
        ret.add("on");
        ret.add("off");
        return ret;
	}
}
