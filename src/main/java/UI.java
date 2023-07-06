import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class UI implements Observer {
    JFrame mainWindow;

    private BackupMode backupMode = BackupMode.NONE;

    JTextArea sourceText;
    JTextArea targetText;
    JButton startBackup;
    JButton chooseSourceDirectory;
    JButton chooseTargetDirectory;
    JProgressBar progressBar;
    JTextArea infoBox;
    JComboBox<BackupMode> dropDownMenu;
    JButton cancel;
    BackupApplication backUpApplication = new BackupApplication(null, null);


    /**
     * builds the UI window
     */
    public UI() {

        backUpApplication.setObserver(this);
        // the whole window
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        sourceText = createDirectoryText("Chosen source directory:", 60);

        // the textarea below the button to choose the target directory. it will display the chosen one
        targetText = createDirectoryText("Chosen target directory:", 390);

        // button to start the backup. is currently not completely implemented
        startBackup = createStartButton();

        // button to choose the source directory
        chooseSourceDirectory = createDirectoryChooseButton(100, DirectoryType.SOURCE);

        // button to choose the target directory
        chooseTargetDirectory = createDirectoryChooseButton(400, DirectoryType.TARGET);

        // textArea which displays more info about the backup mode
        infoBox = createModeInfoBox();

        // dropdown menu to choose the backup mode
        dropDownMenu = createDropDownMenu();

        // progress bar
        progressBar = createProgressBar();

        cancel = createCancelButton();

        createMainWindow();

    }

    private void createMainWindow() {
        mainWindow.add(startBackup);
        mainWindow.add(chooseSourceDirectory);
        mainWindow.add(chooseTargetDirectory);
        mainWindow.add(cancel);
        mainWindow.add(dropDownMenu);
        mainWindow.add(infoBox);
        mainWindow.add(sourceText);
        mainWindow.add(targetText);
        mainWindow.add(progressBar);
        mainWindow.setSize(750, 600);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
    }

    private JButton createCancelButton() {
        JButton cancel = new JButton("cancel");
        cancel.setBounds(400, 500, 150, 30);
        cancel.addActionListener(e -> mainWindow.dispose());
        return cancel;
    }

    private JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(225, 400, 300, 20);
        return progressBar;
    }

    private JComboBox<BackupMode> createDropDownMenu() {
        Vector<BackupMode> v = new Vector<>(List.of(BackupMode.NONE, BackupMode.NEW, BackupMode.CONSECUTIVE, BackupMode.UPDATING));

        JComboBox<BackupMode> dropDownMenu = new JComboBox<>(v);
        dropDownMenu.setSize(200, 30);
        dropDownMenu.setBounds(275, 200, 200, 30);
        dropDownMenu.addActionListener(e -> {
            String infoText = "";
            switch ((BackupMode) Objects.requireNonNull(dropDownMenu.getSelectedItem())) {
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
            infoBox.setText(infoText);
            checkIfBackupPossible();
        });
        return dropDownMenu;
    }

    private static JTextArea createModeInfoBox() {
        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setBounds(175, 250, 400, 110);
        return infoBox;
    }

    private JButton createStartButton() {
        JButton startBackup = new JButton("start backup");
        startBackup.setBounds(200, 500, 150, 30);
        startBackup.setEnabled(false);
        startBackup.addActionListener(e -> {
            startThread(this);
        });
        return startBackup;
    }

    private void startThread(UI ui) {
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Boolean doInBackground() {
                backUpApplication.setProgressSize(4096);
                backUpApplication.setSourceDirectorySize(backUpApplication.getDirectorySizeCalculator().calculateSize
                        (backUpApplication.getSourceRootFile().toPath(), backUpApplication.getDirectorySizeCalculator()));
                switch (ui.getBackupMode()) {
                    case NEW -> backUpApplication.newBackup();
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
        };
        swingWorker.execute();
    }

    private JButton createDirectoryChooseButton(int xPosition, DirectoryType directoryType) {
        JButton button = new JButton(directoryType.equals(DirectoryType.SOURCE) ? "Choose source directory" : "Choose target directory");
        button.setBounds(xPosition, 30, 250, 30);
        button.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (directoryType.equals(DirectoryType.SOURCE)) {
                    this.sourceText.setText("Chosen source directory:\n" + StringUtil.printPath(fileChooser.getSelectedFile().toPath()));
                    backUpApplication.setSourceRootFile(fileChooser.getSelectedFile());
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
        directoryText.setBounds(x, 75, 300, 100);
        directoryText.setEditable(false);
        return directoryText;
    }

    public void checkIfBackupPossible() {
        startBackup.setEnabled(this.backupMode != BackupMode.NONE && backUpApplication.getSourceRootFile() != null
                && backUpApplication.getTargetRootFile() != null);
    }

    public static void main(String[] args) {
        new UI();
    }

    @Override
    public void update(BackupApplication backupApplication) {
        int progress = (int) (((double)backupApplication.getProgressSize()/
                    (double) backupApplication.getSourceDirectorySize())*100);
        progressBar.setValue(progress);
    }

    public BackupMode getBackupMode() {
        return backupMode;
    }
}
