import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo, die Anwendung läuft jetzt noch viel besser");
        //BackupApplication backupApplication = new BackupApplication(new File("./src/main/java/Main.java"), new File("./"));
        BackupApplication.copySingleFile(new File("./src/main/java/Main.java"), new File("./backup"));

    }


}
