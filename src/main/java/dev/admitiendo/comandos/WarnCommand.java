package dev.admitiendo.comandos;

import dev.admitiendo.Main;
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
import java.util.Timer;

public class WarnCommand implements SlashCommandCreateListener {
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

        if (interaction.getFullCommandName().equals("warn")) {
            Role mod = interaction.getServer().get().getRoleById(configManager.modRoleID).get();
            if (user.getRoles(server).contains(mod)) {
                User warned = null;

                if (interaction.getOptionByName("user").isEmpty()) {
                    interaction.createImmediateResponder().setContent("Debes mencionar al usuario.")
                            .setFlags(MessageFlag.EPHEMERAL).respond();
                    return;
                }

                try {
                    warned = interaction.getOptionByName("user").get().getUserValue().get();
                } catch (Exception ex) {
                    warned = null;
                    ex.printStackTrace();
                }

                if (warned == null) {
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

                if (Main.warns.containsKey(warned)) {
                    int warnsDelUsuario = Main.warns.get(warned);
                    if (warnsDelUsuario == 1) {
                        interaction.createImmediateResponder()
                                .setContent("Usuario " + warned.getMentionTag() + " sancionado.").setFlags(MessageFlag.EPHEMERAL).respond();

                        Logs.logToLogsChannel(interaction, new EmbedBuilder()
                                .setTitle("⚠ Segunda Advertencia")
                                .setColor(Color.RED)
                                .setDescription("El usuario " + warned.getMentionTag() + " fue advertido." +
                                        "\nPor: " + user.getMentionTag() + "\nRazón: " + reason + "\nWarns totales: 2")
                                .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                                .setTimestampToNow());

                        Main.warns.remove(warned);
                        Main.warns.put(warned, 2);
                    } else if (warnsDelUsuario == 2) {
                        Logs.logToLogsChannel(interaction, new EmbedBuilder()
                                .setTitle("⚠ Tercera Advertencia")
                                .setColor(Color.RED)
                                .setDescription("El usuario " + warned.getMentionTag() + " fue advertido." +
                                        "\nPor: " + user.getMentionTag() + "\nRazón: " + reason + "\nWarns totales: 3")
                                .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                                .setTimestampToNow());

                        Logs.logToLogsChannel(interaction,
                                new EmbedBuilder()
                                        .setTitle("⚠ El usuario ha llegado a 3 advertencias.")
                                        .setColor(Color.RED)
                                        .setDescription("El usuario " + warned.getMentionTag() +
                                                " fue muteado por 15 minutos por llegar a 3 advertencias. \nSus advertencias fueron reiniciadas.")
                                        .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                                        .setTimestampToNow());
                        interaction.createImmediateResponder()
                                .setContent("Usuario " + warned.getMentionTag() + " sancionado.").setFlags(MessageFlag.EPHEMERAL).respond();

                        Main.warns.remove(warned);
                        warned.addRole(interaction.getServer().get().getRoleById("1241760533108232356").get(), "Silenciado por llegar a 3 advertencias.");
                        Timer t = new java.util.Timer();
                        User finalWarned = warned;
                        t.schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        interaction.getServer().get().getChannelById("1241405740074139729").get().asTextChannel().get().sendMessage("El tiempo de mute de el usuario " + finalWarned.getMentionTag() + " ha expirado!");
                                        finalWarned.removeRole(interaction.getServer().get().getRoleById("1241760533108232356").get(), "Tiempo de mute acabado.");
                                        t.cancel();
                                    }
                                },
                                900000
                        );
                    }
                } else {
                    Logs.logToLogsChannel(interaction, new EmbedBuilder()
                            .setTitle("⚠ Primera Advertencia")
                            .setColor(Color.RED)
                            .setDescription("El usuario " + warned.getMentionTag() + " fue advertido." +
                                    "\nPor: " + user.getMentionTag() + "\nRazón: " + reason + "\nWarns totales: 1")
                            .setFooter(user.getName(), interaction.getServer().get().getIcon().get())
                            .setTimestampToNow());

                    interaction.createImmediateResponder()
                            .setContent("Usuario " + warned.getMentionTag() + " sancionado.").setFlags(MessageFlag.EPHEMERAL).respond();
                    Main.warns.put(warned, 1);
                }
            } else {
                interaction.createImmediateResponder().setContent("No puedes ejecutar este comando!")
                        .setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }
}