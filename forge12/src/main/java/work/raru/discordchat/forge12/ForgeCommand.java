package work.raru.discordchat.forge12;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import work.raru.discordchat.common.MinecraftCommand;

public class ForgeCommand extends CommandBase {

    public static void link(ICommandSender sender, String[] args) {
        MinecraftCommand.link(new ForgePlayer(sender), args.length == 2 ? args[1] : null);
    }

    public static void reload(ICommandSender sender) {
        MinecraftCommand.reload(new ForgePlayer(sender));
    }

    public static void restart(ICommandSender sender) {
        MinecraftCommand.restart(new ForgePlayer(sender));
    }
    public static void help(ICommandSender sender) {
        ForgePlayer player = new ForgePlayer(sender);
        String msg = MinecraftCommand.getHelpMessage("discordchat", player::checkPerm);
        player.sendMsg(msg, true);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1 || args[0].equals("help")) {
            help(sender);
            return;
        }
        switch (args[0]) {
            case "link":
                link(sender, args);
                break;
            case "reload":
                reload(sender);
                break;
            case "restart":
                restart(sender);
                break;
            default:
                help(sender);
                break;
        }
    }

    @Override
    public String getName() {
        return "discordchat";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return MinecraftCommand.getHelpMessage("discordchat", new ForgePlayer(sender)::checkPerm);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> list = new ArrayList<>();
        list.add("dc");
        return list;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
            @Nullable BlockPos pos) {
        return MinecraftCommand.getAutoComplete(args, new ForgePlayer(sender)::checkPerm);
    }
}
