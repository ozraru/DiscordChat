package work.raru.discordchat.forge20;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.UUID;

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
        ServerPlayer player = null;
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
        return ServerLifecycleHooks.getCurrentServer().getWorldPath(LevelResource.DATAPACK_DIR).toFile();
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
