package work.raru.discordchat.forge20;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.ShutdownManager;

public class ForgeEvent {
    private ForgeEvent() {
    }

    @SubscribeEvent
    public static void joinEvent(PlayerLoggedInEvent e) {
        DiscordMessage.instance.gameMessage(e.getEntity().getScoreboardName() + " joined the game");
        ShutdownManager.joinEvent();
    }

    @SubscribeEvent
    public static void quitEvent(PlayerLoggedOutEvent e) {
        DiscordMessage.instance.gameMessage(e.getEntity().getScoreboardName() + " left the game");
        ShutdownManager.quitEvent();
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        DiscordMessage.instance.gameMessage(e.getSource().getLocalizedDeathMessage(e.getEntity()).getString());
    }

    @SubscribeEvent
    public static void serverStartEvent(final ServerStartedEvent e) {
        DiscordMessage.instance.systemMessage("Server started");
        ShutdownManager.quitEvent();
    }

    @SubscribeEvent
    public static void serverStoppingEvent(final ServerStoppingEvent e) {
        DiscordMessage.instance.systemMessage("Server stopping");
        Main.deinit();
    }
}
