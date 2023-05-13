import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo, die Anwendung läuft jetzt noch viel besser");
        //UserInput userInput = new UserInput();
        BackupApplication backupApplication = new BackupApplication(new File("./src"), new File("./backup"));
        backupApplication.backup(backupApplication.getSourceRootFile(), backupApplication.getTargetRootFile());
        backupApplication.cleanUp(backupApplication.getSourceRootFile(), backupApplication.getTargetRootFile());
        //backupApplication.backup(userInput.getSource(), userInput.getTarget());
        //backupApplication.cleanUp(userInput.getSource(), userInput.getTarget());


        //BackupApplication.backup(new File("../../test"), new File("../../backup"));
    }
}
