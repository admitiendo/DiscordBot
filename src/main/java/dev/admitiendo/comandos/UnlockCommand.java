package dev.admitiendo.comandos;

import dev.admitiendo.manager.ConfigManager;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class UnlockCommand implements SlashCommandCreateListener {


    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        if (interaction.getFullCommandName().equals("unlock")) {
            ConfigManager configManager = new ConfigManager();
            User user = interaction.getUser();
            Role ceo = interaction.getServer().get().getRoleById(configManager.ceoRoleID).get();
            Role admin = interaction.getServer().get().getRoleById(configManager.adminRoleID).get();
            Role owner = interaction.getServer().get().getRoleById(configManager.ownerRoleID).get();
            Role dev = interaction.getServer().get().getRoleById(configManager.devRoleID).get();

            Role member = interaction.getServer().get().getRoleById(configManager.memberRoleID).get();

            Server server = interaction.getServer().get();

            if (user.getRoles(server).contains(ceo)
                    || user.getRoles(server).contains(admin)
                    || user.getRoles(server).contains(owner)
                    || user.getRoles(server).contains(dev)) {
                try {
                    PermissionsBuilder perms = new PermissionsBuilder();

                    perms.setAllowed(PermissionType.SEND_MESSAGES);


                    interaction.getChannel().get()
                            .asRegularServerChannel().get().createUpdater()
                            .addPermissionOverwrite(member, perms.build())
                            .update();

                    interaction.createImmediateResponder().setContent("El canal ha sido desbloqueado.").respond();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                interaction.createImmediateResponder().setContent("No puedes usar este comando.").respond();
            }
        }
    }
}
