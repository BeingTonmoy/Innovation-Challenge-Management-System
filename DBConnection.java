import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String[] URL_CANDIDATES = {
            "jdbc:oracle:thin:@127.0.0.1:1521:XE",
            "jdbc:oracle:thin:@127.0.0.1:1521:ORCL",
            "jdbc:oracle:thin:@localhost:1521:XE",
            "jdbc:oracle:thin:@localhost:1521:ORCL",
            "jdbc:oracle:thin:@//127.0.0.1:1521/XE",
            "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL",
            "jdbc:oracle:thin:@//localhost:1521/XE",
            "jdbc:oracle:thin:@//localhost:1521/ORCL"
    };

    private static final String DEFAULT_USER = "scott";
    private static final String DEFAULT_PASSWORD = "tiger";
    private static final int DEFAULT_ADMIN_ID = 100001;
    private static final String DEFAULT_ADMIN_NAME = "adminPass";

    public static Connection getConnection() throws SQLException {
        String user = System.getProperty("oracle.user", DEFAULT_USER);
        String password = System.getProperty("oracle.password", DEFAULT_PASSWORD);

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC driver not found. Add the Oracle JDBC jar to the classpath.", e);
        }

        SQLException lastException = null;
        for (String url : URL_CANDIDATES) {
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                ensureAdminUser(connection);
                ensureSequences(connection);
                return connection;
            } catch (SQLException e) {
                lastException = e;
            }
        }

        throw lastException != null
                ? lastException
                : new SQLException("Unable to connect to the Oracle database.");
    }

    private static void ensureSequences(Connection connection) {
        String[] seqNames = {"USER_SEQ", "INNOVATOR_SEQ"};
        for (String seq : seqNames) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM user_sequences WHERE sequence_name = ?")) {
                ps.setString(1, seq);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // sequence missing - attempt to create with start value = NVL(MAX(id),0)+1
                        String idColumn = seq.equals("USER_SEQ") ? "UserID" : "InnovatorID";
                        String tableName = seq.equals("USER_SEQ") ? "USER_ACCOUNT" : "INNOVATOR";
                        long startWith = 1;
                        try (PreparedStatement pmax = connection.prepareStatement("SELECT NVL(MAX(" + idColumn + "),0)+1 FROM " + tableName)) {
                            try (ResultSet rmax = pmax.executeQuery()) {
                                if (rmax.next()) startWith = rmax.getLong(1);
                            }
                        } catch (SQLException e) {
                            // table may not exist yet; default startWith = 1
                            startWith = 1;
                        }

                        String createSql = "CREATE SEQUENCE " + seq + " START WITH " + startWith + " INCREMENT BY 1 NOCACHE NOCYCLE";
                        try (Statement stmt = connection.createStatement()) {
                            stmt.execute(createSql);
                        }
                    }
                }
            } catch (SQLException e) {
                // ignore - user may not have privileges to query or create sequences
            }
        }
    }

    public static int parseAdminId(String input) {
        if (input == null) {
            return 0;
        }

        String value = input.trim();
        if (value.isEmpty()) {
            return 0;
        }

        if (value.equalsIgnoreCase("adminID") || value.equalsIgnoreCase("adminid")) {
            return DEFAULT_ADMIN_ID;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void ensureAdminUser(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, "ADMIN_USER", new String[]{"TABLE"})) {
            if (!rs.next()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("CREATE TABLE ADMIN_USER (AdminID NUMBER(6) NOT NULL, UserID NUMBER(6) NOT NULL, Name VARCHAR2(60) NOT NULL, Email VARCHAR2(80), Phone VARCHAR2(20), CONSTRAINT pk_admin PRIMARY KEY (AdminID))");
                }
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE ADMIN_USER ADD CONSTRAINT fk_admin_user FOREIGN KEY (UserID) REFERENCES USER_ACCOUNT(UserID)");
        } catch (SQLException e) {
            if (e.getErrorCode() != 942 && e.getErrorCode() != 2275 && e.getErrorCode() != 2264) {
                throw e;
            }
        }

        try (PreparedStatement checkStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM ADMIN_USER WHERE AdminID = ?")) {
            checkStatement.setInt(1, DEFAULT_ADMIN_ID);
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO ADMIN_USER (AdminID, UserID, Name, Email, Phone) VALUES (?, ?, ?, ?, ?)")) {
                        insertStatement.setInt(1, DEFAULT_ADMIN_ID);
                        insertStatement.setInt(2, 1);
                        insertStatement.setString(3, DEFAULT_ADMIN_NAME);
                        insertStatement.setNull(4, java.sql.Types.VARCHAR);
                        insertStatement.setNull(5, java.sql.Types.VARCHAR);
                        insertStatement.executeUpdate();
                    } catch (SQLException e) {
                        if (e.getErrorCode() != 2291) {
                            throw e;
                        }
                    }
                }
            }
        }
    }
}
