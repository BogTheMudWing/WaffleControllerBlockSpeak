package io.github.stonley890.waffleControllerBlockSpeak.commands.discord;

import io.github.stonley890.waffleControllerBlockSpeak.Bot;
import io.github.stonley890.waffleControllerBlockSpeak.WaffleControllerBlockSpeak;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandReload implements DiscordCommand{
    @NotNull
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("reload", "Reload the config.");
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event) {
        if (!Bot.getBotMasters().contains(event.getUser().getIdLong())) {
            event.reply("User with ID " + event.getUser().getId() + " is not registered as a bot master. Execution aborted.").setEphemeral(true).queue();
            return;
        }

        WaffleControllerBlockSpeak.getInstance().reloadConfig();

        event.reply("Success.").setEphemeral(true).queue();
    }
}
