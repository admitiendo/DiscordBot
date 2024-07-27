package dev.admitiendo.comandos;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.awt.*;
import java.util.Random;

public class DadosCommand implements SlashCommandCreateListener {

    public Integer[] dados = {
            1, 2, 3, 4, 5, 6
    };

    public String[] dadosUrl = {
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/one-310338_1280.png",
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/two-310337_960_720.png",
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/three-310336_1280.png",
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/dice-310335_1280.png",
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/five-310334_960_720.png",
            "https://cdn.pixabay.com/photo/2014/04/03/10/24/dice-310333_1280.png"
    };

    public static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        if (interaction.getFullCommandName().equals("dados")) {
            interaction.createImmediateResponder().addEmbed(new EmbedBuilder()
                    .setAuthor(interaction.getUser().getDiscriminatedName() + " Ha tirado los dados.",
                            "",
                            interaction.getUser().getAvatar())
                    .setImage(dadosUrl[randInt(1, 6)])
                    .setColor(Color.GREEN)
                    .setFooter(interaction.getServer().get().getName())
                    .setTimestampToNow()).respond();
        }
    }
}
