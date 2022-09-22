package work.raru.discordchat.common;

public interface IPlatform {
    public IConfig getConfig();
    public IMinecraftChat getChat();
    public IUtility getUtility();
}
