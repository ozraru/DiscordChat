package work.raru.discordchat.common;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.vdurmont.emoji.EmojiParser;

public class DiscordMessage extends ListenerAdapter {

	public static DiscordMessage instance;

	@SuppressWarnings("squid:S3010")
	DiscordMessage() {
		instance = this;
	}

    static GuildMessageChannel getChannel() {
		return (GuildMessageChannel) Discord.jda.getGuildChannelById(Main.platform.getConfig().getDiscordChannelID());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild() && event.getChannel().getIdLong() != Main.platform.getConfig().getDiscordChannelID()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith(Main.platform.getConfig().getDiscordPrefix())) {
            String command = event.getMessage().getContentRaw().substring(Main.platform.getConfig().getDiscordPrefix().length());
            String[] splitCommand = command.split(" ");
            switch (splitCommand[0]) {
                case "link":
                    link(event.getMessage(), splitCommand);
                    break;
                default:
                    break;
            }
        } else {
			if (event.isFromGuild() && !event.isWebhookMessage()
					&& !event.getAuthor().equals(event.getJDA().getSelfUser())) {
				toMinecraft(event.getMessage());
			}
        }
    }

    private void link(Message msg, String[] splitCommand) {
        if (splitCommand.length < 2) {
			msg.reply("Usage: " + Main.platform.getConfig().getDiscordPrefix() + "link <token>").mentionRepliedUser(false).queue();
            return;
        }
		try {
			String minecraftUUID = UserLinkManager.useToken(msg.getMember(), splitCommand[1]);
			if (minecraftUUID != null) {
				msg.reply("Successfully linked to " + minecraftUUID).queue();
			} else {
				msg.reply("Token not found").queue();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.reply("Unexpected error has occurred in accessing database").mentionRepliedUser(false).queue();
		}
    }

    private void toMinecraft(Message msg) {
		String content;
		String minecraftName = null;
		try {
			UUID minecraftUUID = UserLinkManager.getMinecraftUUID(msg.getMember().getIdLong());
			if (minecraftUUID != null) {
                minecraftName = Main.platform.getChat().getName(minecraftUUID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        content = Main.platform.getConfig().getChatFormats().getMinecraftFormat(minecraftName != null, msg.getReferencedMessage() != null, !msg.getAttachments().isEmpty());
		if (minecraftName != null) {
			content = content.replace("%m", minecraftName);
		}

		content = content.replace("%d", 
		msg.getMember() == null 
		? msg.getAuthor().getName() 
		: msg.getMember().getEffectiveName());
		content = content.replace("%t", msg.getContentDisplay());
		if (msg.getReferencedMessage() != null) {
			try {
				UUID minecraftUUID = UserLinkManager.getMinecraftUUID(msg.getMember().getIdLong());
				if (minecraftUUID != null) {
					String replyMinecraft = Main.platform.getChat().getName(minecraftUUID);
					if (replyMinecraft != null) {
						content = content.replace("%rm", replyMinecraft);
						content = content.replace("%rn", replyMinecraft);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			content = content.replace("%rm", "");
			if (msg.getReferencedMessage().getMember() == null) {
				content = content.replace("%rn", msg.getReferencedMessage().getAuthor().getName());
				content = content.replace("%rd", msg.getReferencedMessage().getAuthor().getName());
			} else {
				content = content.replace("%rn", msg.getReferencedMessage().getMember().getEffectiveName());
				content = content.replace("%rd", msg.getReferencedMessage().getMember().getEffectiveName());
			}
            String msgText = msg.getReferencedMessage().getContentDisplay();
            msgText = EmojiParser.parseToAliases(msgText);
            msgText = MarkdownConverter.toMinecraft(msgText);
			content = content.replace("%rt", msgText);
		}
        Main.platform.getChat().broadcast(content);
    }
	
	static final Pattern emojiPattern = Pattern.compile("\\w{2,}");

    public void fromMinecraft(UUID minecraft, String mcName, String message) {
		DiscordWebhook webhook = new DiscordWebhook(Main.platform.getConfig().getWebhookURL());
		String discordName = Main.platform.getConfig().getChatFormats().getDiscordUnlinkedName();
		try {
			Member member = UserLinkManager.getDiscordMember(minecraft);
			if (member != null) {
				discordName = Main.platform.getConfig().getChatFormats().getDiscordLinkedName();
				discordName = discordName.replace("%d", member.getEffectiveName());
				webhook.setAvatarUrl(member.getEffectiveAvatarUrl());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		discordName = discordName.replace("%m", mcName);
		webhook.setUsername(discordName);
		webhook.setContent(message);

		String[] splitMessage = message.split(":");
		String result = message;
		HashSet<String> converted = new HashSet<String>();
		for (int i = 1; i < splitMessage.length; i++) {
			if (!converted.add(splitMessage[i])) {
				continue;
			}
			Matcher matcher = emojiPattern.matcher(splitMessage[i]);
			if (!matcher.matches()) {
				continue;
			}
			List<RichCustomEmoji> emojis = getChannel().getGuild().getEmojisByName(splitMessage[i], true);
			if (emojis.isEmpty()) {
				continue;
			}
			result = result.replaceAll(":"+splitMessage[i]+":", emojis.get(0).getAsMention());
		}

		webhook.setContent(result);
		webhook.setTts(false);
		ThreadManager.execute(webhook);
	}

    public void systemMessage(String message) {
		getChannel().sendMessage(message).queue();
	}

    public void gameMessage(String message) {
		DiscordWebhook webhook = new DiscordWebhook(Main.platform.getConfig().getWebhookURL());

		webhook.setContent(message);
		
		ThreadManager.execute(webhook);
	}
}
