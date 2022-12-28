package work.raru.discordchat.forge12;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.server.FMLServerHandler;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.IMinecraftChat;

public class ForgeChat implements IMinecraftChat {

    @Override
    public void tell(UUID minecraft, String message) {
        EntityPlayer player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(minecraft);
        if (player != null) {
            player.sendMessage(new TextComponentString(message));
        }
    }

    @Override
    public void broadcast(String message) {
        FMLServerHandler.instance().getServer().getPlayerList().sendMessage(new TextComponentString(message));
    }

    @Override
    public String getName(UUID uuid) {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    @SubscribeEvent
    void chatEvent(ServerChatEvent e) {
        DiscordMessage.instance.fromMinecraft(e.getPlayer().getUniqueID(), e.getUsername(), e.getMessage());
    }
    
}
