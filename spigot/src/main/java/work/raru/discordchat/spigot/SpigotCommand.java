package work.raru.discordchat.spigot;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import work.raru.discordchat.common.MinecraftCommand;

public class SpigotCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return MinecraftCommand.getAutoComplete(args, sender::hasPermission);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1 || args[0].equals("help")) {
            sender.sendMessage(MinecraftCommand.getHelpMessage(label, sender::hasPermission));
			return true;
		}
		try {
			switch (args[0]) {
			case "link":
				return MinecraftCommand.link(new SpigotPlayer(sender), args.length == 2 ? args[1] : null);
			case "reload": 
				MinecraftCommand.reload(new SpigotPlayer(sender));
				return true;
			case "restart":
				MinecraftCommand.restart(new SpigotPlayer(sender));
				return true;
			case "emojiful":
				MinecraftCommand.emojiful(new SpigotPlayer(sender));
				return true;
			default:
				sender.sendMessage("Invalid subcommand. To check usage, use: " + label + " help");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage("Unknown error has occured: " + e.getMessage());
			sender.sendMessage("Stacktrace is available in server console.");
			return true;
		}
    }
    
}
