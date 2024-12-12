import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends JPanel implements ActionListener {
    JLabel userL = new JLabel("Username: ");
    JTextField userTF = new JTextField();
    JLabel passL = new JLabel("Password: ");
    JPasswordField passTF = new JPasswordField();
    JPanel loginP = new JPanel(new GridLayout(3, 2));
    JPanel panel = new JPanel();
    JButton login = new JButton("Login");
    JButton register = new JButton("Register");
    CardLayout cl;

    public Login() {
        setLayout(new CardLayout());
        loginP.add(userL);
        loginP.add(userTF);
        loginP.add(passL);
        loginP.add(passTF);
        login.addActionListener(this);
        register.addActionListener(this);
        loginP.add(login);
        loginP.add(register);
        panel.add(loginP);
        add(panel, "login");
        cl = (CardLayout) getLayout();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            try (BufferedReader input = new BufferedReader(new FileReader("passwords.txt"))) {
                String pass = null;
                String line;

                while ((line = input.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    if (st.hasMoreTokens() && userTF.getText().equals(st.nextToken())) {
                        if (st.hasMoreTokens()) {
                            pass = st.nextToken();
                        }
                    }
                }

                if (pass != null) {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(new String(passTF.getPassword()).getBytes());
                    byte[] byteData = md.digest();

                    StringBuilder sb = new StringBuilder();
                    for (byte b : byteData) {
                        sb.append(String.format("%02x", b));
                    }

                    if (pass.equals(sb.toString())) {
                        add(new FileBrowser(userTF.getText()), "fb");
                        cl.show(this, "fb");
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

        if (e.getSource() == register) {
            add(new Register(), "register");
            cl.show(this, "register");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        Login login = new Login();
        frame.add(login);
        frame.setVisible(true);
    }
}
