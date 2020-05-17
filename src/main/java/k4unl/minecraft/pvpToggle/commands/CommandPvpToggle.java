package k4unl.minecraft.pvpToggle.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import k4unl.minecraft.k4lib.commands.impl.CommandK4OpOnly;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.network.packets.PacketDataToClient;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;

public class CommandPvpToggle extends CommandK4OpOnly {

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal("version").executes(this::showVersion));
        argumentBuilder.then(Commands.literal("ui").executes(this::openUI));

        LiteralArgumentBuilder<CommandSource> areaCommand = Commands.literal("area");

        argumentBuilder.then(
                Commands.literal("dimension")
                        .then(Commands.argument("dimension", DimensionArgument.getDimension())
                                .then(Commands.literal("get").executes(this::getDimensionState))
                                .then(Commands.literal("set").
                                        then(Commands.argument("state", IntegerArgumentType.integer(-1, 1)).executes(this::setDimensionState)))
                        )
        );

        argumentBuilder.then(areaCommand.then(Commands.literal("new")
                .then(Commands.argument("name", StringArgumentType.word())
                        .then(Commands.argument("pos1", BlockPosArgument.blockPos())
                                .then(Commands.argument("pos2", BlockPosArgument.blockPos())
                                        .executes(this::addNewArea)
                                )
                        )
                )));

        argumentBuilder.then(areaCommand.then(Commands.literal("delete")
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(this::deleteArea)
                )));

        areaCommand.then(Commands.literal("options")).
                then(Commands.argument("name", StringArgumentType.word())
                        .then(Commands.literal("set")
                                .then(Commands.literal("announce")
                                        .then(Commands.argument("state", BoolArgumentType.bool()).executes(this::setAreaAnnounce)))
                                .then(Commands.literal("forced")
                                        .then(Commands.argument("state", BoolArgumentType.bool()).executes(this::setAreaForced))))
                        .then(Commands.literal("get")
                                .then(Commands.literal("announce").executes(this::getAreaAnnounce))
                                .then(Commands.literal("forced").executes(this::getAreaForced)))
                );
    }

    private int openUI(CommandContext<CommandSource> context) throws CommandSyntaxException {

        PacketDataToClient packet = new PacketDataToClient();
        packet.areasSet = true;
        packet.areaList = Areas.getAreas();
        packet.dimensionsSet = true;
        packet.dimensions = ServerHandler.createDimensionDTOList();
        packet.allDimensionsSet = true;
        packet.allDimensions = ServerHandler.createAllDimensionsDTOList();

        PvpToggle.networkHandler.sendTo(packet, context.getSource().asPlayer());
        return 0;
    }

    private int getAreaForced(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        PvPArea theArea = Areas.getAreaByName(name.toLowerCase());
        if (theArea == null) {
            context.getSource().sendErrorMessage(new StringTextComponent(TextFormatting.RED + "This area cannot be found!"));
            return 0;
        }
        context.getSource().sendFeedback(new StringTextComponent(theArea.getName() + " forced = " + theArea.getForced()), false);

        return 0;
    }

    private int getAreaAnnounce(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        PvPArea theArea = Areas.getAreaByName(name.toLowerCase());
        if (theArea == null) {
            context.getSource().sendErrorMessage(new StringTextComponent(TextFormatting.RED + "This area cannot be found!"));
            return 0;
        }
        context.getSource().sendFeedback(new StringTextComponent(theArea.getName() + " announce = " + theArea.getAnnounce()), false);

        return 0;
    }

    private int setAreaForced(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        PvPArea theArea = Areas.getAreaByName(name.toLowerCase());
        if (theArea == null) {
            context.getSource().sendErrorMessage(new StringTextComponent(TextFormatting.RED + "This area cannot be found!"));
            return 0;
        }
        theArea.setForced(BoolArgumentType.getBool(context, "state"));
        context.getSource().sendFeedback(new StringTextComponent(theArea.getName() + " forced = " + theArea.getForced()), false);

        return 0;
    }

    private int setAreaAnnounce(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        PvPArea theArea = Areas.getAreaByName(name.toLowerCase());
        if (theArea == null) {
            context.getSource().sendErrorMessage(new StringTextComponent(TextFormatting.RED + "This area cannot be found!"));
            return 0;
        }
        theArea.setAnnounce(BoolArgumentType.getBool(context, "state"));
        context.getSource().sendFeedback(new StringTextComponent(theArea.getName() + " announce = " + theArea.getAnnounce()), false);

        return 0;
    }

    private int deleteArea(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        PvPArea theArea = Areas.getAreaByName(name.toLowerCase());
        if (theArea == null) {
            context.getSource().sendErrorMessage(new StringTextComponent(TextFormatting.RED + "This area cannot be found!"));
            return 0;
        }
        Areas.removeAreaByName(name.toLowerCase());
        return 0;
    }

    private int addNewArea(CommandContext<CommandSource> context) throws CommandSyntaxException {
        BlockPos pos1 = BlockPosArgument.getBlockPos(context, "pos1");
        BlockPos pos2 = BlockPosArgument.getBlockPos(context, "pos2");
        String name = StringArgumentType.getString(context, "name");

        Location loc1 = new Location(pos1);
        Location loc2 = new Location(pos2);
        PvPArea newArea = new PvPArea(name.toLowerCase(), loc1, loc2, context.getSource().getEntity().getEntityWorld().getDimension().getType());
        context.getSource().sendFeedback(new StringTextComponent("Creating a new area called " + newArea.getName()), false);
        context.getSource().sendFeedback(new StringTextComponent(newArea.getLoc1().printLocation()), false);
        context.getSource().sendFeedback(new StringTextComponent(newArea.getLoc2().printLocation()), false);
        Areas.addToList(newArea);

        return 0;
    }

    private int setDimensionState(CommandContext<CommandSource> context) {
        DimensionType dimension = DimensionArgument.getDimensionArgument(context, "Dimension");
        int state = IntegerArgumentType.getInteger(context, "state");

        ServerHandler.getDimensionSettings().put(dimension, PvPStatus.fromInt(state));
        PvPStatus f = PvPStatus.fromInt(state);
        context.getSource().sendFeedback(new StringTextComponent("Dimension " + dimension + " = " + (f.equals(PvPStatus.NOTFORCED) ? "Not forced" : (f.equals(PvPStatus.FORCEDON) ? "Forced on" : "Forced off"))), false);
        return 0;
    }

    private int getDimensionState(CommandContext<CommandSource> context) {
        DimensionType dimension = DimensionArgument.getDimensionArgument(context, "Dimension");

        if (ServerHandler.getDimensionSettings().containsKey(dimension.getId())) {
            PvPStatus f = ServerHandler.getDimensionSettings().get(dimension.getId());
            context.getSource().sendFeedback(new StringTextComponent("Dimension " + dimension + " = " + (f.equals(PvPStatus.NOTFORCED) ? "Not forced" : (f.equals(PvPStatus.FORCEDON) ? "Forced on" : "Forced off"))), false);
        } else {
            context.getSource().sendFeedback(new StringTextComponent("Dimension " + dimension + " = Not forced"), false);
        }
        return 0;
    }

    private int showVersion(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new StringTextComponent("PvPToggle version " + ModInfo.VERSION), false);
        return 0;
    }

    @Override
    public String getName() {

        return "pvptoggle";
    }
}
