package work.raru.discordchat.forge20;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.slf4j.Logger;
import work.raru.discordchat.common.Main;
import work.raru.discordchat.common.Permissions;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DiscordChat.MODID)
public class DiscordChat
{
    ForgePlatform platform = new ForgePlatform();
    // Define mod id in a common place for everything to reference
    public static final String MODID = "discordchat";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DiscordChat(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, platform.config.configSpec);
    }
    @SubscribeEvent
    public void permissionRegister(PermissionGatherEvent.Nodes event) {
        for (Permissions permission: Permissions.values()) {
            PermissionNode<Boolean> node = new PermissionNode<>(MODID, permission.node, PermissionTypes.BOOLEAN, (player, playerUUID, context) -> switch (permission.defaultPerm) {
                case ALL -> true;
                case NONE -> false;
                default -> {
                    if (player == null) yield false;
                    yield player.hasPermissions(2);
                }
            });
            ForgeCommand.permissionNodes.put(permission.node, node);
            event.addNodes(node);
        }

    }

    @SubscribeEvent
    public void registerCommand(final RegisterCommandsEvent e) {
        ForgeCommand.register(e.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        try {
            Main.init(platform);
            MinecraftForge.EVENT_BUS.addListener(platform.chat::chatEvent);
            MinecraftForge.EVENT_BUS.register(ForgeEvent.class);
        } catch (Exception ex) {
            LOGGER.error("Failed to initialize DiscordChat",ex);
        }
    }
}
