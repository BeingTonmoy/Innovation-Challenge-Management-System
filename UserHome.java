import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserHome extends JFrame {

    private final Container c;
    private final ImageIcon icon;
    private final JLabel titleLabel;
    private final JLabel welcomeLabel;
    private final JButton projectsButton;
    private final JButton callsButton;
    private final JButton signOutButton;
    private final Cursor cursor;
    private final String username;

    public UserHome(String username) {
        this.username = username;
        setTitle("User Home - IMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F4F7FB"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("User Dashboard");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(40, 30, 400, 40);
        c.add(titleLabel);

        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        welcomeLabel.setBounds(40, 85, 400, 28);
        c.add(welcomeLabel);

        projectsButton = createButton("Projects", 40, 160, Color.decode("#2E75B6"));
        callsButton = createButton("Call Innovation", 380, 160, Color.decode("#4A7A9D"));
        signOutButton = createButton("Sign Out", 220, 320, Color.decode("#D9534F"));

        projectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Projects frame = new Projects(username);
                frame.setVisible(true);
            }
        });

        callsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                SubmitIdea frame = new SubmitIdea(username);
                frame.setVisible(true);
            }
        });

        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Login frame = new Login();
                frame.setVisible(true);
            }
        });
    }

    private JButton createButton(String text, int x, int y, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 240, 100);
        c.add(button);
        return button;
    }

    public static void main(String[] args) {
        UserHome frame = new UserHome("Admin");
        frame.setVisible(true);
    }
}
