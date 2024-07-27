package dev.admitiendo.tickets;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String formatToMMSS(final int seconds) {
        final int millis = seconds * 1000;
        final int sec = millis / 1000 % 60;
        final int min = millis / 60000 % 60;
        final int hr = millis / 3600000 % 24;
        return ((hr > 0) ? String.format("%02d", hr) : "") + String.format("%02d:%02d", min, sec);
    }
    
    public static String getDurationBreakdown(long millis) {
        if (millis < 0L) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        final StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append(" Dias ");
        }
        if (hours > 0) {
            sb.append(hours);
            if (hours == 1) {
                sb.append(" Hora ");
            } else {
                    sb.append(" Horas ");
            }
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(" Minutos ");
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append(" Segundos");
        }
        return sb.toString();
    }
}
