import java.sql.*;

public class DescribeTable {
    public static void main(String[] args) {
        String table = args.length > 0 ? args[0].toUpperCase() : "DEPARTMENT";
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT column_name, data_type FROM user_tab_columns WHERE table_name = ? ORDER BY column_id")) {
                ps.setString(1, table);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("Columns for table " + table + ":");
                    while (rs.next()) {
                        System.out.println(rs.getString(1) + " : " + rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error describing table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
