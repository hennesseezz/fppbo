import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.io.*;
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends JPanel implements ActionListener {
    JLabel userL = new JLabel("Choose a Username: ");
    JTextField userTF = new JTextField();
    JLabel passL = new JLabel("Password");
    JPasswordField passTF = new JPasswordField();
    JLabel passLC = new JLabel("Confirm Password");
    JPasswordField passC = new JPasswordField();
    JButton register = new JButton("Register");
    JButton back = new JButton("Back");
    
    public Register() {
        JPanel loginP = new JPanel();
        loginP.setLayout(new GridLayout(4, 2));
        loginP.add(userL);
        loginP.add(userTF);
        loginP.add(passL);
        loginP.add(passTF);
        loginP.add(passLC);
        loginP.add(passC);
        loginP.add(register);
        loginP.add(back);
        register.addActionListener(this);
        back.addActionListener(this);
        add(loginP);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            if (passTF.getPassword().length > 0 && userTF.getText().length() > 0) {
                String pass = new String(passTF.getPassword());
                String confirm = new String(passC.getPassword());
                
                if (pass.equals(confirm)) {
                    try (BufferedReader input = new BufferedReader(new FileReader("passwords.txt"))) {
                        String line;
                        while ((line = input.readLine()) != null) {
                            StringTokenizer st = new StringTokenizer(line);
                            if (st.hasMoreTokens() && userTF.getText().equals(st.nextToken())) {
                                JOptionPane.showMessageDialog(this, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(pass.getBytes());
                        byte[] byteData = md.digest();
                        
                        StringBuilder sb = new StringBuilder();
                        for (byte b : byteData) {
                            sb.append(String.format("%02x", b));
                        }
                        
                        try (BufferedWriter output = new BufferedWriter(new FileWriter("passwords.txt", true))) {
                            output.write(userTF.getText() + " " + sb.toString() + "\n");
                        }

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
        
        if (e.getSource() == back) {
            Login login = (Login) getParent();
            login.cl.show(login, "login");
        }
    }
}
