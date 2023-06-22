import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BackupApplication {
    private File sourceRootFile;
    private File targetRootFile;

    private BackupMode backupMode;


    public BackupApplication(File sourceRootFile, File targetRootFile) {
        this.sourceRootFile = sourceRootFile;
        this.targetRootFile = targetRootFile;
    }

    /**
     * method for executing the 'newBackup' mode
     */
    public void newBackup() {
        File backupDirectory = this.targetRootFile.toPath().resolve("Backup" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss"))).toFile();
        backupDirectory.mkdir();
        backup(this.sourceRootFile, backupDirectory);
    }

    /**
     * method for executing the 'consecutiveBackup' mode
     */
    public void consecutiveBackup() {
        backup(this.sourceRootFile, this.targetRootFile);
    }

    /**
     * method for executing the 'updatedBackup' mode
     */
    public void updatedBackup()  {
        backup(this.sourceRootFile, this.targetRootFile);
        cleanUp(this.sourceRootFile, this.targetRootFile);
    }

    /**
     * this method executes the basic backup. All the files will be copied using copySingleFile().
     *
     * @param sourceFile source Directory
     * @param targetFile target Directory
     */
    public void backup(File sourceFile, File targetFile) {
        if (sourceFile.listFiles().length == 0) {
            return;
        }
        for (File file : sourceFile.listFiles()) {
            File newEntry = new File(targetFile.toPath().resolve(Path.of(file.getName(), "")).toUri());
            if (!file.isDirectory()) {
                copySingleFile(file, newEntry);
            } else {
                if (!targetFile.isDirectory()) {
                    targetFile.delete(); //TODO hier Umgang mit Fehler einbauen, wenn eine Datei im Zielordner
                    //TODO existieren sollte, die den gleichen Namen hat
                }
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                newEntry.mkdir();
                backup(file, newEntry);
            }
        }
    }

    /**
     * this method copies a single file. If an identical file already exists in the targetDirectory, it will not be copied
     *
     * @param file            th file to be copied
     * @param targetDirectory the directory the files is copied to
     */
    private void copySingleFile(File file, File targetDirectory) {
        try {
            if (!targetDirectory.exists()) {
                targetDirectory.createNewFile();
            }
            if (!FileUtil.compareFiles(file, targetDirectory)) {
                InputStream in = new FileInputStream((file));
                OutputStream out = new FileOutputStream(targetDirectory);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            targetDirectory.delete();
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this method cleans the target directory for the 'updated backup' mode. To achieve this, the method compares the
     * target directory (after the backup) with the source Directory and deletes everything not found in source
     *
     * @param targetDirectory the target Directory
     * @param sourceDirectory the source Directory
     */
    public void cleanUp(File sourceDirectory, File targetDirectory){
        try {
            Files.walkFileTree(sourceDirectory.toPath(), new CleanupFileVisitor(sourceDirectory.toPath(), targetDirectory.toPath()));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public File getSourceRootFile() {
        return sourceRootFile;
    }

    public File getTargetRootFile() {
        return targetRootFile;
    }
}
