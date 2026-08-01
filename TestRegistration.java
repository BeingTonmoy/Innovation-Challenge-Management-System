//******************American International University-Bangladesh (AIUB) */
//******************Advanced Databse Management System (ADMS) Project - Innovation Management System (IMS) */
//******************** Developed by Arfan Rahman Tonmoy (23-51598-2) (arfanrahman12@gmail.com) */
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class TestRegistration {
    public static void main(String[] args) {
        String username = "trial_user_" + System.currentTimeMillis();
        String email = "trial@example.com";
        String password = "Password123";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // ensure default department exists (DeptID = 1)
            try (PreparedStatement pdept = conn.prepareStatement("SELECT COUNT(*) FROM DEPARTMENT WHERE DeptID = 1")) {
                try (ResultSet rd = pdept.executeQuery()) {
                    if (rd.next() && rd.getInt(1) == 0) {
                        try (PreparedStatement insd = conn.prepareStatement("INSERT INTO DEPARTMENT (DeptID, DeptName, Description) VALUES (1, ?, ?)")) {
                            insd.setString(1, "Default Department");
                            insd.setString(2, "Auto-created default department for registrations");
                            insd.executeUpdate();
                            System.out.println("Inserted default department (DeptID=1).");
                        } catch (SQLException e) {
                            // ignore insert failure (missing privileges etc.)
                            System.out.println("Could not insert default department: " + e.getMessage());
                        }
                    }
                }
            } catch (SQLException e) {
                // table may not exist or no privileges; ignore and proceed
            }

            try (PreparedStatement pcheck = conn.prepareStatement("SELECT COUNT(*) FROM USER_ACCOUNT WHERE Username = ?")) {
                pcheck.setString(1, username);
                try (ResultSet rc = pcheck.executeQuery()) {
                    if (rc.next() && rc.getInt(1) > 0) {
                        System.out.println("Username already exists. Aborting test.");
                        return;
                    }
                }
            }

            int finalUserId;
            try (PreparedStatement s1 = conn.prepareStatement("SELECT user_seq.NEXTVAL FROM DUAL"); ResultSet r1 = s1.executeQuery()) {
                if (r1.next()) finalUserId = r1.getInt(1);
                else throw new SQLException("Failed to get next user_seq value.");
            }

            String hashed = hashPassword(password);

            try (PreparedStatement insUser = conn.prepareStatement(
                    "INSERT INTO USER_ACCOUNT (UserID, Username, PasswordHash, Role, Email, Status) VALUES (?, ?, ?, ?, ?, 'ACTIVE')")) {
                insUser.setInt(1, finalUserId);
                insUser.setString(2, username);
                insUser.setString(3, hashed);
                insUser.setString(4, "INNOVATOR");
                insUser.setString(5, email);
                insUser.executeUpdate();
            }

            try (PreparedStatement insInnov = conn.prepareStatement(
                    "INSERT INTO INNOVATOR (InnovatorID, UserID, DeptID, Name, Email, Phone, Expertise) VALUES (innovator_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)")) {
                insInnov.setInt(1, finalUserId);
                insInnov.setInt(2, 1);
                insInnov.setString(3, username);
                insInnov.setString(4, email);
                insInnov.setString(5, null);
                insInnov.setString(6, null);
                insInnov.executeUpdate();
            }

            conn.commit();
            System.out.println("Test registration succeeded. UserID=" + finalUserId);
        } catch (Exception e) {
            System.out.println("Exception during test registration:");
            e.printStackTrace();
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        if (password == null) return null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
