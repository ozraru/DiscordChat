package work.raru.discordchat.spigot;

import org.bukkit.plugin.java.JavaPlugin;

import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.ShutdownManager;

public class Plugin extends JavaPlugin {
    
    static Plugin instance;

	@SuppressWarnings("squid:S2696")
    @Override
    public void onEnable() {
        instance = this;
        try {
            SpigotPlatform platform = new SpigotPlatform();
            Main.init(platform);
            SpigotCommand executer = new SpigotCommand();
            getCommand("discordchat").setExecutor(executer);
            getCommand("discordchat").setTabCompleter(executer);
            getServer().getPluginManager().registerEvents(platform.chat, this);
            getServer().getPluginManager().registerEvents(new SpigotEvent(), this);
			getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                DiscordMessage.instance.systemMessage("Server started");
                ShutdownManager.quitEvent();
			});
        } catch (Exception e) {
            e.printStackTrace();
            this.setEnabled(false);
        }
    }
    
    @Override
    public void onDisable() {
        try {
            DiscordMessage.instance.systemMessage("Plugin disabled (perhaps stopping server)");
            Main.deinit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
