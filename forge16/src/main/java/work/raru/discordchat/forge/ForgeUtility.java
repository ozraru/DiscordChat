package work.raru.discordchat.forge;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

public class ForgeUtility implements IUtility {

    @Override
    public IPlayer getPlayer(IPlayer sender, @Nullable String playerIdentity) {
        if (playerIdentity == null) {
            return sender;
        } else {
            return getPlayer(playerIdentity);
        }
    }

    @Override
    public IPlayer getPlayer(@Nonnull String playerIdentity) {
        ServerPlayerEntity player = null;
        try {
            UUID uuid = UUID.fromString(playerIdentity);
            player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        } catch (IllegalArgumentException e) {
            player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playerIdentity);
        }
        if (player == null) {
            return null;
        }
        return new ForgePlayer(player);
    }

    @Override
    @Nonnull
    public File getDatapackDir() {
        @SuppressWarnings("null") @Nonnull LevelSave save = (LevelSave) ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, ServerLifecycleHooks.getCurrentServer(), "field_71310_m");
		
        Path worldDir = save.getWorldDir();

        File worldFile = worldDir.toFile();

        return new File(worldFile, "datapacks");
    }

    @Override
    public int getPlayerNum() {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerCount();
    }

    @Override
    public void shutdownServer() {
        ServerLifecycleHooks.getCurrentServer().halt(false);
    }

    @Override
    public boolean isShutdowning() {
        return !ServerLifecycleHooks.getCurrentServer().isRunning();
    }
}
