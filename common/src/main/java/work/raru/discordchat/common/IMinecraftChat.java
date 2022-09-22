package work.raru.discordchat.common;

import java.util.UUID;

public interface IMinecraftChat {
    public abstract void tell(UUID minecraft, String message);
    public abstract void broadcast(String message);

    public abstract String getName(UUID uuid);
}
