//******************American International University-Bangladesh (AIUB) */
//******************Advanced Database Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AdminProject extends JFrame {

    private final Container c;
    private final ImageIcon icon;
    private final JLabel titleLabel;
    private final JLabel userLabel;
    private final JLabel projectListLabel;
    private final JTable projectTable;
    private final DefaultTableModel projectTableModel;
    private final JLabel nameLabel;
    private final JTextField nameField;
    private final JLabel statusLabel;
    private final JComboBox<String> statusCombo;
    private final JLabel descriptionLabel;
    private final JTextArea descriptionArea;
    private final JLabel startDateLabel;
    private final JTextField startDateField;
    private final JLabel endDateLabel;
    private final JTextField endDateField;
    private final JLabel memberLabel;
    private final JTable memberTable;
    private final DefaultTableModel memberTableModel;
    private final JButton saveButton;
    private final JButton deleteButton;
    private final JButton refreshButton;
    private final JButton backButton;
    private final JButton logoutButton;
    private final Cursor cursor;
    private final String username;

    private int selectedProjectId = -1;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AdminProject(String username) {
        this.username = username;
        setTitle("Admin Project Management - IMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F2F2F2"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("Admin Project Management");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(30, 20, 520, 40);
        c.add(titleLabel);

        userLabel = new JLabel("User: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setBounds(30, 70, 320, 24);
        c.add(userLabel);

        projectListLabel = new JLabel("Projects accepted by admin");
        projectListLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        projectListLabel.setBounds(30, 110, 320, 24);
        c.add(projectListLabel);

        String[] projectColumns = {"ProjectID", "Project Title", "Status", "Start Date", "End Date"};
        projectTableModel = new DefaultTableModel(projectColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        projectTable = new JTable(projectTableModel);
        projectTable.removeColumn(projectTable.getColumnModel().getColumn(0));
        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectTable.setRowHeight(28);
        JScrollPane projectScroll = new JScrollPane(projectTable);
        projectScroll.setBounds(30, 150, 900, 220);
        c.add(projectScroll);

        nameLabel = new JLabel("Project Title:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameLabel.setBounds(30, 390, 200, 24);
        c.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setBounds(30, 420, 520, 36);
        c.add(nameField);

        statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        statusLabel.setBounds(570, 390, 120, 24);
        c.add(statusLabel);

        statusCombo = new JComboBox<>(new String[] {"ONGOING", "COMPLETED"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusCombo.setBounds(570, 420, 180, 36);
        c.add(statusCombo);

        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        descriptionLabel.setBounds(30, 470, 200, 24);
        c.add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBounds(30, 500, 520, 120);
        c.add(descriptionScroll);

        startDateLabel = new JLabel("Start date (yyyy-MM-dd):");
        startDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        startDateLabel.setBounds(570, 470, 240, 24);
        c.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        startDateField.setBounds(570, 500, 180, 36);
        c.add(startDateField);

        endDateLabel = new JLabel("End date (yyyy-MM-dd):");
        endDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        endDateLabel.setBounds(570, 550, 240, 24);
        c.add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        endDateField.setBounds(570, 580, 180, 36);
        c.add(endDateField);

        memberLabel = new JLabel("Project members:");
        memberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        memberLabel.setBounds(30, 640, 200, 24);
        c.add(memberLabel);

        String[] memberColumns = {"MemberID", "Name", "Joined"};
        memberTableModel = new DefaultTableModel(memberColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.removeColumn(memberTable.getColumnModel().getColumn(0));
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(28);
        JScrollPane memberScroll = new JScrollPane(memberTable);
        memberScroll.setBounds(30, 670, 520, 120);
        c.add(memberScroll);

        saveButton = createButton("Save Changes", 570, 630, Color.decode("#2E75B6"));
        deleteButton = createButton("Delete Project", 570, 690, Color.decode("#DC3545"));
        refreshButton = createButton("Refresh", 770, 630, Color.decode("#4A7A9D"));
        backButton = createButton("Back", 570, 745, Color.decode("#6C757D"));
        logoutButton = createButton("Logout", 770, 745, Color.decode("#D9534F"));

        projectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = projectTable.getSelectedRow();
                    if (row >= 0) {
                        selectedProjectId = Integer.parseInt(projectTableModel.getValueAt(row, 0).toString());
                        loadSelectedProjectDetails();
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveProject();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deleteProject();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshProjects();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Admin frame = new Admin(username);
                frame.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Login frame = new Login();
                frame.setVisible(true);
            }
        });

        ensureTables();
        refreshProjects();
    }

    private JButton createButton(String text, int x, int y, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 180, 40);
        c.add(button);
        return button;
    }

    private void ensureTables() {
        try (Connection conn = DBConnection.getConnection()) {
            if (!tableExists(conn, "INNOVATION_PROJECT")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE INNOVATION_PROJECT (PROJECTID NUMBER(10) PRIMARY KEY, IDEAID NUMBER(10), PROJECTTITLE VARCHAR2(200), STARTDATE DATE, ENDDATE DATE, STATUS VARCHAR2(12) DEFAULT 'ONGOING', DESCRIPTION VARCHAR2(300), CREATEDBY NUMBER(10), CREATEDDATE DATE, CONSTRAINT CK_PROJECT_STATUS CHECK (STATUS IN ('ONGOING','COMPLETED'))) ");
                }
            } else {
                if (!columnExists(conn, "INNOVATION_PROJECT", "DESCRIPTION")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("ALTER TABLE INNOVATION_PROJECT ADD (DESCRIPTION VARCHAR2(300))");
                    }
                }
                if (!columnExists(conn, "INNOVATION_PROJECT", "STATUS")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("ALTER TABLE INNOVATION_PROJECT ADD (STATUS VARCHAR2(12) DEFAULT 'ONGOING')");
                    }
                }
                if (!constraintExists(conn, "INNOVATION_PROJECT", "CK_PROJECT_STATUS")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("ALTER TABLE INNOVATION_PROJECT ADD CONSTRAINT CK_PROJECT_STATUS CHECK (STATUS IN ('ONGOING','COMPLETED'))");
                    }
                }
            }
            if (!tableExists(conn, "PROJECT_MEMBER")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE PROJECT_MEMBER (PROJECTID NUMBER(10), INNOVATORID NUMBER(10), JOINDATE DATE, PRIMARY KEY (PROJECTID, INNOVATORID))");
                }
            } else if (!columnExists(conn, "PROJECT_MEMBER", "JOINDATE")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("ALTER TABLE PROJECT_MEMBER ADD (JOINDATE DATE)");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to ensure tables: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase())) {
            return rs.next();
        }
    }

    private boolean constraintExists(Connection conn, String tableName, String constraintName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM USER_CONSTRAINTS WHERE TABLE_NAME = ? AND CONSTRAINT_NAME = ?")) {
            ps.setString(1, tableName.toUpperCase());
            ps.setString(2, constraintName.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void refreshProjects() {
        projectTableModel.setRowCount(0);
        memberTableModel.setRowCount(0);
        selectedProjectId = -1;
        nameField.setText("");
        statusCombo.setSelectedIndex(0);
        descriptionArea.setText("");
        startDateField.setText("");
        endDateField.setText("");

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT PROJECTID, PROJECTTITLE, STATUS, STARTDATE, ENDDATE FROM INNOVATION_PROJECT ORDER BY PROJECTID")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        projectTableModel.addRow(new Object[]{rs.getInt("PROJECTID"), rs.getString("PROJECTTITLE"), rs.getString("STATUS"), rs.getDate("STARTDATE") != null ? rs.getDate("STARTDATE").toString() : "", rs.getDate("ENDDATE") != null ? rs.getDate("ENDDATE").toString() : ""});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load projects: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedProjectDetails() {
        if (selectedProjectId <= 0) {
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT PROJECTTITLE, STATUS, DESCRIPTION, STARTDATE, ENDDATE FROM INNOVATION_PROJECT WHERE PROJECTID = ?")) {
                ps.setInt(1, selectedProjectId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nameField.setText(rs.getString("PROJECTTITLE"));
                        statusCombo.setSelectedItem(rs.getString("STATUS") != null ? rs.getString("STATUS").toUpperCase() : "ONGOING");
                        descriptionArea.setText(rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION") : "");
                        startDateField.setText(rs.getDate("STARTDATE") != null ? rs.getDate("STARTDATE").toString() : "");
                        endDateField.setText(rs.getDate("ENDDATE") != null ? rs.getDate("ENDDATE").toString() : "");
                    }
                }
            }
            loadProjectMembers(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load project details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProjectMembers(Connection conn) throws SQLException {
        memberTableModel.setRowCount(0);
        if (selectedProjectId <= 0) {
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT pm.INNOVATORID, NVL(i.NAME, 'Unknown') AS NAME, pm.JOINDATE FROM PROJECT_MEMBER pm LEFT JOIN INNOVATOR i ON pm.INNOVATORID = i.INNOVATORID WHERE pm.PROJECTID = ? ORDER BY pm.JOINDATE")) {
            ps.setInt(1, selectedProjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date joined = null;
                    try {
                        joined = rs.getDate("JOINDATE");
                    } catch (SQLException ex) {
                        joined = null;
                    }
                    memberTableModel.addRow(new Object[]{rs.getInt("INNOVATORID"), rs.getString("NAME"), joined != null ? joined.toString() : ""});
                }
            }
        }
    }

    private void saveProject() {
        if (selectedProjectId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a project before saving.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String title = nameField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        String description = descriptionArea.getText().trim();
        String startDateText = startDateField.getText().trim();
        String endDateText = endDateField.getText().trim();

        if (title.isEmpty() || startDateText.isEmpty() || endDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Project title, start date, and end date are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateText, DATE_FORMAT);
            LocalDate endDate = LocalDate.parse(endDateText, DATE_FORMAT);
            if (!endDate.isAfter(startDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try (Connection conn = DBConnection.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE INNOVATION_PROJECT SET PROJECTTITLE = ?, STATUS = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ? WHERE PROJECTID = ?")) {
                    ps.setString(1, title);
                    ps.setString(2, status);
                    ps.setString(3, description);
                    ps.setDate(4, java.sql.Date.valueOf(startDate));
                    ps.setDate(5, java.sql.Date.valueOf(endDate));
                    ps.setInt(6, selectedProjectId);
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Project updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshProjects();
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use the yyyy-MM-dd date format.", "Date Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to save project: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProject() {
        if (selectedProjectId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a project before deleting.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this project and all member links?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PROJECT_MEMBER WHERE PROJECTID = ?")) {
                ps.setInt(1, selectedProjectId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM INNOVATION_PROJECT WHERE PROJECTID = ?")) {
                ps.setInt(1, selectedProjectId);
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Project deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshProjects();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to delete project: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        AdminProject frame = new AdminProject("Admin");
        frame.setVisible(true);
    }
}
