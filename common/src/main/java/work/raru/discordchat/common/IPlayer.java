package work.raru.discordchat.common;

import java.util.UUID;

public interface IPlayer {
    public abstract boolean isPlayer();
    public abstract UUID getUUID();
    public abstract String getName();
    public abstract boolean checkPerm(Permissions perm);
    public abstract void sendMsg(String msg, boolean isSuccess);
    public abstract void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard);
    public abstract void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest);
}
