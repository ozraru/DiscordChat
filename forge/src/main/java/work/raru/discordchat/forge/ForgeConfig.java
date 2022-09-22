package work.raru.discordchat.forge;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import work.raru.discordchat.common.IConfig;

public class ForgeConfig implements IConfig {
    @SuppressWarnings({"java:S115"})
    public class ForgeConfigObj {
        private static final String defaultDiscordToken = "TOKEN";
        public final ConfigValue<String> discordToken;
        private static final long defaultDiscordChannel = 123456789012345678l;
        public final ConfigValue<Long> discordChannel;
        private static final String defaultDiscordPrefix = "%mc ";
        public final ConfigValue<String> discordPrefix;
        private static final String defaultDiscordWebhook = "https://discord.com/api/webhooks/XXXXXXXXXXXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        public final ConfigValue<String> discordWebhook;

        // private static final boolean defaultLoginRestriction = false;
        // public final ConfigValue<Boolean> loginRestriction;

        private static final String defaultChatDiscordLinked = "[MC] %d";
        public final ConfigValue<String> chatDiscordLinked;
        private static final String defaultChatDiscordUnlinked = "[MC] %m";
        public final ConfigValue<String> chatDiscordUnlinked;

        private static final String defaultChatMCNormalLinked = "[Discord] <%m> %t";
        public final ConfigValue<String> chatMCNormalLinked;
        private static final String defaultChatMCNormalUnlinked = "[Discord] <%d> %t";
        public final ConfigValue<String> chatMCNormalUnlinked;
        private static final String defaultChatMCReplyLinked = "[Discord reply] <%m> %t";
        public final ConfigValue<String> chatMCReplyLinked;
        private static final String defaultChatMCReplyUnlinked = "[Discord reply] <%d> %t";
        public final ConfigValue<String> chatMCReplyUnlinked;
        private static final String defaultChatMCAttachmentLinked = "[Discord attachment] <%m> %t";
        public final ConfigValue<String> chatMCAttachmentLinked;
        private static final String defaultChatMCAttachmentUnlinked = "[Discord attachment] <%d> %t";
        public final ConfigValue<String> chatMCAttachmentUnlinked;
        private static final String defaultChatMCReplyAttachmentLinked = "[Discord reply attachment] <%m> %t";
        public final ConfigValue<String> chatMCReplyAttachmentLinked;
        private static final String defaultChatMCReplyAttachmentUnlinked = "[Discord reply attachment] <%d> %t";
        public final ConfigValue<String> chatMCReplyAttachmentUnlinked;

        private static final int defaultLinkTokenExpire = 0;
        public final ConfigValue<Integer> linkTokenExpire;

        private static final String defaultDatabasePrefix = "dc_";
        public final ConfigValue<String> databasePrefix;
        private static final String defaultDatabaseType = "sqlite";
        public final ConfigValue<String> databaseType;
        private static final String defaultDatabaseUrl = "jdbc:sqlite:./config/DiscordChat.sqlite";
        public final ConfigValue<String> databaseUrl;
        private static final String defaultDatabaseUser = "USERNAME";
        public final ConfigValue<String> databaseUser;
        private static final String defaultDatabasePassword = "PASSWORD";
        public final ConfigValue<String> databasePassword;

        private static final boolean defaultDebug = false;
        public final ConfigValue<Boolean> debug;

