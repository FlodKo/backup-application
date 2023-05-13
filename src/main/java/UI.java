import javax.swing.*;
import java.nio.file.Path;

public class UI {
    JFrame mainWindow;
    private Path sourcePath = null; // TODO: muss hier Datentyp File statt Path hin?
    private Path targetPath = null;
    JProgressBar progressBar;


    /**
     * builds the UI window
     */
    public UI() {
        // the whole window
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // the textarea below the button to choose the source directory. it will display the chosen one
        JTextArea srcText = new JTextArea("chosen source Directory:");
        srcText.setBounds(60, 75, 300, 100);
        srcText.setEditable(false);

        // the textarea below the button to choose the target directory. it will display the chosen one
        JTextArea targetText = new JTextArea("chosen target Directory:");
        targetText.setBounds(390, 75, 300, 100);
        targetText.setEditable(false);

        // button to choose the source directory
        JButton chooseSourceDirectory = new JButton("Choose Source Directory"); // TODO: in dropdown menü ändern?
        chooseSourceDirectory.setBounds(100, 30, 250, 30);
        chooseSourceDirectory.addActionListener(e -> {
            JFileChooser source = new JFileChooser();
            source.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = source.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                sourcePath = source.getSelectedFile().toPath();
                srcText.append("\n" + printPath(sourcePath));
            }
        });

        // button to choose the target directory
        JButton chooseTargetDirectory = new JButton("Choose Target Directory");
        chooseTargetDirectory.setBounds(400, 30, 250, 30);
        chooseTargetDirectory.addActionListener(e -> {
            JFileChooser target = new JFileChooser();
            target.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = target.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                targetPath = target.getSelectedFile().toPath();
                targetText.append("\n" + printPath(targetPath));
            }
        });

        // textArea which displays more info about the backup mode
        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setBounds(175, 250, 400, 110);

        // dropdown menu to choose the backup mode
        JComboBox<String> dropDownMenu = new JComboBox<>(new String[]{"choose Backup Mode",
                "new Backup", "consecutive Backup", "updated Backup"}); //TODO: standard ändern
        dropDownMenu.setSize(200, 30);
        dropDownMenu.setBounds(275, 200, 200, 30);
        dropDownMenu.addActionListener(e -> {
            String infoText = "";
            switch (dropDownMenu.getSelectedIndex()) {
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
            infoBox.setText(infoText);
        });

        // progress bar
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(225, 400, 300, 20);

        // button to start the backup. is currently not completely implemented
        JButton startBackup = new JButton("start backup");
        startBackup.setBounds(200, 500, 150, 30);
        startBackup.addActionListener(e -> {
            BackupApplication backupApplication = new BackupApplication(sourcePath.toFile(), targetPath.toFile());
            switch (dropDownMenu.getSelectedIndex()) {
                case 1 -> backupApplication.newBackup();
                case 2 -> backupApplication.consecutiveBackup();
                case 3 -> backupApplication.updatedBackup();
            }
            fill();
        });

        // button to close the window. also terminates the program
        JButton cancel = new JButton("cancel");
        cancel.setBounds(400, 500, 150, 30);
        cancel.addActionListener(e -> mainWindow.dispose());

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
