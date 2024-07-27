package dev.admitiendo.tickets;

import dev.admitiendo.manager.ConfigManager;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.Arrays;

public class TicketCommands implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = interaction.getUser();
        TicketMessages messages = new TicketMessages();

        Server server = null;

        if (interaction.getServer().isEmpty()) {
            System.out.println("Error, empty server???");
            return;
        }

        server = interaction.getServer().get();

        ConfigManager configManager = new ConfigManager();

        if (interaction.getFullCommandName().equals("tickets")) {
            Role devRole = interaction.getServer().get().getRoleById(configManager.devRoleID).get();

            if (!user.getRoles(server).contains(devRole)) {
                interaction.createImmediateResponder().setContent("No puedes usar este comando! :x:").setFlags(MessageFlag.EPHEMERAL).respond();
                return;
            }

            interaction.createImmediateResponder().setContent("Se ha enviado el mensaje de tickets.").setFlags(MessageFlag.EPHEMERAL).respond();

            new MessageBuilder().setEmbed(
                            messages.getTicketsEmbed())
                    .addComponents(ActionRow.of(SelectMenu.createStringMenu("ticketsMainMenu", Arrays.asList(
                            SelectMenuOption.create("Dudas", "dudas", "Tengo una duda. \nI have a question.", "❓"),
                            SelectMenuOption.create("Reportes", "reporteusuario", "Reporta a un jugador. \nReport a player.", "\uD83D\uDEAB"),
                            SelectMenuOption.create("Apelaciones", "apelaciones", "Mi sanción es injusta, quiero apelar. \nMy punishment is unfair, i want to appeal.", "\uD83D\uDD28"),
                            SelectMenuOption.create("Reportar Staff", "reportestaff", "Quiero reportar a un staff. \nI want to report a staff.", "\uD83D\uDEE1\uFE0F"),
                            SelectMenuOption.create("Tienda", "tiendaticket", "Problemas con una compra. \nProblems with a purchase.", "\uD83D\uDED2"),
                            SelectMenuOption.create("Contraseña", "contraolvidada", "Olvidé mi contraseña. \nI forgot my password.", "\uD83D\uDD11")
                    ))))
                    .send(interaction.getChannel().get());
        }
    }
}
