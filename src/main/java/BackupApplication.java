import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BackupApplication {
    private File sourceFile;
    private File targetFile;


    public BackupApplication(File sourceFile, File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;

    }
    public void backup (File sourceDirectory, File targetDirectory) {
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

    private void copySingleFile(File file, File targetDirectory)  {
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
     * @param targetDirectory the target Directory
     * @param sourceDirectory the source Directory
     */
    public void cleanUp (File sourceDirectory, File targetDirectory) {
        File[] sourceDirectoryArray = sourceDirectory.listFiles();
        File[] targetDirectoryArray = targetDirectory.listFiles();
        if (sourceDirectoryArray.length < targetDirectoryArray.length) {
            for (int i = 0; i < sourceDirectoryArray.length; i++) {
                for (File file: targetDirectoryArray) {
                    if (!file.getName().equals(sourceDirectoryArray[i].getName())) {
                        file.delete();
                    }
                }
                if (targetDirectoryArray[i].isDirectory()) { // muss hier überprüft werden, ob sourceDirectoryArray[i] auch ein directory ist?
                    for (File sourceFile: sourceDirectoryArray) {
                        if (sourceFile.getName().equals(targetDirectoryArray[i].getName())) {
                            cleanUp(sourceFile, targetDirectoryArray[i]);
                        }
                    }
                }
            }
        }
        for (File targetFile:targetDirectoryArray) {
            if (targetFile.isDirectory()) {
                for (File sourceFile: sourceDirectoryArray) {
                    if (sourceFile.getName().equals(targetFile.getName())) {
                        cleanUp(sourceFile, targetFile);
                    }
                }
            }
        }
    }

    private boolean isInDirectory(File file, File directory) {
        for (File f: directory.listFiles()) {
            if (!f.isDirectory()) {
                if (FileUtil.generateHash(f).equals(FileUtil.generateHash(file))) {
                    return true;
                }
            } else {
                isInDirectory(file, f);
            }
        }
        return false;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }
}
