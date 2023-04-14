import java.io.*;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class BackupApplication {
    private File sourceFile;
    private File targetFile;

    public BackupApplication(File sourceFile, File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }
    public static void backup (File sourceDirectory, File targetDirectory) {
        if (sourceDirectory.listFiles().length == 0) {
            return;
        }
        for (File file : sourceDirectory.listFiles()) {
            File newTargetDirectory = new File(targetDirectory.toPath().resolve(Path.of(file.getName(),"")).toUri());
            if (!file.isDirectory()) {
                copySingleFile(file, newTargetDirectory);
            } else {
                if (!targetDirectory.isDirectory()){
                    targetDirectory.delete();
                }
                if (!targetDirectory.exists()) {
                    targetDirectory.mkdir();
                }
                newTargetDirectory.mkdir();
                backup(file, newTargetDirectory);
            }
        }

    }
    private static void copySingleFile(File file, File destinationDirectory)  {
        try {
            if (!destinationDirectory.exists()) {
                destinationDirectory.createNewFile();
            }
            if (!FileUtil.compareFiles(file, destinationDirectory)) {
                InputStream in = new FileInputStream((file));
                OutputStream out = new FileOutputStream(destinationDirectory);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            destinationDirectory.delete();
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
