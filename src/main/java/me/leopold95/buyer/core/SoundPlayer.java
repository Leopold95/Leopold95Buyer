package me.leopold95.buyer.core;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer {
    public static void tryPlaySound(Player player, String soundPath, String volumePath){
        try {
            player.playSound(player,
                    Sound.valueOf(Config.getString(soundPath)),
                    Config.getInt(volumePath),
                    1f);
        }
        catch (Exception ep) {
            String message = Config.getMessage("item-click-sold-all-sound-bad")
                    .replace("%sound%", Config.getString("slot-multiplayer-pressed-volume"));
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }
}
