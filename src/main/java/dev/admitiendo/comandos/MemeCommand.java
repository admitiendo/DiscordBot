package dev.admitiendo.comandos;

import dev.admitiendo.JSONReader;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class MemeCommand implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {

        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = interaction.getUser();

        JSONObject object = null;

        try {
            object = JSONReader.readJsonFromUrl("https://www.reddit.com/r/SpanishMeme/random.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (object != null) {
            interaction.createImmediateResponder().addEmbed(
                    new EmbedBuilder()
                            .setColor(Color.BLUE)
                            .setTitle("Meme random generado!")
                            .addField("• Tu meme legendario", object.get("title").toString())
                            .setImage(object.get("url").toString())
                            .setAuthor("\uD83D\uDE02 Herramienta de memes aleatorios")
                            .setFooter("\uD83D\uDE02 Meme de patio de juegos")
                            .setTimestampToNow()

            ).respond();
        } else {
            interaction.createImmediateResponder().setContent("No se ha podido generar un meme. :(").respond();
        }
    }
}
