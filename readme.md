# Tests

### 1) JavaDoc 
1. IntelliJ Menüpunkt **"Tools"** --> "Generate JavaDoc"
1. Wählt eine "Output directory", dort wird die Dokumentation gespeichert.
1. Im Feld "Other command line arguments:" müsst ihr folgendes einfügen: "--enable-preview --release 14". Danach könnt ihr auf "OK" klicken. Sollte es keinen Fehler geben, sollte sich der Browser mit der Dokumentation öffnen. Geschieht dies nicht, exestieren Fehler, welche im Terminal ausgelesen werden können. In diesem Fall ist die entsprechende Zelle mit "Fehler" zu kennzeichnen.

### 2) PMD
1. Herunterladen/Speichern von [PMD-Vorlagen](https://sol.cs.hm.edu/course/st/download.do?filename=pmd.xml&mimetype=text/xml "Schiedermeier-PMD")
1. INtelliJ Menüpunkt **"Files"** --> Settings --> Other Settings --> PMD --> beim Seitenlabel *RuleSet* auf das '+' --> auf "Browse" --> PMD-File auswählen
1. Laufen lassen könnt ihr das PMD indem ihr, auf den zu testenden Ordner rechts-klickt und Run **PMD auswählt** --> Custom Rules --> eure PMD-Datei

### 3) Checkstyle
1. Herunterladen/Speichern con [Checkstyle-Vorlagen](https://sol.cs.hm.edu/course/st/download.do?filename=checkstyle.xml&mimetype=text/xml "Schiedermeier Checkstyle")
**stellt sicher dass ihr die neuste Version des Checkstyle-Plugins habt**
1. IntelliJ Menüpunkt **"Files"** --> Settings --> Other Settings --> Checkstyle --> auf '+' bei "Configuration File" --> im Pop-Up Fenster einen Namen eingeben und über "Browse" Checkstyle-File auswählen
####falls ihr trotzdem noch Probleme habt
1. versucht Bestandteile von Java 14 Preview aus zu kommentieren. Beachtet bei Fehlern, welche Auswirkungen sie auf den auskommentierten Teil haben.
1. versucht eure Java-Version auf "13 - SDK default" zu setzen und nochmal zu parsen. (siehe unten)
1. prüft ob ihr Rechtschreibfehler in der Datei habt (grün unterringelt von IntelliJ) --> Worte wie *Electro* könnt ihr zum Wörtebuch von IntelliJ hinzufügen. (siehe unten)
##### Ändern der Java-Version
1. intelliJ Menüpunkt **"Files"** --> Project Settings
1. heir auf **Project** und dann die Projekt SDK ändern
##### Aufnehmen neuer Worte ins Wörterbuch
1. klickt auf das Wort, dass ihr hinzufügen wollt
1. presst **ALT** + **ENTETR** auf der Tastatur oder klickt auf die Glühbirne, die in der Zeile des Wortes erscheint 
1. wählt: save "Wort" in project-level dictionary

### IntelliJ Line Coverage
In IntelliJ oben rechts neben dem grünen Run-Button auf das Schild-Symbol mit Button klicken
![Coverage Tool](https://i.stack.imgur.com/sW22A.gif)

### Mutations
1. instaliert das **"PIT Runner"** Plugin für IntelliJ
1. in IntelliJ --> auf **Edit Configuration**, im Drop-Down Menü neben dem Run-Symbol
1. im Pop-Up Fenster auf das '+' --> im Drop-Down Menü "Pit Runner" auswählen
1. bennent diese Datei und tragt folgende Daten ein:

|im Feld | Daten | wird automatisch generiert |
| --- | --- | --- |
| Target classes | edu.hm.severin.powergrid.* | Nein |
| Target tests | edu.* | Nein |
| Source dir | < Euer-PATH-zum-Project > | Ja |
| Report dir | < Euer-PATH-zum-Project >/report | Ja |
| Other params | --jvmArgs "--enable-preview" --outputFormats XML,HTML | Nein |
      
1. Apply und OK
1. um Mutations-Tests laufen zu lassen, wählt im Drop-Down Menü eure Pit Konfiguration und lasst diese laufen

**Wichtig**: wenn ihr euer Projekt auf GitHub hochladet -> löscht vorher den report-Ordner, der durch die Mutationen entstanden ist

### CNN Überprüfen
das Plugin von IntelliJ, dass automatisch den CNN zählt heißt **Code Metrics**
1. File --> Other Settings --> Code Metrics
1. im Seiten-Label stellt ihr folgendes ein:

|Name | Daten |
|--- | --- |
| Complexity level low | 0|
| Complexity level normal | 3 |
| Complexity level high | 5 |
| Complexity level extreme | 10 |
| Show metrics above complexity | 1|

für die Auswahlmöglichkeiten darunter wählt ihr alle aus
1. im Seiten-Label Advanced setzt ihr die folgenden Optionen aus **1** und jeglichen Rest auf **0**
      1. anonymous Class
      1. break statement
      1. class
      1. conditional expression
      1. continue satement
      1. do while
       1. for
       1. foreach
       1. if
      1. labeled statement
      1. method
      1. switch
      1. catch
      1. while
      1. polyadic expr.
1. Apply + OK

Das Plugin zählt den CNN der kompletten Klasse relativ unschön, hier müssen die einzelnen Zahlen im Drop-Down Menü aufaddiert werden
1. im Kopf der Klasse --> klickt auf den Code Metrics Kommentar (das orangene Feld) 
1. dann öffnet sich ein Drop-Down Menü in dem die CNN-Zahlen der einzelnen Methoden angezeigt werden
1. diese müssen dann aufaddiert werden

### SpotBugs
1. Installiert das **SpotBugs** Plugin
1. in IntelliJ unten rechts klickt ihr dann auf das SpotBugs-Käfer Symbol
1. klickt auf **"Anylize Project files not including Test sources"** (das blaue Quadrat mit grünen Pfeil)

## Prüfen der Abgaben
An manchen Stellen in dem Code ist es nicht möglich (oder haben wir nach langen Suchen) keine Möglichkeit gefunden die auftretenden Probleme zu lösen. Diese wurden aber mit einem Kommentar markiert.

### Version 1.11 / BER
| Art der Testung           | Phillip   | Severin   | Teresa   | Tobias    |
| ---                       | ---       | ---       | ---       | ---       |
| JavaDoc                   |    Ja   |   Ja  |     |      |
| PMD                       |    Ja   |  Ja    |      |      |
| Checkstyle                |    Ja   |  Ja    |      |      |
| IntelliJ Line Coverage    |    Ja   |  Ja    |      |      |
| Mutations                 |    Ja   |   Ja   |      |      |
| CCN Class                 |    Ja   |   Ja   |      |      |
| CCN Methods               |    Ja   |   Ja   |      |      |
| Spotbugs               |    Ja   |   Ja   |      |      |
| Externe Tests             |    Ja   |  Ja    |      |       |


### Version2.11 / Nyancat
| Art der Testung           | Phillip   | Severin   | Teresa   | Tobias    |
| ---                       | ---       | ---       | ---       | ---       |
| JavaDoc                   |    Ja   |   Ja |     |      |
| PMD                       |    Ja   |   Ja   |      |      |
| Checkstyle                |    Ja   |   Ja   |      |      |
| IntelliJ Line Coverage    |    Ja   |   Ja   |      |      |
| Mutations                 |    Ja   |   Ja   |      |      |
| CCN Class                 |    Ja   |   Ja   |      |      |
| CCN Methods               |    Ja   |   Ja   |      |      |
| Spotbugs               |    Ja   |   Ja   |      |      |
| Externe Tests             |    Ja   |  Ja    |      |       |


### Version3.12 / Berlin
| Art der Testung           | Phillip   | Severin   | Teresa   | Tobias    |
| ---                       | ---       | ---       | ---       | ---       |
| JavaDoc                   |    Ja   |   Ja |     |      |
| PMD                       |    Ja   |   Ja   |      |      |
| Checkstyle                |    Ja   |   Ja   |      |      |
| IntelliJ Line Coverage    |    Ja   |   Ja   |      |      |
| Mutations                 |    Ja   |   Ja   |      |      |
| CCN Class                 |    Ja   |   Ja   |      |      |
| CCN Methods               |    Ja   |   Ja   |      |      |
| Spotbugs               |    Ja   |   Ja   |      |      |
| Externe Tests             |    Ja   |  Ja    |      |       |

### Version4.12 / Pikachu
| Art der Testung           | Phillip   | Severin   | Teresa   | Tobias    |
| ---                       | ---       | ---       | ---       | ---       |
| JavaDoc                   |    Ja   |   Ja |     |      |
| PMD                       |    Ja   |   Ja   |      |      |
| Checkstyle                |    Ja   |   Ja   |      |      |
| IntelliJ Line Coverage    |    Ja   |   Ja   |      |      |
| Mutations                 |    Ja   |   Ja   |      |      |
| CCN Class                 |    Ja   |   Ja   |      |      |
| CCN Methods               |    Ja   |   Ja   |      |      |
| Spotbugs               |    Ja   |   Ja   |      |      |
| Externe Tests             |    Ja   |  Ja    |      |       |
