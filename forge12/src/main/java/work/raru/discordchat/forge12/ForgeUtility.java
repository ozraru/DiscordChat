package work.raru.discordchat.forge12;

import java.io.File;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.IUtility;

public class ForgeUtility implements IUtility {

    MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
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
        EntityPlayer player = null;
        try {
            UUID uuid = UUID.fromString(playerIdentity);
            player = getServer().getPlayerList().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException e) {
            player = getServer().getPlayerList().getPlayerByUsername(playerIdentity);
        }
        if (player == null) {
            return null;
        }
        return new ForgePlayer(player);
    }

    @Override
    @Nonnull
    public File getDatapackDir() {
        return new File(getServer().worlds[0].getChunkSaveLocation(), "datapacks");
    }

    @Override
    public int getPlayerNum() {
        return getServer().getPlayerList().getCurrentPlayerCount();
    }

    @Override
    public void shutdownServer() {
        getServer().initiateShutdown();
    }

    @Override
    public boolean isShutdowning() {
        return !getServer().isServerRunning();
    }
}
