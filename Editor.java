import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

// Editor class extends JPanel and implements ActionListener to handle GUI actions
public class Editor extends JPanel implements ActionListener {
    File file; // Represents the file being edited
    JButton save = new JButton("Save"); // Button to save changes
    JButton savec = new JButton("Save and Close"); // Button to save changes and close the editor
    JTextArea text = new JTextArea(20, 40); // Text area for editing file content

    // Constructor that initializes the editor with the specified file path
    public Editor(String s) {
        file = new File(s); // Create a File object for the given file path
        save.addActionListener(this); // Add action listener to the save button
        savec.addActionListener(this); // Add action listener to the save-and-close button
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Set layout for the panel

        // Check if the file exists and read its content into the text area
        if (file.exists()) {
            try (BufferedReader input = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = input.readLine()) != null) {
                    text.append(line + "\n"); // Append each line to the text area
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print error details to the console
                JOptionPane.showMessageDialog(this, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE); // Show error dialog
            }
        }

        // Add text area to the panel wrapped in a scroll pane
        add(new JScrollPane(text));

        // Create a panel for buttons and add the buttons to it
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(save);
        buttonPanel.add(savec);
        add(buttonPanel); // Add the button panel to the main panel
    }

    // Handle button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        try (FileWriter out = new FileWriter(file)) { // Open the file for writing
            out.write(text.getText()); // Write the content of the text area to the file
            JOptionPane.showMessageDialog(this, "File saved successfully!"); // Show success message

            // If the "Save and Close" button is clicked, navigate back to the login screen
            if (e.getSource() == savec) {
                Login login = (Login) SwingUtilities.getAncestorOfClass(Login.class, this); // Get the parent component of type Login
                if (login != null) {
                    login.cl.show(login, "fb"); // Switch to the "fb" (presumably a card in the CardLayout)
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace(); // Print error details to the console
            JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE); // Show error dialog
        }
    }
}
