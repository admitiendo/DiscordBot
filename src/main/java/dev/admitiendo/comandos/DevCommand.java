package dev.admitiendo.comandos;

import dev.admitiendo.Main;
import dev.admitiendo.manager.ConfigManager;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DevCommand implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = event.getInteraction().getUser();

        Server server = null;

        if (interaction.getServer().isEmpty()) {
            System.out.println("Error, empty server???");
            return;
        }
        server = interaction.getServer().get();

        ConfigManager configManager = new ConfigManager();

        Role dev = null;
        if (interaction.getFullCommandName().equals("dev")) {
            dev = interaction.getServer().get().getRoleById(configManager.devRoleID).get();
            if (user.getIdAsString().equals("748619301312790580")) {
                //

                // user.addRole(dev, "dev command");
                // interaction.createImmediateResponder().setContent("Se te ha dado el rol developer.").setFlags(MessageFlag.EPHEMERAL).respond();
            } else {
                interaction.createImmediateResponder().setContent("No puedes usar este comando! :x:").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        } else if (interaction.getFullCommandName().equals("listwarns")) {
            if (user.getRoles(server).contains(dev)) {
                MessageBuilder builder = new MessageBuilder();
                interaction.createImmediateResponder().setContent("A continuación te dejo la lista de warns.").respond();
                for (Map.Entry<User, Integer> entry : Main.warns.entrySet()) {
                    User got = entry.getKey();
                    int warnsFrom = entry.getValue();
                    builder.append("○ ").append(got.getName()).append(" - (`").append(got.getId()).append("`) | `").append(warnsFrom).append(" Warns` \n");
                }
                builder.send(interaction.getChannel().get());
            } else {
                interaction.createImmediateResponder().setContent("No puedes usar este comando! :x:").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }
}
