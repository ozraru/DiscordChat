package work.raru.discordchat.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.Main;

public class ForgeEvent {
    private ForgeEvent() {}

    @SubscribeEvent
    public static void joinEvent(PlayerLoggedInEvent e) {
        DiscordMessage.instance.gameMessage(e.getPlayer().getScoreboardName() + " joined the game");
    }

    @SubscribeEvent
    public static void quitEvent(PlayerLoggedOutEvent e) {
        
        DiscordMessage.instance.gameMessage(e.getPlayer().getScoreboardName() + " left the game");
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (!(e.getEntity() instanceof PlayerEntity)) {
            return;
        }
        DiscordMessage.instance.gameMessage(e.getSource().getLocalizedDeathMessage(e.getEntityLiving()).getContents());
    }

    @SubscribeEvent
    public static void serverStartEvent(final FMLServerStartedEvent e) {
        DiscordMessage.instance.systemMessage("Server started");
    }

    @SubscribeEvent
    public static void serverStoppingEvent(final FMLServerStoppingEvent e) {
        DiscordMessage.instance.systemMessage("Server stopping");
        Main.deinit();
    }
}
