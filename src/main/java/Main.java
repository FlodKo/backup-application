import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo, die Anwendung läuft jetzt noch viel besser");
        UserInput userInput = new UserInput();
        BackupApplication.backup(userInput.getSource(), userInput.getTarget());
        //BackupApplication backupApplication = new BackupApplication(new File("./src/main/java/Main.java"), new File("./"));
        //BackupApplication.backup(new File("../../test"), new File("../../backup"));
    }
}
