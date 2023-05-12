import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class UI {
    JFrame mainWindow;
    private Path sourcePath = null; // TODO: muss hier Datentyp File statt Path hin?
    private Path targetPath = null;
    JProgressBar progressBar;

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public UI() {
        // the whole window
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // the textarea below the button to choose the source directory. it will display the chosen one
        JTextArea srcText = new JTextArea("chosen source Directory:");
        srcText.setBounds(60, 75, 300, 100);
        srcText.setEditable(false);

        // the textarea below the button to choose the target directory. it will display the chosen one
        JTextArea targetText = new JTextArea("target Directory:");
        targetText.setBounds(390, 75, 300, 100);
        targetText.setEditable(false);

        // button to choose the source directory
        JButton chooseSourceDirectory = new JButton("Choose Source Directory"); // TODO: in dropdown menü ändern?
        chooseSourceDirectory.setBounds(100,30, 250,30);
        chooseSourceDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser source = new JFileChooser();
                source.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int state = source.showOpenDialog(null);
                if (state == JFileChooser.APPROVE_OPTION) {
                    sourcePath = source.getSelectedFile().toPath();
                    srcText.append("\n" + sourcePath);
                }
            }
        });

        // button to choose the target directory
        JButton chooseTargetDirectory = new JButton("Choose Target Directory");
        chooseTargetDirectory.setBounds(400,30, 250,30);
        chooseTargetDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser target = new JFileChooser();
                target.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int state = target.showOpenDialog(null);
                if (state == JFileChooser.APPROVE_OPTION) {
                    targetPath = target.getSelectedFile().toPath();
                    targetText.append("\n"+targetPath);
                }
            }
        });

        // textArea which displays more info about the backup mode
        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setBounds(175,250, 400, 110);

        // dropdown menu to choose the backup mode
        JComboBox<String> dropDownMenu = new JComboBox<>(new String[]{"choose Backup Mode",
                "new Backup", "consecutive Backup", "updated Backup"}); //TODO: standard ändern
        dropDownMenu.setSize(200,30);
        dropDownMenu.setBounds(275,200, 200, 30);
        dropDownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        // progress bar
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(225, 400, 300, 20);

        // button to start the backup. is currently not completely implemented
        JButton startBackup = new JButton("start backup");
        startBackup.setBounds(200,500, 150, 30);
        startBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (dropDownMenu.getSelectedIndex()) {
                    case 1 -> System.out.println(1); // TODO: Methodenaufruf für Modus 1
                    case 2 -> System.out.println(2); // TODO: Methodenaufruf für Modus 2
                    case 3 -> System.out.println(3); // TODO: Methodenaufruf für Modus 3
                }
                fill();
            }
        });

        // button to close the window. also terminates the program
        JButton cancel = new JButton("cancel");
        cancel.setBounds(400,500, 150, 30);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.dispose();

            }
        });

        mainWindow.add(startBackup);
        mainWindow.add(cancel);
        mainWindow.add(dropDownMenu);
        mainWindow.add(infoBox);
        mainWindow.add(chooseSourceDirectory);
        mainWindow.add(chooseTargetDirectory);
        mainWindow.add(srcText);
        mainWindow.add(targetText);
        mainWindow.add(progressBar);
        mainWindow.setSize(750,600);
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

    public static void main(String[] args) {
        new UI();
    }
}
