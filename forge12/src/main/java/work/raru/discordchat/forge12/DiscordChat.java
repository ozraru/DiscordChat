package work.raru.discordchat.forge12;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import work.raru.discordchat.common.DiscordMessage;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.Permissions;
import work.raru.discordchat.common.ShutdownManager;

import java.sql.SQLException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = DiscordChat.MODID, name = "DiscordChat", version = Main.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class DiscordChat {
    static final String MODID = "discordchat";

    ForgePlatform platform;

    public DiscordChat() throws SQLException, ClassNotFoundException {
        platform = new ForgePlatform();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(platform.config);
    }

    @EventHandler
    public void start(final FMLInitializationEvent e) {
        for (Permissions perm : Permissions.values()) {
            DefaultPermissionLevel level;
            switch (perm.defaultPerm) {
                case ALL:
                    level = DefaultPermissionLevel.ALL;
                    break;
                case OP:
                    level = DefaultPermissionLevel.OP;
                    break;
                case NONE:
                    level = DefaultPermissionLevel.NONE;
                    break;
                default:
                level = DefaultPermissionLevel.OP;
                    break;
            }
            PermissionAPI.registerNode(perm.node, level, perm.description);
        }
    }

    private boolean initialized = false;

    @EventHandler
    public void serverStartingEvent(final FMLServerStartingEvent e) {
        try {
            Main.init(platform);
            MinecraftForge.EVENT_BUS.register(platform.chat);
            MinecraftForge.EVENT_BUS.register(ForgeEvent.class);
            e.registerServerCommand(new ForgeCommand());
            initialized = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void serverStartEvent(final FMLServerStartedEvent e) {
        if (initialized) {
            DiscordMessage.instance.systemMessage("Server started");
            ShutdownManager.quitEvent();
        }
    }

    @EventHandler
    public void serverStoppingEvent(final FMLServerStoppingEvent e) {
        if (initialized) {
            DiscordMessage.instance.systemMessage("Server stopping");
            Main.deinit();
        }
    }
}
