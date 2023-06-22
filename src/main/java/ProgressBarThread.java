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
            progress = (int) (backupApplication.getProgressSize()/backupApplication.getSourceDirectorySize());
            try {
                int finalProgress = progress;
                SwingUtilities.invokeAndWait(() -> progressBar.setValue(finalProgress));
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Exception thrown while invoking progressBar.setValue().");
            }
        }
    }

    /*
    public void run() {
        double i = 0;
        while (i < 100) {
            i += 5;
            progressBar.setValue((int) i);
            try {
                double finalI = i;
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setValue((int) finalI);
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Exception thrown while invoking progressBar.setValue().");
            }
        }
    }*/
}
