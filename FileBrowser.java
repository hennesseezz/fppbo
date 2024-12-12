import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.GridLayout;
import java.io.File;

public class FileBrowser extends JPanel implements ActionListener {
    JLabel label = new JLabel("File list: ");
    JButton newFile = new JButton("New File");
    JButton open = new JButton("Open");
    JTextField newFileTF = new JTextField(10);
    ButtonGroup bg;
    File directory;

    public FileBrowser(String dir) {
        directory = new File(dir);
        directory.mkdir();

        File[] files = directory.listFiles();
        int fileCount = (files == null) ? 0 : files.length;

        JPanel fileList = new JPanel(new GridLayout(fileCount + 3, 1));
        fileList.add(label);
        bg = new ButtonGroup();

        if (files != null) {
            for (File file : files) {
                JRadioButton radio = new JRadioButton(file.getName());
                radio.setActionCommand(file.getName());
                bg.add(radio);
                fileList.add(radio);
            }
        }

        JPanel newP = new JPanel();
        newP.add(newFileTF);
        newP.add(newFile);
        newFile.addActionListener(this);
        open.addActionListener(this);
        fileList.add(open);
        fileList.add(newP);
        add(fileList);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Login login = (Login) SwingUtilities.getAncestorOfClass(Login.class, this);
        if (login == null) {
            JOptionPane.showMessageDialog(this, "Parent Login panel not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (e.getSource() == open) {
            if (bg.getSelection() != null) {
                String selectedFile = directory.getAbsolutePath() + File.separator + bg.getSelection().getActionCommand();
                login.add(new Editor(selectedFile), "editor");
                login.cl.show(login, "editor");
            } else {
                JOptionPane.showMessageDialog(this, "No file selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == newFile) {
            String fileName = newFileTF.getText().trim();
            if (!fileName.isEmpty()) {
                String filePath = directory.getAbsolutePath() + File.separator + fileName + ".txt";
                File newFile = new File(filePath);

                if (!newFile.exists()) {
                    try {
                        if (newFile.createNewFile()) {
                            login.add(new Editor(filePath), "editor");
                            login.cl.show(login, "editor");
                        } else {
                            JOptionPane.showMessageDialog(this, "Could not create the file!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error creating file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "File already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "File name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
