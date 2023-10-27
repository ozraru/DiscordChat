package work.raru.discordchat.forge19;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import work.raru.discordchat.common.MinecraftCommand;
import work.raru.discordchat.common.Permissions;

import java.util.HashMap;
import java.util.Map;

public class ForgeCommand {
    private ForgeCommand() {}

    public static Map<String, PermissionNode<Boolean>> permissionNodes = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("discordchat");
        LiteralArgumentBuilder<CommandSourceStack> link = Commands.literal("link").requires(source -> {
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

    public static int link(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = null;
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
            context.getSource().sendFailure(MutableComponent.create(new LiteralContents("Unknown error has occurred")));
            return 0;
        }
    }

    public static int reload(CommandContext<CommandSourceStack> context) {
        MinecraftCommand.reload(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int restart(CommandContext<CommandSourceStack> context) {
        MinecraftCommand.restart(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int emojiful(CommandContext<CommandSourceStack> context) {
        MinecraftCommand.emojiful(new ForgePlayer(context.getSource()));
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<CommandSourceStack> context) {
        ForgePlayer player = new ForgePlayer(context.getSource());
        String msg = MinecraftCommand.getHelpMessage(context.getRootNode().getName(), player::checkPerm);
        player.sendMsg(msg, true);
        return Command.SINGLE_SUCCESS;
    }
}
