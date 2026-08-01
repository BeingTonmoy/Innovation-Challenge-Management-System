import java.sql.*;
public class DescribeConstraint {
    public static void main(String[] args) {
        String table = args.length > 0 ? args[0].toUpperCase() : "IDEA";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT cc.column_name, c.constraint_type, c.constraint_name, c.search_condition " +
                 "FROM user_constraints c JOIN user_cons_columns cc ON c.constraint_name = cc.constraint_name " +
                 "WHERE c.table_name = ? AND cc.column_name = 'STATUS'")) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
                    System.out.println(rs.getString(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
