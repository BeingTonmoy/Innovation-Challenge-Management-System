import java.sql.*;

public class ListTables {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT table_name FROM user_tables ORDER BY table_name")) {
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("User tables:");
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
