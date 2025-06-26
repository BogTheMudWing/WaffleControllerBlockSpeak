package io.github.stonley890.waffleControllerBlockSpeak;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class StandingTimer {

    private static int counter = 0;
    public static int TIME = 20 * 60 * 2;
    private static KeyedBossBar bossBar;
    private static BukkitTask task = null;

    public static void init() {
        KeyedBossBar blockRemoveTimer = Bukkit.getBossBar(NamespacedKey.minecraft("block_remove_timer"));
        if (blockRemoveTimer == null) blockRemoveTimer = Bukkit.createBossBar(NamespacedKey.minecraft("block_remove_timer"), "Time until next standing block removal", BarColor.RED, BarStyle.SOLID);
        Bukkit.getOnlinePlayers().forEach(blockRemoveTimer::addPlayer);
        blockRemoveTimer.setVisible(true);
        bossBar = blockRemoveTimer;
        task = Bukkit.getScheduler().runTaskTimer(WaffleControllerBlockSpeak.getInstance(), StandingTimer::tick, 0, 1);
    }

    public static void stop() {
        bossBar.setVisible(false);
        task.cancel();
    }

    private static void tick() {

        if (counter >= TIME) {

            Player player = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);

            if (player == null) return;

            Material type = player.getLocation().add(0,-0.01,0).getBlock().getType();
            ComponentBuilder message = new ComponentBuilder().append("WAFFLE CONTROLLER chooses ").color(ChatColor.YELLOW).append(player.getName()).color(ChatColor.RED).append(" and removes ").color(ChatColor.YELLOW).append(type.name()).color(ChatColor.RED);

            Bukkit.spigot().broadcast(message.build());

            BlockRemover.addToDeletedList(Collections.singleton(type));

            counter = 0;
        }
        counter++;

        bossBar.setProgress((double) counter / TIME);
        bossBar.setVisible(true);
    }

}
