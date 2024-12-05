package io.github.stonley890.waffleControllerBlockSpeak;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Bot {

    private static JDA JDA;

    public static JDA getJDA() {
        return JDA;
    }

    public static void startBot(String token) throws InterruptedException {
        // Note: It is important to register your ReadyListener before building
        JDA jda = JDABuilder.createDefault("token")
//                .addEventListeners(new ReadyListener())
                .build();

        // optionally block until JDA is ready
        jda.awaitReady();
        JDA = jda;
    }

    public static @NotNull List<Long> getBotMasters() {
        return WaffleControllerBlockSpeak.getInstance().getConfig().getLongList("botMasters");
    }
}
