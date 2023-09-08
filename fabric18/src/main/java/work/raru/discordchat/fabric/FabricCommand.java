package work.raru.discordchat.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import work.raru.discordchat.common.MinecraftCommand;
import work.raru.discordchat.common.Permissions;

public class FabricCommand {
    private FabricCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("discordchat");
        LiteralArgumentBuilder<ServerCommandSource> link = CommandManager.literal("link").requires(source -> {
            FabricPlayer player = new FabricPlayer(source);
            return player.checkPerm(Permissions.COMMAND_LINK);
        }).then(CommandManager.argument("target", EntityArgumentType.player())).executes(FabricCommand::link);
        root.then(link);
        root.then(CommandManager.literal("reload").requires(source -> new FabricPlayer(source).checkPerm(Permissions.COMMAND_RELOAD)).executes(FabricCommand::reload));
        root.then(CommandManager.literal("restart").requires(source -> new FabricPlayer(source).checkPerm(Permissions.COMMAND_RESTART)).executes(FabricCommand::restart));
        root.then(CommandManager.literal("emojiful").requires(source -> new FabricPlayer(source).checkPerm(Permissions.COMMAND_EMOJIFUL)).executes(FabricCommand::emojiful));
        root.then(CommandManager.literal("help").executes(FabricCommand::help));
        dispatcher.register(root);
    }

    public static int link(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity target = null;
            try {
                target = EntityArgumentType.getPlayer(context, "target");
            } catch (Exception e) {
                // ignore
            }
            FabricPlayer player = target != null ? new FabricPlayer(target) : new FabricPlayer(context.getSource());
            MinecraftCommand.link(new FabricPlayer(context.getSource()), player);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            DiscordChat.LOGGER.error("An error occurred while running a command:", e);
            context.getSource().sendError(new LiteralText("Unknown error has occurred"));
            return 0;
        }
    }

    public static int reload(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.reload(new FabricPlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int restart(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.restart(new FabricPlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int emojiful(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.emojiful(new FabricPlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<ServerCommandSource> context) {
        FabricPlayer player = new FabricPlayer(context.getSource());
        String msg = MinecraftCommand.getHelpMessage(context.getRootNode().getName(), player::checkPerm);
        player.sendMsg(msg, true);
        return Command.SINGLE_SUCCESS;
    }
}
