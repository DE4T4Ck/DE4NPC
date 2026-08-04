# DE4SpawnNPC 0.1

Ein eigenständiges NPC-Plugin für **Paper 1.21.11** und **Java 21**. Es erstellt geschützte Villager-Figuren am Spawn, die beim Rechtsklick einen konfigurierbaren Spielerbefehl ausführen. Citizens oder ein anderes NPC-Plugin wird nicht benötigt.

## Funktionen

- Unbewegliche, lautlose und unverwundbare Spawn-Figuren
- Dauerhafte Speicherung in `plugins/DE4SpawnNPC/npcs.yml`
- Beliebige Spielerbefehle als Klickaktion
- Automatische Berufe für RTP, Shop und Auktionshaus
- Farbig formatierbare Namen
- Erstellen, Löschen, Auflisten, Teleportieren und Neuladen per Befehl
- Kompatibel mit `/rtp` und `/shop` aus DE4Core sowie `/ah` und `/baltop` aus DE4Market

## Build und Installation

Mit Java 21 und Maven kann das Plugin gebaut werden:

```bash
mvn clean package
```

Die fertige Datei heißt `target/DE4SpawnNPC-0.1.jar`. Alternativ erstellt der enthaltene GitHub-Workflow die JAR automatisch. Anschließend die JAR in den `plugins`-Ordner legen und den Paper-Server vollständig neu starten.

## Figuren erstellen

Stelle dich genau an den Ort und schaue in die Richtung, in der die Figur erscheinen soll. Benutze dann beispielsweise:

```text
/de4npc create rtp rtp &a&lZufälliger Teleport
/de4npc create shop shop &6&lServer-Shop
/de4npc create auktion ah &e&lAuktionshaus
/de4npc create spawn spawn &b&lZurück zum Spawn
/de4npc create baltop baltop &6&lReichste Spieler
/de4npc create bounties bounties &c&lKopfgelder
```

Syntax:

```text
/de4npc create <ID> <Befehl> <Anzeigename>
```

Der Befehl wird ohne `/` angegeben. Er läuft als der Spieler, der die Figur anklickt, und beachtet deshalb dessen Rechte.

## Verwaltung

```text
/de4npc list
/de4npc delete <ID>
/de4npc tp <ID>
/de4npc reload
```

Zum Verwalten wird `de4spawnnpc.admin` benötigt; diese Berechtigung besitzen standardmäßig nur Server-Operatoren.

## Aussehen ändern

In `plugins/DE4SpawnNPC/npcs.yml` können `profession` und `type` angepasst werden. Mögliche Typen sind `PLAINS`, `DESERT`, `JUNGLE`, `SAVANNA`, `SNOW`, `SWAMP` und `TAIGA`. Anschließend `/de4npc reload` ausführen.
