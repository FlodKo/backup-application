import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BackupApplication {
    private File sourceFile;
    private File targetFile;
    private ArrayList<BigInteger> hashList = new ArrayList<>();

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

    private void copySingleFile(File file, File destinationDirectory)  {
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
        BigInteger hash = FileUtil.generateHash(file);
        if (hash != null) {
            hashList.add(hash);
        }
    }

    public void cleanUp (File destinationDirectory) {
        for (File file : destinationDirectory.listFiles()) {
          if (isInArrayList(FileUtil.generateHash(file))) {
              file.delete();
          }
        }
    }

    public ArrayList<BigInteger> getHashList() {
        return hashList;
    }

    private boolean isInArrayList(BigInteger hash) {
        for (BigInteger b: getHashList()) {
            if (hash.equals(b)) {
                return true;
            }
        }
        return false;
    }
}
