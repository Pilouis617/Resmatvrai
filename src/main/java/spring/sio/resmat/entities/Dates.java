package spring.sio.resmat.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Affichage des dates au format français dans les vues.
 */
public final class Dates {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH'h'mm");

    private Dates() {
    }

    public static String fr(LocalDateTime date) {
        return date == null ? "" : date.format(FORMAT);
    }
}
