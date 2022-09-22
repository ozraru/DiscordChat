package work.raru.discordchat.spigot;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import work.raru.discordchat.common.IPlayer;

public class SpigotPlayer implements IPlayer {

    boolean isPlayer;
    OfflinePlayer player = null;
    CommandSender sender = null;

    SpigotPlayer(CommandSender sender) {
        isPlayer = sender instanceof Player;
        if (isPlayer) {
            this.player = ((Player) sender);
        }
        this.sender = sender;
    }

    SpigotPlayer(OfflinePlayer player) {
        isPlayer = true;
        this.player = player;
        this.sender = player.getPlayer();
    }

    @Override
    public boolean isPlayer() {
        return isPlayer;
    }

    @Override
    public UUID getUUID() {
        if (player == null) {
            return null;
        }
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        if (player == null) {
            return null;
        }
        return player.getName();
    }

    @Override
    public boolean checkPerm(String perm) {
        if (sender == null) {
            return false;
        }
        return sender.hasPermission(perm);
    }

    @Override
    public void sendMsg(String msg, boolean isSuccess) {
        if (sender == null) {
            return;
        }
        sender.sendMessage(msg);
    }

    @Override
    public void sendMsgClipboard(String msg, boolean isSuccess, String clickClipboard) {
        if (sender == null) {
            return;
        }
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickClipboard));
        sender.spigot().sendMessage(message);
    }

    @Override
    public void sendMsgSuggest(String msg, boolean isSuccess, String clickSuggest) {
        if (sender == null) {
            return;
        }
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickSuggest));
        sender.spigot().sendMessage(message);
    }
    
}
