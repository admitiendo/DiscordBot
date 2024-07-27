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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class MuteCommand implements SlashCommandCreateListener {
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

        if (interaction.getFullCommandName().equals("mute")) {
            Role mod = interaction.getServer().get().getRoleById(configManager.modRoleID).get();
            if (user.getRoles(server).contains(mod)) {
                User muted = null;

                if (interaction.getOptionByName("user").isEmpty()) {
                    interaction.createImmediateResponder().setContent("Debes mencionar al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                if (interaction.getOptionByName("time").isEmpty()) {
                    interaction.createImmediateResponder().setContent("Debes especificar cuanto tiempo de mute. (En minutos)")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                long timeDeMute = interaction.getOptionByName("time").get().getLongValue().get() * 60000;

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

                if (muted.getRoles(server).contains(mutedRole)) {
                    interaction.createImmediateResponder().setContent("El usuario " + muted.getMentionTag() + " ya esta muteado.").setFlags(MessageFlag.EPHEMERAL).respond();
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
                        .setDescription("El usuario " + muted.getMentionTag() + " fue muteado." +
                                "\nPor: " + user.getName() + "\nRazón: " + reason + "\nTiempo: " + timeFormat(timeDeMute))
                        .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                        .setTimestampToNow());

                interaction.createImmediateResponder().setContent("Usuario " + muted.getMentionTag() + " sancionado.").setFlags(MessageFlag.EPHEMERAL).respond();
                muted.addRole(mutedRole, reason);
                Timer t = new java.util.Timer();
                User finalWarned = muted;
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                interaction.getServer().get().getChannelById("1241405740074139729").get().asTextChannel().get().sendMessage("El tiempo de mute de el usuario " + finalWarned.getMentionTag() + " ha expirado!");
                                finalWarned.removeRole(mutedRole, "Tiempo de mute acabado.");
                                t.cancel();
                            }
                        },
                        timeDeMute
                );
            } else {
                interaction.createImmediateResponder().setContent("No puedes ejecutar este comando!")
                        .setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }

    public static String timeFormat(Long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);

        if (time < 3600000) {
            dateFormat = new SimpleDateFormat("mm:ss");
        } else if (time > 3600000) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }

        return dateFormat.format(date);
    }
}
