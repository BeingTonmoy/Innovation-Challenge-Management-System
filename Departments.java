//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Departments extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel deptNameLabel;
    private JLabel descriptionLabel;
    private JLabel locationLabel;
    private JTextField deptNameField;
    private JTextField descriptionField;
    private JTextField locationField;
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    private JButton logoutButton;
    private Cursor cursor;
    private final String username;
    private int selectedDeptId = -1;

    public Departments(String username) {
        this.username = username;
        setTitle("Department Administration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 620);
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

        titleLabel = new JLabel("Department Management");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(30, 20, 520, 40);
        c.add(titleLabel);

        userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setBounds(30, 70, 420, 24);
        c.add(userLabel);

        deptNameLabel = new JLabel("Department Name:");
        deptNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        deptNameLabel.setBounds(30, 120, 200, 26);
        c.add(deptNameLabel);

        deptNameField = new JTextField();
        deptNameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        deptNameField.setBounds(30, 150, 380, 36);
        c.add(deptNameField);

        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        descriptionLabel.setBounds(30, 200, 200, 26);
        c.add(descriptionLabel);

        descriptionField = new JTextField();
        descriptionField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        descriptionField.setBounds(30, 230, 380, 36);
        c.add(descriptionField);

        locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        locationLabel.setBounds(30, 280, 200, 26);
        c.add(locationLabel);

        locationField = new JTextField();
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        locationField.setBounds(30, 310, 380, 36);
        c.add(locationField);

        addButton = createButton("Add Department", 440, 140, Color.decode("#2E75B6"));
        updateButton = createButton("Update Department", 440, 210, Color.decode("#4A7A9D"));
        deleteButton = createButton("Delete Department", 440, 280, Color.decode("#D9534F"));
        refreshButton = createButton("Refresh", 440, 350, Color.decode("#5A9B67"));
        backButton = createButton("Back to Admin", 440, 420, Color.decode("#6C757D"));
        logoutButton = createButton("Logout", 440, 490, Color.decode("#A93226"));

        String[] columns = {"DeptID", "DeptName", "Description", "Location"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        departmentTable = new JTable(tableModel);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentTable.setRowHeight(28);
        departmentTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        departmentTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        departmentTable.getColumnModel().getColumn(2).setPreferredWidth(260);
        departmentTable.getColumnModel().getColumn(3).setPreferredWidth(160);

        JScrollPane tableScroll = new JScrollPane(departmentTable);
        tableScroll.setBounds(30, 370, 380, 180);
        c.add(tableScroll);

        departmentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = departmentTable.getSelectedRow();
                if (row >= 0) {
                    selectedDeptId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    deptNameField.setText(tableModel.getValueAt(row, 1).toString());
                    descriptionField.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
                    locationField.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                }
            }
        });

        addButton.addActionListener(e -> addDepartment());
        updateButton.addActionListener(e -> updateDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());
        refreshButton.addActionListener(e -> loadDepartments());
        backButton.addActionListener(e -> {
            setVisible(false);
            Admin frame = new Admin(username);
            frame.setVisible(true);
        });
        logoutButton.addActionListener(e -> {
            setVisible(false);
            Login frame = new Login();
            frame.setVisible(true);
        });

        ensureDepartmentTable();
        loadDepartments();
    }

    private JButton createButton(String text, int x, int y, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 380, 50);
        c.add(button);
        return button;
    }

    private void ensureDepartmentTable() {
        try (Connection conn = DBConnection.getConnection()) {
            if (!tableExists(conn, "DEPARTMENT")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE DEPARTMENT (DeptID NUMBER(6) NOT NULL, DeptName VARCHAR2(60) NOT NULL, Description VARCHAR2(200), Location VARCHAR2(60), CONSTRAINT pk_department PRIMARY KEY (DeptID))");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to prepare department table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private void loadDepartments() {
        tableModel.setRowCount(0);
        selectedDeptId = -1;
        deptNameField.setText("");
        descriptionField.setText("");
        locationField.setText("");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DeptID, DeptName, Description, Location FROM DEPARTMENT ORDER BY DeptID")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("DeptID"),
                        rs.getString("DeptName"),
                        rs.getString("Description"),
                        rs.getString("Location")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load departments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDepartment() {
        String name = deptNameField.getText().trim();
        String desc = descriptionField.getText().trim();
        String location = locationField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int deptId = nextId(conn, "DEPARTMENT", "DeptID");
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO DEPARTMENT (DeptID, DeptName, Description, Location) VALUES (?, ?, ?, ?)") ) {
                ps.setInt(1, deptId);
                ps.setString(2, name);
                ps.setString(3, desc.isEmpty() ? null : desc);
                ps.setString(4, location.isEmpty() ? null : location);
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Department added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDepartments();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to add department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDepartment() {
        if (selectedDeptId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a department first.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = deptNameField.getText().trim();
        String desc = descriptionField.getText().trim();
        String location = locationField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE DEPARTMENT SET DeptName = ?, Description = ?, Location = ? WHERE DeptID = ?")) {
            ps.setString(1, name);
            ps.setString(2, desc.isEmpty() ? null : desc);
            ps.setString(3, location.isEmpty() ? null : location);
            ps.setInt(4, selectedDeptId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Department updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDepartments();
            } else {
                JOptionPane.showMessageDialog(this, "No department record was updated.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to update department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDepartment() {
        if (selectedDeptId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a department first.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected department?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM DEPARTMENT WHERE DeptID = ?")) {
            ps.setInt(1, selectedDeptId);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Department deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDepartments();
            } else {
                JOptionPane.showMessageDialog(this, "No department record was deleted.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to delete department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

    public static void main(String[] args) {
        Departments frame = new Departments("Admin");
        frame.setVisible(true);
    }
}
