import javax.swing.*;

public class ProgressBarThread extends Thread {
    private final JProgressBar progressBar;
    private BackupApplication backupApplication = null;
    public ProgressBarThread(JProgressBar progressBar, BackupApplication backupApplication) {
        this.progressBar = progressBar;
        this.backupApplication = backupApplication;
    }
    /*public void run() {
        int progress = 0;
        while (progress <= 100) {
            progress = (int) (backupApplication.getProgressSize()/backupApplication.getSourceDirectorySize());
            progressBar.setValue(progress);
        }
    }*/
    public void run() {
        double i = 0;
        while (i < 100) {
            i += 5;
            progressBar.setValue((int) i);
        }
    }
}
