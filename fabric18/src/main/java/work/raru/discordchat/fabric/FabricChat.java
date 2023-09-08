package work.raru.discordchat.fabric;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import work.raru.discordchat.common.IMinecraftChat;

import java.util.Optional;
import java.util.UUID;

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
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
        if (player != null){
            return player.getEntityName();
        }
        Optional<GameProfile> userCache = server.getUserCache().getByUuid(uuid);
        if(userCache.isPresent()){
            return userCache.get().getName();
        }
        DiscordChat.LOGGER.warn("Player with uuid " + uuid.toString() +" not found.");
        return "Unknown";
    }
}
