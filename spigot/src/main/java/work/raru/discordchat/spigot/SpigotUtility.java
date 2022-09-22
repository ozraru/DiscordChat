package work.raru.discordchat.spigot;

import java.io.File;
import java.util.UUID;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

public class SpigotUtility implements IUtility {

    public @NotNull IPlayer getPlayer(@NotNull IPlayer sender, @Nullable String playerIdentity) {
		if (playerIdentity == null) {
            return sender;
		} else {
			return getPlayer(playerIdentity);
		}
	}
	
	@SuppressWarnings("deprecation")
	public @Nullable IPlayer getPlayer(@NotNull String playerIdentity) {
		try {
			UUID uuid = UUID.fromString(playerIdentity);
			return new SpigotPlayer(Plugin.instance.getServer().getOfflinePlayer(uuid));
		} catch (IllegalArgumentException e) {
            return new SpigotPlayer(Plugin.instance.getServer().getOfflinePlayer(playerIdentity));
        }
	}

	@Override
	public File getDatapackDir() {
		return new File(Plugin.instance.getServer().getWorlds().get(0).getWorldFolder(), "datapacks");
	}
}
