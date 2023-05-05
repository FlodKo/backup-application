import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class UI {
    JFrame mainWindow;
    private Path sourcePath = null;
    private Path targetPath = null;

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
        infoBox.setSize(400, 200);
        infoBox.setBounds(175,250, 400, 200);

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
                    case 1 -> infoText = "Modus 1 blabla bla.";
                    case 2 -> infoText = "Modus 2 blablabla. \n dasd";
                    case 3 -> infoText = "Modus 3 blabla";
                }
                infoBox.setText(infoText);
            }
        });

        // button to start the backup. is currently not completely implemented
        JButton startBackup = new JButton("start backup");
        startBackup.setBounds(200,500, 150, 30);
        startBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (dropDownMenu.getSelectedIndex()) {
                    case 1 -> System.out.println(1);//Methodenaufruf für Modus 1
                    case 2 -> System.out.println(2); // Methodenaufruf für Modus 2
                    case 3 -> System.out.println(3); // Methodenaufruf für Modus 3
                }
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
        mainWindow.setSize(750,600);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);



    }

    public static void main(String[] args) {
        new UI();
    }
}
