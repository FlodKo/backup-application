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
            File newEntry = new File(targetDirectory.toPath().resolve(Path.of(file.getName(),"")).toUri());
            if (!file.isDirectory()) {
                copySingleFile(file, newEntry);
            } else {
                if (!targetDirectory.isDirectory()){
                    targetDirectory.delete(); //TODO hier Umgang mit Fehler einbauen, wenn eine Datei im Zielordner
                                              //TODO existieren sollte, die den gleichen Namen hat
                }
                if (!targetDirectory.exists()) {
                    targetDirectory.mkdir();
                }
                newEntry.mkdir();
                backup(file, newEntry);
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
