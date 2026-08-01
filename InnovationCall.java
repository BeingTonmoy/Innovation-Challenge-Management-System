//******************American International University-Bangladesh (AIUB) */
//******************Object Oriented Programming 1 (JAVA) GUI Project - Group 5 | Date: 09/05/2024 */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
//*********************Group Members :Arfan Rahman (23-51598-2), TANMAY ROY RONY (23-51745-2), Swarna sikder (23-51779-2), Sanjida Affrin Bristi (23-51788-2) */
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InnovationCall extends JFrame {

    private Container c;
    private ImageIcon icon;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel callStatsLabel;
    private JTable ideaTable;
    private DefaultTableModel tableModel;
    private JTextField scoreField;
    private JTextArea commentsArea;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JButton backButton;
    private JButton passwordButton;
    private Cursor cursor;
    private final String username;

    public InnovationCall(String username) {
        this.username = username;
        // Frame Layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Admin Dashboard - IMS");
        this.setSize(920, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.decode("#F2F2F2"));

        // Icon
        icon = new ImageIcon(getClass().getResource("/images/Icon.png"));
        this.setIconImage(icon.getImage());

        // Cursor
        cursor = new Cursor(Cursor.HAND_CURSOR);

        titleLabel = new JLabel("Admin Dashboard - IMS");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setBounds(30, 40, 760, 45);
        c.add(titleLabel);

        userLabel = new JLabel("User: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setBounds(30, 15, 240, 25);
        c.add(userLabel);

        callStatsLabel = new JLabel("Innovation Calls: Open (0) Closed (0)");
        callStatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        callStatsLabel.setBounds(30, 90, 760, 30);
        callStatsLabel.setBorder(BorderFactory.createLineBorder(Color.decode("#2E75B6"), 2));
        c.add(callStatsLabel);

        String[] columns = {"Idea Title", "Innovator", "Category", "Status", "IdeaID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ideaTable = new JTable(tableModel);
        ideaTable.removeColumn(ideaTable.getColumnModel().getColumn(4));
        ideaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ideaTable.setRowHeight(28);
        JScrollPane tableScroll = new JScrollPane(ideaTable);
        tableScroll.setBounds(30, 130, 840, 260);
        c.add(tableScroll);

        JLabel scoreLabel = new JLabel("Score [1-10]");
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        scoreLabel.setBounds(30, 410, 200, 30);
        c.add(scoreLabel);

        scoreField = new JTextField();
        scoreField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreField.setBounds(30, 450, 200, 40);
        c.add(scoreField);

        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        commentsLabel.setBounds(260, 410, 200, 30);
        c.add(commentsLabel);

        commentsArea = new JTextArea();
        commentsArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane commentsScroll = new JScrollPane(commentsArea);
        commentsScroll.setBounds(260, 450, 440, 80);
        c.add(commentsScroll);

        approveButton = new JButton("Approve");
        approveButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        approveButton.setBackground(Color.decode("#7BB661"));
        approveButton.setForeground(Color.WHITE);
        approveButton.setCursor(cursor);
        approveButton.setBounds(720, 450, 150, 50);
        c.add(approveButton);

        rejectButton = new JButton("Reject");
        rejectButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        rejectButton.setBackground(Color.decode("#D55A61"));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setCursor(cursor);
        rejectButton.setBounds(720, 520, 150, 50);
        c.add(rejectButton);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        refreshButton.setBounds(720, 380, 150, 40);
        refreshButton.setCursor(cursor);
        c.add(refreshButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        backButton.setBounds(30, 550, 150, 40);
        backButton.setCursor(cursor);
        c.add(backButton);

        passwordButton = new JButton("Change Pass");
        passwordButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordButton.setBounds(200, 550, 170, 40);
        passwordButton.setCursor(cursor);
        c.add(passwordButton);

        logoutButton = new JButton("Log out");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        logoutButton.setBounds(390, 550, 150, 40);
        logoutButton.setCursor(cursor);
        logoutButton.setBackground(Color.decode("#C00000"));
        logoutButton.setForeground(Color.WHITE);
        c.add(logoutButton);

        loadDashboard();

        approveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                evaluateSelectedIdea("APPROVED");
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                evaluateSelectedIdea("REJECTED");
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                loadDashboard();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                Home frame = new Home();
                frame.setVisible(true);
            }
        });

        passwordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AdminPassword frame = new AdminPassword();
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

    private void loadDashboard() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT SUM(CASE WHEN UPPER(STATUS) = 'OPEN' THEN 1 ELSE 0 END) AS OPEN_COUNT, " +
                    "SUM(CASE WHEN UPPER(STATUS) = 'CLOSED' THEN 1 ELSE 0 END) AS CLOSED_COUNT " +
                    "FROM INNOVATION_CALL")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int openCount = rs.getInt("OPEN_COUNT");
                        int closedCount = rs.getInt("CLOSED_COUNT");
                        callStatsLabel.setText("Innovation Calls: Open (" + openCount + ") Closed (" + closedCount + ")");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT i.IDEAID, i.TITLE, n.NAME, i.CATEGORY, i.STATUS " +
                    "FROM IDEA i JOIN INNOVATOR n ON i.INNOVATORID = n.INNOVATORID " +
                    "ORDER BY CASE WHEN UPPER(i.STATUS) = 'PENDING' THEN 1 ELSE 2 END, i.SUBMISSIONDATE DESC")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("TITLE"),
                            rs.getString("NAME"),
                            rs.getString("CATEGORY"),
                            rs.getString("STATUS"),
                            rs.getInt("IDEAID")
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Unable to load dashboard: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void evaluateSelectedIdea(String decision) {
        int selectedRow = ideaTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Select an idea first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String scoreText = scoreField.getText().trim();
        String comments = commentsArea.getText().trim();
        if (scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Enter a score between 1 and 10.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText);
            if (score < 1 || score > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Score must be a number between 1 and 10.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ideaId = (int) tableModel.getValueAt(selectedRow, 4);
        String newStatus = decision.equals("APPROVED") ? "Approved" : "Rejected";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int evaluationId = 1;
            try (PreparedStatement seqStmt = conn.prepareStatement("SELECT NVL(MAX(EVALUATIONID),0)+1 FROM EVALUATION")) {
                try (ResultSet rs = seqStmt.executeQuery()) {
                    if (rs.next()) {
                        evaluationId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement insertEval = conn.prepareStatement(
                    "INSERT INTO EVALUATION (EVALUATIONID, IDEAID, ADMINID, EVALUATIONDATE, SCORE, COMMENTS, DECISIONSTATUS) " +
                            "VALUES (?, ?, ?, SYSDATE, ?, ?, ? )")) {
                insertEval.setInt(1, evaluationId);
                insertEval.setInt(2, ideaId);
                insertEval.setInt(3, 100001);
                insertEval.setInt(4, score);
                insertEval.setString(5, comments);
                insertEval.setString(6, decision);
                insertEval.executeUpdate();
            }

            try (PreparedStatement updateIdea = conn.prepareStatement(
                    "UPDATE IDEA SET STATUS = ? WHERE IDEAID = ?")) {
                updateIdea.setString(1, newStatus.toUpperCase());
                updateIdea.setInt(2, ideaId);
                updateIdea.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Idea " + newStatus.toLowerCase() + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            scoreField.setText("");
            commentsArea.setText("");
            loadDashboard();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to evaluate idea: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        InnovationCall frame = new InnovationCall("Admin");
        frame.setVisible(true);
    }
}
