package io.github.stonley890.waffleControllerBlockSpeak.commands.discord;

import io.github.stonley890.waffleControllerBlockSpeak.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscordCommandManager extends ListenerAdapter {

    static final List<DiscordCommand> commands = new ArrayList<>();


    @SuppressWarnings({"null"})
    public static void init() {

        List<DiscordCommand> addList = new ArrayList<>();

        addList.add(new DiscordCommandJoin());
        addList.add(new DiscordCommandReload());
        addList.add(new DiscordCommandBlocktimer());

        addCommands(addList);

    }

    @Override
    @SuppressWarnings({"null"})
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        for (DiscordCommand command : commands) {
            if (event.getName().equals(command.getName())) {
                command.onCommand(event);
                return;
            }
        }
        EmbedBuilder noMatchEmbed = new EmbedBuilder();
        noMatchEmbed.setColor(Color.RED).setTitle("No commands match your request.").setDescription("This is a fatal error and should not be possible. The command has been scheduled for deletion to prevent further exceptions.");
        event.reply("Command requested is not available in command directory. Aborting execution..").addEmbeds(noMatchEmbed.build()).queue();

        String commandId = event.getCommandId();
        Objects.requireNonNull(event.getGuild()).deleteCommandById(commandId).queue();
    }

    public static void addCommands(@NotNull List<DiscordCommand> commands) {

        if (commands.isEmpty()) return;

        JDA jda = Bot.getJDA();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<SlashCommandData> commandData = new ArrayList<>();
        for (DiscordCommand command : commands) {
            if (command != null) {
                commandData.add(command.getCommandData());
                try {
                    jda.addEventListener(command);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        for (Guild guild : jda.getGuilds()) {
            // register commands
            for (SlashCommandData commandDatum : commandData) {
                guild.upsertCommand(commandDatum).queue();
            }
        }

        commands.removeIf(Objects::isNull);
        DiscordCommandManager.commands.addAll(commands);

    }

}