package work.raru.discordchat.spigot;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.IMinecraftChat;

public class SpigotChat implements IMinecraftChat, Listener {

    @Override
    public void tell(UUID minecraft, String message) {
        Player player = Plugin.instance.getServer().getPlayer(minecraft);
        if (player == null) {
            return;
        }
        player.sendMessage(message);
    }

    @Override
    public void broadcast(String message) {
        Plugin.instance.getServer().broadcastMessage(message);
    }

    @Override
    public String getName(UUID uuid) {
        return Plugin.instance.getServer().getOfflinePlayer(uuid).getName();
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        DiscordMessage.instance.fromMinecraft(e.getPlayer().getUniqueId(), e.getPlayer().getDisplayName(), e.getMessage());
    }
}
