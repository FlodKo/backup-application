import javax.swing.*;
import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class UI implements Observer {
    JFrame mainWindow;

    private BackupMode backupMode = BackupMode.NONE;

    JTextArea sourceText;
    JTextArea targetText;
    JButton startBackupButton;
    JButton chooseSourceDirectory;
    JButton chooseExternalTargetDirectory;
    JButton chooseLocalTargetDirectory;
    JProgressBar progressBar;
    JTextArea backModeInfoBox;
    JComboBox<BackupMode> chooseBackupMode;
    JButton cancelButton;
    BackupApplication backUpApplication = new BackupApplication(null, null);
    SwingWorker backupProgressSwingWorker;


    /**
     * builds the UI window
     */
    public UI() {
        backUpApplication.setObserver(this);
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        sourceText = createDirectoryText("Chosen source directory:", 60);
        targetText = createDirectoryText("Chosen target directory:", 390);
        startBackupButton = createStartButton();
        chooseSourceDirectory = createDirectoryChooseButton(60, 30, DirectoryType.SOURCE);
        chooseExternalTargetDirectory = createDirectoryChooseButton(390, 30, DirectoryType.TARGET_EXTERNAL);
        chooseLocalTargetDirectory = createDirectoryChooseButton(390, 70, DirectoryType.TARGET_INTERNAL);
        backModeInfoBox = createModeInfoBox();
        chooseBackupMode = createBackupModeMenu();
        progressBar = createProgressBar();
        cancelButton = createCancelButton();
        createMainWindow();
    }

    private void createMainWindow() {
        mainWindow.add(startBackupButton);
        mainWindow.add(chooseSourceDirectory);
        mainWindow.add(chooseLocalTargetDirectory);
        mainWindow.add(chooseExternalTargetDirectory);
        mainWindow.add(cancelButton);
        mainWindow.add(chooseBackupMode);
        mainWindow.add(backModeInfoBox);
        mainWindow.add(sourceText);
        mainWindow.add(targetText);
        mainWindow.add(progressBar);
        mainWindow.setSize(750, 600);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
    }

    private JButton createCancelButton() {
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(400, 500, 150, 30);
        cancel.addActionListener(e -> {
            if (backupProgressSwingWorker != null) {
                backupProgressSwingWorker.cancel(true);
            }
            mainWindow.dispose();
        });
        return cancel;
    }

    private JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(225, 440, 300, 20);
        return progressBar;
    }

    private JComboBox<BackupMode> createBackupModeMenu() {
        Vector<BackupMode> v = new Vector<>(List.of(BackupMode.NONE, BackupMode.NEW, BackupMode.CONSECUTIVE, BackupMode.UPDATING));

        JComboBox<BackupMode> dropDownMenu = new JComboBox<>(v);
        dropDownMenu.setSize(200, 30);
        dropDownMenu.setBounds(275, 260, 200, 30);
        dropDownMenu.addActionListener(e -> {
            String infoText = "";
            switch ((BackupMode) Objects.requireNonNull(dropDownMenu.getSelectedItem())) {
                case NONE -> infoText = """
                                                
                        Choose a Backup mode for more information.""";
                case NEW -> infoText = """
                        New Backup:\s

                        In this mode, a completely new backup of the source
                        directory will be created in the target location.""";
                case CONSECUTIVE -> infoText = """
                        Consecutive Backup:\s

                        In consecutive mode, all of those files in the source
                        directory, which don't exist in the target location
                        or were changed since the last backup will be copied.""";
                case UPDATING -> infoText = """
                        Updated Backup:
                                                    
                        In updated backup mode, additionally to copying
                        non-existing and changed files to the target directory,
                        all of the files not existing in the source directory
                        anymore will be deleted in the target directory.""";
            }
            this.backupMode = (BackupMode) dropDownMenu.getSelectedItem();
            backModeInfoBox.setText(infoText);
            checkIfBackupPossible();
        });
        return dropDownMenu;
    }

    private static JTextArea createModeInfoBox() {
        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setBounds(175, 310, 400, 110);
        infoBox.setText("""
                                        
                Choose a Backup mode for more information.""");
        return infoBox;
    }

    private JButton createStartButton() {
        JButton startBackup = new JButton("Start Backup");
        startBackup.setBounds(200, 500, 150, 30);
        startBackup.setEnabled(false);
        startBackup.setToolTipText("Start the backup");
        startBackup.addActionListener(e -> {
            backUpApplication.setProgressSize(4096);
            backUpApplication.setSourceDirectorySize(backUpApplication.getDirectorySizeCalculator().calculateSize
                    (backUpApplication.getSourceRootFile().toPath(), backUpApplication.getDirectorySizeCalculator()));
            if (backupMode == BackupMode.NEW) {
                String newDirectoryName = JOptionPane.showInputDialog(null,
                        "Choose a name for the new backup directory",
                        ("Backup" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm"))));
                if (newDirectoryName != null) {
                    startSwingWorkerThread(this, newDirectoryName);
                }
            } else {
                startSwingWorkerThread(this, null);
            }
        });
        return startBackup;
    }

    private JButton createDirectoryChooseButton(int xPosition, int yPosition, DirectoryType directoryType) {
        String buttonText = switch (directoryType) {
            case SOURCE -> "Choose source directory";
            case TARGET_EXTERNAL -> "Choose external target directory";
            default -> "Choose internal target directory";
        };
        JButton button = new JButton(buttonText);
        button.setBounds(xPosition, yPosition, 300, 30);
        button.addActionListener((e) -> {
            File file = null;
            if (directoryType.equals(DirectoryType.TARGET_EXTERNAL)) {
                String userHome = System.getProperty("user.home");
                file = new java.io.File(userHome + "/../../media");

            }
            JFileChooser fileChooser = new JFileChooser(file);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (directoryType.equals(DirectoryType.SOURCE)) {
                    this.sourceText.setText("Chosen source directory:\n" + StringUtil.printPath(fileChooser.getSelectedFile().toPath()));
                    backUpApplication.setSourceRootFile(fileChooser.getSelectedFile());
                } else if (directoryType.equals(DirectoryType.TARGET_INTERNAL)) {
                    this.targetText.setText("Chosen target directory:\n" + StringUtil.printPath(fileChooser.getSelectedFile().toPath()));
                    backUpApplication.setTargetRootFile(fileChooser.getSelectedFile());
                } else {
                    this.targetText.setText("Chosen target directory:\n" + StringUtil.printPath(fileChooser.getSelectedFile().toPath()));
                    backUpApplication.setTargetRootFile(fileChooser.getSelectedFile());
                }
            }
            checkIfBackupPossible();
        });

        return button;
    }

    private static JTextArea createDirectoryText(String text, int x) {
        // the textarea below the button to choose the source directory. it will display the chosen one
        JTextArea directoryText = new JTextArea(text);
        directoryText.setBounds(x, 130, 300, 100);
        directoryText.setEditable(false);
        return directoryText;
    }

    public void checkIfBackupPossible() {
        startBackupButton.setEnabled(this.backupMode != BackupMode.NONE && backUpApplication.getSourceRootFile() != null
                && backUpApplication.getTargetRootFile() != null);
    }

    private void startSwingWorkerThread(UI ui, String newDirectoryName) {
         backupProgressSwingWorker = new SwingWorker() {
            @Override
            protected Boolean doInBackground() {
                switch (ui.getBackupMode()) {
                    case NEW -> backUpApplication.newBackup(newDirectoryName);
                    case CONSECUTIVE -> backUpApplication.consecutiveBackup();
                    case UPDATING -> {
                        Object[] options = {"OK", "Cancel"};
                        int input = JOptionPane.showOptionDialog(null,
                                """
                                        This will delete all files in the target directory,
                                        that are not present in the source directory.
                                                            
                                        If you have any files in the target directory that
                                        should not be deleted, safe them somewhere else.
                                                            
                                        Are you sure you want to continue?
                                        """, "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        if (input == JOptionPane.YES_OPTION) {
                            backUpApplication.updatedBackup();
                        }
                    }
                }
                return true;
            }

            @Override
            protected void done() {
                if (!backupProgressSwingWorker.isCancelled()) {
                    JOptionPane.showMessageDialog(null, "The backup is done.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        backupProgressSwingWorker.execute();
    }

    @Override
    public void update(BackupApplication backupApplication) {
        int progress = (int) (((double) backupApplication.getProgressSize() /
                (double) backupApplication.getSourceDirectorySize()) * 100);
        progressBar.setValue(progress);
    }

    public BackupMode getBackupMode() {
        return backupMode;
    }

    public static void main(String[] args) {
        new UI();
    }

}
