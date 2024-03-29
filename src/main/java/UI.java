import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class UI implements Observer {
    private final JFrame mainWindow;

    private BackupMode backupMode = BackupMode.NONE;

    private final JTextArea sourceText;
    private final JTextArea targetText;
    private final JButton startBackupButton;
    private final JButton chooseSourceDirectory;
    private final JButton chooseExternalTargetDirectory;
    private final JButton chooseLocalTargetDirectory;
    private final JProgressBar progressBar;
    private final JTextArea backModeInfoBox;
    private final JComboBox<BackupMode> backupModeMenu;
    private final JButton cancelButton;
    private final BackupApplication backUpApplication = new BackupApplication(null, null);
    private SwingWorker backupProgressSwingWorker;


    /**
     * builds the UI window.
     */
    public UI() {
        backUpApplication.setObserver(this);
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        sourceText = createDirectoryText("Chosen source directory:", 60);
        targetText = createDirectoryText("Chosen target directory:", 390);
        startBackupButton = createStartButton();
        chooseSourceDirectory = createDirectoryChooseButton(60, 300, DirectoryType.SOURCE);
        chooseExternalTargetDirectory = createDirectoryChooseButton(660,
                30, DirectoryType.TARGET_EXTERNAL);
        chooseLocalTargetDirectory = createDirectoryChooseButton(390,
                260, DirectoryType.TARGET_INTERNAL);
        backModeInfoBox = createModeInfoBox();
        backupModeMenu = createBackupModeMenu();
        progressBar = createProgressBar();
        cancelButton = createCancelButton();
        createMainWindow();
    }

    /**
     * creates the main window.
     */
    private void createMainWindow() {
        mainWindow.add(startBackupButton);
        mainWindow.add(chooseSourceDirectory);
        mainWindow.add(chooseLocalTargetDirectory);
        mainWindow.add(chooseExternalTargetDirectory);
        mainWindow.add(cancelButton);
        mainWindow.add(backupModeMenu);
        mainWindow.add(backModeInfoBox);
        mainWindow.add(sourceText);
        mainWindow.add(targetText);
        mainWindow.add(progressBar);
        mainWindow.setSize(750, 600);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
    }

    /**
     * creates the 'cancel' button.
     * @return UI.cancelButton
     */
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

    /**
     * creates the progressBar.
     * @return UI.progressBar
     */
    private JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(225, 440, 300, 20);
        return progressBar;
    }

    /**
     * creates the Dropdown menu which is uses to choose a BackupMode. It uses updateBackupModeMenu() to update the
     * text written to UI.backModeInfoBox.
     * @return UI.backupModeMenu
     */
    private JComboBox<BackupMode> createBackupModeMenu() {
        Vector<BackupMode> v = new Vector<>(List.of(BackupMode.NONE, BackupMode.NEW, BackupMode.CONSECUTIVE, BackupMode.UPDATING));

        JComboBox<BackupMode> dropDownMenu = new JComboBox<>(v);
        dropDownMenu.setSize(200, 30);
        dropDownMenu.setBounds(275, 260, 200, 30);
        dropDownMenu.addActionListener(e -> {
            updateBackupModeMenu(dropDownMenu);
            checkIfBackupPossible();
        });
        return dropDownMenu;
    }

    /**
     * updates the text in UI.backModeInfoBox according to the input given to UI.backupModeMenu.
     * @param dropDownMenu UI.backupModeMenu
     */
    private void updateBackupModeMenu(JComboBox<BackupMode> dropDownMenu) {
        String infoText = switch ((BackupMode) Objects.requireNonNull(dropDownMenu.getSelectedItem())) {
            case NONE -> """
                    Choose a Backup mode for more information.""";
            case NEW -> """
                    New Backup:

                    In this mode, a completely new backup of the source
                    directory will be created in the target location.""";
            case CONSECUTIVE -> """
                    Consecutive Backup:

                    In consecutive mode, all of those files in the source
                    directory, which don't exist in the target location
                    or were changed since the last backup will be copied.""";
            case UPDATING -> """
                    Updated Backup:

                    In updated backup mode, additionally to copying
                    non-existing and changed files to the target directory,
                    all of the files not existent in the source directory
                    anymore will be deleted in the target directory.""";
        };
        this.backupMode = (BackupMode) dropDownMenu.getSelectedItem();
        backModeInfoBox.setText(infoText);
    }

    /**
     * creates the infoBox displaying context to the chosen BackupMode.
     * @return UI.backModeInfoBox
     */
    private static JTextArea createModeInfoBox() {
        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setBounds(175, 310, 400, 110);
        infoBox.setText("""

                Choose a Backup mode for more information.""");
        return infoBox;
    }

    /**
     * creates the 'start' button. Calls startBackup().
     * @return UI.startButton
     */
    private JButton createStartButton() {
        JButton startBackup = new JButton("Start Backup");
        startBackup.setBounds(200, 500, 150, 30);
        startBackup.setEnabled(false);
        startBackup.addActionListener(e -> startBackup());
        return startBackup;
    }

    /**
     * method used for starting the Backup. Start the WorkerThread by calling startSwingWorkerThread().
     */
    private void startBackup() {
        backUpApplication.setProgressSize(4096);
        backUpApplication.setSourceDirectorySize(backUpApplication.getDirectorySizeCalculator().calculateSize(
                backUpApplication.getSourceRootFile().toPath(), backUpApplication.getDirectorySizeCalculator()));
        if (backupMode == BackupMode.NEW) {
            String newDirectoryName = JOptionPane.showInputDialog(null,
                    "Choose a name for the new backup directory",
                    ("Backup" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm"))));
            if (newDirectoryName != null) {
                startSwingWorkerThread(newDirectoryName);
            }
        } else if (backupMode == BackupMode.UPDATING) {
            if (showUpdatingModeWarning()) {
                startSwingWorkerThread(null);
            }
        } else {
            startSwingWorkerThread(null);
        }
    }

    /**
     * shows a warning dialog if the "update" mode is chosen
     * @return Yes/No option
     */
    private boolean showUpdatingModeWarning() {
        Object[] options = {"OK", "Cancel"};
        int input = JOptionPane.showOptionDialog(null,
                """
                        This will delete all files in the target directory,
                        which are not present in the source directory.

                        If there are any files in the target directory which
                        should not be deleted, safe them somewhere else.

                        Are you sure you want to continue?
                        """, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return input == JOptionPane.YES_OPTION;
    }

    /**
     * starts a WorkerThread on which runs the backup.
     * @param newDirectoryName the name of the new backup folder chosen by the user
     *                         in case backUpMode is BackUpMode.NEW
     */
    private void startSwingWorkerThread(String newDirectoryName) {
        backupProgressSwingWorker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                System.out.println("starting backup in '" + backupMode + "' mode.");
                switch (backupMode) {
                    case NEW -> backUpApplication.newBackup(newDirectoryName);
                    case CONSECUTIVE -> backUpApplication.consecutiveBackup();
                    case UPDATING -> backUpApplication.updatedBackup();
                }
                return true;
            }

            @Override
            protected void done() {
                if (!backupProgressSwingWorker.isCancelled()) {
                    System.out.println("backup finished.");
                    JOptionPane.showMessageDialog(null, "The backup is done.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        backupProgressSwingWorker.execute();
    }

    /**
     * creates the DirectoryChooseButtons. Calls openFileChooser().
     * @param xPosition     vertical position
     * @param width         the button's width
     * @param directoryType the directoryType is used to differentiate between the different DirectoryChooseButtons
     * @return UI.chooseSourceDirectory, UI.chooseExternalTargetDirectory or UI.chooseLocalTargetDirectory
     */
    private JButton createDirectoryChooseButton(int xPosition, int width, DirectoryType directoryType) {
        int yPosition = 45;

        JButton button;
        if (directoryType == DirectoryType.TARGET_EXTERNAL) {
            //Path imagePath = Paths.get("../../../src/resources/USB vertical.png").toAbsolutePath(); // for compiler
            Path imagePath = Paths.get("src/resources/USB vertical.png").toAbsolutePath();
            System.out.println(imagePath);
            ImageIcon usbStick = new ImageIcon((imagePath.toString()));
            button = new JButton(usbStick);
            button.setToolTipText("choose external directory");
        } else {
            String buttonText = switch (directoryType) {
                case SOURCE -> "Choose source directory";
                case TARGET_INTERNAL -> "Choose target directory";
                default -> "";
            };
            button = new JButton(buttonText);
        }
        button.setBounds(xPosition, yPosition, width, 30);

        button.addActionListener((e) -> {
            openFileChooser(directoryType);
            checkIfBackupPossible();
        });

        return button;
    }

    /**
     * opens a JFileChooser in order to allow the user to choose source and target directories.
     * @param directoryType the directoryType is used to differentiate between the different DirectoryChooseButtons
     */
    private void openFileChooser(DirectoryType directoryType) {
        File file = null;
        if (directoryType.equals(DirectoryType.TARGET_EXTERNAL)) {
            String userHome = System.getProperty("user.home");
            file = new File(userHome + "/../../media");

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
    }

    /**
     * creates the JTextAreas where the chosen directories get displayed.
     * @param text the path of the chosen directory
     * @param xPosition vertical position
     * @return UI.sourceText or UI.targetText
     */
    private static JTextArea createDirectoryText(String text, int xPosition) {
        JTextArea directoryText = new JTextArea(text);
        directoryText.setBounds(xPosition, 130, 300, 100);
        directoryText.setEditable(false);
        return directoryText;
    }

    /**
     * checks whether a target directory, a source directory and a backup mode is chosen.
     */
    public void checkIfBackupPossible() {
        startBackupButton.setEnabled(this.backupMode != BackupMode.NONE && backUpApplication.getSourceRootFile() != null
                && backUpApplication.getTargetRootFile() != null);
    }

    /**
     * updates progressBar's value.
     * @param backupApplication the instance of BackupApplication
     */
    @Override
    public void update(BackupApplication backupApplication) {
        int progress = (int) (((double) backupApplication.getProgressSize()
                / (double) backupApplication.getSourceDirectorySize()) * 100);
        progressBar.setValue(progress);
    }

    public static void main(String[] args) {
        new UI();
    }

}
