//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
                ensurePlSqlObjects(connection);
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

    private static void ensurePlSqlObjects(Connection connection) {
        String initProcSql = "CREATE OR REPLACE PROCEDURE IMS_INIT_APP AS " +
                "BEGIN " +
                "  INSERT INTO ADMIN_USER (AdminID, UserID, Name, Email, Phone) " +
                "  SELECT 100001, 1, 'adminPass', NULL, NULL FROM DUAL " +
                "  WHERE NOT EXISTS (SELECT 1 FROM ADMIN_USER WHERE AdminID = 100001); " +
                "END;";

        String registerProcSql = "CREATE OR REPLACE PROCEDURE IMS_REGISTER_USER(" +
                "  p_username IN VARCHAR2, " +
                "  p_passwordhash IN VARCHAR2, " +
                "  p_email IN VARCHAR2, " +
                "  p_name IN VARCHAR2, " +
                "  p_deptid IN NUMBER, " +
                "  p_userid OUT NUMBER, " +
                "  p_innovatorid OUT NUMBER " +
                ") AS " +
                "BEGIN " +
                "  SELECT user_seq.NEXTVAL INTO p_userid FROM DUAL; " +
                "  INSERT INTO USER_ACCOUNT (UserID, Username, PasswordHash, Role, Email, Status) " +
                "  VALUES (p_userid, p_username, p_passwordhash, 'INNOVATOR', p_email, 'ACTIVE'); " +
                "  INSERT INTO INNOVATOR (InnovatorID, UserID, DeptID, Name, Email, Phone, Expertise) " +
                "  VALUES (innovator_seq.NEXTVAL, p_userid, p_deptid, p_name, p_email, NULL, NULL); " +
                "  SELECT innovator_seq.CURRVAL INTO p_innovatorid FROM DUAL; " +
                "END;";

        String projectProcSql = "CREATE OR REPLACE PROCEDURE IMS_CREATE_PROJECT(" +
                "  p_ideaid IN NUMBER, " +
                "  p_title IN VARCHAR2, " +
                "  p_startdate IN DATE, " +
                "  p_enddate IN DATE, " +
                "  p_status IN VARCHAR2, " +
                "  p_description IN VARCHAR2, " +
                "  p_projectid OUT NUMBER " +
                ") AS " +
                "BEGIN " +
                "  SELECT NVL(MAX(PROJECTID), 0) + 1 INTO p_projectid FROM INNOVATION_PROJECT; " +
                "  INSERT INTO INNOVATION_PROJECT (PROJECTID, IDEAID, PROJECTTITLE, STARTDATE, ENDDATE, STATUS, DESCRIPTION) " +
                "  VALUES (p_projectid, p_ideaid, p_title, p_startdate, p_enddate, p_status, p_description); " +
                "END;";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(initProcSql);
            stmt.execute(registerProcSql);
            stmt.execute(projectProcSql);
            try (java.sql.CallableStatement cs = connection.prepareCall("{ call IMS_INIT_APP() }")) {
                cs.execute();
            }
        } catch (SQLException e) {
            // keep compatibility with Oracle accounts that do not permit procedure creation or if the table is not ready yet
            // the core Java SQL flow remains available and will continue to work.
        }
    }

    public static int createUserAccountWithProcedure(Connection conn, String username, String passwordHash,
                                                    String email, String name, Integer deptId) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{ call IMS_REGISTER_USER(?, ?, ?, ?, ?, ?, ?) }")) {
            cs.setString(1, username);
            cs.setString(2, passwordHash);
            cs.setString(3, email);
            cs.setString(4, name);
            if (deptId == null) {
                cs.setNull(5, Types.INTEGER);
            } else {
                cs.setInt(5, deptId);
            }
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.execute();
            return cs.getInt(6);
        } catch (SQLException e) {
            int finalUserId;
            try (PreparedStatement s1 = conn.prepareStatement("SELECT user_seq.NEXTVAL FROM DUAL")) {
                try (ResultSet r1 = s1.executeQuery()) {
                    if (r1.next()) {
                        finalUserId = r1.getInt(1);
                    } else {
                        throw new SQLException("Failed to get next user_seq value.");
                    }
                }
            }

            try (PreparedStatement insUser = conn.prepareStatement(
                    "INSERT INTO USER_ACCOUNT (UserID, Username, PasswordHash, Role, Email, Status) VALUES (?, ?, ?, ?, ?, 'ACTIVE')")) {
                insUser.setInt(1, finalUserId);
                insUser.setString(2, username);
                insUser.setString(3, passwordHash);
                insUser.setString(4, "INNOVATOR");
                insUser.setString(5, email);
                insUser.executeUpdate();
            }

            try (PreparedStatement insInnov = conn.prepareStatement(
                    "INSERT INTO INNOVATOR (InnovatorID, UserID, DeptID, Name, Email, Phone, Expertise) VALUES (innovator_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)")) {
                insInnov.setInt(1, finalUserId);
                insInnov.setInt(2, deptId == null ? 1 : deptId);
                insInnov.setString(3, name);
                insInnov.setString(4, email);
                insInnov.setString(5, null);
                insInnov.setString(6, null);
                insInnov.executeUpdate();
            }
            return finalUserId;
        }
    }

    public static int createProjectWithProcedure(Connection conn, int ideaId, String title, java.sql.Date startDate,
                                                java.sql.Date endDate, String status, String description) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{ call IMS_CREATE_PROJECT(?, ?, ?, ?, ?, ?, ?) }")) {
            cs.setInt(1, ideaId);
            cs.setString(2, title);
            cs.setDate(3, startDate);
            cs.setDate(4, endDate);
            cs.setString(5, status);
            cs.setString(6, description);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.execute();
            return cs.getInt(7);
        } catch (SQLException e) {
            int projectId = 1;
            try (PreparedStatement ps = conn.prepareStatement("SELECT NVL(MAX(PROJECTID), 0) + 1 FROM INNOVATION_PROJECT")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        projectId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement insProject = conn.prepareStatement(
                    "INSERT INTO INNOVATION_PROJECT (PROJECTID, IDEAID, PROJECTTITLE, STARTDATE, ENDDATE, STATUS, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                insProject.setInt(1, projectId);
                insProject.setInt(2, ideaId);
                insProject.setString(3, title);
                insProject.setDate(4, startDate);
                insProject.setDate(5, endDate);
                insProject.setString(6, status);
                insProject.setString(7, description);
                insProject.executeUpdate();
            }
            return projectId;
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
