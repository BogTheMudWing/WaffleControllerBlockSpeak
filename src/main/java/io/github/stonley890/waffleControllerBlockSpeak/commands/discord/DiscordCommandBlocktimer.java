package io.github.stonley890.waffleControllerBlockSpeak.commands.discord;

import io.github.stonley890.waffleControllerBlockSpeak.Bot;
import io.github.stonley890.waffleControllerBlockSpeak.StandingTimer;
import io.github.stonley890.waffleControllerBlockSpeak.WaffleControllerBlockSpeak;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandBlocktimer implements DiscordCommand{
    @NotNull
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("blocktimer", "Manage the block timer.")
                .addSubcommands(
                        new SubcommandData("start", "Start the block timer."),
                        new SubcommandData("stop", "Stop the block timer."),
                        new SubcommandData("set-time", "Set the time of the block timer.")
                                .addOption(OptionType.INTEGER, "ticks", "The number of ticks to set the block timer to.")
                );
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event) {
        if (!Bot.getBotMasters().contains(event.getUser().getIdLong())) {
            event.reply("User with ID " + event.getUser().getId() + " is not registered as a bot master. Execution aborted.").setEphemeral(true).queue();
            return;
        }

        String subCommand = event.getSubcommandName();
        if (subCommand == null) {
            event.reply("Command execution cannot be completed without subcommand. Execution aborted.").setEphemeral(true).queue();
            return;
        }

        switch (subCommand) {
            case "start" -> {
                StandingTimer.init();
                event.reply("Block timer started.").setEphemeral(true).queue();
                return;
            }
            case "stop" -> {
                StandingTimer.stop();
                event.reply("Block timer stopped.").setEphemeral(true).queue();
                return;
            }
            case "set-time" -> {
                Integer ticks = event.getOption("ticks", OptionMapping::getAsInt);
                if (ticks == null) {
                    event.reply("Ticks must be defined. Execution aborted.").setEphemeral(true).queue();
                    return;
                }
                StandingTimer.TIME = ticks;
            }
            default -> {

            }
        }

        WaffleControllerBlockSpeak.getInstance().reloadConfig();

        event.reply("Success.").setEphemeral(true).queue();
    }
}
