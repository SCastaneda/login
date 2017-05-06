package dao;

import Utils.SQLUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class User {

    public static boolean addUser(SQLUtils conn, String userName, String password, String info) {
        String q = "INSERT INTO users (username, password, info) VALUES (?, ?, ?)";

        try {
            List<Integer> keys = new ArrayList<>();
            return conn.runPreparedInsert(q, keys, userName, BCrypt.hashpw(password, BCrypt.gensalt(12)), info);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(SQLUtils conn, String userName, String password) {

        String q = "SELECT password FROM users WHERE username = ?";

        try {
            Optional<ResultSet> oRs = conn.runPreparedQuery(q, userName);
            if (oRs.isPresent()) {
                ResultSet rs = oRs.get();
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    return BCrypt.checkpw(password, dbPassword);
                } else {
                    System.out.println("Username not found");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String createSession(SQLUtils conn, String userName) {
        String q = "UPDATE users SET session = ?, login_time = UNIX_TIMESTAMP(NOW()) WHERE username = ?";

        String session = generateSession();
        List<Integer> keys = new ArrayList<>();
        try {
            if (conn.runPreparedUpdate(q, session, userName) == 1) {
                return session;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean validateSession(SQLUtils conn, String session, String userName, long maxSessionDuration) {
        String q = "SELECT 1 FROM users WHERE user_name = ? AND session = ? AND (UNIX_TIMESTAMP(NOW()) - login_time) > ?";

        try {
            Optional<ResultSet> oRs = conn.runPreparedQuery(q, userName, session, maxSessionDuration);
            if (oRs.isPresent()) {
                ResultSet rs = oRs.get();
                return rs.next();
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String generateSession() {
        return String.valueOf(UUID.randomUUID());
    }


}
