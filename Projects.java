//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
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

public class Projects extends JFrame {

    private final Container c;
    private final ImageIcon icon;
    private final JLabel titleLabel;
    private final JLabel welcomeLabel;
    private final JLabel projectLabel;
    private final JComboBox<ProjectOption> projectCombo;
    private final JLabel adminLabel;
    private final JTextField adminField;
    private final JLabel ratingLabel;
    private final JTextField ratingField;
    private final JLabel attachmentLabel;
    private final JTextField attachmentField;
    private final JLabel statusLabel;
    private final JComboBox<String> statusCombo;
    private final JLabel descriptionLabel;
    private final JTextArea descriptionArea;
    private final JLabel startDateLabel;
    private final JTextField startDateField;
    private final JLabel endDateLabel;
    private final JTextField endDateField;
    private final JLabel membersLabel;
    private final JList<String> innovatorList;
    private final DefaultListModel<String> innovatorModel;
    private final JLabel projectMembersLabel;
    private final JTable memberTable;
    private final DefaultTableModel memberTableModel;
    private final JButton createProjectButton;
    private final JButton addMemberButton;
    private final JButton removeMemberButton;
    private final JButton refreshButton;
    private final JButton backButton;
    private final JButton signOutButton;
    private final Cursor cursor;
    private final String username;

