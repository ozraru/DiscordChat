package work.raru.discordchat.forge;

import java.util.UUID;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.IMinecraftChat;

public class ForgeChat implements IMinecraftChat {

    @Override
    public void tell(UUID minecraft, String message) {
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(minecraft);
        if (player != null) {
            player.sendMessage(new StringTextComponent(message), Util.NIL_UUID);
        }
    }

    @Override
    public void broadcast(String message) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new StringTextComponent(message), ChatType.CHAT, Util.NIL_UUID);
    }

    @Override
    public String getName(UUID uuid) {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    // @SubscribeEvent
    void chatEvent(ServerChatEvent e) {
        DiscordMessage.instance.fromMinecraft(e.getPlayer().getUUID(), e.getPlayer().getName().getContents(), e.getMessage());
    }
    
}
