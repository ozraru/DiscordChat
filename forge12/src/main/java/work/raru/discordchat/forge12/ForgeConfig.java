package work.raru.discordchat.forge12;

import java.net.MalformedURLException;
import java.net.URL;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import work.raru.discordchat.common.IConfig;

public class ForgeConfig implements IConfig {
    @Config(modid = DiscordChat.MODID)
    public static class ForgeConfigObj {
        public static class DiscordConfigObj {
            @Comment({"Discord Bot token"})
            public String token = "TOKEN";
            @Comment({"Discord Channel ID"})
            public String channel = "123456789012345678";
            @Comment({"Discord Command Prefix"})
            public String prefix = "%mc ";
            @Comment({"Discord Webhook URL",
            "If using thread, add URL to ?thread_id=XXXXXXXXXXXXXXXXXX",
            "Example: https://discord.com/api/webhooks/XXXXXXXXXXXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX?thread_id=XXXXXXXXXXXXXXXXXX"})
            public String webhook = "https://discord.com/api/webhooks/XXXXXXXXXXXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        }

        public static DiscordConfigObj discord = new DiscordConfigObj();

        @Comment({"Login restriction", "If turned on",
        "- Logging in linked user require discordchat.login.linked (default for all users)",
        "- Logging in unlinked user require discordchat.login.unlinked (default for op)",
        "- Linking when login denied by DiscordChat if user have discordchat.login.link (default for all user)"})
        public static boolean loginRestriction = false;

        @Comment({"Chat formatting", "%d replaced to Discord name", "%m replaced to MCID"})
        public static ChatFormatConfigObj chatFormat = new ChatFormatConfigObj();

        public static class ChatFormatConfigObj {
            @Comment({"Minecraft -> Discord"})
            public ChatFormatDiscordConfigObj discord = new ChatFormatDiscordConfigObj();
            public class ChatFormatDiscordConfigObj {
                @Comment({"If player linked to Discord"})
                public String linked = "[MC] %d";
                @Comment({"If player unlinked"})
                public String unlinked = "[MC] %m";
            }

            @Comment({"Discord -> Minecraft", "%t replaced to message text."})
            public ChatFormatMinecraftConfigObj minecraft = new ChatFormatMinecraftConfigObj();
            public class ChatFormatMinecraftConfigObj {
                @Comment({"if message has no reply and attachment"})
                public String normalLinked = "[Discord] <%m> %t";
                public String normalUnlinked = "[Discord] <%d> %t";
                @Comment({"if message has reply",
                            "%rt replaced to replyed message text.",
                            "%rd replaced to replyed user Discord name.",
                            "%rm replaced to MCID. If don't linked, empty.",
                            "%rn replaced to name. If linked MCID, else, Discord name."})
                public String replyLinked = "[Discord reply] <%m> %t";
                public String replyUnlinked = "[Discord reply] <%d> %t";
                @Comment({"if message has attachments"})
                public String attachmentLinked = "[Discord attachment] <%m> %t";
                public String attachmentUnlinked = "[Discord attachment] <%d> %t";
                @Comment({"if message has reply and attachments"})
                public String replyAttachmentLinked = "[Discord reply attachment] <%m> %t";
                public String replyAttachmentUnlinked = "[Discord reply attachment] <%d> %t";
            }
        }

        @Comment({"Link Token Expire Period"})
        public static int linkTokenExpire = 300;

        @Comment({"Database Settings"})
        public static DatabaseConfigObj database = new DatabaseConfigObj();
        public static class DatabaseConfigObj {
            @Comment({"Table prefix"})
            public String prefix = "dc_";
            @Comment({"Database Type", "Below is available", "sqlite: SQLite", "postgresql: PostgreSQL"})
            public String type = "sqlite";
            @Comment({"Database URL", "SQLite: jdbc:sqlite:<path>", "PostgreSQL: jdbc:postgresql://<address>/<dbname>"})
            public String url = "jdbc:sqlite:./config/DiscordChat.sqlite";
            @Comment({"Database Login User", "ignored if SQLite"})
            public String user = "USERNAME";
            @Comment({"Database Login Password", "ignored if SQLite"})
            public String password = "PASSWORD";
        }
        
        @Comment({"Shutdown time", "Wait time duration until shutdown(second)", "If set -1, disable auto shutdown feature"})
        public static int shutdownTime = -1;

        @Comment({"this may increase log"})
        public static boolean debug = false;
    }

    @Override
    public void reload() {
        // Unimplemented
    }

    @Override
    public boolean getDebug() {
        return ForgeConfigObj.debug;
    }

    @Override
    public String getToken() {
        return ForgeConfigObj.discord.token;
    }

    @Override
    public long getDiscordChannelID() {
        return Long.parseLong(ForgeConfigObj.discord.channel);
    }

    @Override
    public String getDiscordPrefix() {
        return ForgeConfigObj.discord.prefix;
    }

    @Override
    public URL getWebhookURL() {
        try {
            return new URL(ForgeConfigObj.discord.webhook);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getLoginRestriction() {
        return ForgeConfigObj.loginRestriction;
    }

    @Override
    public int getTokenExpire() {
        return ForgeConfigObj.linkTokenExpire;
    }

    @Override
    public String getDatabaseType() {
        return ForgeConfigObj.database.type;
    }

    @Override
    public String getDatabaseUrl() {
        return ForgeConfigObj.database.url;
    }

    @Override
    public String getDatabaseUser() {
        return ForgeConfigObj.database.user;
    }

    @Override
    public String getDatabasePassword() {
        return ForgeConfigObj.database.password;
    }

    @Override
    public String getDatabaseTablePrefix() {
        return ForgeConfigObj.database.prefix;
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
			return ForgeConfigObj.chatFormat.discord.linked;
		}
		public String getDiscordUnlinkedName() {
			return ForgeConfigObj.chatFormat.discord.unlinked;
		}

        @SuppressWarnings("java:S3776")
		public String getMinecraftFormat(boolean linked, boolean reply, boolean attachment) {
			if (reply) {
				if (attachment) {
                    if (linked) {
                        return ForgeConfigObj.chatFormat.minecraft.replyAttachmentLinked;
                    } else {
                        return ForgeConfigObj.chatFormat.minecraft.replyAttachmentUnlinked;
                    }
				} else {
                    if (linked) {
                        return ForgeConfigObj.chatFormat.minecraft.replyLinked;
                    } else {
                        return ForgeConfigObj.chatFormat.minecraft.replyUnlinked;
                    }
				}
			} else {
				if (attachment) {
                    if (linked) {
                        return ForgeConfigObj.chatFormat.minecraft.attachmentLinked;
                    } else {
                        return ForgeConfigObj.chatFormat.minecraft.attachmentUnlinked;
                    }
				} else {
                    if (linked) {
                        return ForgeConfigObj.chatFormat.minecraft.normalLinked;
                    } else {
                        return ForgeConfigObj.chatFormat.minecraft.normalUnlinked;
                    }
				}
			}
		}
	}

    @Override
    public int getShutdownTimeSecond() {
        return ForgeConfigObj.shutdownTime;
    }
}
