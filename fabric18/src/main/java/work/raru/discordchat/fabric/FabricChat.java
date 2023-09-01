package work.raru.discordchat.fabric;

import java.util.UUID;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import work.raru.discordchat.common.IMinecraftChat;

public class FabricChat implements IMinecraftChat {

    MinecraftServer server;

    FabricChat(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void tell(UUID minecraft, String message) {
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(minecraft);
        if (player != null) {
            player.sendMessage(new LiteralText(message), MessageType.SYSTEM, Util.NIL_UUID);
        }
    }

    @Override
    public void broadcast(String message) {
        server.getPlayerManager().broadcast(new LiteralText(message), MessageType.SYSTEM, Util.NIL_UUID);
    }

    @Override
    public String getName(UUID uuid) {
        return server.getPlayerManager().getPlayer(uuid).getEntityName();
    }
}
