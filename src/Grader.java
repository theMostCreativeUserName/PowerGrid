/* (C) 2020, R. Schiedermeier, rs@cs.hm.edu
 * Private Java 14, Linux i386 4.18.20
 * emma: Intel Core i7-4790/3600 MHz, 8 Core(s), 32067 MByte RAM
 * Fujitsu Celsius W530
 */
import java.util.Arrays;
import java.util.function.Function;

/** Demoprogramm, das eine Praktikumsbewertung simuliert.
 * Aufrufbeispiel: Ein Student hat frueher die Aufgaben 1-3 gut geloest und die vierte mittelmaessig.
 * Er hat neue, gute Loesungen fuer Aufgaben 4-6 hochgeladen:
 * java Grader 9897 ...889
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-03-07
 */
public class Grader {
    /** Hauptprogramm
     * @param args Kommandozeilenargumente:
     * 1.) Auf dem Server gespeicherte Bewertungen als String von Ziffern;
     * jede Ziffer = fruehere Bewertung einer Loesung von 1 (min) bis 9 (max) Punkte oder 0 = fehlt.
     * 2.) Auf den Server hochgeladene Loesungen als String von Ziffern (wie oben).
     */
    public static void main(String... args) {
        final int pass = 8;  // noetige Punkte fuer eine abgenommene (erfolgreiche) Loesung
        final int fixup = 6; // noetige Punkte fuer eine Abnahme mit Auflagen
        final int maxNewGradings = 2; // Anzahl neuer Bewertungen
        final int numAssignments = 10; // Anzahl Aufgaben

        final Function<String, int[]> asPoints = string -> Arrays.copyOf(string.chars().map(code -> code - '0').toArray(),
                numAssignments);
        final int[] gradings = asPoints.apply(args[0]); // frueher erworbene Punkte
        final int[] uploads = asPoints.apply(args[1]); // Punkte der aktuellen Abgaben

        int newlyGraded = 0;
        boolean continueGrading = true;
        for(int assignment = 0; continueGrading && assignment < numAssignments; assignment++)
            if(gradings[assignment] >= pass)
                // erfolgreiche Bewertung liegt vor => weiter
                ;
            else if(uploads[assignment] == 0)
                // keine Abgabe => stop, aufhoeren
                continueGrading = false;
            else if(gradings[assignment] >= fixup) {
                // Bewertung mit Auflagen liegt vor => neu bewerten, weiter wenn ok
                gradings[assignment] = uploads[assignment];
                continueGrading = gradings[assignment] >= pass;
            }
            else if(newlyGraded < maxNewGradings) {
                // neue Abgabe noch nicht bewertet => bewerten, mitzaehlen, weiter wenn ok
                gradings[assignment] = uploads[assignment];
                continueGrading = gradings[assignment] >= pass;
                newlyGraded++;  // mitzaehlen
            }
        System.out.println(Arrays.toString(gradings));
    }

}