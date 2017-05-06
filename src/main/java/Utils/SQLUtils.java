package Utils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class SQLUtils {
    Connection _conn = null;

    public SQLUtils() {
        this("localhost", "test", "root", "test");
    }

    public SQLUtils(String url, String db, String user, String password) {

        try {
            _conn = DriverManager.getConnection("jdbc:mysql://" + url + "/" + db + "?" +
                    "user=" + user + "&password=" + password);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public Optional<ResultSet> runPreparedQuery(String query, Object... parameters) throws SQLException {

        System.out.println(query);

        ResultSet rs;

        PreparedStatement stmt = _conn.prepareStatement(query);

        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
        }

        rs = stmt.executeQuery();


        return Optional.of(rs);
    }

    public boolean runPreparedInsert(String query, List<Integer> keys, Object... parameters) throws SQLException {

        System.out.println(query);

        PreparedStatement stmt = _conn.prepareStatement(query);

        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
        }

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            keys.add(rs.getInt(1));
        } else {
            return false;
        }

        return true;
    }

    public int runPreparedUpdate(String query, Object... parameters) throws SQLException {

        System.out.println(query);

        PreparedStatement stmt = _conn.prepareStatement(query);

        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
        }

        return stmt.executeUpdate();

    }


}




