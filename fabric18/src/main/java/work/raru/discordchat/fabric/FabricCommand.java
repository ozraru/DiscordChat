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
            ForgePlayer player = new ForgePlayer(source);
            return player.checkPerm(Permissions.COMMAND_LINK);
        }).then(CommandManager.argument("target", EntityArgumentType.player())).executes(FabricCommand::link);
        root.then(link);
        root.then(CommandManager.literal("reload").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_RELOAD);
        }).executes(FabricCommand::reload));
        root.then(CommandManager.literal("restart").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_RESTART);
        }).executes(FabricCommand::restart));
        root.then(CommandManager.literal("emojiful").requires(source -> {
            return new ForgePlayer(source).checkPerm(Permissions.COMMAND_EMOJIFUL);
        }).executes(FabricCommand::emojiful));
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
            ForgePlayer player = target != null ? new FabricPlayer(target) : new FabricPlayer(context.getSource());
            MinecraftCommand.link(new ForgePlayer(context.getSource()), player);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendError(new LiteralText("Unknown error has occured"));
            return 0;
        }
    }

    public static int reload(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.reload(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int restart(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.restart(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int emojiful(CommandContext<ServerCommandSource> context) {
        MinecraftCommand.emojiful(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<ServerCommandSource> context) {
        ForgePlayer player = new ForgePlayer(context.getSource());
        String msg = MinecraftCommand.getHelpMessage(context.getRootNode().getName(), player::checkPerm);
        player.sendMsg(msg, true);
        return Command.SINGLE_SUCCESS;
    }
}
