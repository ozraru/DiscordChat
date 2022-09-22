package work.raru.discordchat.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA.Status;

public class MinecraftCommand {
    private MinecraftCommand() {}

    static Random random = new Random();

    public static List<String> getAutoComplete(String[] args, Predicate<String> perm) {
		ArrayList<String> result = new ArrayList<String>();
		if (args.length == 1) {
            if (perm.test(Permissions.COMMAND_LINK.node)) {
                result.add("link");
            }
            if (perm.test(Permissions.COMMAND_RELOAD.node)) {
                result.add("reload");
            }
            if (perm.test(Permissions.COMMAND_RESTART.node)) {
                result.add("restart");
            }
            if (perm.test(Permissions.COMMAND_EMOJIFUL.node)) {
                result.add("emojiful");
            }
            result.add("help");
		}
		if (args.length == 2 && args[1].equals("link") && perm.test(Permissions.COMMAND_LINK_OTHER.node)) {
            return null; // Can specify player
		}
		return result;
    }

    public static String getHelpMessage(String label, Predicate<String> perm) {
        String msg = "";
        if (perm.test(Permissions.COMMAND_LINK.node)) {
            msg += label + " link: Link your accounts to Discord\n";
        }
        if (perm.test(Permissions.COMMAND_LINK_OTHER.node)) {
            msg += label + " link <player>: Issue other accounts token to link Discord\n";
        }
        if (perm.test(Permissions.COMMAND_RELOAD.node)) {
            msg += label + " reload: Reload config file\n";
        }
        if (perm.test(Permissions.COMMAND_RESTART.node)) {
            msg += label + " restart: Restart JDA\n";
        }
        if (perm.test(Permissions.COMMAND_EMOJIFUL.node)) {
            msg += label + " emojiful: Generate emojiful resource pack\n";
        }
        msg += label + " help: Show this message\n";
        return msg;
    }

    public static boolean link(@Nonnull IPlayer player, String targetArg) {
        return link(player, Main.platform.getUtility().getPlayer(player, targetArg));
    }

    public static boolean link(@Nonnull IPlayer player, @Nonnull IPlayer target) {
        if (!player.checkPerm(Permissions.COMMAND_LINK)) {
            player.sendMsg("You don't have permission: "+Permissions.COMMAND_LINK, false);
            return true;
        }
        if (target == null) {
            player.sendMsg("Not found User", false);
            return false;
        }
        if (target.getUUID() == null) {
            return false;
        }
        if (player.getUUID() != target.getUUID() && !player.checkPerm(Permissions.COMMAND_LINK_OTHER)) {
            player.sendMsg("You don't have permission: "+Permissions.COMMAND_LINK_OTHER, false);
            return true;
        }
        int expirationSeconds = Main.platform.getConfig().getTokenExpire();
        try {
            String token = UserLinkManager.linkQueue(target.getUUID(), expirationSeconds);
            String commandText = Main.platform.getConfig().getDiscordPrefix() + "link " + token;
            String messageText = String.format(
                    "Please send '%s' in discord #%s or %s's DM in %d seconds (click here to copy command)",
                    commandText, DiscordMessage.getChannel().getName(),
                    DiscordMessage.getChannel().getGuild().getSelfMember().getEffectiveName(),
                    expirationSeconds);
            player.sendMsgClipboard(messageText, true, commandText);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMsg("Error Link queue", false);
            return true;
        }
    }

    public static void reload(@Nonnull IPlayer player) {
        if (!player.checkPerm("discordchat.command.reload")) {
            player.sendMsg("You don't have permission: discordchat.command.reload", false);
            return;
        }
        Main.platform.getConfig().reload();
        player.sendMsg("Successfully reloaded", true);
        return;
    }

    public static void restart(@Nonnull IPlayer player) {
        if (!player.checkPerm("discordchat.command.restart")) {
            player.sendMsg("You don't have permission: discordchat.command.restart", false);
            return;
        }
        player.sendMsg("Shutdowning JDA...", true);
        try {
            Discord.shutdown(false);
            boolean shutdowned = false;
            for (int i = 0; i < 30; i++) {
                if (Discord.jda.getStatus().equals(Status.SHUTDOWN)) {
                    shutdowned = true;
                    break;
                }
                Thread.sleep(100);
            }
            if (shutdowned) {
                player.sendMsg("JDA successfully shutdowned", true);
            } else {
                player.sendMsg("shutdown timeout. Force shutdown...", true);
                Discord.shutdown(true);
                player.sendMsg("Force shutdown requested.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMsg("Unknown error has occured while shutdown JDA: " + e.getMessage(), false);
            player.sendMsg("Stacktrace is available in server console.", false);
            player.sendMsg("Ignore and continue restart...", true);
        }
        player.sendMsg("Starting JDA...", true);
        Discord.init();
        player.sendMsg("Waiting for Connect...", true);
        try {
            Discord.jda.awaitReady();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            player.sendMsg("Failed to start JDA.", false);
            return;
        }
        player.sendMsg("JDA started.", true);
        return;
    }

    public static void emojiful(@Nonnull IPlayer player) {
        try {
            if (!player.checkPerm("discordchat.command.emojiful")) {
                player.sendMsg("You don't have permission: discordchat.command.emojiful", false);
            }
            EmojifulGenerator.generate();
            player.sendMsg("Successfully generated emojiful datapack.", true);
            player.sendMsgSuggest("Restart server or click here to '/datapack enable \"file/discordchat\"'", true, "/datapack enable \"file/discordchat\"");
            player.sendMsg("Warning: this command may cause all client freeze few seconds", true);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMsg("Failed to generate emojiful datapack.", false);
        }
    }
}