    private int currentIdeaId = -1;
    private int currentProjectId = -1;
    private int currentInnovatorId = -1;
    private int loggedInnovatorId = -1;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Projects(String username) {
        this.username = username;
        setTitle("Projects - IMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F5F7FB"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("Project Management");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(30, 25, 420, 40);
        c.add(titleLabel);

        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setBounds(30, 75, 400, 24);
        c.add(welcomeLabel);

        projectLabel = new JLabel("Accepted idea as project");
        projectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        projectLabel.setBounds(30, 120, 240, 24);
        c.add(projectLabel);

        projectCombo = new JComboBox<>();
        projectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        projectCombo.setBounds(30, 150, 420, 36);
        c.add(projectCombo);

        adminLabel = new JLabel("Accepted by admin");
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        adminLabel.setBounds(30, 220, 180, 24);
        c.add(adminLabel);

        adminField = new JTextField();
        adminField.setBounds(30, 250, 420, 36);
        adminField.setEditable(false);
        c.add(adminField);

        ratingLabel = new JLabel("Admin rating");
        ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ratingLabel.setBounds(30, 310, 180, 24);
        c.add(ratingLabel);

        ratingField = new JTextField();
        ratingField.setBounds(30, 340, 180, 36);
        ratingField.setEditable(false);
        c.add(ratingField);

        attachmentLabel = new JLabel("Attachment");
        attachmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        attachmentLabel.setBounds(240, 310, 180, 24);
        c.add(attachmentLabel);

        attachmentField = new JTextField();
        attachmentField.setBounds(240, 340, 210, 36);
        attachmentField.setEditable(false);
        c.add(attachmentField);

        statusLabel = new JLabel("Project status");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setBounds(470, 310, 180, 24);
        c.add(statusLabel);

        statusCombo = new JComboBox<>(new String[] {"ONGOING", "COMPLETED"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusCombo.setBounds(470, 340, 180, 36);
        c.add(statusCombo);

        descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionLabel.setBounds(30, 410, 220, 24);
        c.add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBounds(30, 440, 420, 100);
        c.add(descriptionScroll);

        startDateLabel = new JLabel("Start date (yyyy-MM-dd)");
        startDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        startDateLabel.setBounds(470, 410, 220, 24);
        c.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setBounds(470, 440, 180, 36);
        c.add(startDateField);

        endDateLabel = new JLabel("End date (yyyy-MM-dd)");
        endDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        endDateLabel.setBounds(470, 490, 220, 24);
        c.add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setBounds(470, 520, 180, 36);
        c.add(endDateField);

        createProjectButton = createButton("Create Project", 670, 150, Color.decode("#2E75B6"));
        refreshButton = createButton("Refresh", 670, 220, Color.decode("#4A7A9D"));
        addMemberButton = createButton("Add Member", 670, 290, Color.decode("#1F7A1F"));
        removeMemberButton = createButton("Remove Member", 670, 360, Color.decode("#DC3545"));
        backButton = createButton("Back", 670, 430, Color.decode("#6C757D"));
        signOutButton = createButton("Sign Out", 670, 500, Color.decode("#D9534F"));

        membersLabel = new JLabel("Innovators available as project members");
        membersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        membersLabel.setBounds(30, 560, 320, 24);
        c.add(membersLabel);

        innovatorModel = new DefaultListModel<>();
        innovatorList = new JList<>(innovatorModel);
        innovatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        innovatorList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JScrollPane listScroll = new JScrollPane(innovatorList);
        listScroll.setBounds(30, 590, 400, 140);
        c.add(listScroll);

        projectMembersLabel = new JLabel("Project members");
        projectMembersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        projectMembersLabel.setBounds(450, 560, 180, 24);
        c.add(projectMembersLabel);

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
        memberScroll.setBounds(450, 590, 500, 140);
        c.add(memberScroll);

        projectCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                loadSelectedIdeaDetails();
            }
        });

        innovatorList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selected = innovatorList.getSelectedIndex();
                    if (selected >= 0) {
                        String selectedValue = innovatorModel.getElementAt(selected);
                        String[] parts = selectedValue.split("\\|");
                        if (parts.length >= 2) {
                            currentInnovatorId = Integer.parseInt(parts[0].trim());
                        }
                    }
                }
            }
        });

        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                createProject();
            }
        });

        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addProjectMember();
            }
        });

        removeMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                removeProjectMember();
            }
        });

        memberTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = memberTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        currentInnovatorId = Integer.parseInt(memberTableModel.getValueAt(selectedRow, 0).toString());
                    }
                }
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
                UserHome frame = new UserHome(username);
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

        ensureProjectTables();
        refreshProjects();
    }

    private JButton createButton(String text, int x, int y, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 180, 45);
        c.add(button);
        return button;
    }

    private void ensureProjectTables() {
        try (Connection conn = DBConnection.getConnection()) {
            if (!tableExists(conn, "INNOVATION_PROJECT")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE INNOVATION_PROJECT (PROJECTID NUMBER(10) PRIMARY KEY, IDEAID NUMBER(10), PROJECTTITLE VARCHAR2(200), STARTDATE DATE, ENDDATE DATE, STATUS VARCHAR2(12) DEFAULT 'ONGOING', DESCRIPTION VARCHAR2(300), CREATEDBY NUMBER(10), CREATEDDATE DATE, CONSTRAINT ck_project_status CHECK (STATUS IN ('ONGOING','COMPLETED'))) ");
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
            } else {
                if (!columnExists(conn, "PROJECT_MEMBER", "JOINDATE")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("ALTER TABLE PROJECT_MEMBER ADD (JOINDATE DATE)");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to prepare project tables: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private void refreshProjects() {
        loadAcceptedIdeas();
        loadInnovators();
        clearSelections();
    }

    private void loadProjectMembers(Connection conn) throws SQLException {
        memberTableModel.setRowCount(0);
        if (currentProjectId <= 0) {
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT pm.INNOVATORID, NVL(i.Name, 'Unknown') AS NAME, pm.JOINDATE " +
                "FROM PROJECT_MEMBER pm LEFT JOIN INNOVATOR i ON pm.INNOVATORID = i.INNOVATORID " +
                "WHERE pm.PROJECTID = ? ORDER BY pm.JOINDATE")) {
            ps.setInt(1, currentProjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date joinDate = null;
                    try {
                        joinDate = rs.getDate("JOINDATE");
                    } catch (SQLException ex) {
                        // Column may not exist in older schema; ignore and leave blank.
                    }
                    memberTableModel.addRow(new Object[]{rs.getInt("INNOVATORID"), rs.getString("NAME"), joinDate != null ? joinDate.toString() : ""});
                }
            }
        }
    }

    private void removeProjectMember() {
        if (currentProjectId <= 0) {
            JOptionPane.showMessageDialog(this, "Create or select a project first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a project member to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int innovatorId = Integer.parseInt(memberTableModel.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Remove selected member from this project?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM PROJECT_MEMBER WHERE PROJECTID = ? AND INNOVATORID = ?")) {
                ps.setInt(1, currentProjectId);
                ps.setInt(2, innovatorId);
                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Project member removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProjectMembers(conn);
                    currentInnovatorId = -1;
                } else {
                    JOptionPane.showMessageDialog(this, "Member was not found in this project.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to remove member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

    private void clearSelections() {
        adminField.setText("");
        ratingField.setText("");
        attachmentField.setText("");
        descriptionArea.setText("");
        statusCombo.setSelectedIndex(0);
        startDateField.setText("");
        endDateField.setText("");
        memberTableModel.setRowCount(0);
        currentIdeaId = -1;
        currentProjectId = -1;
        currentInnovatorId = -1;
    }

    private void loadAcceptedIdeas() {
        DefaultComboBoxModel<ProjectOption> model = new DefaultComboBoxModel<>();
        projectCombo.setModel(model);
        try (Connection conn = DBConnection.getConnection()) {
            loggedInnovatorId = resolveLoggedInnovatorId(conn);
            if (loggedInnovatorId <= 0) {
                JOptionPane.showMessageDialog(this, "No innovator profile was found for this user.", "User Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT IDEAID, TITLE FROM IDEA WHERE INNOVATORID = ? AND UPPER(STATUS) = 'APPROVED' ORDER BY IDEAID")) {
                ps.setInt(1, loggedInnovatorId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        model.addElement(new ProjectOption(rs.getInt("IDEAID"), rs.getString("TITLE")));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load accepted ideas: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int resolveLoggedInnovatorId(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT UserID FROM USER_ACCOUNT WHERE Username = ?")) {
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
                }
            }
        }
        return -1;
    }

    private void loadSelectedIdeaDetails() {
        ProjectOption option = (ProjectOption) projectCombo.getSelectedItem();
        if (option == null) {
            clearSelections();
            return;
        }
        currentIdeaId = option.ideaId;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT e.SCORE, ua.Username, au.Name FROM EVALUATION e " +
                    "LEFT JOIN ADMIN_USER au ON e.ADMINID = au.AdminID " +
                    "LEFT JOIN USER_ACCOUNT ua ON au.UserID = ua.UserID " +
                    "WHERE e.IDEAID = ? ORDER BY e.EVALUATIONDATE DESC")) {
                ps.setInt(1, currentIdeaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        adminField.setText(rs.getString("Username") != null ? rs.getString("Username") : rs.getString("Name"));
                        ratingField.setText(String.valueOf(rs.getInt("SCORE")));
                    } else {
                        adminField.setText("Not evaluated yet");
                        ratingField.setText("-" );
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT FILENAME, FILEURL FROM ATTACHMENT WHERE IDEAID = ? ORDER BY ATTACHMENTID DESC")) {
                ps.setInt(1, currentIdeaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        attachmentField.setText(rs.getString("FILENAME") + (rs.getString("FILEURL") != null ? " | " + rs.getString("FILEURL") : ""));
                    } else {
                        attachmentField.setText("No attachment");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT PROJECTID, STARTDATE, ENDDATE, STATUS, DESCRIPTION FROM INNOVATION_PROJECT WHERE IDEAID = ?")) {
                ps.setInt(1, currentIdeaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        currentProjectId = rs.getInt("PROJECTID");
                        startDateField.setText(rs.getDate("STARTDATE") != null ? rs.getDate("STARTDATE").toString() : "");
                        endDateField.setText(rs.getDate("ENDDATE") != null ? rs.getDate("ENDDATE").toString() : "");
                        String status = rs.getString("STATUS");
                        statusCombo.setSelectedItem(status != null ? status.toUpperCase() : "ONGOING");
                        descriptionArea.setText(rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION") : "");
                        loadProjectMembers(conn);
                    } else {
                        startDateField.setText("");
                        endDateField.setText("");
                        statusCombo.setSelectedIndex(0);
                        descriptionArea.setText("");
                        memberTableModel.setRowCount(0);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load idea details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createProject() {
        if (currentIdeaId <= 0) {
            JOptionPane.showMessageDialog(this, "Select an accepted idea first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (startDateField.getText().trim().isEmpty() || endDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both start and end dates.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText().trim(), DATE_FORMAT);
            LocalDate endDate = LocalDate.parse(endDateField.getText().trim(), DATE_FORMAT);
            if (!endDate.isAfter(startDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try (Connection conn = DBConnection.getConnection()) {
                int projectId = currentProjectId > 0 ? currentProjectId : nextId(conn, "INNOVATION_PROJECT", "PROJECTID");
                if (currentProjectId <= 0) {
                    String insertSql = "INSERT INTO INNOVATION_PROJECT (PROJECTID, IDEAID, PROJECTTITLE, STARTDATE, ENDDATE, STATUS, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setInt(1, projectId);
                        ps.setInt(2, currentIdeaId);
                        ps.setString(3, ((ProjectOption) projectCombo.getSelectedItem()).title);
                        ps.setDate(4, java.sql.Date.valueOf(startDate));
                        ps.setDate(5, java.sql.Date.valueOf(endDate));
                        ps.setString(6, statusCombo.getSelectedItem().toString());
                        ps.setString(7, descriptionArea.getText().trim());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE INNOVATION_PROJECT SET PROJECTTITLE = ?, STARTDATE = ?, ENDDATE = ?, STATUS = ?, DESCRIPTION = ? WHERE PROJECTID = ?")) {
                        ps.setString(1, ((ProjectOption) projectCombo.getSelectedItem()).title);
                        ps.setDate(2, java.sql.Date.valueOf(startDate));
                        ps.setDate(3, java.sql.Date.valueOf(endDate));
                        ps.setString(4, statusCombo.getSelectedItem().toString());
                        ps.setString(5, descriptionArea.getText().trim());
                        ps.setInt(6, currentProjectId);
                        ps.executeUpdate();
                    }
                }
                currentProjectId = projectId;
                JOptionPane.showMessageDialog(this, "Project saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use the yyyy-MM-dd date format.", "Date Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to save project: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProjectMember() {
        if (currentProjectId <= 0) {
            JOptionPane.showMessageDialog(this, "Create the project first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentInnovatorId <= 0) {
            JOptionPane.showMessageDialog(this, "Select an innovator from the list.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            boolean hasJoinDate = columnExists(conn, "PROJECT_MEMBER", "JOINDATE");
            boolean hasMemberId = columnExists(conn, "PROJECT_MEMBER", "MEMBERID");
            int memberId = -1;
            if (hasMemberId) {
                memberId = nextId(conn, "PROJECT_MEMBER", "MEMBERID");
            }
            String insertSql;
            if (hasMemberId && hasJoinDate) {
                insertSql = "INSERT INTO PROJECT_MEMBER (MEMBERID, PROJECTID, INNOVATORID, JOINDATE) SELECT ?, ?, ?, SYSDATE FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM PROJECT_MEMBER WHERE PROJECTID = ? AND INNOVATORID = ?)";
            } else if (hasMemberId) {
                insertSql = "INSERT INTO PROJECT_MEMBER (MEMBERID, PROJECTID, INNOVATORID) SELECT ?, ?, ? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM PROJECT_MEMBER WHERE PROJECTID = ? AND INNOVATORID = ?)";
            } else if (hasJoinDate) {
                insertSql = "INSERT INTO PROJECT_MEMBER (PROJECTID, INNOVATORID, JOINDATE) SELECT ?, ?, SYSDATE FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM PROJECT_MEMBER WHERE PROJECTID = ? AND INNOVATORID = ?)";
            } else {
                insertSql = "INSERT INTO PROJECT_MEMBER (PROJECTID, INNOVATORID) SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM PROJECT_MEMBER WHERE PROJECTID = ? AND INNOVATORID = ?)";
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                int idx = 1;
                if (hasMemberId) {
                    ps.setInt(idx++, memberId);
                }
                ps.setInt(idx++, currentProjectId);
                ps.setInt(idx++, currentInnovatorId);
                ps.setInt(idx++, currentProjectId);
                ps.setInt(idx++, currentInnovatorId);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Innovator added to project.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "This innovator is already a project member.", "Duplicate", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to add member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInnovators() {
        innovatorModel.clear();
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT i.InnovatorID, i.Name, ua.Username FROM INNOVATOR i LEFT JOIN USER_ACCOUNT ua ON i.UserID = ua.UserID ORDER BY i.Name")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        innovatorModel.addElement(rs.getInt("InnovatorID") + " | " + rs.getString("Name") + " (" + (rs.getString("Username") != null ? rs.getString("Username") : "") + ")");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load innovators: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int nextId(Connection conn, String table, String column) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT NVL(MAX(" + column + "), 0) + 1 FROM " + table)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 1;
    }

    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        java.sql.DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase())) {
            return rs.next();
        }
    }

    private static class ProjectOption {
        private final int ideaId;
        private final String title;

        private ProjectOption(int ideaId, String title) {
            this.ideaId = ideaId;
            this.title = title;
        }

        @Override
        public String toString() {
            return title + " (Idea ID: " + ideaId + ")";
        }
    }

    public static void main(String[] args) {
        Projects frame = new Projects("Admin");
        frame.setVisible(true);
    }
}
