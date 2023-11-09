package work.raru.discordchat.forge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import work.raru.discordchat.common.MinecraftCommand;
import work.raru.discordchat.common.Permissions;

public class ForgeCommand {
    private ForgeCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal("discordchat");
        LiteralArgumentBuilder<CommandSource> link = Commands.literal("link").requires(source -> {
            ForgePlayer player = new ForgePlayer(source);
            return player.checkPerm(Permissions.COMMAND_LINK);
        }).then(Commands.argument("target", EntityArgument.player())).executes(ForgeCommand::link);
        root.then(link);
        root.then(Commands.literal("reload").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_RELOAD);
        }).executes(ForgeCommand::reload));
        root.then(Commands.literal("restart").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_RESTART);
        }).executes(ForgeCommand::restart));
        root.then(Commands.literal("emojiful").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_EMOJIFUL);
        }).executes(ForgeCommand::emojiful));
        root.then(Commands.literal("help").executes(ForgeCommand::help));
        dispatcher.register(root);
    }

    public static int link(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity target = null;
            try {
                target = EntityArgument.getPlayer(context, "target");
            } catch (Exception e) {
                // ignore
            }
            ForgePlayer player = target != null ? new ForgePlayer(target) : new ForgePlayer(context.getSource());
            MinecraftCommand.link(new ForgePlayer(context.getSource()), player);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendFailure(new StringTextComponent("Unknown error has occured"));
            return 0;
        }
    }

    public static int reload(CommandContext<CommandSource> context) {
        MinecraftCommand.reload(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int restart(CommandContext<CommandSource> context) {
        MinecraftCommand.restart(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int emojiful(CommandContext<CommandSource> context) {
        MinecraftCommand.emojiful(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<CommandSource> context) {
        ForgePlayer player = new ForgePlayer(context.getSource());
        String msg = MinecraftCommand.getHelpMessage(context.getRootNode().getName(), player::checkPerm);
        player.sendMsg(msg, true);
        return Command.SINGLE_SUCCESS;
    }
}
