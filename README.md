# backup-application
Backup Application in Java



# To-Dos: (13.5.)
- progressBar implementieren
- iwie tracken, wann das backup fertig ist und reaktion auf dem Fenster zeigen
- Main.java und UserInput.java können weg -> done
- Fehlervermeidung einbauen, z.B. wenn kein directory gewählt ist -> done
- die ganzen warnings in BackupApplication angucken
- kann isInDirectory() (in BackupApplication) weg?
- Ui:
  - Fehlervermeidung für wenn oben nichts ausgewählt ist -> Start button ausgrauen? -> Done
  - alten Eintrag löschen, wenn ne neue file gewählt wird -> done 

# To-Dos: (19.6.)
- Pop-Up wenn Backup fertig
- Schönheitssachen
  - nachfragefenster nur ein nein button
  - sachen im drop-down menü nicht capslock
- Progressbar richtig implementieren
- JavaDocs schreiben
- Name für neues Backup wählen lassen?
- Externe medien sachen → einfach weiterer Button mit /media/... als ausgangspunkt?
- Main schreiben




# Was wir brauchen
- BackupApplication
  - Ausgabe, was geändert wurde
- Klasse für Umbenennung (statisch)
- Klasse für Hash (muss das gespeichert werden? sonst statisch)

# Konflikte, die auftreten können
- Dateien wurden in einen Ordner im gleichen Directory verschoben
→ Dann wird überprüft, ob der Hash der datei in den backup hashes mit drinnen ist 
und wenn nicht, wird gefragt, ob die datei gelöscht werden soll


Es gibt drei Modi:
- consecutive Backup: es wird ein Directory gegeben, an den das Backup geschrieben werden soll, schon vorhandene Dateien
  werden nicht kopiert
- updated Backup: es wird ein Directory gegeben, an den das Backup geschrieben werden soll, schon vorhandene Dateien
  werden nicht kopiert, nicht mehr vorhandene Dateien gelöscht
- new Backup: einfach n neues Backup an nem neuen Ort
- 