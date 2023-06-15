import javax.swing.*;

public class ProgressBarThread extends Thread {
    private final JProgressBar progressBar;
    public ProgressBarThread(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public void run() {
        double i = 0;
        boolean running = true;
        while (/*running*/ i < 100) {
            i += 0.000001;
            progressBar.setValue((int) i);
        }
    }
}
