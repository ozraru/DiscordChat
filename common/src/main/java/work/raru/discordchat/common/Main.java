package work.raru.discordchat.common;

import java.sql.SQLException;

public class Main {
    private Main() {}

    public static final String VERSION = "0.5";

    @SuppressWarnings("squid:S1444")
    public static IPlatform platform;

    public static void init(IPlatform plat) throws SQLException, ClassNotFoundException, InterruptedException {
        platform = plat;
        Discord.init();
        DatabaseManager.init();
        Discord.jda.awaitReady();
        DiscordMessage.instance.systemMessage("DiscordChat loaded");
    }

    public static void deinit() {
        Discord.shutdown(false);
        DatabaseManager.disconnect();
    }
}
