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

public class Registration extends JFrame {

    private Container c;
    private ImageIcon icon, logo;
    private JLabel label1, imgLabel;
    private Font f1, f2, f3, f4, f5, f6; // font
    private JTextField tf2, tf4; // textfield: tf2=Email, tf4=Username
    private JButton btn1, btn2, btn3, btn4, nBtn; //button
    private JPasswordField tf3; //password field
    private Cursor cursor;
    private int a, b; // kept but unused (captcha removed)

    Registration() {
         // Frame Layout
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setTitle("IMS - Registration");
         this.setSize(900, 660);
         this.setLocationRelativeTo(null);
         this.setResizable(false);
 
         c = this.getContentPane();
         c.setLayout(null);
         c.setBackground(Color.decode("#F2F2F2"));
 
          // Icon
        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        this.setIconImage(icon.getImage());

        
         // Fonts
         f1 = new Font("Segoe UI Black", Font.PLAIN, 35);
         f2 = new Font("Segoe UI Black", Font.PLAIN, 25);
         f3 = new Font("Segoe UI Semibold", Font.PLAIN, 35);
         f4 = new Font("Segoe UI", Font.PLAIN, 25);
         f5 = new Font("Segoe UI", Font.PLAIN, 19);
         f6 = new Font("Segoe UI", Font.PLAIN, 25);
 
         // Title
         label1 = new JLabel();
         label1.setText("Register your IMS user account now.");
         label1.setBounds(30, 25, 680, 50);
         label1.setFont(f1);
         c.add(label1);
 
        // Username
        label1 = new JLabel();
        label1.setText("Username");
        label1.setBounds(248, 75, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf4 = new JTextField();
        tf4.setBounds(260, 120, 260, 30);
        tf4.setFont(f5);
        c.add(tf4);

        // Email
        label1 = new JLabel();
        label1.setText("Email");
        label1.setBounds(248, 145, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf2 = new JTextField();
        tf2.setBounds(260, 187, 260, 30);
        tf2.setFont(f5);
        c.add(tf2);

         // Password
         label1 = new JLabel();
         label1.setText("Password");
         label1.setBounds(248, 210, 500, 50); 
         label1.setFont(f4);
         c.add(label1);

         tf3 = new JPasswordField();
         tf3.setBounds(260, 257, 260, 30);
         tf3.setFont(f2);
         tf3.setEchoChar('*');
         c.add(tf3);
 
         // Cursor for JButtons
         cursor = new Cursor(Cursor.HAND_CURSOR);
 
         // JButtons
         btn1 = new JButton("Exit");
         btn1.setBounds(53, 540, 183, 50);
         btn1.setFont(f2);
         btn1.setCursor(cursor);
         btn1.setForeground(Color.WHITE);
         btn1.setBackground(Color.decode("#C00000"));
         c.add(btn1);
 
         btn2 = new JButton("Back");
         btn2.setBounds(251, 540, 183, 50);
         btn2.setFont(f2);
         btn2.setCursor(cursor);
         btn2.setForeground(Color.WHITE);
         btn2.setBackground(Color.decode("#2E75B6"));
         c.add(btn2);
 
         btn3 = new JButton("Reset");
         btn3.setBounds(450, 540, 183, 50);
         btn3.setFont(f2);
         btn3.setCursor(cursor);
         btn3.setForeground(Color.WHITE);
         btn3.setBackground(Color.decode("#2E75B6"));
         c.add(btn3);
 
         btn4 = new JButton("Register");
         btn4.setBounds(649, 540, 183, 50);
         btn4.setFont(f2);
         btn4.setCursor(cursor);
         btn4.setForeground(Color.WHITE);
         btn4.setBackground(Color.decode("#2E75B6"));
         c.add(btn4);
 
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

        // Reset Button
        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                setVisible(false);
                Registration frame = new Registration();
                frame.setVisible(true);
            }
        });

        // Register Button
        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                String textFieldEmail = tf2.getText().trim(); // Email
                String textFieldPassword = tf3.getText(); // Password
                String textFieldUsername = tf4.getText().trim(); // Username

                if (textFieldUsername.isEmpty() || textFieldPassword.isEmpty() || textFieldEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all of the fields.", "Warning!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // proceed with DB insertion: always use sequence for UserID and default Role='NONE'
                try (Connection conn = DBConnection.getConnection()) {
                    conn.setAutoCommit(false);

                    // check username uniqueness
                    try (PreparedStatement pcheck = conn.prepareStatement("SELECT COUNT(*) FROM USER_ACCOUNT WHERE Username = ?")) {
                        pcheck.setString(1, textFieldUsername);
                        try (ResultSet rc = pcheck.executeQuery()) {
                            if (rc.next() && rc.getInt(1) > 0) {
                                JOptionPane.showMessageDialog(null, "Username already exists.", "Registration Error", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }

                    int finalUserId;
                    try (PreparedStatement s1 = conn.prepareStatement("SELECT user_seq.NEXTVAL FROM DUAL"); ResultSet r1 = s1.executeQuery()) {
                        if (r1.next()) finalUserId = r1.getInt(1);
                        else throw new SQLException("Failed to get next user_seq value.");
                    }

                    String hashedPassword = hashPassword(textFieldPassword);

                    try (PreparedStatement insUser = conn.prepareStatement(
                            "INSERT INTO USER_ACCOUNT (UserID, Username, PasswordHash, Role, Email, Status) VALUES (?, ?, ?, ?, ?, 'ACTIVE')")) {
                        insUser.setInt(1, finalUserId);
                        insUser.setString(2, textFieldUsername);
                        insUser.setString(3, hashedPassword);
                        insUser.setString(4, "INNOVATOR"); // set default role to INNOVATOR
                        insUser.setString(5, textFieldEmail);
                        insUser.executeUpdate();
                    }

                    // create innovator record (assign DeptID = 1 by default)
                    try (PreparedStatement insInnov = conn.prepareStatement(
                            "INSERT INTO INNOVATOR (InnovatorID, UserID, DeptID, Name, Email, Phone, Expertise) VALUES (innovator_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)")) {
                        insInnov.setInt(1, finalUserId);
                        insInnov.setInt(2, 1); // default DeptID
                        insInnov.setString(3, textFieldUsername); // name
                        insInnov.setString(4, textFieldEmail);
                        insInnov.setString(5, null);
                        insInnov.setString(6, null);
                        insInnov.executeUpdate();
                    }

                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Registration Successfully Completed.", "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    Home frame = new Home();
                    frame.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                } catch (NoSuchAlgorithmException nse) {
                    JOptionPane.showMessageDialog(null, "Hashing error: " + nse.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {

        Registration frame = new Registration();
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