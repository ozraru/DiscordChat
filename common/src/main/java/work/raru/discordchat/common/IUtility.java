package work.raru.discordchat.common;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IUtility {

    public abstract @Nullable IPlayer getPlayer(IPlayer sender, @Nullable String playerIdentity);
    public abstract @Nullable IPlayer getPlayer(@Nonnull String playerIdentity);

    public abstract @Nonnull File getDatapackDir();

}
