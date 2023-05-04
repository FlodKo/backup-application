import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserInput {
    private File source;
    private File target;
    Scanner pathInput = new Scanner(System.in);

    public UserInput() {
        this.source = getPathInput(0);
        this.target = getPathInput(1);
    }

    public File getPathInput(int state) {
        if (state <= 0) {System.out.println("Hello to this backup programm.");}
        System.out.println("Please enter the Path of the " + (state==0 ? "source":"target") + " directory:");
        File enteredFile = null;
        while (enteredFile == null) {
            String input = pathInput.nextLine();
            try {
                enteredFile = new File(input);
                if (!enteredFile.exists()) {
                    throw new FileNotFoundException();
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
