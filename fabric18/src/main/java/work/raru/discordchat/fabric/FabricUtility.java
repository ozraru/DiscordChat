package work.raru.discordchat.fabric;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.UUID;

public class FabricUtility implements IUtility {

    MinecraftServer server;

    FabricUtility(MinecraftServer server) {
        this.server = server;
    }

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
        ServerPlayerEntity player;
        try {
            UUID uuid = UUID.fromString(playerIdentity);
            player = server.getPlayerManager().getPlayer(uuid);
        } catch (IllegalArgumentException e) {
            player = server.getPlayerManager().getPlayer(playerIdentity);
        }
        if (player == null) {
            return null;
        }
        return new FabricPlayer(player);
    }

    @Override
    @Nonnull
    public File getDatapackDir() {
        return server.getSavePath(WorldSavePath.DATAPACKS).toFile();
    }

    @Override
    public int getPlayerNum() {
        return server.getCurrentPlayerCount();
    }

    @Override
    public void shutdownServer() {
        server.stop(true);
    }

    @Override
    public boolean isShutdowning() {
        return !server.isRunning();
    }
}
