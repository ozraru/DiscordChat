package work.raru.discordchat.common;

import java.util.UUID;

public interface IPlayer {
    public abstract boolean isPlayer();
    public abstract UUID getUUID();
    public abstract String getName();
    public default boolean checkPerm(Permissions perm) {
        return checkPerm(perm.node);
    };
    public abstract boolean checkPerm(String perm);
    public abstract void sendMsg(String msg, boolean isSuccess);
    public abstract void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard);
    public abstract void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest);
}
