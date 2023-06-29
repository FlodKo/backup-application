import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ProgressBarThread extends Thread {
    private final JProgressBar progressBar;
    private final BackupApplication backupApplication;
    public ProgressBarThread(JProgressBar progressBar, BackupApplication backupApplication) {
        this.progressBar = progressBar;
        this.backupApplication = backupApplication;
    }

    /**
     * in this method is used to update UI.progressbar
     */
    public void run() {
        int progress;
        do {
            progress = (int) (((double)backupApplication.getProgressSize()/
                    (double) backupApplication.getSourceDirectorySize())*100);
            try {
                if (progress > 98) {
                    progress = 100;
                }
                int finalProgress = progress;

                SwingUtilities.invokeAndWait(() -> progressBar.setValue(finalProgress));
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Exception thrown while invoking progressBar.setValue().");
            }
        }
        while (progress < 100);
    }
}
