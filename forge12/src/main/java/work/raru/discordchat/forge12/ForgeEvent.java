package work.raru.discordchat.forge12;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.LoginChecker;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.ShutdownManager;

public class ForgeEvent {
    private ForgeEvent() {
    }

    @SubscribeEvent
    public static void loginEvent(ServerConnectionFromClientEvent e) {
        if (!Main.platform.getConfig().getLoginRestriction()) {
            return;
        }
        NetHandlerPlayServer handler = (NetHandlerPlayServer)e.getHandler();
        String msg = LoginChecker.checkLogin(new ForgePlayer(handler.player));
        if (msg != null) {
            handler.disconnect(new TextComponentString(msg));
        }
    }

    @SubscribeEvent
    public static void joinEvent(PlayerLoggedInEvent e) {
        DiscordMessage.instance.gameMessage(e.player.getDisplayNameString() + " joined the game");
        ShutdownManager.joinEvent();
    }

    @SubscribeEvent
    public static void quitEvent(PlayerLoggedOutEvent e) {
        DiscordMessage.instance.gameMessage(e.player.getDisplayNameString() + " left the game");
        ShutdownManager.quitEvent();
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (!(e.getEntity() instanceof EntityPlayer)) {
            return;
        }
        DiscordMessage.instance.gameMessage(e.getSource().getDeathMessage(e.getEntityLiving()).getFormattedText());
    }
}
