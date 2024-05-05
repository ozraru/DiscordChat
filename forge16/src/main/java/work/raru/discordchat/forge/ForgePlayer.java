package work.raru.discordchat.forge;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.Permissions;

public class ForgePlayer implements IPlayer {

    final CommandSource sender;
    final ServerPlayerEntity player;
    final String name;
    final UUID uuid;

    ForgePlayer(ServerPlayerEntity player) {
        this.sender = null;
        this.player = player;
        this.name = player.getName().getContents();
        this.uuid = player.getUUID();
    }

    ForgePlayer(CommandSource source) {
        sender = source;
        ServerPlayerEntity pl;
        try {
            pl = source.getPlayerOrException();
        } catch (Exception e) {
            pl = null;
        }
        player = pl;
        name = source.getTextName();
        Entity entity = source.getEntity();
        if (entity != null) {
            uuid = entity.getUUID();
        } else {
            uuid = null;
        }
    }

    @Override
    public boolean isPlayer() {
        return player != null;
    }

    @Nullable
    @Override
    public UUID getUUID() {
        if (player != null) {
            return player.getUUID();
        }
        return null;
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean checkPerm(Permissions perm) {
        if (uuid == null) {
            return true;
        }
        if (player == null) {
            return false;
        }
        return PermissionAPI.hasPermission(player, perm.node);
    }

    public void sendMsg(TextComponent msg, boolean isSuccess) {
        if (sender != null) {
            if (isSuccess) {
                sender.sendSuccess(msg, true);
            } else {
                sender.sendFailure(msg);
            }
            return;
        }
        if (player != null) {
            player.sendMessage(msg, Util.NIL_UUID);
            return;
        }
    }

    @Override
    public void sendMsg(String msg, boolean isSuccess) {
        StringTextComponent text = new StringTextComponent(msg);
        sendMsg(text, isSuccess);
    }

    @Override
    public void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard) {
        TextComponent message = new StringTextComponent(msg);
        Style style = Style.EMPTY;
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickClipboard));
        message.setStyle(style);
        sendMsg(message, isSuccess);
    }

    @Override
    public void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest) {
        TextComponent message = new StringTextComponent(msg);
        Style style = Style.EMPTY;
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickSuggest));
        message.setStyle(style);
        sendMsg(message, isSuccess);
    }
    
}
