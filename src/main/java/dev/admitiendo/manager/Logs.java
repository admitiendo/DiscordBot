package dev.admitiendo.manager;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Logs {

    private static ConfigManager configManager = new ConfigManager();

    public static void logToLogsChannel(SlashCommandInteraction interaction, EmbedBuilder embed) {
        interaction.getServer().get().getChannelById(configManager.logsChannel).get().asTextChannel().get().sendMessage(embed);
    }

    public static void logToLogsChannel(SlashCommandInteraction interaction, String text) {
        interaction.getServer().get().getChannelById(configManager.logsChannel).get().asTextChannel().get().sendMessage(text);
    }
}
