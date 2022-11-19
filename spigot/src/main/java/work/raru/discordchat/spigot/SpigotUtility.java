package work.raru.discordchat.spigot;

import java.io.File;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

public class SpigotUtility implements IUtility {

    public IPlayer getPlayer(IPlayer sender, @Nullable String playerIdentity) {
		if (playerIdentity == null) {
            return sender;
		} else {
			return getPlayer(playerIdentity);
		}
	}
	
	@SuppressWarnings("deprecation")
	public @Nullable IPlayer getPlayer(@Nonnull String playerIdentity) {
		try {
			UUID uuid = UUID.fromString(playerIdentity);
			return new SpigotPlayer(Plugin.instance.getServer().getOfflinePlayer(uuid));
		} catch (IllegalArgumentException e) {
            return new SpigotPlayer(Plugin.instance.getServer().getOfflinePlayer(playerIdentity));
        }
	}

	@Override
	@Nonnull
	public File getDatapackDir() {
		return new File(Plugin.instance.getServer().getWorlds().get(0).getWorldFolder(), "datapacks");
	}

	@Override
	public int getPlayerNum() {
		return Plugin.instance.getServer().getOnlinePlayers().size();
	}

	@Override
	public void shutdownServer() {
		Plugin.instance.getServer().shutdown();
	}

	@Override
	public boolean isShutdowning() {
		return !Plugin.instance.isEnabled();
	}
}
