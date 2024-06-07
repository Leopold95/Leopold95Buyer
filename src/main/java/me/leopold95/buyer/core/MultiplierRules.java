package me.leopold95.buyer.core;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MultiplierRules {
    private int pointsPerItem;
    private int pointsRequired;
    private Keys keys;

    public MultiplierRules(Keys keys){
        this.keys = keys;

        pointsPerItem = Config.getInt("points-per-1-sold-item");
        pointsRequired = Config.getInt("points-for-0-01-multiplier");
    }

    public void add(Player player, int itemsSold){
        int totalPoints = pointsPerItem * itemsSold;

        if(totalPoints < pointsRequired)
            return;

        double multToGet = (double) totalPoints / (pointsRequired * 10);
        double playerMultiplier = getPlayerMultiplier(player);
        playerMultiplier += multToGet;

        BigDecimal bd = new BigDecimal(playerMultiplier).setScale(3, RoundingMode.HALF_UP);
        playerMultiplier = bd.doubleValue();

        player.getPersistentDataContainer().set(keys.BUYER_MULTIPLAYER, PersistentDataType.DOUBLE, playerMultiplier);
        String message = Config.getMessage("multiplier-added")
                .replace("%points%", String.valueOf(totalPoints))
                .replace("%multipiler%", String.valueOf(multToGet));
        player.sendMessage(message);
    }

    private double getPlayerMultiplier(Player player){
        return player.getPersistentDataContainer().get(keys.BUYER_MULTIPLAYER, PersistentDataType.DOUBLE);
    }
}
