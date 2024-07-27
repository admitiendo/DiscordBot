package dev.admitiendo.tickets;

import dev.admitiendo.BotSettings;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.Random;

public class TicketMessages {

    public static Color randomColor() {
        Random rand = new Random();

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        return new Color(r, g, b);
    }

    public String alreadyOpen() {
        return "Ya tienes un ticket abierto!";
    }

    public String createdTicket() {
        return "Has abierto un ticket.";
    }

    public ActionRow getDeleteRow() {
        return ActionRow.of(
                Button.danger("deleteTicketButton", "Borrar Ticket", "\uD83D\uDEE0"),
                Button.secondary("createTranscript", "Crear transcript.", "⌨"));
    }

    public EmbedBuilder getTicketsEmbed() {
        return new EmbedBuilder()
                .setColor(randomColor())
                .setFooter("ZenosBox | Network", new BotSettings().logo)
                .setThumbnail(new BotSettings().logo)
                .setTitle
                        (
                                ":flag_es: Presiona un boton acorde al soporte que necesites." + "\n" +
                                        ":flag_us: Press any button related on what you need help." + "\n"
                        )
                .setDescription("");
    }

    public ActionRow getTicketsRow() {
        return ActionRow.of(
                Button.primary("generalSupportButton", "Crear Ticket", "\uD83D\uDCE9")
        );
    }

    public ActionRow getCreatedTicketRow() {
        return ActionRow.of(
                Button.primary("closeTicketButton", "Cerrar Ticket", "\uD83D\uDEAB")
        );
    }
}
