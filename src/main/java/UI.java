import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {
    JFrame mainWindow;

    public UI() {
        mainWindow = new JFrame("simple backup application");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTextArea infoBox = new JTextArea();
        infoBox.setSize(300, 200);
        infoBox.setBounds(100,150, 300, 200);

        JComboBox<String> dropDownMenu = new JComboBox<>(new String[]{"please choose...","mode1", "mode2", "mode3"});
        dropDownMenu.setSize(200,30);
        dropDownMenu.setBounds(200,50, 200, 30);
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
        startBackup.setBounds(100,400, 150, 30);
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
        cancel.setBounds(300,400, 150, 30);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.dispose();

            }
        });

        mainWindow.add(startBackup);
        mainWindow.add(cancel);
        mainWindow.add(dropDownMenu);
        mainWindow.add(infoBox);
        mainWindow.setSize(500,600);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);

    }

    public static void main(String[] args) {
        new UI();
    }
}
