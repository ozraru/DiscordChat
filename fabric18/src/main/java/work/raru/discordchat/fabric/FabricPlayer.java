package work.raru.discordchat.fabric;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import work.raru.discordchat.common.IPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class FabricPlayer implements IPlayer {

    final ServerCommandSource sender;
    final ServerPlayerEntity player;

    FabricPlayer(ServerPlayerEntity player) {
        this.sender = null;
        this.player = player;
    }

    FabricPlayer(ServerCommandSource source) {
        sender = source;
        ServerPlayerEntity pl;
        try {
            pl = source.getPlayer();
        } catch (Exception e) {
            pl = null;
        }
        player = pl;
    }

    @Override
    public boolean isPlayer() {
        return player != null;
    }

    @Nullable
    @Override
    public UUID getUUID() {
        if (player != null) {
            return player.getUuid();
        }
        return null;
    }

    @Nullable
    @Override
    public String getName() {
        if (player != null){
            return player.getName().getString();
        }else if(sender.getName() != null){
            return sender.getName();
        }else{
            return null;
        }
    }

    @Override
    public boolean checkPerm(work.raru.discordchat.common.Permissions perm) {
        if (sender != null) {
            switch (perm.defaultPerm) {
                case ALL: {
                    return Permissions.check(sender, perm.node, true);
                }
                case OP: {
                    return Permissions.check(sender, perm.node, 2);
                }
                case NONE: {
                    return Permissions.check(sender, perm.node, false);
                }
            }
        }
        if (player != null) {
            switch (perm.defaultPerm) {
                case ALL: {
                    return Permissions.check(player, perm.node, true);
                }
                case OP: {
                    return Permissions.check(player, perm.node, 2);
                }
                case NONE: {
                    return Permissions.check(player, perm.node, false);
                }
            }
        }
        return false;
    }

    public void sendMsg(Text msg, boolean isSuccess) {
        if (sender != null) {
            if (isSuccess) {
                //sender.sendSuccess(msg, true);
                sender.sendFeedback(msg,false);
            } else {
                sender.sendError(msg);
            }
            return;
        }
        if (player != null) {
            player.sendMessage(msg, false);

        }
    }

    @Override
    public void sendMsg(String msg, boolean isSuccess) {
        Text text = Text.of(msg);
        sendMsg(text, isSuccess);
    }

    @Override
    public void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard) {
        LiteralText text = new LiteralText(msg);
        Style style = Style.EMPTY;
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickClipboard));
        Text decoratedText = text.setStyle(style);
        sendMsg(decoratedText, isSuccess);
    }

    @Override
    public void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest) {
        LiteralText text = new LiteralText(msg);
        Style style = Style.EMPTY;
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickSuggest));
        Text decoratedText = text.setStyle(style);
        sendMsg(decoratedText, isSuccess);
    }
    
}
