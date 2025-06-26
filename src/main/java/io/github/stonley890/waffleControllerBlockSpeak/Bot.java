package io.github.stonley890.waffleControllerBlockSpeak;

import io.github.stonley890.waffleControllerBlockSpeak.commands.discord.DiscordCommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Bot {

    private static JDA JDA;
    private static AudioManager audioManager;

    public static JDA getJDA() {
        return JDA;
    }

    public static void startBot(String token) throws InterruptedException {
        // Note: It is important to register your ReadyListener before building
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new DiscordCommandManager())
                .build();

        // optionally block until JDA is ready
        jda.awaitReady();
        JDA = jda;
        DiscordCommandManager.init();
    }

    public static @NotNull List<Long> getBotMasters() {
        return WaffleControllerBlockSpeak.getInstance().getConfig().getLongList("botMasters");
    }

    public static AudioManager getAudioManager() {
        return audioManager;
    }

    public static void setAudioManager(AudioManager instance) {
        audioManager = instance;
    }
}
