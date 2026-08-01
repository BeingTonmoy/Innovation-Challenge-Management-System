//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel label1;
    private Font f1, f2, f3, f4, f5, f6;
    private JTextField tf1;
    private JButton btn1, btn2, btn3, nBtn, btn4;
    private JPasswordField tf2;
    private Cursor cursor;

    Login() {
        // Frame Layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Innovation Management System - Login");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F2F2F2"));

        
        // Icon
        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        this.setIconImage(icon.getImage());

        
        // Fonts
        f1 = new Font("Segoe UI Black", Font.BOLD, 60);
        f2 = new Font("Segoe UI Black", Font.PLAIN, 25);
        f3 = new Font("Segoe UI Semibold", Font.PLAIN, 35);
        f4 = new Font("Segoe UI", Font.PLAIN, 30);
        f5 = new Font("Segoe UI", Font.PLAIN, 22);
        f6 = new Font("Segoe UI", Font.PLAIN, 25);

        // Title
        label1 = new JLabel();
        label1.setText("IMS Login");
        label1.setBounds(210, 50, 500, 90);
        label1.setFont(f1);
        c.add(label1);

        // Username
        label1 = new JLabel();
        label1.setText("Username");
        label1.setBounds(248, 145, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf1 = new JTextField();
        tf1.setBounds(255, 200, 340, 35);
        tf1.setFont(f5);
        c.add(tf1);

        // Password
        label1 = new JLabel();
        label1.setText("Password");
        label1.setBounds(248, 240, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf2 = new JPasswordField();
        tf2.setBounds(255, 290, 340, 35);
        tf2.setFont(f2);
        tf2.setEchoChar('*');
        c.add(tf2);

        // Cursor for JButtons
        cursor = new Cursor(Cursor.HAND_CURSOR);

        // JButtons
        btn1 = new JButton("Exit");
        btn1.setBounds(320, 470, 215, 50);
        btn1.setFont(f2);
        btn1.setCursor(cursor);
        btn1.setForeground(Color.WHITE);
        btn1.setBackground(Color.decode("#ed112a"));
        c.add(btn1);

        btn2 = new JButton("Back");
        btn2.setBounds(320, 410, 215, 50);
        btn2.setFont(f2);
        btn2.setCursor(cursor);
        btn2.setForeground(Color.WHITE);
        btn2.setBackground(Color.decode("#2E75B6"));
        c.add(btn2);

        btn3 = new JButton("Login");
        btn3.setBounds(320, 350, 215, 50);
        btn3.setFont(f2);
        btn3.setCursor(cursor);
        btn3.setForeground(Color.WHITE);
        btn3.setBackground(Color.decode("#008000"));
        c.add(btn3);
        

        nBtn = new JButton("");
        nBtn.setBounds(0, 0, 0, 0);
        c.add(nBtn);

        // Exit Button
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        // Back Button
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                setVisible(false);
                Home frame = new Home();
                frame.setVisible(true);
            }
        });

        // Login Button
        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                String username = tf1.getText().trim();
                String password = new String(tf2.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all of the fields.", "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String hashed = hashPassword(password);
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT UserID, Role FROM USER_ACCOUNT WHERE Username = ? AND PasswordHash = ? AND Status = 'ACTIVE'")) {
                        ps.setString(1, username);
                        ps.setString(2, hashed);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int userId = rs.getInt(1);
                                String role = rs.getString(2);
                                JOptionPane.showMessageDialog(null, "Welcome! " + username, "Logged in!",
                                        JOptionPane.INFORMATION_MESSAGE);
                                if (role != null && role.equalsIgnoreCase("ADMIN")) {
                                    int opt = JOptionPane.showOptionDialog(null,
                                            "You are logged in as ADMIN. Open admin dashboard?",
                                            "Admin Login",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            new String[]{"Open Admin Dashboard", "Continue"},
                                            "Open Admin Dashboard");
                                    if (opt == JOptionPane.YES_OPTION) {
                                        setVisible(false);
                                        Admin frame = new Admin(username);
                                        frame.setVisible(true);
                                    } else {
                                        setVisible(false);
                                        UserHome frame = new UserHome(username);
                                        frame.setVisible(true);
                                    }
                                } else {
                                    setVisible(false);
                                    UserHome frame = new UserHome(username);
                                    frame.setVisible(true);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Login Error",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException | NoSuchAlgorithmException ex) {
                    JOptionPane.showMessageDialog(null, "Login error: " + ex.getMessage(), "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }

    public static void main(String[] args) {

        Login frame = new Login();
        frame.setVisible(true);
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        if (password == null) return null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
