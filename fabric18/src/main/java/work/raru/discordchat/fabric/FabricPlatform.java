package work.raru.discordchat.fabric;

import net.minecraft.server.MinecraftServer;
import work.raru.discordchat.common.IConfig;
import work.raru.discordchat.common.IMinecraftChat;
import work.raru.discordchat.common.IPlatform;
import work.raru.discordchat.common.IUtility;

public class FabricPlatform implements IPlatform {

    FabricConfig config;
    FabricChat chat;
    FabricUtility utility;

    FabricPlatform(MinecraftServer server) {
        config = new FabricConfig();
        chat = new FabricChat(server);
        utility = new FabricUtility(server);
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
