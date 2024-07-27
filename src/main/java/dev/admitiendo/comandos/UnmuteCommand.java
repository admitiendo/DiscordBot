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

public class UnmuteCommand implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = interaction.getUser();


        Server server = null;

        if (interaction.getServer().isEmpty()) {
            System.out.println("Error, empty server???");
            return;
        }
        server = interaction.getServer().get();

        ConfigManager configManager = new ConfigManager();

        if (interaction.getFullCommandName().equals("unmute")) {
            Role mod = interaction.getServer().get().getRoleById(configManager.modRoleID).get();
            if (user.getRoles(server).contains(mod)) {
                User muted = null;

                if (interaction.getOptionByName("user").isEmpty()) {
                    interaction.createImmediateResponder().setContent("Debes mencionar al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                try {
                    muted = interaction.getOptionByName("user").get().getUserValue().get();
                } catch (Exception ex) {
                    muted = null;
                    ex.printStackTrace();
                }

                if (muted == null) {
                    interaction.createImmediateResponder().setContent("No se ha encontrado al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                String reason = "No hay razón.";

                Role mutedRole = interaction.getServer().get().getRoleById("1241760533108232356").get();

                if (!muted.getRoles(server).contains(mutedRole)) {
                    interaction.createImmediateResponder().setContent("El usuario " + muted.getMentionTag() + " no esta muteado.").setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }


                if (interaction.getOptionByName("reason").isEmpty()) {
                    reason = "No hay razón. ";
                } else {
                    reason = interaction.getOptionByName("reason").get().getStringValue().get();
                }


                Logs.logToLogsChannel(interaction, new EmbedBuilder()
                        .setTitle("⚠ Mute")
                        .setColor(Color.RED)
                        .setDescription("El usuario " + muted.getMentionTag() + " fue desmuteado." +
                                "\nPor: " + user.getName() + "\nRazón: " + reason)
                        .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                        .setTimestampToNow());

                server.removeRoleFromUser(muted, mutedRole, reason);
                interaction.createImmediateResponder().setContent("El usuario " + muted.getMentionTag() + " ya no esta silenciado.").setFlags(MessageFlag.EPHEMERAL).respond();
            } else {
                interaction.createImmediateResponder().setContent("No puedes ejecutar este comando!")
                        .setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }
}
