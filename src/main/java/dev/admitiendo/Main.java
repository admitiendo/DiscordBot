package dev.admitiendo;

import dev.admitiendo.comandos.*;
import dev.admitiendo.listener.MemberJoinListener;
import dev.admitiendo.tickets.TicketCommands;
import dev.admitiendo.tickets.listener.ComponentListener;
import dev.admitiendo.tickets.listener.ModalListener;
import dev.admitiendo.tickets.listener.TicketMenuListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.*;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.lang.Boolean.TRUE;

public class Main {

    public static HashMap<User, Integer> warns = new HashMap<>();

    public static DiscordApi discordApi;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DiscordApi api = new DiscordApiBuilder()
                    .setToken("YOUR TOKEN")
                .setAllIntents()
                    .login().join();

        discordApi = api;

        api.updateActivity(ActivityType.PLAYING, "bot");

        FallbackLoggerConfiguration.setDebug(true);
        FallbackLoggerConfiguration.setTrace(true);

        ServerMemberJoinListener listener = new MemberJoinListener();
        api.addServerMemberJoinListener(listener);
        api.getGlobalSlashCommands().get().forEach(ApplicationCommand::delete);
        registerCommands(api);
        loadListeners(api);
        System.out.println("El bot ha empezado a funcionar!, usuario:" + api.getYourself().getDiscriminatedName());
        System.out.println("Invitacion: " + api.createBotInvite());
    }

    public static void registerCommands(DiscordApi api) {
        SlashCommand dadosCommand =
                SlashCommand.with("dados", "Tira los dados!").createGlobal(api).join();

        SlashCommand lockCommand =
                SlashCommand.with("lock", "Bloquea un canal.").createGlobal(api).join();

        SlashCommand unlockCommand =
                SlashCommand.with("unlock", "Desbloquea un canal.").createGlobal(api).join();

        SlashCommand warnCommand =
                SlashCommand.with("warn", "Advierte a un usuario.",
                                Arrays
                                        .asList(
                                                SlashCommandOption.createUserOption("user", "Usuario al que advertir.",
                                                        true)
                                                ,SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "reason", "Razón de el warn")))

                        .setDefaultEnabledForPermissions(PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS)
                        .createGlobal(api).join();

        SlashCommand muteCommand =
                SlashCommand.with("mute", "Mutea a un usuario.",
                                Arrays.asList(
                                        SlashCommandOption.createUserOption("user", "Usuario al que mutear.",
                                                true),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "reason", "Razón de el mute."),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.LONG, "time", "Tiempo de mute.")))

                        .setDefaultEnabledForPermissions(PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS)
                        .createGlobal(api).join();

        SlashCommand unmuteCommand =
                SlashCommand.with("unmute", "Elimina el mute de un usuario.",
                                Arrays.asList(
                                        SlashCommandOption.createUserOption("user", "Usuario al que quitar el mute.",
                                                true),
                                        SlashCommandOption.createWithOptions
                                                (SlashCommandOptionType.STRING, "reason", "Razón de quitar el mute.")))
                        .setDefaultEnabledForPermissions(PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS)
                        .createGlobal(api).join();

        SlashCommand devCommand =
                SlashCommand.with("dev", "dev command").setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR)
                        .createGlobal(api).join();

        SlashCommand listwarnsCommand =
                SlashCommand.with("listwarns", "Obtén la lista de los warns actuales.")
                        .setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR)
                        .createGlobal(api).join();

        SlashCommand tickets =
                SlashCommand.with("tickets", "Envia el panel de tickets.")
                        .setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR)
                        .createGlobal(api).join();

        /* SlashCommand memeCommand =
                SlashCommand.with("meme", "Envia un meme.").createGlobal(api).join();
         */

        SlashCommand banCommand =
                SlashCommand.with("ban", "Banea a un usuario.",
                                Arrays.asList(
                                        SlashCommandOption.createUserOption("user", "Usuario al que banear.",
                                                true),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "reason", "Razón de el baneo.")))

                        .setDefaultEnabledForPermissions(PermissionType.BAN_MEMBERS, PermissionType.KICK_MEMBERS)
                        .createGlobal(api).join();
    }

    public static void loadListeners(DiscordApi api) {
        api.addListener(new BanCommand());
        api.addListener(new DadosCommand());
        api.addListener(new LockCommand());
        api.addListener(new UnlockCommand());
        api.addListener(new WarnCommand());
        api.addListener(new DevCommand());
        api.addListener(new MuteCommand());
        api.addListener(new UnmuteCommand());

        api.addListener(new TicketCommands());
        api.addListener(new ModalListener());
        api.addListener(new ComponentListener());

        api.addListener(new TicketMenuListener());

        // api.addListener(new MemeCommand());
    }

    public static Set<User> openTickets = new HashSet<>();

    public static boolean hasOpenTicket(User user) {
        return openTickets.contains(user);
    }

    public static void setOpenTicket(User user, boolean bool) {
        if (bool == TRUE) {
            openTickets.add(user);
        } else {
            openTickets.remove(user);
        }
    }

    public static Set<User> getOpenTickets() {
        return openTickets;
    }
}