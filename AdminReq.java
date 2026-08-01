//******************American International University-Bangladesh (AIUB) */
//******************Object Oriented Programming 1 (JAVA) GUI Project - Group 5 | Date: 09/05/2024 */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
//*********************Group Members :Arfan Rahman (23-51598-2), TANMAY ROY RONY (23-51745-2), Swarna sikder (23-51779-2), Sanjida Affrin Bristi (23-51788-2) */
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;

public class AdminReq extends JFrame {
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;
    private ImageIcon icon;

    public AdminReq() {
        // Frame setup
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("HomeTutor - Requests");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Icon
        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        this.setIconImage(icon.getImage());

        // List setup
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(10);

        // Add the list to a scrolling pane
        scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from file
        loadData();
    }

    private void loadData() {
        String path = ".\\Data\\stu_req.txt";
        File file = new File(path);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                listModel.addElement(line); // Add each line to the list model
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminReq frame = new AdminReq();
            frame.setVisible(true);
        });
    }
}
