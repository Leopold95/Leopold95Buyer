package me.leopold95.buyer.abstraction;

import me.leopold95.buyer.Buyer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepitingTask implements Runnable {
    private int taskId;

    public RepitingTask(Buyer plugin, int updateDelay) {
        taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, updateDelay).getTaskId();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}