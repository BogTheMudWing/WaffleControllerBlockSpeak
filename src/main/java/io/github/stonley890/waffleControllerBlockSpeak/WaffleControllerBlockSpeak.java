package io.github.stonley890.waffleControllerBlockSpeak;

import org.bukkit.plugin.java.JavaPlugin;

public final class WaffleControllerBlockSpeak extends JavaPlugin {

    private static WaffleControllerBlockSpeak PLUGIN;

    @Override
    public void onEnable() {

        PLUGIN = this;

        try {
            Bot.startBot(getConfig().getString("token"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static WaffleControllerBlockSpeak getInstance() {
        return PLUGIN;
    }

}
