import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ProgressBarThread extends Thread {
    private final JProgressBar progressBar;
    private BackupApplication backupApplication = null;
    public ProgressBarThread(JProgressBar progressBar, BackupApplication backupApplication) {
        this.progressBar = progressBar;
        this.backupApplication = backupApplication;
    }
    public void run() {
        int progress = 0;
        while (progress <= 100) {
            progress = (int) (((double)backupApplication.getProgressSize()/(double) backupApplication.getSourceDirectorySize())*100);
            try {
                int finalProgress = progress;
                SwingUtilities.invokeAndWait(() -> progressBar.setValue(finalProgress));
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Exception thrown while invoking progressBar.setValue().");
            }
        }
    }
}
