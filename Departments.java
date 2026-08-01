import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Departments extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel infoLabel;
    private JButton backButton;
    private JButton logoutButton;
    private final String username;
    private Cursor cursor;

    public Departments(String username) {
        this.username = username;
        setTitle("Departments");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F7F9FB"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("Departments");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
        titleLabel.setBounds(30, 30, 620, 40);
        c.add(titleLabel);

        userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setBounds(30, 80, 400, 24);
        c.add(userLabel);

        infoLabel = new JLabel("This area will manage department records.");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        infoLabel.setBounds(30, 130, 620, 26);
        c.add(infoLabel);

        backButton = new JButton("Back to Admin");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setBackground(Color.decode("#2E75B6"));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(cursor);
        backButton.setBounds(30, 280, 200, 50);
        c.add(backButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoutButton.setBackground(Color.decode("#D9534F"));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setCursor(cursor);
        logoutButton.setBounds(250, 280, 200, 50);
        c.add(logoutButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Admin frame = new Admin(username);
                frame.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Login frame = new Login();
                frame.setVisible(true);
            }
        });
    }
}