        public ForgeConfigObj(ForgeConfigSpec.Builder builder) {

            this.debug = builder.comment("Debug mode", "this may increase log")
            .define("debug", defaultDebug);

            builder.comment("Discord Settings")
            .push("Discord");
            
            this.discordPrefix = builder.comment("Discord Command Prefix")
            .define("prefix", defaultDiscordPrefix);
            
            this.discordWebhook = builder.comment("Discord Webhook URL",
            "If using thread, add URL to ?thread_id=XXXXXXXXXXXXXXXXXX",
            "Example: https://discord.com/api/webhooks/XXXXXXXXXXXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX?thread_id=XXXXXXXXXXXXXXXXXX")
            .define("webhook", defaultDiscordWebhook);
            
            this.discordChannel = builder.comment("Discord Channel ID")
            .define("channel", defaultDiscordChannel);

            this.discordToken = builder.comment("Discord Bot token")
            .define("token", defaultDiscordToken);
            
            builder.pop();

            // this.loginRestriction = builder.comment("Login restriction", "If turned on",
            //     "- Logging in linked user require discordchat.login.linked (default for all users)",
            //     "- Logging in unlinked user require discordchat.login.unlinked (default for op)",
            //     "- Linking when login denied by DiscordChat if user have discordchat.login.link (default for all user)")
            // .define("login_restriction", defaultLoginRestriction);

            builder.comment("Chat formatting", "%d replaced to Discord name", "%m replaced to MCID")
            .push("ChatFormat");

            builder.comment("Discord -> Minecraft", "%t replaced to message text.")
            .push("Minecraft");

            builder.comment("if message has reply and attachments")
            .push("ReplyAttachment");

            this.chatMCReplyAttachmentLinked = builder.define("linked", defaultChatMCReplyAttachmentLinked);
            this.chatMCReplyAttachmentUnlinked = builder.define("unlinked", defaultChatMCReplyAttachmentUnlinked);

            builder.pop();

            builder.comment("if message has attachments")
            .push("Attachment");

            this.chatMCAttachmentLinked = builder.define("linked", defaultChatMCAttachmentLinked);
            this.chatMCAttachmentUnlinked = builder.define("unlinked", defaultChatMCAttachmentUnlinked);

            builder.pop();

            builder.comment("if message has reply",
            "%rt replaced to replyed message text.",
            "%rd replaced to replyed user Discord name.",
            "%rm replaced to MCID. If don't linked, empty.",
            "%rn replaced to name. If linked MCID, else, Discord name.")
            .push("Reply");

            this.chatMCReplyLinked = builder.define("linked", defaultChatMCReplyLinked);
            this.chatMCReplyUnlinked = builder.define("unlinked", defaultChatMCReplyUnlinked);

            builder.pop();

            builder.comment("if message has no reply and attachment")
            .push("Normal");

            this.chatMCNormalLinked = builder.define("linked", defaultChatMCNormalLinked);
            this.chatMCNormalUnlinked = builder.define("unlinked", defaultChatMCNormalUnlinked);

            builder.pop();

            builder.pop();

            builder.comment("Minecraft -> Discord")
            .push("Discord");

            this.chatDiscordLinked = builder.comment("If player linked to Discord")
            .define("linked", defaultChatDiscordLinked);
            this.chatDiscordUnlinked = builder.comment("If player unlinked")
            .define("unlinked", defaultChatDiscordUnlinked);

            builder.pop();

            builder.pop();

            builder.comment("Link Settings")
            .push("Link");
            this.linkTokenExpire = builder.comment("Link Token Expire Period")
            .define("token", defaultLinkTokenExpire);
            builder.pop();

            builder.comment("Database Settings")
            .push("Database");
            this.databasePrefix = builder.comment("Table prefix")
            .define("table_prefix", defaultDatabasePrefix);
            this.databaseType = builder.comment("Database Type", "Below is available", "sqlite: SQLite", "postgresql: PostgreSQL")
            .define("type", defaultDatabaseType);
            this.databaseUrl = builder.comment("Database URL", "SQLite: jdbc:sqlite:<path>", "PostgreSQL: jdbc:postgresql://<address>/<dbname>")
            .define("url", defaultDatabaseUrl);
            this.databaseUser = builder.comment("Database Login User", "ignored if SQLite")
            .define("user", defaultDatabaseUser);
            this.databasePassword = builder.comment("Database Login Password", "ignored if SQLite")
            .define("password", defaultDatabasePassword);
            builder.pop();
        }
    }

    ForgeConfigObj config;
    ForgeConfigSpec configSpec;
    
    ForgeConfig() {
        Pair<ForgeConfigObj, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ForgeConfigObj::new);
        config = pair.getLeft();
        configSpec = pair.getRight();
    }

    @Override
    public void reload() {
        // Not supported yet
    }

    @Override
    public boolean getDebug() {
        return config.debug.get();
    }

    @Override
    public String getToken() {
        // TODO Auto-generated method stub
        return config.discordToken.get();
    }

    @Override
    public long getDiscordChannelID() {
        return config.discordChannel.get();
    }

    @Override
    public String getDiscordPrefix() {
        return config.discordPrefix.get();
    }

    @Override
    public URL getWebhookURL() {
        try {
            return new URL(config.discordWebhook.get());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getLoginRestriction() {
        // return config.loginRestriction.get();
        return false;
    }

    @Override
    public int getTokenExpire() {
        return config.linkTokenExpire.get();
    }

    @Override
    public String getDatabaseType() {
        return config.databaseType.get();
    }

    @Override
    public String getDatabaseUrl() {
        return config.databaseUrl.get();
    }

    @Override
    public String getDatabaseUser() {
        return config.databaseUser.get();
    }

    @Override
    public String getDatabasePassword() {
        return config.databasePassword.get();
    }

    @Override
    public String getDatabaseTablePrefix() {
        return config.databasePrefix.get();
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
			return config.chatDiscordLinked.get();
		}
		public String getDiscordUnlinkedName() {
			return config.chatDiscordUnlinked.get();
		}

        @SuppressWarnings("java:S3776")
		public String getMinecraftFormat(boolean linked, boolean reply, boolean attachment) {
			if (reply) {
				if (attachment) {
                    if (linked) {
                        return config.chatMCReplyAttachmentLinked.get();
                    } else {
                        return config.chatMCReplyAttachmentUnlinked.get();
                    }
				} else {
                    if (linked) {
                        return config.chatMCReplyLinked.get();
                    } else {
                        return config.chatMCReplyUnlinked.get();
                    }
				}
			} else {
				if (attachment) {
                    if (linked) {
                        return config.chatMCAttachmentLinked.get();
                    } else {
                        return config.chatMCAttachmentUnlinked.get();
                    }
				} else {
                    if (linked) {
                        return config.chatMCNormalLinked.get();
                    } else {
                        return config.chatMCNormalUnlinked.get();
                    }
				}
			}
		}
	}
}
