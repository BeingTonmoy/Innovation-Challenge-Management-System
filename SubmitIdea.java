import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;

public class SubmitIdea extends JFrame {

    private Container c;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel callLabel;
    private JLabel ideaLabel;
    private JLabel descLabel;
    private JLabel categoryLabel;
    private JLabel attachmentLabel;
    private JComboBox<CallItem> callCombo;
    private JTextField ideaField;
    private JTextArea descArea;
    private JComboBox<String> categoryCombo;
    private JTextField attachmentField;
    private JButton chooseButton;
    private JButton submitButton;
    private JButton backButton;
    private JButton logoutButton;
    private Cursor cursor;
    private File selectedFile;
    private final String username;

    public SubmitIdea(String username) {
        this.username = username;
        setTitle("Submit New Idea (Innovator)");
        setSize(900, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F2F2F2"));

        cursor = new Cursor(Cursor.HAND_CURSOR);

        userLabel = new JLabel("User: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setBounds(40, 15, 260, 25);
        c.add(userLabel);

        titleLabel = new JLabel("Submit New Idea (Innovator)");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(240, 40, 520, 40);
        c.add(titleLabel);

        callLabel = new JLabel("Innovation Call:");
        callLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        callLabel.setBounds(40, 90, 300, 35);
        c.add(callLabel);

        callCombo = new JComboBox<>();
        callCombo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        callCombo.setBounds(40, 130, 580, 45);
        c.add(callCombo);

        ideaLabel = new JLabel("Idea Title:");
        ideaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        ideaLabel.setBounds(40, 190, 300, 35);
        c.add(ideaLabel);

        ideaField = new JTextField();
        ideaField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        ideaField.setBounds(40, 230, 580, 45);
        c.add(ideaField);

        descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        descLabel.setBounds(40, 290, 300, 35);
        c.add(descLabel);

        descArea = new JTextArea();
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(40, 330, 580, 120);
        c.add(descScroll);

        categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        categoryLabel.setBounds(40, 470, 300, 35);
        c.add(categoryLabel);

        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        categoryCombo.setBounds(40, 510, 280, 45);
        c.add(categoryCombo);

        attachmentLabel = new JLabel("Attachment:");
        attachmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        attachmentLabel.setBounds(340, 470, 200, 35);
        c.add(attachmentLabel);

        attachmentField = new JTextField();
        attachmentField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        attachmentField.setBounds(340, 510, 220, 45);
        attachmentField.setEditable(false);
        c.add(attachmentField);

        chooseButton = new JButton("Choose File");
        chooseButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        chooseButton.setBounds(580, 510, 160, 45);
        chooseButton.setCursor(cursor);
        c.add(chooseButton);

        submitButton = new JButton("Submit Idea");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        submitButton.setBounds(320, 570, 260, 55);
        submitButton.setCursor(cursor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(Color.decode("#2E75B6"));
        c.add(submitButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        backButton.setBounds(40, 570, 130, 50);
        backButton.setCursor(cursor);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.decode("#2E75B6"));
        c.add(backButton);

        logoutButton = new JButton("Log out");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        logoutButton.setBounds(740, 570, 130, 50);
        logoutButton.setCursor(cursor);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.decode("#C00000"));
        c.add(logoutButton);

        loadOpenCalls();
        loadCategories();

        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(SubmitIdea.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.getSelectedFile();
                    attachmentField.setText(selectedFile.getName());
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                submitIdea();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Home frame = new Home();
                frame.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
    }

    private void loadOpenCalls() {
        callCombo.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT CALLID, TITLE FROM INNOVATION_CALL WHERE UPPER(STATUS) = 'OPEN' ORDER BY CALLID")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    callCombo.addItem(new CallItem(rs.getInt("CALLID"), rs.getString("TITLE")));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load open calls: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
        if (callCombo.getItemCount() == 0) {
            callCombo.addItem(new CallItem(0, "No open calls available"));
            callCombo.setEnabled(false);
        }
    }

    private void loadCategories() {
        categoryCombo.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT CATEGORY FROM IDEA WHERE CATEGORY IS NOT NULL ORDER BY CATEGORY")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categoryCombo.addItem(rs.getString("CATEGORY"));
                }
            }
        } catch (SQLException e) {
            // ignore and use fallback categories
        }
        if (categoryCombo.getItemCount() == 0) {
            categoryCombo.addItem("AgriTech");
            categoryCombo.addItem("EdTech");
            categoryCombo.addItem("GreenTech");
            categoryCombo.addItem("HealthTech");
            categoryCombo.addItem("FinTech");
            categoryCombo.addItem("Other");
        }
    }

    private void submitIdea() {
        CallItem callItem = (CallItem) callCombo.getSelectedItem();
        String title = ideaField.getText().trim();
        String description = descArea.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();

        if (callItem == null || callItem.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an open innovation call.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (title.isEmpty() || description.isEmpty() || category == null || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all idea fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int ideaId = 1;
            try (PreparedStatement ps = conn.prepareStatement("SELECT NVL(MAX(IDEAID),0)+1 FROM IDEA")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ideaId = rs.getInt(1);
                    }
                }
            }

            int innovatorId = 1; // TODO: Replace with actual logged-in innovator id

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO IDEA (IDEAID, INNOVATORID, CALLID, TITLE, DESCRIPTION, CATEGORY, SUBMISSIONDATE, STATUS) " +
                            "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, 'PENDING')")) {
                ps.setInt(1, ideaId);
                ps.setInt(2, innovatorId);
                ps.setInt(3, callItem.getId());
                ps.setString(4, title);
                ps.setString(5, description);
                ps.setString(6, category);
                ps.executeUpdate();
            }

            if (selectedFile != null && selectedFile.exists()) {
                int attachmentId = 1;
                try (PreparedStatement ps = conn.prepareStatement("SELECT NVL(MAX(ATTACHMENTID),0)+1 FROM ATTACHMENT")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            attachmentId = rs.getInt(1);
                        }
                    }
                }
                String filename = selectedFile.getName();
                String fileType = getFileExtension(selectedFile.getName());
                long fileSize = selectedFile.length();
                String fileUrl = selectedFile.getAbsolutePath();

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO ATTACHMENT (ATTACHMENTID, IDEAID, FILENAME, FILETYPE, FILESIZE, UPLOADDATE, FILEURL) " +
                                "VALUES (?, ?, ?, ?, ?, SYSDATE, ? )")) {
                    ps.setInt(1, attachmentId);
                    ps.setInt(2, ideaId);
                    ps.setString(3, filename);
                    ps.setString(4, fileType);
                    ps.setLong(5, fileSize);
                    ps.setString(6, fileUrl);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Idea submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to submit idea: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        ideaField.setText("");
        descArea.setText("");
        attachmentField.setText("");
        selectedFile = null;
        callCombo.setSelectedIndex(0);
        if (categoryCombo.getItemCount() > 0) {
            categoryCombo.setSelectedIndex(0);
        }
    }

    private String getFileExtension(String name) {
        int idx = name.lastIndexOf('.');
        if (idx > 0 && idx < name.length() - 1) {
            return name.substring(idx + 1).toLowerCase();
        }
        return "";
    }

    public static void main(String[] args) {
        SubmitIdea frame = new SubmitIdea("User");
        frame.setVisible(true);
    }

    private static class CallItem {
        private final int id;
        private final String title;

        public CallItem(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
