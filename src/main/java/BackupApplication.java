import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class BackupApplication {
    private File sourceFile;
    private File targetFile;
    private DirectoryContainer sourceContainer;
    private DirectoryContainer targetContainer;

    public BackupApplication(File sourceDirectory, File targetDirectory) {
        assert !sourceDirectory.isDirectory() : "sourceDirectory is not a directory.";
        assert !targetDirectory.isDirectory() : "targetDirectory is not a directory.";
        this.sourceFile = sourceDirectory;
        this.targetFile = targetDirectory;
    }

    public static void generateLists(File directory, DirectoryContainer container) {
        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                FileContainer newFileContainer = new FileContainer(file);
                container.getFiles().add(newFileContainer);
            } else {
                DirectoryContainer newContainer = new DirectoryContainer(file);
                container.getFiles().add(newContainer);
                generateLists(file, newContainer);
            }
        }


    }

    public static void printList(ArrayList<Object> list, int indent) {
        for (Object object: list) {
            try {
                File file = (File)object;
                for (int i = 0; i < indent; i++) {
                    System.out.print("   ");
                                    }
                System.out.println(file.getName());
            }
            catch (ClassCastException e) {
                System.out.println("-folder-");
                printList((ArrayList<Object>) object, indent+1);
            }
        }
    }

    public void compareFile() {

    }
/*
    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }

    public ArrayList<Object> getSourceDirectoryList() {
        return sourceDirectoryList;
    }

    public ArrayList<Object> getTargetDirectoryList() {
        return targetDirectoryList;
    }
*/
    public static void main(String[] args) {
        File src = new File(args[0]);
        DirectoryContainer list = new DirectoryContainer(src);
        generateLists(src ,list);
        //printList(list,0);
        System.out.println("bla");
    }
}
