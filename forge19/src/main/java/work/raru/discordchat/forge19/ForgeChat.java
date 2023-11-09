package work.raru.discordchat.forge19;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.IMinecraftChat;

import java.util.UUID;

public class ForgeChat implements IMinecraftChat {

    @Override
    public void tell(UUID minecraft, String message) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(minecraft);
        if (player != null) {
            player.sendSystemMessage(MutableComponent.create(new LiteralContents(message)));
        }
    }

    @Override
    public void broadcast(String message) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastSystemMessage(MutableComponent.create(new LiteralContents(message)), false);
    }

    @Override
    public String getName(UUID uuid) {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    // @SubscribeEvent
    void chatEvent(ServerChatEvent e) {
        DiscordMessage.instance.fromMinecraft(e.getPlayer().getUUID(), e.getPlayer().getName().getString(), e.getMessage().getString());
    }
    
}
