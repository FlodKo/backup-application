import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class UI {
    JFrame mainWindow;
    Path sourcePath = null;
    Path targetPath = null;

    public UI() {
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTextArea srcText = new JTextArea("chosen source Directory:");
        srcText.setBounds(60, 75, 300, 100);
        srcText.setEditable(false);

        JTextArea targetText = new JTextArea("target Directory:");
        targetText.setBounds(390, 75, 300, 100);
        targetText.setEditable(false);


        JButton chooseSourceDirectory = new JButton("Choose Source Directory");
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

        JTextArea infoBox = new JTextArea();
        infoBox.setEditable(false);
        infoBox.setSize(400, 200);
        infoBox.setBounds(175,250, 400, 200);

        JComboBox<String> dropDownMenu = new JComboBox<>(new String[]{"choose Backup Mode","mode1", "mode2", "mode3"});
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
