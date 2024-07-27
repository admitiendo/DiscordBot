package dev.admitiendo.tickets.listener;

import dev.admitiendo.Main;
import dev.admitiendo.manager.ConfigManager;
import dev.admitiendo.tickets.TicketMessages;
import dev.admitiendo.tickets.tickets.TicketManager;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.io.File;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ComponentListener implements MessageComponentCreateListener {
    @Override
    public void onComponentCreate(MessageComponentCreateEvent event) {
        MessageComponentInteraction mci = event.getMessageComponentInteraction();
        String id = mci.getCustomId();

        Optional<Server> server = mci.getServer();
        Optional<User> user = event.getApi().getCachedUserById(mci.getUser().getId());

        TicketMessages messages = new TicketMessages();


        ConfigManager configManager = new ConfigManager();
        Role ceo = mci.getServer().get().getRoleById(configManager.ceoRoleID).get();
        Role admin = mci.getServer().get().getRoleById(configManager.adminRoleID).get();
        Role owner = mci.getServer().get().getRoleById(configManager.ownerRoleID).get();
        Role dev = mci.getServer().get().getRoleById(configManager.devRoleID).get();
        Role staffRole = mci.getServer().get().getRoleById(configManager.staffRoleID).get();

        Role member = mci.getServer().get().getRoleById(configManager.memberRoleID).get();

        switch (id) {
            case "createTranscript":
                try {
                    mci.createImmediateResponder().setContent("Se te ha enviado el transcript por privado.").setFlags(MessageFlag.EPHEMERAL).respond();
                    new TicketManager().generateTranscript(mci.getChannel().get()).send(user.get());
                    new TicketManager().generateTranscript(mci.getChannel().get()).send(mci.getServer().get().getChannelById("1241405740074139729").get().asTextChannel().get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            case "closeTicketButton":
                try {
                    new TicketManager().close(user.get(), server.get(), mci.getChannel().get());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(":white_check_mark:").respond();
                break;
            case "deleteTicketButton":
                if (server.get().getRoles(user.get()).contains(staffRole)) {
                    event.getInteraction().respondWithModal(
                            "deleteTicketModal", "Borrar Ticket",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "deleteTicketInput",
                                    "Escribe \"DELETE\" si quieres borrar el ticket.",
                                    3, 16))).exceptionally(ExceptionLogger.get());

                } else {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("No tienes permisos para cerrar el ticket.").respond();
                }
                break;
            case "dudas":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
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
                }
                break;
            case "reporteusuario":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
                    event.getInteraction().respondWithModal(
                            "reportUsuario", "Ticket sobre reportar a un usuario.",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "reportUsuarioNick",
                                    "Escribe tu nick de minecraft.",
                                    3, 16))
                    ).exceptionally(ExceptionLogger.get());
                }
                break;
            case "apelaciones":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
                    event.getInteraction().respondWithModal(
                            "appealTicket", "Ticket sobre apelación.",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "appealNick",
                                    "Escribe tu nick de minecraft.",
                                    3, 16))
                    ).exceptionally(ExceptionLogger.get());
                }
                break;
            case "reportestaff":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
                    event.getInteraction().respondWithModal(
                            "reportStaff", "Ticket sobre reportar a un staff.",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "reportStaffNick",
                                    "Escribe tu nick de minecraft.",
                                    3, 16))
                    ).exceptionally(ExceptionLogger.get());
                }
                break;
            case "tiendaticket":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
                    event.getInteraction().respondWithModal(
                            "tiendaTicket", "Ticket sobre problema con la tienda.",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "storeTicketNick",
                                    "Escribe tu nick de minecraft.",
                                    3, 16))
                    ).exceptionally(ExceptionLogger.get());
                }
                break;
            case "contraolvidada":
                if (Main.hasOpenTicket(user.get())) {
                    mci.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(messages.alreadyOpen()).respond();
                } else {
                    event.getInteraction().respondWithModal(
                            "contraolvidadaTicket", "Ticket sobre contraseña olvidada",
                            ActionRow.of(TextInput.create(
                                    TextInputStyle.PARAGRAPH,
                                    "contraolvidadaNick",
                                    "Escribe tu nick de minecraft.",
                                    3, 16))
                    ).exceptionally(ExceptionLogger.get());
                }
                break;
        }
    }
}
