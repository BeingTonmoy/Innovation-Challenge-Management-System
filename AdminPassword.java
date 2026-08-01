import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPassword extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel label1;
    private Font f1, f2, f3, f4, f5, f6;
    private JTextField tf1;
    private JButton btn1, btn2, btn3, nBtn;
    private JPasswordField tf2;
    private Cursor cursor;

    AdminPassword() {
        // Frame Layout
        this.setTitle("Home Tutor - Admin Change Password");
        this.setSize(520, 400);
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
        f3 = new Font("Segoe UI Black", Font.PLAIN, 40);
        f4 = new Font("Segoe UI", Font.PLAIN, 30);
        f5 = new Font("Segoe UI", Font.PLAIN, 22);
        f6 = new Font("Segoe UI", Font.PLAIN, 25);

        // Title
        label1 = new JLabel();
        label1.setText("Change Admin Name");
        label1.setBounds(50, 5, 500, 90);
        label1.setFont(f3);
        c.add(label1);

        label1 = new JLabel();
        label1.setText("And Password");
        label1.setBounds(50, 50, 500, 90);
        label1.setFont(f3);
        c.add(label1);

        // User Name
        label1 = new JLabel();
        label1.setText("Name");
        label1.setBounds(50, 140, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf1 = new JTextField();
        tf1.setBounds(210, 150, 250, 35);
        tf1.setFont(f5);
        c.add(tf1);

        // Password
        label1 = new JLabel();
        label1.setText("Password");
        label1.setBounds(50, 200, 500, 50);
        label1.setFont(f4);
        c.add(label1);

        tf2 = new JPasswordField();
        tf2.setBounds(210, 210, 250, 35);
        tf2.setFont(f2);
        tf2.setEchoChar('*');
        c.add(tf2);

        // Cursor for JButtons
        cursor = new Cursor(Cursor.HAND_CURSOR);

        // JButtons
        btn1 = new JButton("Cancel");
        btn1.setBounds(31, 280, 208, 43);
        btn1.setFont(f2);
        btn1.setCursor(cursor);
        btn1.setForeground(Color.WHITE);
        btn1.setBackground(Color.decode("#2E75B6"));
        c.add(btn1);

        btn2 = new JButton("Change");
        btn2.setBounds(265, 280, 208, 43);
        btn2.setFont(f2);
        btn2.setCursor(cursor);
        btn2.setForeground(Color.WHITE);
        btn2.setBackground(Color.decode("#2E75B6"));
        c.add(btn2);

        nBtn = new JButton("");
        nBtn.setBounds(0, 0, 0, 0);
        c.add(nBtn);

        // Close Button
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });

        // Change Button
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                String textField1 = tf1.getText().trim(); // Admin ID
                String textField2 = String.valueOf(tf2.getPassword()).trim(); // New password value

                if (textField1.isEmpty() || textField2.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all of the fields.", "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    int adminId = DBConnection.parseAdminId(textField1);

                    try (Connection conn = DBConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE ADMIN_USER SET Name = ? WHERE AdminID = ?")) {

                        stmt.setString(1, textField2);
                        stmt.setInt(2, adminId);

                        int affected = stmt.executeUpdate();
                        if (affected > 0) {
                            JOptionPane.showMessageDialog(null, "Admin password/name has been changed.",
                                    "Admin Password", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "No admin account found to update.", "Update Failed",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Update Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
    }

    public static void main(String[] args) {

        AdminPassword frame = new AdminPassword();
        frame.setVisible(true);
    }
}
