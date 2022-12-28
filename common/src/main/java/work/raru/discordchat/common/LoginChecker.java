package work.raru.discordchat.common;

import javax.annotation.Nullable;

public class LoginChecker {
    private LoginChecker() {}

    public static @Nullable String checkLogin(IPlayer player) {
        if (!Main.platform.getConfig().getLoginRestriction()) {
            return null;
        }
        try {
            if (UserLinkManager.getDiscordId(player.getUUID()) < 0) {
                if (player.checkPerm(Permissions.LOGIN_UNLINKED)) {
                    return null;
                }
            } else {
                if (player.checkPerm(Permissions.LOGIN_LINKED)) {
                    return null;
                }
            }
            if (player.checkPerm(Permissions.LOGIN_LINK)) {
                int expirationSeconds = Main.platform.getConfig().getTokenExpire();
                @SuppressWarnings("null")
                String token = UserLinkManager.linkQueue(player.getUUID(), expirationSeconds);
                String commandText = Main.platform.getConfig().getDiscordPrefix() + "link " + token;
                return String.format(
                        "Please send '%s' in discord #%s in %d seconds",
                        commandText, DiscordMessage.getChannel().getName(),
                        expirationSeconds);
            } else {
                return "You don't permitted to login and linking on login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error occured while checking login by DiscordChat";
        }
    }
}
