package dev.admitiendo.tickets.listener;

import dev.admitiendo.tickets.TicketMessages;
import dev.admitiendo.tickets.tickets.TicketManager;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.util.List;

public class TicketMenuListener implements SelectMenuChooseListener {
    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent event) {
        SelectMenuInteraction interaction = event.getSelectMenuInteraction();

        List<SelectMenuOption> options = interaction.getChosenOptions();

        String customID = options.get(0).getValue();

        TicketMessages messages = new TicketMessages();

        Server server = null;

        if (interaction.getServer().isEmpty()) {
            System.out.println("Error, empty server???");
            return;
        }

        server = interaction.getServer().get();

        if (customID.equals("dudas")) {
            event.getInteraction().respondWithModal(
                    "generalTicket", "Ticket sobre dudas.",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "generalSupportNick",
                            "Escribe tu nick de minecraft.",
                            3, 16)),
                    ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH,
                            "generalSupportHelp",
                            "¿Cual es tu duda?",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
            /*
            String nick = event.getInteraction().asModalInteraction().get().getTextInputValueByCustomId("generalSupportNick").get();
            String supportHelp = event.getInteraction().asModalInteraction().get().getTextInputValueByCustomId("generalSupportHelp").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createDuda(interaction.getUser(), nick, supportHelp, interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }


             */
        } else if (customID.equals("reporteusuario")) {
            event.getInteraction().respondWithModal(
                    "reportUsuario", "Ticket sobre reportar a un usuario.",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "reportUsuarioNick",
                            "Escribe tu nick de minecraft.",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
        } else if (customID.equals("apelaciones")) {
            event.getInteraction().respondWithModal(
                    "appealTicket", "Ticket sobre apelación.",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "appealNick",
                            "Escribe tu nick de minecraft.",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
        } else if (customID.equals("reportestaff")) {
            event.getInteraction().respondWithModal(
                    "reportStaff", "Ticket sobre reportar a un staff.",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "reportStaffNick",
                            "Escribe tu nick de minecraft.",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
        } else if (customID.equals("tiendaticket")) {
            event.getInteraction().respondWithModal(
                    "tiendaTicket", "Ticket sobre problema con la tienda.",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "storeTicketNick",
                            "Escribe tu nick de minecraft.",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
        } else if (customID.equals("contraolvidada")) {
            event.getInteraction().respondWithModal(
                    "contraolvidadaTicket", "Ticket sobre contraseña olvidada",
                    ActionRow.of(TextInput.create(
                            TextInputStyle.PARAGRAPH,
                            "contraolvidadaNick",
                            "Escribe tu nick de minecraft.",
                            3, 16))
            ).exceptionally(ExceptionLogger.get());
        }
    }
}
