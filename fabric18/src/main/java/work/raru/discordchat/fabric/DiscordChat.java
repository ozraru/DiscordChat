package work.raru.discordchat.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import work.raru.discordchat.common.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordChat implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("discordchat");

    FabricPlatform platform;

	@Override
	public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(FabricCommand::register);
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
	}

	void onServerStarting(MinecraftServer server) {
        try {
			platform = new FabricPlatform(server);
            Main.init(platform);
			
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}