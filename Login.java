import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Login class extends JPanel and implements ActionListener to handle user login functionality
public class Login extends JPanel implements ActionListener {
    JLabel userL = new JLabel("Username: "); // Label for username input
    JTextField userTF = new JTextField(); // Text field for entering the username
    JLabel passL = new JLabel("Password: "); // Label for password input
    JPasswordField passTF = new JPasswordField(); // Password field for entering the password
    JPanel loginP = new JPanel(new GridLayout(3, 2)); // Panel for arranging components in a grid layout
    JPanel panel = new JPanel(); // Main panel to hold the login components
    JButton login = new JButton("Login"); // Button to initiate login
    JButton register = new JButton("Register"); // Button to navigate to the registration screen
    CardLayout cl; // CardLayout to switch between login and other panels

    // Constructor initializes the login panel layout
    public Login() {
        setLayout(new CardLayout()); // Set layout manager to CardLayout for panel switching
        loginP.add(userL); // Add username label to the grid layout
        loginP.add(userTF); // Add username text field to the grid layout
        loginP.add(passL); // Add password label to the grid layout
        loginP.add(passTF); // Add password field to the grid layout
        login.addActionListener(this); // Set action listener for the login button
        register.addActionListener(this); // Set action listener for the register button
        loginP.add(login); // Add login button to the grid layout
        loginP.add(register); // Add register button to the grid layout
        panel.add(loginP); // Add the grid layout panel to the main panel
        add(panel, "login"); // Add the main panel to the CardLayout with the name "login"
        cl = (CardLayout) getLayout(); // Initialize CardLayout reference
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle login button action
        if (e.getSource() == login) {
            try (BufferedReader input = new BufferedReader(new FileReader("passwords.txt"))) {
                String pass = null; // Variable to store the hashed password from the file
                String line;

                // Read the password file line by line to find the username
                while ((line = input.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    if (st.hasMoreTokens() && userTF.getText().equals(st.nextToken())) {
                        if (st.hasMoreTokens()) {
                            pass = st.nextToken(); // Store the hashed password if username matches
                        }
                    }
                }

                if (pass != null) {
                    // Hash the entered password using SHA-256
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(new String(passTF.getPassword()).getBytes());
                    byte[] byteData = md.digest();

                    // Convert hashed bytes to hexadecimal string
                    StringBuilder sb = new StringBuilder();
                    for (byte b : byteData) {
                        sb.append(String.format("%02x", b));
                    }

                    // Compare the hashed entered password with the stored hash
                    if (pass.equals(sb.toString())) {
                        add(new FileBrowser(userTF.getText()), "fb"); // Add FileBrowser panel to the CardLayout
                        cl.show(this, "fb"); // Switch to the FileBrowser panel
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Password file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while hashing the password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Handle register button action
        if (e.getSource() == register) {
            add(new Register(), "register"); // Add Register panel to the CardLayout
            cl.show(this, "register"); // Switch to the Register panel
        }
    }

    // Main method to launch the Login application
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login"); // Create a JFrame with the title "Login"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        frame.setSize(500, 500); // Set the frame size
        Login login = new Login(); // Create an instance of the Login class
        frame.add(login); // Add the Login panel to the frame
        frame.setVisible(true); // Make the frame visible
    }
}
