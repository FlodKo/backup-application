import java.io.File;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo, die Anwendung läuft jetzt noch viel besser");
        //UserInput userInput = new UserInput();
        BackupApplication backupApplication = new BackupApplication(new File("./src"), new File("./backup"));
        backupApplication.backup(backupApplication.getSourceFile(), backupApplication.getTargetFile());
        backupApplication.cleanUp(backupApplication.getSourceFile(), backupApplication.getTargetFile());
        //backupApplication.backup(userInput.getSource(), userInput.getTarget());
        //backupApplication.cleanUp(userInput.getSource(), userInput.getTarget());


        //BackupApplication.backup(new File("../../test"), new File("../../backup"));
    }
}
