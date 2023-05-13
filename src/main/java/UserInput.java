import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserInput {
    private final File source;
    private final File target;
    private final BackupMode backupMode;

    private final
    Scanner pathInput = new Scanner(System.in);

    public UserInput() {
        this.backupMode = getBackupMode();
        this.source = getPathInput(0);
        this.target = getPathInput(1);
    }

    private BackupMode getBackupMode(){
        System.out.println("Hello to this backup programm.");
        System.out.println("What backup mode should be used?");
        System.out.println("Enter 1 for a new backup \nEnter two to override an existing backup\nEnter 3 to merge new files to an existing backup.");
        String input = pathInput.nextLine();
        BackupMode backupMode = null;
        while (backupMode == null){
            try{
                int inputNumber = Integer.parseInt(input);
                switch (inputNumber){
                    case 1: backupMode = BackupMode.NEW;
                    case 2 : backupMode = BackupMode.UPDATING;
                    case 3 : backupMode = BackupMode.CONSECUTIVE;
                }
                if (backupMode == null){
                    System.out.println("The entered mode is not valid. Try again.");
                }
            } catch (NumberFormatException e){
                System.out.println("Please enter a number between 1 and 3. Try again");
            }
        }
        return backupMode;
    }

    public File getPathInput(int state) {
        System.out.println("Please enter the Path of the " + (state==0 ? "source":"target") + " directory:");
        File enteredFile = null;
        while (enteredFile == null) {
            String input = pathInput.nextLine();
            try {
                enteredFile = new File(input);
                if (!enteredFile.exists() && state == 0) {
                    throw new FileNotFoundException();
                } else if (!enteredFile.exists() && state == 1){
                    enteredFile.mkdir();
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("The entered Path is faulty, try again:");
                enteredFile = null;
            }
        }
        return enteredFile;
    }

    public File getSource() {
        return source;
    }

    public File getTarget() {
        return target;
    }

}
