import java.io.File;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo, die Anwendung läuft jetzt noch viel besser");
        UserInput userInput = new UserInput();
        BackupApplication backupApplication = new BackupApplication(new File("./src/main/java/Main.java"), new File("./"));
        backupApplication.backup(userInput.getSource(), userInput.getTarget());
        for (BigInteger b : backupApplication.getHashList()) {
            System.out.println(b);
        }
        //BackupApplication.backup(new File("../../test"), new File("../../backup"));
    }
}
