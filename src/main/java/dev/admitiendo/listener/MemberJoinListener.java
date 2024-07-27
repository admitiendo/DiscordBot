package dev.admitiendo.listener;

import dev.admitiendo.manager.ConfigManager;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.awt.*;
import java.util.Random;

public class MemberJoinListener implements ServerMemberJoinListener {


    public static Color randomColor() {
        Random rand = new Random();

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        return new Color(r, g, b);
    }

    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        User user = event.getUser();
        Server server = event.getServer();

        // System.out.println("Nuevo usuario: " + user.getDiscriminatedName());

        ConfigManager configManager = new ConfigManager();

        Role member = server.getRoleById(configManager.memberRoleID).get();
        user.addRole(member, "Join");

        new MessageBuilder()
                .setContent("¡Bienvenid@ " + user.getMentionTag() + "!")
                        .addEmbed(new EmbedBuilder()
                                .setTitle("")
                                .setImage("https://media.discordapp.net/attachments/1241409824277659758/1250731012372168754/OIG1.png?ex=666d5306&is=666c0186&hm=ab0df4158afa91ee48a16d6437dd6b4b04d66a368514b029c42400ec4e8ef579&=&format=webp&quality=lossless")
                                .setColor(randomColor())
                                .setThumbnail(user.getAvatar())
                                .setDescription(
                                        "\uD83D\uDCE5 Bienvenido " + user.getMentionTag() +
                                                " a **ZenosBox | Network**" +
                                                "\n¡Disfruta del servidor!" +
                                                "\nAhora somos `" + server.getMemberCount() + "` usuarios." +
                                                "\n \n    __**Información sobre el usuario**__" +
                                                "\n**\uD83E\uDC36** Nombre de usuario: " + user.getName() +
                                                "\n**\uD83E\uDC36** Creación de la cuenta \n" + "<t:" + user.getCreationTimestamp().getEpochSecond() + ":f> (<t:" + user.getCreationTimestamp().getEpochSecond() + ":R>)" +
                                                "\n**\uD83E\uDC36** ID: " + user.getIdAsString())
                                .setFooter(server.getName(), server.getIcon().get())
                                .setTimestampToNow()).send(server.getChannelById("1241390750890197093").get().asTextChannel().get());
    }
}
