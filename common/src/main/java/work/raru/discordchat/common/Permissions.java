package work.raru.discordchat.common;

public enum Permissions {

    COMMAND_LINK("discordchat.command.link", "Permission for /discordchat link", DefaultPermissions.ALL),
    COMMAND_LINK_OTHER("discordchat.command.link.other", "Permission for /discordchat link <other>", DefaultPermissions.OP),
    COMMAND_RELOAD("discordchat.command.reload", "Permission for /discordchat reload", DefaultPermissions.OP),
    COMMAND_RESTART("discordchat.command.restart", "Permission for /discordchat restart", DefaultPermissions.OP),
    COMMAND_EMOJIFUL("discordchat.command.emojiful", "Permission for /discordchat emojiful", DefaultPermissions.OP),

    LOGIN_LINK("discordchat.login.link", "Linking while logging in", DefaultPermissions.ALL),
    LOGIN_LINKED("discordchat.login.linked", "Loging in who linked", DefaultPermissions.ALL),
    LOGIN_UNLINKED("discordchat.login.unlinked", "Loging in who unlinked", DefaultPermissions.OP);

    public final String node;
    public final String description;
    public final DefaultPermissions defaultPerm;

    private Permissions(String node, String description, DefaultPermissions defaultPerm) {
        this.node = node;
        this.description = description;
        this.defaultPerm = defaultPerm;
    }
    public enum DefaultPermissions {
        ALL, OP, NONE;
    }
}
