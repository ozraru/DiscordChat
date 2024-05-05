package work.raru.discordchat.forge19;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import work.raru.discordchat.common.IPlayer;
import work.raru.discordchat.common.Permissions;

import javax.annotation.Nullable;
import java.util.UUID;

import static work.raru.discordchat.forge19.ForgeCommand.permissionNodes;

public class ForgePlayer implements IPlayer {

    final CommandSourceStack sender;
    final ServerPlayer player;
    final String name;
    final UUID uuid;

    ForgePlayer(ServerPlayer player) {
        this.sender = null;
        this.player = player;
        this.name = player.getName().getString();
        this.uuid = player.getUUID();
    }

    ForgePlayer(CommandSourceStack source) {
        sender = source;
        ServerPlayer pl;
        try {
            pl = sender.getPlayerOrException();
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
        PermissionNode<Boolean> node = permissionNodes.get(perm.node);
        if (node == null) return false;
        return PermissionAPI.getPermission(player, node);
    }

    public void sendMsg(MutableComponent component, boolean isSuccess) {
        if (sender != null) {
            if (isSuccess) {
                sender.sendSuccess(component, true);
            } else {
                sender.sendFailure(component);
            }
            return;
        }
        if (player != null) {
            player.sendSystemMessage(component);
            return;
        }
    }

    @Override
    public void sendMsg(String msg, boolean isSuccess) {
        MutableComponent component = MutableComponent.create(new LiteralContents(msg));
        sendMsg(component, isSuccess);
    }

    @Override
    public void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard) {
        MutableComponent component = MutableComponent.create(new LiteralContents(msg));
        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickClipboard));;
        sendMsg(component.withStyle(style), isSuccess);
    }

    @Override
    public void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest) {
        MutableComponent component = MutableComponent.create(new LiteralContents(msg));
        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickSuggest));
        sendMsg(component.withStyle(style), isSuccess);
    }
    
}
