package dev.admitiendo.comandos;

import dev.admitiendo.manager.ConfigManager;
import dev.admitiendo.manager.Logs;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.awt.*;
import java.time.Duration;

public class BanCommand implements SlashCommandCreateListener {

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = interaction.getUser();
        ConfigManager configManager = new ConfigManager();

        Server server = null;

        if (interaction.getServer().isEmpty()) {
            System.out.println("Error, empty server???");
            return;
        }
        server = interaction.getServer().get();

        if (interaction.getFullCommandName().equals("ban")) {
            Role mod = interaction.getServer().get().getRoleById(configManager.modRoleID).get();
            if (user.getRoles(server).contains(mod)) {
                User baned = null;

                if (interaction.getOptionByName("user").isEmpty()) {
                    interaction.createImmediateResponder().setContent("Debes mencionar al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                try {
                    baned = interaction.getOptionByName("user").get().getUserValue().get();
                } catch (Exception ex) {
                    baned = null;
                    ex.printStackTrace();
                }

                if (baned == null) {
                    interaction.createImmediateResponder().setContent("No se ha encontrado al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                String reason = "No hay razón.";

                if (interaction.getOptionByName("reason").isEmpty()) {
                    reason = "No hay razón. ";
                } else {
                    reason = interaction.getOptionByName("reason").get().getStringValue().get();
                }


                Logs.logToLogsChannel(interaction, new EmbedBuilder()
                        .setTitle("⚠ Baneo")
                        .setColor(Color.RED)
                        .setDescription("El usuario " + baned.getMentionTag() + " fue baneado." +
                                "\nPor: " + user.getMentionTag() + "\nRazón: " + reason + "\nTiempo: 30 días.")
                        .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                        .setTimestampToNow());

                interaction.createImmediateResponder().setContent("El usuario " + baned.getMentionTag() + " fue baneado por 30 dias.").setFlags(MessageFlag.EPHEMERAL).respond();
                server.banUser(baned, Duration.ofDays(30), reason);
            } else {
                interaction.createImmediateResponder().setContent("No puedes ejecutar este comando!")
                        .setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }
}
