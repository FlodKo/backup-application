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
- 