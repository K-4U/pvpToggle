package k4unl.minecraft.pvpToggle.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import k4unl.minecraft.k4lib.commands.Command;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.server.User;
import k4unl.minecraft.pvpToggle.server.Users;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.UUID;

public class CommandPVP implements Command {


    @Override
    public void register(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal("on").executes(this::enablePVP));
        argumentBuilder.then(Commands.literal("off").executes(this::disablePVP));
    }

    private int disablePVP(CommandContext<CommandSource> context) throws CommandSyntaxException {
        UUID id = context.getSource().asPlayer().getGameProfile().getId();
        User sndr = Users.getUserByUUID(id);
        if (Users.isInCoolDown(id)) {
            float coolDownInSeconds = sndr.getCoolDown() / 20.0F;
            context.getSource().sendFeedback(new StringTextComponent("You're in cooldown! You have to wait " + coolDownInSeconds + "s before switching again."), false);
            return 0;
        }
        if (sndr.getPVP()) {
            sndr.setPVP(false);
            context.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "PVP is now disabled. Players can no longer" +
                    " hurt/kill " + TextFormatting.GREEN + "you! To turn PVP back on, type /pvp on"), false);
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(id), context.getSource().getEntity().dimension);
        } else {
            context.getSource().sendFeedback(new StringTextComponent("PVP is already disabled for you"), false);
        }

        return 0;
    }

    private int enablePVP(CommandContext<CommandSource> context) throws CommandSyntaxException {
        UUID id = context.getSource().asPlayer().getGameProfile().getId();
        User sndr = Users.getUserByUUID(id);
        if (Users.isInCoolDown(id)) {
            float coolDownInSeconds = sndr.getCoolDown() / 20.0F;
            context.getSource().sendFeedback(new StringTextComponent("You're in cooldown! You have to wait " + coolDownInSeconds + "s before switching again."), false);
            return 0;
        }

        if (sndr.getPVP()) {
            context.getSource().sendFeedback(new StringTextComponent("PVP is already enabled for you"), false);
        } else {
            sndr.setPVP(true);
            context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "Warning: PVP is now enabled. Players who also have PVP " +
                    TextFormatting.RED + "enabled " +
                    "can now hurt/kill you! To turn this off, type /pvp off"), false);
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(id), context.getSource().getEntity().dimension);
        }

        return 0;
    }

    @Override
    public String getName() {

        return "pvp";
    }

    @Override
    public boolean canUse(CommandSource commandSource) {
        return commandSource.getEntity() instanceof ServerPlayerEntity;
    }

}
