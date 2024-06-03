package me.leopold95.buyer.commands;

import me.leopold95.buyer.enums.CommandList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuyerCommandTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1)
            return List.of(
                    CommandList.OPEN,
                    CommandList.ADD_ITEM,
                    CommandList.REMOVE_ITEM
            );
        return List.of();
    }
}
