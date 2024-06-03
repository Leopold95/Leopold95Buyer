package me.leopold95.buyer.commands;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.enums.CommandList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyerCommand implements CommandExecutor {
    private Buyer plugin;
    public BuyerCommand(Buyer plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Config.getMessage("console-bad"));
            return true;
        }

        if(args.length == 0){
            return false;
        }

        Player player = (Player) sender;

        switch (args[0]) {
            case CommandList.OPEN -> {
                player.openInventory(plugin.buyerInventories.pageMain());
            }

            case CommandList.ADD_ITEM -> {
                if(args.length != 2){
                    String message = Config.getMessage("command-add-args-bad")
                            .replace("%subcommand%", CommandList.ADD_ITEM)
                            .replace("%subargs1%", Config.getMessage("command-add-subargs-item"))
                            .replace("%subargs2%", Config.getMessage("command-add-subargs-cost"));

                    sender.sendMessage(message);
                    return true;
                }

            }
            case CommandList.REMOVE_ITEM -> {

            }
        }


        return true;
    }
}
