package work.raru.discordchat.forge12;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.Permissions;

public class ForgePlayer implements IPlayer {

    final ICommandSender sender;
    final EntityPlayer player;
    final String name;
    final UUID uuid;

    ForgePlayer(EntityPlayer player) {
        this.sender = null;
        this.player = player;
        this.name = player.getName();
        this.uuid = player.getUniqueID();
    }

    ForgePlayer(ICommandSender source) {
        sender = source;
        EntityPlayer pl = null;
        if (source instanceof EntityPlayer) {
            pl = (EntityPlayer) source;
        }
        player = pl;
        name = source.getName();
        Entity entity = source.getCommandSenderEntity();
        if (entity != null) {
            uuid = entity.getUniqueID();
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
            return player.getUniqueID();
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

    public void sendMsg(ITextComponent msg) {
        if (sender != null) {
            sender.sendMessage(msg);
            return;
        }
        if (player != null) {
            player.sendMessage(msg);
            return;
        }
    }

    @Override
    public void sendMsg(String msg, boolean isSuccess) {
        sendMsg(new TextComponentString(msg));
    }

    @Override
    public void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard) {
        ITextComponent message = new TextComponentString(msg);
        Style style = new Style();
        style = style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickClipboard));
        message.setStyle(style);
        sendMsg(message);
    }

    @Override
    public void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest) {
        ITextComponent message = new TextComponentString(msg);
        Style style = new Style();
        style = style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickSuggest));
        message.setStyle(style);
        sendMsg(message);
    }
    
}
