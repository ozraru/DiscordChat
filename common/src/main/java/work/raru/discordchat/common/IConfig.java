package work.raru.discordchat.common;

import java.net.URL;

public interface IConfig {

    public abstract void reload();

    public abstract boolean getDebug();

    public abstract String getToken();

    public abstract long getDiscordChannelID();

    public abstract String getDiscordPrefix();

    public abstract URL getWebhookURL();

    public abstract boolean getLoginRestriction();

    public abstract int getTokenExpire();

	public abstract String getDatabaseType();

	public abstract String getDatabaseUrl();

	public abstract String getDatabaseUser();

	public abstract String getDatabasePassword();

	public abstract String getDatabaseTablePrefix();

	public abstract ChatFormats getChatFormats();

    public interface ChatFormats {

		public abstract String getDiscordLinkedName();

		public abstract String getDiscordUnlinkedName();

		public abstract String getMinecraftFormat(boolean linked, boolean reply, boolean attachment);

	}
}
