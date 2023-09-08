package work.raru.discordchat.common;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (!event.isFromGuild()) {
			return;
		}
		if (event.getChannel().getIdLong() != Main.platform.getConfig().getDiscordChannelID()) {
			return;
		}
		if (event.getMessage().getContentRaw().startsWith(Main.platform.getConfig().getDiscordPrefix())) {
			String command = event.getMessage().getContentRaw()
					.substring(Main.platform.getConfig().getDiscordPrefix().length());
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

	private void link(Message msg, @Nonnull String[] splitCommand) {
		if (splitCommand.length < 2) {
			msg.reply("Usage: " + Main.platform.getConfig().getDiscordPrefix() + "link <token>")
					.mentionRepliedUser(false).queue();
			return;
		}
		Member member = msg.getMember();
		if (member == null) {
			msg.reply("You can't link because you don't have Member Object in Discord.").queue();
			return;
		}
		try {
			@SuppressWarnings("null")
			String minecraftUUID = UserLinkManager.useToken(member, splitCommand[1]);
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
		@Nullable
		Member member = msg.getMember();
		String minecraftName = null;
		if (member != null) {
			try {
				UUID minecraftUUID = UserLinkManager.getMinecraftUUID(member.getIdLong());
				if (minecraftUUID != null) {
					minecraftName = Main.platform.getChat().getName(minecraftUUID);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String mcMsg = Main.platform.getConfig().getChatFormats().getMinecraftFormat(minecraftName != null,
				msg.getReferencedMessage() != null, !msg.getAttachments().isEmpty());
		if (minecraftName != null) {
			mcMsg = mcMsg.replace("%m", minecraftName);
		}

		mcMsg = mcMsg.replace("%d",
				member == null
						? msg.getAuthor().getName()
						: member.getEffectiveName());

		String content = msg.getContentDisplay();
		content = EmojiParser.parseToAliases(content);
		content = MarkdownConverter.toMinecraft(content);
		mcMsg = mcMsg.replace("%t", content);

		Message refMsg = msg.getReferencedMessage();
		if (refMsg != null) {
			try {
				UUID minecraftUUID = UserLinkManager.getMinecraftUUID(refMsg.getAuthor().getIdLong());
				if (minecraftUUID != null) {
					String replyMinecraft = Main.platform.getChat().getName(minecraftUUID);
					if (replyMinecraft != null) {
						mcMsg = mcMsg.replace("%rm", replyMinecraft);
						mcMsg = mcMsg.replace("%rn", replyMinecraft);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Member refMember = refMsg.getMember();
			mcMsg = mcMsg.replace("%rm", "");
			if (refMember == null) {
				mcMsg = mcMsg.replace("%rn", refMsg.getAuthor().getName());
				mcMsg = mcMsg.replace("%rd", refMsg.getAuthor().getName());
			} else {
				mcMsg = mcMsg.replace("%rn", refMember.getEffectiveName());
				mcMsg = mcMsg.replace("%rd", refMember.getEffectiveName());
			}
			String refText = refMsg.getContentDisplay();
			refText = EmojiParser.parseToAliases(refText);
			refText = MarkdownConverter.toMinecraft(refText);
			mcMsg = mcMsg.replace("%rt", refText);
		}
		Main.platform.getChat().broadcast(mcMsg);
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
			@SuppressWarnings("null")
			List<RichCustomEmoji> emojis = getChannel().getGuild().getEmojisByName(splitMessage[i], true);
			if (emojis.isEmpty()) {
				continue;
			}
			result = result.replaceAll(":" + splitMessage[i] + ":", emojis.get(0).getAsMention());
		}

		webhook.setContent(result);
		webhook.setTts(false);
		ThreadManager.execute(webhook);
	}

	public void systemMessage(@Nonnull String message) {
		getChannel().sendMessage(message).queue();
	}

	public void gameMessage(String message) {
		DiscordWebhook webhook = new DiscordWebhook(Main.platform.getConfig().getWebhookURL());

		webhook.setContent(message);

		ThreadManager.execute(webhook);
	}
}
