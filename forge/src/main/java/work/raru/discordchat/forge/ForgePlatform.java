package work.raru.discordchat.forge;

import work.raru.discordchat.common.IConfig;
import work.raru.discordchat.common.IMinecraftChat;
import work.raru.discordchat.common.IPlatform;
import work.raru.discordchat.common.IUtility;

public class ForgePlatform implements IPlatform {

    ForgeConfig config;
    ForgeChat chat;
    ForgeUtility utility;

    ForgePlatform() {
        config = new ForgeConfig();
        chat = new ForgeChat();
        utility = new ForgeUtility();
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public IMinecraftChat getChat() {
        return chat;
    }

    @Override
    public IUtility getUtility() {
        return utility;
    }
    
}
