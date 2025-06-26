package io.github.stonley890.waffleControllerBlockSpeak.commands.discord;

import io.github.stonley890.waffleControllerBlockSpeak.Bot;
import io.github.stonley890.waffleControllerBlockSpeak.Headphones;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandJoin implements DiscordCommand {

    @NotNull
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("join", "Join a voice channel.")
                .addOption(OptionType.CHANNEL, "channel", "The voice channel to join", true);
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event) {

        if (!Bot.getBotMasters().contains(event.getUser().getIdLong())) {
            event.reply("User with ID " + event.getUser().getId() + " is not registered as a bot master. Execution aborted.").setEphemeral(true).queue();
            return;
        }

        Long channelId = event.getOption("channel", OptionMapping::getAsLong);
        if (channelId == null) {
            event.reply("Required option channel is null. Execution aborted.").setEphemeral(true).queue();
            return;
        }
        VoiceChannel channel = Bot.getJDA().getChannelById(VoiceChannel.class, channelId);
        if (channel == null) {
            event.reply("Specified voice channel does not exist. Execution aborted.").setEphemeral(true).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        audioManager.openAudioConnection(channel);
        Headphones headphones = new Headphones();
        audioManager.setReceivingHandler(headphones);
        Bot.setAudioManager(audioManager);

        event.reply("Success.").setEphemeral(true).queue();
    }

}
