import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Users extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JLabel idLabel;
    private JTextField idField;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel roleLabel;
    private JComboBox<String> roleCombo;
    private JLabel statusLabel;
    private JComboBox<String> statusCombo;
    private JButton refreshButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton logoutButton;
    private final String username;
    private Cursor cursor;

    public Users(String username) {
        this.username = username;
        setTitle("Users - IMS Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#EDF2F7"));

        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 34));
        titleLabel.setBounds(30, 20, 380, 40);
        c.add(titleLabel);

        userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setBounds(30, 70, 360, 24);
        c.add(userLabel);

        tableModel = new DefaultTableModel(new Object[]{"UserID", "Username", "Role", "Email", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(28);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tableScroll = new JScrollPane(userTable);
        tableScroll.setBounds(30, 110, 800, 280);
        tableScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#AEC6EA")), "User Accounts"));
        c.add(tableScroll);

        idLabel = new JLabel("User ID");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idLabel.setBounds(30, 410, 100, 24);
        c.add(idLabel);

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idField.setBounds(30, 440, 180, 36);
        idField.setEditable(false);
        c.add(idField);

        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameLabel.setBounds(240, 410, 120, 24);
        c.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBounds(240, 440, 220, 36);
        c.add(usernameField);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setBounds(490, 410, 80, 24);
        c.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setBounds(490, 440, 210, 36);
        c.add(emailField);

        roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setBounds(30, 490, 100, 24);
        c.add(roleLabel);

        roleCombo = new JComboBox<>(new String[]{"INNOVATOR", "ADMIN"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleCombo.setBounds(30, 520, 180, 36);
        c.add(roleCombo);

        statusLabel = new JLabel("Status");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setBounds(240, 490, 100, 24);
        c.add(statusLabel);

        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusCombo.setBounds(240, 520, 220, 36);
        c.add(statusCombo);

        refreshButton = createButton("Refresh List", 30, 570, Color.decode("#2E75B6"));
        saveButton = createButton("Save Changes", 260, 570, Color.decode("#4A7A9D"));
        deleteButton = createButton("Delete User", 460, 570, Color.decode("#D9534F"));
        backButton = createButton("Back to Admin", 20, 570, Color.decode("#2E75B6"));
        logoutButton = createButton("Logout", 660, 570, Color.decode("#C00000"));

        backButton.setBounds(30, 565, 180, 40);
        logoutButton.setBounds(660, 565, 170, 40);

        c.add(refreshButton);
        c.add(saveButton);
        c.add(deleteButton);
        c.add(backButton);
        c.add(logoutButton);

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

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                loadUsers();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deleteUser();
            }
        });

        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    populateSelectedUser();
                }
            }
        });

        loadUsers();
    }

    private JButton createButton(String text, int x, int y, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(cursor);
        button.setBounds(x, y, 180, 40);
        return button;
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT UserID, Username, Role, Email, Status FROM USER_ACCOUNT ORDER BY UserID")) { // Adjusted query to select only necessary columns   
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getString("Role"),
                            rs.getString("Email"),
                            rs.getString("Status")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        clearSelection();
    }

    private void clearSelection() {
        userTable.clearSelection();
        idField.setText("");
        usernameField.setText("");
        emailField.setText("");
        roleCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
    }

    private void populateSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            return;
        }

        idField.setText(String.valueOf(userTable.getValueAt(row, 0)));
        usernameField.setText(String.valueOf(userTable.getValueAt(row, 1)));
        roleCombo.setSelectedItem(String.valueOf(userTable.getValueAt(row, 2)));
        emailField.setText(String.valueOf(userTable.getValueAt(row, 3)));
        statusCombo.setSelectedItem(String.valueOf(userTable.getValueAt(row, 4)));
    }

    private void updateUser() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a user to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedUsername = usernameField.getText().trim();
        String selectedEmail = emailField.getText().trim();
        String selectedRole = roleCombo.getSelectedItem().toString();
        String selectedStatus = statusCombo.getSelectedItem().toString();

        if (selectedUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedRole.equals("ADMIN") && !selectedRole.equals("INNOVATOR")) {
            JOptionPane.showMessageDialog(this, "Role must be ADMIN or INNOVATOR.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(idField.getText());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE USER_ACCOUNT SET Username = ?, Email = ?, Role = ?, Status = ? WHERE UserID = ?")) {  // Adjusted query to update only necessary columns
            ps.setString(1, selectedUsername);
            ps.setString(2, selectedEmail);
            ps.setString(3, selectedRole);
            ps.setString(4, selectedStatus);
            ps.setInt(5, userId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "No user was updated.", "Update Failed", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to update user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedUsername = usernameField.getText().trim();
        if (selectedUsername.equalsIgnoreCase(username)) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account while logged in.", "Action Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(idField.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete user " + selectedUsername + "? This cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement deleteInnovator = conn.prepareStatement("DELETE FROM INNOVATOR WHERE UserID = ?")) {  // Adjusted query to delete only necessary records
                deleteInnovator.setInt(1, userId);
                deleteInnovator.executeUpdate();
            } catch (SQLException ignored) {
                // ignore if table or FK doesn't exist
            }
            try (PreparedStatement deleteAdminUser = conn.prepareStatement("DELETE FROM ADMIN_USER WHERE UserID = ?")) { // Adjusted query to delete only necessary records
                deleteAdminUser.setInt(1, userId);
                deleteAdminUser.executeUpdate();
            } catch (SQLException ignored) {
                // ignore if table or FK doesn't exist
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM USER_ACCOUNT WHERE UserID = ?")) { // Adjusted query to delete only necessary records
                ps.setInt(1, userId);
                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "No user was deleted.", "Delete Failed", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to delete user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
