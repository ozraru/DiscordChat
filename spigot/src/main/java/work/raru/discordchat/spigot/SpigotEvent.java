package work.raru.discordchat.spigot;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.LoginChecker;
import work.raru.discordchat.common.Main;

public class SpigotEvent implements Listener {
	@EventHandler
	public void loginEvent(PlayerLoginEvent e) {
        if (!Main.platform.getConfig().getLoginRestriction()) {
            return;
        }
		try {
            String msg = LoginChecker.checkLogin(new SpigotPlayer((OfflinePlayer) e.getPlayer()));
            if (msg != null) {
                e.disallow(Result.KICK_WHITELIST, msg);
            }
		} catch (Exception ex) {
			ex.printStackTrace();
			e.disallow(Result.KICK_OTHER, "Sorry, unknown error occured in DiscordChat. Please call Administrator.");
		}
	}
	

	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		if (e.getJoinMessage() != null) {
			DiscordMessage.instance.gameMessage(e.getJoinMessage().replaceAll("§.", ""));
		}
	}

	@EventHandler
	public void quitEvent(PlayerQuitEvent e) {
		if (e.getQuitMessage() != null) {
			DiscordMessage.instance.gameMessage(e.getQuitMessage().replaceAll("§.", ""));
		}
	}
	
	@EventHandler
	public void deathEvent(PlayerDeathEvent e) {
		if (e.getDeathMessage() != null) {
			DiscordMessage.instance.gameMessage(e.getDeathMessage().replaceAll("§.", ""));
		}
	}
}
