package me.leopold95.buyer.commands;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.core.SoundPlayer;
import me.leopold95.buyer.enums.CommandList;
import me.leopold95.buyer.enums.PermissionsList;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class BuyerCommand implements CommandExecutor {
    private Buyer plugin;
    public BuyerCommand(Buyer plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Config.getMessage("console-bad"));
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0){
            plugin.buyerAdmin.openPage(player, plugin.buyerAdmin.getPageMain(player));
            return true;
        }

        switch (args[0]) {
            case CommandList.OPEN:
                plugin.buyerAdmin.openPage(player, plugin.buyerAdmin.getPageMain(player));
            break;

            case CommandList.ADD_ITEM:
                if(!player.hasPermission(PermissionsList.BUYER_ADD)){
                    player.sendMessage(Config.getMessage("add-item-permission"));
                    return true;
                }


                if(args.length != 2){
                    String message = Config.getMessage("command-add-args-bad")
                            .replace("%subcommand%", CommandList.ADD_ITEM)
                            .replace("%subargs1%", Config.getMessage("command-add-subargs-item"))
                            .replace("%subargs2%", Config.getMessage("command-add-subargs-cost"));

                    sender.sendMessage(message);
                    return true;
                }

                ItemStack item = player.getInventory().getItemInMainHand().clone();
                item.setAmount(1);
                double cost = Double.parseDouble(args[1]);

                try {
                    plugin.buyerAdmin.soldRange.addItem(item, cost, player);
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(Config.getMessage("command-add-error"));
                }

                SoundPlayer.tryPlaySound(player, "buyer-added-sound", "buyer-added-sound-volume");

            break;
            case CommandList.REMOVE_ITEM:
                if(!player.hasPermission(PermissionsList.BUYER_REMOVE)){
                    player.sendMessage(Config.getMessage("remove-item-permission"));
                    return true;
                }

                try {
                    ItemStack item2 = player.getInventory().getItemInMainHand().clone();
                    item2.setAmount(1);
                    plugin.buyerAdmin.soldRange.removeItem(item2, player);
                } catch (IOException e) {
                    player.sendMessage(Config.getMessage("remove-item-bad-unexpected"));
                    e.printStackTrace();
                }

                SoundPlayer.tryPlaySound(player, "buyer-removed-sound", "buyer-removed-sound-volume");
            break;
        }


        return true;
    }
}
