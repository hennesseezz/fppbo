import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

public class Editor extends JPanel implements ActionListener {
    File file;
    JButton save = new JButton("Save");
    JButton savec = new JButton("Save and Close");
    JTextArea text = new JTextArea(20, 40);

    public Editor(String s) {
        file = new File(s);
        save.addActionListener(this);
        savec.addActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (file.exists()) {
            try (BufferedReader input = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = input.readLine()) != null) {
                    text.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        add(new JScrollPane(text));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(save);
        buttonPanel.add(savec);
        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (FileWriter out = new FileWriter(file)) {
            out.write(text.getText());
            JOptionPane.showMessageDialog(this, "File saved successfully!");

            if (e.getSource() == savec) {
                Login login = (Login) SwingUtilities.getAncestorOfClass(Login.class, this);
                if (login != null) {
                    login.cl.show(login, "fb");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
