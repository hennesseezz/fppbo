import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.io.*;
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Register class extends JPanel and implements ActionListener to handle user registration
public class Register extends JPanel implements ActionListener {
    JLabel userL = new JLabel("Choose a Username: "); // Label for username input
    JTextField userTF = new JTextField(); // Text field for entering the username
    JLabel passL = new JLabel("Password"); // Label for password input
    JPasswordField passTF = new JPasswordField(); // Password field for entering the password
    JLabel passLC = new JLabel("Confirm Password"); // Label for confirming the password
    JPasswordField passC = new JPasswordField(); // Password field for confirming the password
    JButton register = new JButton("Register"); // Button to register the user
    JButton back = new JButton("Back"); // Button to go back to the previous screen
    
    // Constructor initializes the registration form layout
    public Register() {
        JPanel loginP = new JPanel(); // Panel to hold form components
        loginP.setLayout(new GridLayout(4, 2)); // Use GridLayout for neat arrangement
        loginP.add(userL); // Add username label to the panel
        loginP.add(userTF); // Add username text field to the panel
        loginP.add(passL); // Add password label to the panel
        loginP.add(passTF); // Add password field to the panel
        loginP.add(passLC); // Add confirm password label to the panel
        loginP.add(passC); // Add confirm password field to the panel
        loginP.add(register); // Add register button to the panel
        loginP.add(back); // Add back button to the panel
        register.addActionListener(this); // Set action listener for the register button
        back.addActionListener(this); // Set action listener for the back button
        add(loginP); // Add the form panel to the main panel
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle register button action
        if (e.getSource() == register) {
            // Ensure both username and password fields are filled
            if (passTF.getPassword().length > 0 && userTF.getText().length() > 0) {
                String pass = new String(passTF.getPassword()); // Convert password to string
                String confirm = new String(passC.getPassword()); // Convert confirm password to string
                
                // Check if the entered passwords match
                if (pass.equals(confirm)) {
                    try (BufferedReader input = new BufferedReader(new FileReader("passwords.txt"))) {
                        String line;
                        // Check if the username already exists in the file
                        while ((line = input.readLine()) != null) {
                            StringTokenizer st = new StringTokenizer(line);
                            if (st.hasMoreTokens() && userTF.getText().equals(st.nextToken())) {
                                JOptionPane.showMessageDialog(this, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        
                        // Hash the password using SHA-256
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(pass.getBytes());
                        byte[] byteData = md.digest();
                        
                        // Convert hashed bytes to hexadecimal string
                        StringBuilder sb = new StringBuilder();
                        for (byte b : byteData) {
                            sb.append(String.format("%02x", b));
                        }
                        
                        // Write the username and hashed password to the file
                        try (BufferedWriter output = new BufferedWriter(new FileWriter("passwords.txt", true))) {
                            output.write(userTF.getText() + " " + sb.toString() + "\n");
                        }

                        // Show success message and navigate back to the login screen
                        JOptionPane.showMessageDialog(this, "Registered successfully!");
                        Login login = (Login) getParent();
                        login.cl.show(login, "login");
                        
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Password file not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(this, "An error occurred while accessing the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(this, "An error occurred while hashing the password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Handle back button action
        if (e.getSource() == back) {
            Login login = (Login) getParent(); // Get the parent Login panel
            login.cl.show(login, "login"); // Navigate back to the login screen
        }
    }
}
