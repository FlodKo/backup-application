import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class BackupApplication {

    public void generateLists() {

    }

    public void compareFile() {
        // for test purposes
        File file = new File("./.gitignore");
        File file2 = new File("./README.md");
        try {
            System.out.println(FileUtil.compareFiles(file,file2));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
