package work.raru.discordchat.spigot;

import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.configuration.file.FileConfiguration;

import work.raru.discordchat.common.IConfig;

public class SpigotConfig implements IConfig {
    FileConfiguration config;
    SpigotConfig() {
        reload();
    }

	public void reload() {
        Plugin.instance.saveDefaultConfig();
		Plugin.instance.reloadConfig();
        config = Plugin.instance.getConfig();
	}

	public boolean getDebug() {
		return config.getBoolean("debug");
	}
	
	public String getToken() {
		return config.getString("discord.token");
	}

	public long getDiscordChannelID() {
		return config.getLong("discord.channel");
	}
	public String getDiscordPrefix() {
		return config.getString("discord.prefix");
	}
	public URL getWebhookURL() {
		try {
            return new URL(config.getString("discord.webhook"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public boolean getLoginRestriction() {
		return config.getBoolean("login_restriction");
	}
	
	public int getTokenExpire() {
		return config.getInt("link.tokenExpire");
	}

	public String getDatabaseType() {
		return config.getString("database.type");
	}
	public String getDatabaseUrl() {
		return config.getString("database.url");
	}
	public String getDatabaseUser() {
		return config.getString("database.user");
	}
	public String getDatabasePassword() {
		return config.getString("database.password");
	}
	public String getDatabaseTablePrefix() {
		return config.getString("database.table_prefix");
	}

    ChatFormats chatFormats = null;

	public IConfig.ChatFormats getChatFormats() {
        if (chatFormats == null) {
            chatFormats = new ChatFormats();
        }
		return chatFormats;
	}
	
	public class ChatFormats implements IConfig.ChatFormats {

		public String getDiscordLinkedName() {
			return config.getString("chat_format.discord.linked_name");
		}
		public String getDiscordUnlinkedName() {
			return config.getString("chat_format.discord.unlinked_name");
		}

		public String getMinecraftFormat(boolean linked, boolean reply, boolean attachment) {
			StringBuilder key = new StringBuilder("chat_format.minecraft.");
			if (reply) {
				if (attachment) {
					key.append("reply_attachment.");
				} else {
					key.append("reply.");
				}
			} else {
				if (attachment) {
					key.append("attachment.");
				} else {
					key.append("normal.");
				}
			}
			if (linked) {
				key.append("linked");
			} else {
				key.append("unlinked");
			}
			return config.getString(key.toString());
		}
	}
}
