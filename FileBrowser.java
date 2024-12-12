import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.GridLayout;
import java.io.File;

// FileBrowser class extends JPanel and implements ActionListener to handle GUI actions
public class FileBrowser extends JPanel implements ActionListener {
    JLabel label = new JLabel("File list: "); // Label to display the title of the file list
    JButton newFile = new JButton("New File"); // Button to create a new file
    JButton open = new JButton("Open"); // Button to open a selected file
    JTextField newFileTF = new JTextField(10); // Text field to enter the name of a new file
    ButtonGroup bg; // Group for radio buttons representing files
    File directory; // Directory where files are stored

    // Constructor to initialize the file browser with a specified directory
    public FileBrowser(String dir) {
        directory = new File(dir); // Create a File object for the directory
        directory.mkdir(); // Create the directory if it doesn't exist

        // Get the list of files in the directory
        File[] files = directory.listFiles();
        int fileCount = (files == null) ? 0 : files.length; // Handle null case for empty directories

        // Create a panel to display the file list, allowing for additional rows for buttons and input
        JPanel fileList = new JPanel(new GridLayout(fileCount + 3, 1));
        fileList.add(label); // Add the label to the file list panel
        bg = new ButtonGroup(); // Initialize the button group

        // Add a radio button for each file in the directory
        if (files != null) {
            for (File file : files) {
                JRadioButton radio = new JRadioButton(file.getName()); // Create a radio button for the file
                radio.setActionCommand(file.getName()); // Set the action command to the file name
                bg.add(radio); // Add the radio button to the group
                fileList.add(radio); // Add the radio button to the panel
            }
        }

        // Create a panel for new file input and buttons
        JPanel newP = new JPanel();
        newP.add(newFileTF); // Add the text field for new file name
        newP.add(newFile); // Add the "New File" button
        newFile.addActionListener(this); // Add action listener to the "New File" button
        open.addActionListener(this); // Add action listener to the "Open" button
        fileList.add(open); // Add the "Open" button to the file list panel
        fileList.add(newP); // Add the new file input panel to the file list panel
        add(fileList); // Add the file list panel to the main panel
    }

    // Handle button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the parent Login panel, which handles navigation
        Login login = (Login) SwingUtilities.getAncestorOfClass(Login.class, this);
        if (login == null) {
            JOptionPane.showMessageDialog(this, "Parent Login panel not found!", "Error", JOptionPane.ERROR_MESSAGE); // Show error if parent is missing
            return;
        }

        // Handle "Open" button action
        if (e.getSource() == open) {
            if (bg.getSelection() != null) { // Check if a file is selected
                String selectedFile = directory.getAbsolutePath() + File.separator + bg.getSelection().getActionCommand(); // Get the selected file path
                login.add(new Editor(selectedFile), "editor"); // Add a new Editor panel to the Login panel
                login.cl.show(login, "editor"); // Switch to the "editor" view
            } else {
                JOptionPane.showMessageDialog(this, "No file selected!", "Error", JOptionPane.ERROR_MESSAGE); // Show error if no file is selected
            }
        }

        // Handle "New File" button action
        if (e.getSource() == newFile) {
            String fileName = newFileTF.getText().trim(); // Get the entered file name
            if (!fileName.isEmpty()) { // Check if the file name is not empty
                String filePath = directory.getAbsolutePath() + File.separator + fileName + ".txt"; // Create the file path
                File newFile = new File(filePath); // Create a File object for the new file

                if (!newFile.exists()) { // Check if the file doesn't already exist
                    try {
                        if (newFile.createNewFile()) { // Attempt to create the new file
                            login.add(new Editor(filePath), "editor"); // Add a new Editor panel for the file
                            login.cl.show(login, "editor"); // Switch to the "editor" view
                        } else {
                            JOptionPane.showMessageDialog(this, "Could not create the file!", "Error", JOptionPane.ERROR_MESSAGE); // Show error if file creation fails
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Print error details to the console
                        JOptionPane.showMessageDialog(this, "Error creating file.", "Error", JOptionPane.ERROR_MESSAGE); // Show error dialog
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "File already exists!", "Error", JOptionPane.ERROR_MESSAGE); // Show error if the file already exists
                }
            } else {
                JOptionPane.showMessageDialog(this, "File name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE); // Show error if the file name is empty
            }
        }
    }
}
