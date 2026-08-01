//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Admin extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel instructionLabel;
    private JButton ideaProposalButton;
    private JButton innovationCallButton;
    private JButton usersButton;
    private JButton departmentsButton;
    private JButton logoutButton;
    private Cursor cursor;
    private final String username;

    public Admin(String username) {
        this.username = username;
        setTitle("IMS Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F2F2F7"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("IMS Admin Panel");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 34));
        titleLabel.setBounds(30, 30, 520, 40);
        c.add(titleLabel);

        userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setBounds(30, 80, 400, 28);
        c.add(userLabel);

        instructionLabel = new JLabel("Choose an administration area below.");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        instructionLabel.setBounds(30, 115, 460, 24);
        c.add(instructionLabel);

        ideaProposalButton = createButton("Idea Proposal", 30, 170);
        innovationCallButton = createButton("Project Manage", 380, 170);
        usersButton = createButton("Users", 30, 300);
        departmentsButton = createButton("Departments", 380, 300);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoutButton.setBackground(Color.decode("#D9534F"));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setCursor(cursor);
        logoutButton.setBounds(520, 410, 150, 50);
        c.add(logoutButton);

        ideaProposalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                InnovationCall frame = new InnovationCall(username);
                frame.setVisible(true);
            }
        });

        innovationCallButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                AdminProject frame = new AdminProject(username);
                frame.setVisible(true);
            }
        });

        usersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Users frame = new Users(username);
                frame.setVisible(true);
            }
        });

        departmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Departments frame = new Departments(username);
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

    public Admin() {
        this("Admin");
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(Color.decode("#2E75B6"));
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 290, 100);
        c.add(button);
        return button;
    }

    public static void main(String[] args) {
        Admin frame = new Admin("Admin");
        frame.setVisible(true);
    }
}
