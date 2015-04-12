package k4unl.minecraft.pvpToggle.commands;

import k4unl.minecraft.k4lib.lib.SpecialChars;
import k4unl.minecraft.pvpToggle.lib.User;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.network.packets.PacketSetPvP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandPVP extends CommandBase{

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender){
		return true;
	}
	
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

		User sndr = Users.getUserByName(sender.getCommandSenderName());
        if(Users.isInCoolDown(sender.getCommandSenderName())){
            float coolDownInSeconds = sndr.getCoolDown() / 20.0F;
            sender.addChatMessage(new ChatComponentText("You're in cooldown! You have to wait " + coolDownInSeconds + "s before switching again."));
            return;
        }
        if(var2[0].equals("on")){
            if(sndr.getPVP()){
                sender.addChatMessage(new ChatComponentText("PVP is already enabled for you"));
            }else{
                sndr.setPVP(true);
                sender.addChatMessage(new ChatComponentText(SpecialChars.RED + "Warning: PVP is now enabled. Players who also have PVP " +
                  SpecialChars.RED + "enabled " +
                  "can now hurt/kill you! To turn this off, type /pvp off"));
                NetworkHandler.sendTo(new PacketSetPvP(true), (EntityPlayerMP)sender);
            }
        }else if(var2[0].equals("off")){
            if(sndr.getPVP()){
                sndr.setPVP(false);
                sender.addChatMessage(new ChatComponentText(SpecialChars.GREEN + "PVP is now disabled. Players can no longer" +
                  " hurt/kill " + SpecialChars.GREEN + "you! To turn PVP back on, type /pvp on"));
                NetworkHandler.sendTo(new PacketSetPvP(false), (EntityPlayerMP)sender);
            }else{
                sender.addChatMessage(new ChatComponentText("PVP is already disabled for you"));
            }
        }
	}

	@Override
	public List addTabCompletionOptions(ICommandSender cmd, String[] args) {
		return null;
	}
}
