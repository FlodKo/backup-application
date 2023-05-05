# backup-application
Backup Application in Java

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