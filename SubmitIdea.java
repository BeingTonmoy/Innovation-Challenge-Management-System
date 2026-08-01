//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
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
    private JComboBox<DeptItem> departmentCombo;
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

        callLabel = new JLabel("Department:");
        callLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        callLabel.setBounds(40, 90, 300, 35);
        c.add(callLabel);

        departmentCombo = new JComboBox<>();
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        departmentCombo.setBounds(40, 130, 580, 45);
        c.add(departmentCombo);

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

        loadDepartments();
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
                UserHome frame = new UserHome(username);
                frame.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
    }

    private void loadDepartments() {
        departmentCombo.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DeptID, DeptName FROM DEPARTMENT ORDER BY DeptName")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    departmentCombo.addItem(new DeptItem(rs.getInt("DeptID"), rs.getString("DeptName")));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load departments: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
        if (departmentCombo.getItemCount() == 0) {
            departmentCombo.addItem(new DeptItem(0, "No departments available"));
            departmentCombo.setEnabled(false);
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
        DeptItem deptItem = (DeptItem) departmentCombo.getSelectedItem();
        String title = ideaField.getText().trim();
        String description = descArea.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();

        if (deptItem == null || deptItem.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a department.", "Validation", JOptionPane.WARNING_MESSAGE);
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

            int innovatorId = resolveInnovatorId(conn);
            if (innovatorId <= 0) {
                throw new SQLException("No innovator profile was found for user: " + username);
            }

            ensureIdeaHasDeptColumn(conn);
            boolean hasCallId = ideaColumnExists(conn, "CALLID");
            Integer callId = null;
            if (hasCallId) {
                callId = resolveActiveCallId(conn);
                if (callId == null) {
                    throw new SQLException("No innovation call is available for idea submission. Please ask an administrator to create an active innovation call.");
                }
            }

            String insertSql;
            if (hasCallId) {
                insertSql = "INSERT INTO IDEA (IDEAID, INNOVATORID, DEPTID, CALLID, TITLE, DESCRIPTION, CATEGORY, SUBMISSIONDATE, STATUS) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, 'PENDING')";
            } else {
                insertSql = "INSERT INTO IDEA (IDEAID, INNOVATORID, DEPTID, TITLE, DESCRIPTION, CATEGORY, SUBMISSIONDATE, STATUS) " +
                            "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, 'PENDING')";
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                int idx = 1;
                ps.setInt(idx++, ideaId);
                ps.setInt(idx++, innovatorId);
                ps.setInt(idx++, deptItem.getId());
                if (hasCallId) {
                    ps.setInt(idx++, callId);
                }
                ps.setString(idx++, title);
                ps.setString(idx++, description);
                ps.setString(idx++, category);
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

    private int resolveInnovatorId(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT UserID FROM USER_ACCOUNT WHERE UPPER(Username) = UPPER(?)")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("UserID");
                    try (PreparedStatement ps2 = conn.prepareStatement("SELECT InnovatorID FROM INNOVATOR WHERE UserID = ?")) {
                        ps2.setInt(1, userId);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                return rs2.getInt("InnovatorID");
                            }
                        }
                    }

                    int innovatorId = 1;
                    try (PreparedStatement ps2 = conn.prepareStatement("SELECT NVL(MAX(InnovatorID),0)+1 FROM INNOVATOR")) {
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                innovatorId = rs2.getInt(1);
                            }
                        }
                    }

                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "INSERT INTO INNOVATOR (InnovatorID, UserID, DeptID, Name, Email, Phone, Expertise) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        ps2.setInt(1, innovatorId);
                        ps2.setInt(2, userId);
                        ps2.setInt(3, 1);
                        ps2.setString(4, username);
                        ps2.setString(5, username + "@local");
                        ps2.setNull(6, java.sql.Types.VARCHAR);
                        ps2.setNull(7, java.sql.Types.VARCHAR);
                        ps2.executeUpdate();
                    }
                    return innovatorId;
                }
            }
        }
        return -1;
    }

    private void clearForm() {
        ideaField.setText("");
        descArea.setText("");
        attachmentField.setText("");
        selectedFile = null;
        if (departmentCombo.getItemCount() > 0) {
            departmentCombo.setSelectedIndex(0);
        }
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

    private boolean ideaColumnExists(Connection conn, String columnName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'IDEA' AND COLUMN_NAME = ?")) {
            ps.setString(1, columnName.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Integer resolveActiveCallId(Connection conn) throws SQLException {
        if (!tableExists(conn, "INNOVATION_CALL")) {
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT CALLID FROM INNOVATION_CALL WHERE UPPER(STATUS) = 'OPEN' AND ROWNUM = 1")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CALLID");
                }
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT MIN(CALLID) FROM INNOVATION_CALL")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int callId = rs.getInt(1);
                    return rs.wasNull() ? null : callId;
                }
            }
        }
        return null;
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM USER_TABLES WHERE TABLE_NAME = ?")) {
            ps.setString(1, tableName.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void ensureIdeaHasDeptColumn(Connection conn) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getColumns(null, null, "IDEA", "DEPTID")) {
            if (!rs.next()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("ALTER TABLE IDEA ADD (DEPTID NUMBER(6))");
                }
            }
        }
    }

    private static class DeptItem {
        private final int id;
        private final String name;

        public DeptItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
