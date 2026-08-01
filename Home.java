//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Home extends JFrame {

    private Container c;
    private ImageIcon icon, logo;
    private JLabel label1,  label2, imgLabel;
    private Font f1, f2;
    private JButton btn1, btn2, btn3, nBtn;
    private Cursor cursor;

    Home() {
        // Frame Layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("INNOVATON CHALLENGE MANAGEMENT SYSTEM - Home");
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
        f1 = new Font("Tahoma", Font.BOLD, 48);
        f2 = new Font("Segoe UI Black", Font.PLAIN, 25);

        // Title
        label1 = new JLabel();
        label1.setText("Manage your innovation");
        label1.setBounds(30, 55, 800, 65);
        label1.setFont(f1);
        c.add(label1);

        label1 = new JLabel();
        label1.setText("Call for innovation");
        label1.setBounds(90, 120, 500, 65);
        label1.setFont(f1);
        c.add(label1);

        // Cursor for JButtons
        cursor = new Cursor(Cursor.HAND_CURSOR);

        // JButtons
        btn1 = new JButton("Login");
        btn1.setBounds(650, 234, 195, 50);
        btn1.setFont(f2);
        btn1.setCursor(cursor);
        btn1.setForeground(Color.BLUE);
        btn1.setBackground(Color.decode("#2E75B6"));
        c.add(btn1);
        
        label2 = new JLabel();
        label2.setText("Don't have an account? ");
        label2.setBounds(300, 500, 600, 50);
        label2.setFont(f2);
        c.add(label2);

        btn2 = new JButton("Register");
        btn2.setBounds(600, 500, 195, 50);
        btn2.setFont(f2);
        btn2.setCursor(cursor);
        btn2.setForeground(Color.GREEN);
        btn2.setBackground(Color.decode("#2E75B6"));
        c.add(btn2);

        btn3 = new JButton("Exit");
        btn3.setBounds(650, 308, 195, 50);
        btn3.setFont(f2);
        btn3.setCursor(cursor);
        btn3.setForeground(Color.RED);
        btn3.setBackground(Color.decode("#C00000"));
        c.add(btn3);

        
        
        nBtn = new JButton("");
        nBtn.setBounds(0, 0, 0, 0);
        c.add(nBtn);

        // Login
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                setVisible(false);
                Login frame = new Login();
                frame.setVisible(true);
            }
        });

        // Register
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                Registration frame = new Registration();
                frame.setVisible(true);
                setVisible(false);
            }
        });

        // Exit
        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        
    }

    public static void main(String[] args) {

        Home frame = new Home();
        frame.setVisible(true);
    }
}
