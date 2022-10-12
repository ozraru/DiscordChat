package work.raru.discordchat.common;

import java.util.UUID;

import javax.annotation.Nullable;

public interface IMinecraftChat {

    // if player is not exist, just ignore.
    public abstract void tell(UUID minecraft, String message);

    public abstract void broadcast(String message);

    @Nullable
    public abstract String getName(UUID uuid);
}
