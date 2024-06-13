package me.leopold95.buyer.commands;

import me.leopold95.buyer.enums.CommandList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


import java.util.ArrayList;
import java.util.List;

public class BuyerCommandTab implements TabCompleter {
    @Override
    public  List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1){
            List<String> list = new ArrayList<String>();
            list.add(CommandList.OPEN);
            list.add(CommandList.ADD_ITEM);
            list.add(CommandList.REMOVE_ITEM);
            return list;
        }

        return new ArrayList<String>();
    }
}
