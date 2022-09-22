package work.raru.discordchat.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

public class EmojifulGenerator {
	private EmojifulGenerator() {
	}

	private static final String MCMETA = "{\n"
			+ "  \"pack\": {\n"
			+ "    \"pack_format\": 6,\n"
			+ "    \"description\": \"Emojiful emojis by DiscordLink\"\n"
			+ "  }\n"
			+ "}";

	private static final String TEMPLATE = "{\n"
			+ "  \"category\": \"{category}\",\n"
			+ "  \"name\": \"{name}\",\n"
			+ "  \"url\": \"{url}\",\n"
			+ "  \"type\": \"emojiful:emoji_recipe\"\n"
			+ "}";

	// @SuppressWarnings({"java:S899","java:S4042"})
	static void generate() throws IOException {
		// ready dir
		File datapackDir = Main.platform.getUtility().getDatapackDir();
		assert (datapackDir.isDirectory());
		File packDir = new File(datapackDir, "discordchat");
		File jsonDir = new File(packDir, "data/emojiful/recipes");
		// clear old data
		if (jsonDir.exists()) {
			try {
				for (File file : jsonDir.listFiles()) {
					Files.delete(file.toPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// generate folder
		jsonDir.mkdirs();
		try (FileWriter writer = new FileWriter(new File(packDir, "pack.mcmeta"))) {
			writer.write(MCMETA);
			writer.flush();
		}
		// get emotes and save it
		List<RichCustomEmoji> emotes = DiscordMessage.getChannel().getGuild().getEmojis();
		for (RichCustomEmoji emote : emotes) {
			String jsonData = TEMPLATE.replace("{url}", emote.getImageUrl());
			jsonData = jsonData.replace("{name}", emote.getName());
			jsonData = jsonData.replace("{category}", emote.getGuild().getName());
			File jsonFile = new File(jsonDir, emote.getId() + ".json");
			if (jsonFile.createNewFile()) {
				try (FileWriter writer = new FileWriter(jsonFile)) {
					writer.write(jsonData);
					writer.flush();
				}
			}
		}
	}
}