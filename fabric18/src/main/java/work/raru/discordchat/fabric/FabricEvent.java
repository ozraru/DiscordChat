package work.raru.discordchat.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.ShutdownManager;

public class FabricEvent {
    private FabricEvent() {

    }

    public static void init(){
        ServerLifecycleEvents.SERVER_STARTING.register(FabricEvent::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(FabricEvent::serverStartEvent);
        ServerLifecycleEvents.SERVER_STOPPING.register(FabricEvent::serverStoppingEvent);
        ServerPlayConnectionEvents.JOIN.register(FabricEvent::joinEvent);
        ServerPlayConnectionEvents.DISCONNECT.register(FabricEvent::disconnectEvent);
    }

    private static void disconnectEvent(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        DiscordMessage.instance.gameMessage(player.getName().getString() + " left the game");
        ShutdownManager.quitEvent();
    }

    private static void joinEvent(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        DiscordMessage.instance.gameMessage(player.getName().getString() + " joined the game");
        ShutdownManager.joinEvent();
    }

    private static void serverStoppingEvent(MinecraftServer minecraftServer) {
        DiscordMessage.instance.systemMessage("Server stopping");
        Main.deinit();
    }

    private static void serverStartEvent(MinecraftServer minecraftServer) {
        DiscordMessage.instance.systemMessage("Server started");
        ShutdownManager.quitEvent();
    }

    private static void onServerStarting(MinecraftServer server) {
        try {
            FabricPlatform platform = new FabricPlatform(server);
            Main.init(platform);

        } catch (Exception ex) {
            DiscordChat.LOGGER.error("Initialize error",ex);
        }
    }

}
