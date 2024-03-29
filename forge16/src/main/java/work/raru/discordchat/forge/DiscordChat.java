package work.raru.discordchat.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.server.permission.PermissionAPI;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.Permissions;

import java.sql.SQLException;

import org.apache.commons.lang3.tuple.Pair;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("discordchat")
public class DiscordChat {
    ForgePlatform platform;

    public DiscordChat() throws SQLException, ClassNotFoundException {
        // This is One-Sided Mod
        // https://docs.minecraftforge.net/en/1.16.x/concepts/sides/#writing-one-sided-mods
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        
        platform = new ForgePlatform();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, platform.config.configSpec);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::start);
    }

    public void start(final FMLCommonSetupEvent e) {
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

        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartingEvent);
    }

    @SubscribeEvent
    public void registerCommand(final RegisterCommandsEvent e) {
        ForgeCommand.register(e.getDispatcher());
    }

    @SubscribeEvent
    public void serverStartingEvent(final FMLServerStartingEvent e) {
        try {
            Main.init(platform);
            MinecraftForge.EVENT_BUS.addListener(platform.chat::chatEvent);
            MinecraftForge.EVENT_BUS.register(ForgeEvent.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
