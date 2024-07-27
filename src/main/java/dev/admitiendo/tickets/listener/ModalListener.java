package dev.admitiendo.tickets.listener;

import dev.admitiendo.tickets.TicketMessages;
import dev.admitiendo.tickets.tickets.TicketManager;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.api.listener.interaction.ModalSubmitListener;

import java.util.Optional;

public class ModalListener implements ModalSubmitListener {
    @Override
    public void onModalSubmit(ModalSubmitEvent event) {
        ModalInteraction interaction = event.getModalInteraction();
        String customID = interaction.getCustomId();

        TicketMessages messages = new TicketMessages();

        if (customID.equals("generalTicket")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("generalSupportNick").get();
            String supportHelp = event.getModalInteraction().getTextInputValueByCustomId("generalSupportHelp").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createDuda(interaction.getUser(), nick, supportHelp, interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (customID.equals("deleteTicketModal")) {
            String confirmation = event.getModalInteraction().getTextInputValueByCustomId("deleteTicketInput").get();
            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(" ").respond();
            if (confirmation.equalsIgnoreCase("delete")) {
                interaction.getChannel().get().asServerTextChannel().get().delete("El ticket fue eliminado por: " + interaction.getUser());
            }
        } else if (customID.equals("reportUsuario")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("reportUsuarioNick").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createReportUser(interaction.getUser(), nick, "Report usuario", interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (customID.equals("appealTicket")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("appealNick").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createAppeal(interaction.getUser(), nick, "Apelación", interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (customID.equals("reportStaff")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("reportStaffNick").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createReportStaff(interaction.getUser(), nick, "Reportar staff", interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (customID.equals("tiendaTicket")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("storeTicketNick").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createStore(interaction.getUser(), nick, "Soporte con la tienda", interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (customID.equals("contraolvidadaTicket")) {
            String nick = event.getModalInteraction().getTextInputValueByCustomId("contraolvidadaNick").get();
            try {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.createdTicket()).respond();
                new TicketManager().createPassword(interaction.getUser(), nick, "Contraseña olvidada", interaction.getServer().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
