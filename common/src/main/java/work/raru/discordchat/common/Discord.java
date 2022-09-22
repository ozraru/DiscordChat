package work.raru.discordchat.common;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Discord {
    private Discord() {};

    static JDA jda;

    static void init() {
        jda = JDABuilder.createDefault(Main.platform.getConfig().getToken())
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build();
        jda.addEventListener(new DiscordMessage());
    }

    static void shutdown(boolean force) {
        if (force) {
            jda.shutdownNow();
        } else {
            jda.shutdown();
        }
    }
}
