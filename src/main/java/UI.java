import javax.swing.*;
import java.nio.file.Path;

public class UI {
    JFrame mainWindow;
    private Path sourcePath = null; // TODO: muss hier Datentyp File statt Path hin?
    private Path targetPath = null;

    JTextArea srcText;
    JTextArea targetText;
    JButton chooseSourceDirectory;
    JButton chooseTargetDirectory;
    JProgressBar progressBar;
    JButton startBackup;
    JTextArea infoBox;
    JComboBox<String> dropDownMenu;
    JButton cancel;
    boolean sourceChosen = false;
    boolean targetChosen = false;
    boolean modeChosen = false;


    private void createSRCText() {

    }

    /**
     * builds the UI window
     */
    public UI() {
        // the whole window
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        srcText = createDirectoryText("chosen source Directory:", 60);

        // the textarea below the button to choose the target directory. it will display the chosen one
        targetText = createDirectoryText("chosen target Directory:", 390);

        // button to choose the source directory
        chooseSourceDirectory = createDirectoryChooseButton("Choose Source Directory", 100, srcText, "src");

        // button to choose the target directory
        chooseTargetDirectory = createDirectoryChooseButton("Choose Target Directory", 400, targetText, "target");

        // textArea which displays more info about the backup mode
        infoBox = createModeInfoBox();

        // button to start the backup. is currently not completely implemented
        startBackup = createStartButton();

        // dropdown menu to choose the backup mode
        dropDownMenu = createDropDownMenu();

        // progress bar
        progressBar = createProgressBar();

        cancel = createCancelButton();

        createMainWindow();


    }

    private void createMainWindow() {
        mainWindow.add(startBackup);
        mainWindow.add(cancel);
        mainWindow.add(dropDownMenu);
        mainWindow.add(infoBox);
        mainWindow.add(chooseSourceDirectory);
        mainWindow.add(chooseTargetDirectory);
        mainWindow.add(srcText);
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

    private JComboBox<String> createDropDownMenu() {
        JComboBox<String> dropDownMenu = new JComboBox<>(new String[]{"choose Backup Mode",
                "new Backup", "consecutive Backup", "updated Backup"}); //TODO: standard ändern
        dropDownMenu.setSize(200, 30);
        dropDownMenu.setBounds(275, 200, 200, 30);
        dropDownMenu.addActionListener(e -> {
            String infoText = "";
            switch (dropDownMenu.getSelectedIndex()) {
                case 0 -> modeChosen = false;
                case 1 -> infoText = """
                        New Backup:\s

                        In this mode, a completely new backup of the source
                        directory will be created in the target location.""";
                case 2 -> infoText = """
                        Consecutive Backup:\s

                        In consecutive mode, all of those files in the source
                        directory, which don't exist in the target location
                        or were changed since the last backup will be copied.""";
                case 3 -> infoText = """
                        Updated Backup:
                                                    
                        In updated backup mode, additionally to copying
                        non-existing and changed files to the target directory,
                        all of the files not existing in the source directory
                        anymore will be deleted in the target directory.""";
            }
            if (dropDownMenu.getSelectedIndex() != 0) {
                modeChosen = true;
            }
            infoBox.setText(infoText);
            backupPossible();
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
            BackupApplication backupApplication = new BackupApplication(sourcePath.toFile(), targetPath.toFile());
            switch (dropDownMenu.getSelectedIndex()) {
                case 1 -> backupApplication.newBackup();
                case 2 -> backupApplication.consecutiveBackup();
                case 3 -> backupApplication.updatedBackup();
            }
            fill();
        });
        return startBackup;
    }

    private JButton createDirectoryChooseButton(String buttonText, int xPosition, JTextArea srcText, String type) {
        JButton chooseSourceDirectory = new JButton(buttonText); // TODO: in dropdown menü ändern?
        chooseSourceDirectory.setBounds(xPosition, 30, 250, 30);
        chooseSourceDirectory.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (type.equals("src")) {
                srcText.setText("chosen source Directory:");
            } else {
                targetText.setText("chosen target Directory:");
            }
            int state = fileChooser.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                srcText.append("\n" + printPath(fileChooser.getSelectedFile().toPath()));
                if (type.equals("src")) {
                    sourceChosen = true;
                } else {
                    targetChosen = true;
                }
            } else {
                if (type.equals("src")) {
                    sourceChosen = false;
                } else {
                    targetChosen = false;
                }
            }
            backupPossible();
        });
        return chooseSourceDirectory;
    }

    private static JTextArea createDirectoryText(String text, int x) {
        // the textarea below the button to choose the source directory. it will display the chosen one
        JTextArea directoryText = new JTextArea(text);
        directoryText.setBounds(x, 75, 300, 100);
        directoryText.setEditable(false);
        return directoryText;
    }

    /**
     * fills progressBar, not yet implemented //
     */
    //TODO:
    // Ideen für Implementierung:
    // - Prozentzahl an Anzahl von Dateien festmachen? An der Größe?
    // - Phasen des backups als String reinschreiben? Also Scanning..., Copying Files..., Deleting Files... ?

    public void fill() {
        double i = 0;
        boolean running = true;
        while (/*running*/ i < 100) {
            i += 0.000001;
            progressBar.setValue((int) i);
        }
    }

    public void backupPossible() {
        startBackup.setEnabled(modeChosen && targetChosen && sourceChosen);
    }

    /**
     * reformats the given path so it fits in the textBox
     * @param path the given path
     * @return the formatted String
     */
    public String printPath(Path path) {
        String[] splitPath = path.toString().split("(?<=/)");
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder eachLine = new StringBuilder();
        int i = 0;
        while (i < splitPath.length) {
            while (i < splitPath.length && (splitPath[i].length() > 45 || eachLine.length() + splitPath[i].length() < 45)){
                eachLine.append(splitPath[i]);
                i++;
            }
            stringBuilder.append(eachLine).append("\n");
            eachLine.setLength(0);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        new UI();
    }
}
