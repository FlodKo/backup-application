import javax.swing.*;

public class ProgressBarThread extends Thread {
    private final JProgressBar progressBar;
    public ProgressBarThread(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public void run() {
        int progress = 0;
        while (progress <= 100) {
            progress = (int) (BackupApplication.getProgressSize()/BackupApplication.getSourceDirectorySize());
            progressBar.setValue(progress);
        }
    }
}
