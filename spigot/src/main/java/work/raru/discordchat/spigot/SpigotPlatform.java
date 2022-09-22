package work.raru.discordchat.spigot;

import work.raru.discordchat.common.IConfig;
import work.raru.discordchat.common.IMinecraftChat;
import work.raru.discordchat.common.IPlatform;
import work.raru.discordchat.common.IUtility;

public class SpigotPlatform implements IPlatform {

    public final SpigotChat chat = new SpigotChat();
    public final SpigotConfig config = new SpigotConfig();
    public final SpigotUtility utility = new SpigotUtility();

    @Override
    public IMinecraftChat getChat() {
        return chat;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public IUtility getUtility() {
        return utility;
    }
    
}
