package dev.admitiendo.tickets.tickets;

import dev.admitiendo.Main;
import dev.admitiendo.manager.ConfigManager;
import dev.admitiendo.tickets.TicketMessages;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TicketManager {
    public void close(User user, Server server, TextChannel channel) throws ExecutionException, InterruptedException {
        User ticketOwner = server.getApi().getUserById(channel.asServerTextChannel().get().getTopic()).get();
        TicketMessages messages = new TicketMessages();

        new MessageBuilder()
                .addComponents(messages.getDeleteRow())
                .addEmbed(new EmbedBuilder()
                        .setTitle("Este ticket ha sido cerrado por: " + user.getName())
                        .setDescription(" ")
                        .setColor(Color.RED))
                .send(channel);

        ServerTextChannelUpdater updater = new ServerTextChannelUpdater(channel.asServerTextChannel().get())
                .setName("closed-" + ticketOwner.getName())
                .addPermissionOverwrite(ticketOwner, new PermissionsBuilder().setAllDenied().build());

        generateTranscript(channel).send(ticketOwner);
        Main.setOpenTicket(user, false);
        updater.update();
    }

    public MessageBuilder generateTranscript(TextChannel channel) {
        MessageBuilder builder = new MessageBuilder();
        CompletableFuture<MessageSet> set =
                channel.getMessages(500);
        builder.append("Ticket Transcript - " + channel.asServerChannel().get().getName()).append("\n");
        builder.append("```");
        for (Message message : set.join()) {
            builder.append(message.getAuthor().getDiscriminatedName()).append(":").append(message.getContent()).append("\n");
        }
        builder.append("```");
        return builder;
    }

    public void create(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById("1251178455119298681").get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById("1247205731757850735").get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("Nuestros staffs te atenderan el día de hoy.")
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&1247205731757850735>");
    }

    private ConfigManager configManager = new ConfigManager();

    public void createDuda(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsDudas).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-duda-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("¿Cual es su duda " + user.getMentionTag() + "?")
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&" + configManager.staffRoleID + ">");
    }

    public void createReportUser(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsReportUser).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-report-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("¿A quién deseas reportar " + user.getMentionTag() + "?" + "\n \n¿Tienes pruebas?")
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&" + configManager.staffRoleID + ">");
    }

    public void createAppeal(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsAppeal).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-appeal-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("¿Qué deseas apelar " + user.getMentionTag() + "?" + "\n**\uD83E\uDC36** Nick: " + name)
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&" + configManager.staffRoleID + ">");
    }

    public void createStore(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsTienda).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-tienda-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("¿Qué problema tienes con la tienda " + user.getMentionTag() + "?" + "\n**\uD83E\uDC36** Nick: " + name)
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&" + configManager.staffRoleID + ">");
    }

    public void createReportStaff(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsReportStaff).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-reportstaff-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("¿Qué staff quieres reportar " + user.getMentionTag() + "? \n¿Tienes pruebas?" + "\n**\uD83E\uDC36** Nick: " + name)
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@&"  + configManager.staffRoleID + ">");
    }

    public void createPassword(User user, String name, String help, Server server) throws ExecutionException, InterruptedException {
        ChannelCategory generalCategory = server.getChannelCategoryById(configManager.ticketsPassword).get().asChannelCategory().get();

        TicketMessages messages = new TicketMessages();

        Permissions permissionsUser = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionsStaff = new PermissionsBuilder().setAllowed(
                PermissionType.SEND_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.MANAGE_MESSAGES,
                PermissionType.VIEW_CHANNEL,
                PermissionType.ATTACH_FILE,
                PermissionType.EMBED_LINKS).build();

        Permissions permissionGlobal = new PermissionsBuilder().setAllDenied().build();

        ServerTextChannelBuilder ticket = new ServerTextChannelBuilder
                (server)
                .setCategory(generalCategory)
                .setName("ticket-appeal-" + user.getName())
                .setTopic(user.getIdAsString())
                .addPermissionOverwrite(server.getEveryoneRole(), permissionGlobal)
                .addPermissionOverwrite(server.getRoleById(configManager.staffRoleID).get(), permissionsStaff)
                .addPermissionOverwrite(user, permissionsUser);

        TextChannel ticketChannel = ticket.create().get();

        new MessageBuilder()
                .addEmbed(new EmbedBuilder()
                        .setTitle("Ticket | " + user.getName())
                        .setDescription("Nuestros staffs te atenderan en un momento." + "\n**\uD83E\uDC36** Nick: " + name)
                        .setFooter("Ticket | " + user.getIdAsString())
                        .setColor(Color.BLUE))
                .addComponents(messages.getCreatedTicketRow())
                .send(ticketChannel);

        Main.setOpenTicket(user, true);

        server.getChannelById(ticketChannel.getIdAsString()).get().asTextChannel().get()
                .sendMessage(user.getMentionTag() + " | <@ " + configManager.staffRoleID + ">");
    }
}
