package Utils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class SQLUtils {
    Connection _conn = null;

    public SQLUtils() {
        this("localhost", "test", "root", "test");
    }

    /**
     * @param url      URL to the database
     * @param db       Database to connect to
     * @param user     username to the database
     * @param password password to the database
     */
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

    /**
     * @param query
     * @param parameters
     * @return Optional ResultSet from running the query with the given parameters
     * @throws SQLException
     */
    public Optional<ResultSet> runPreparedQuery(String query, Object... parameters) throws SQLException {

        System.out.println(query);

        ResultSet rs;

        PreparedStatement stmt = _conn.prepareStatement(query);

        System.out.println("Parameters: ");
        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
            System.out.println(parameters[i - 1]);
        }

        rs = stmt.executeQuery();


        return Optional.of(rs);
    }

    /**
     * @param query      query to be sent to the database
     * @param keys       this list gets populated with the key of the inserted row
     * @param parameters parameters for the query
     * @return true if a row was inserted into the table, false otherwise
     * @throws SQLException
     */
    public boolean runPreparedInsert(String query, List<Integer> keys, Object... parameters) throws SQLException {

        System.out.println(query);

        PreparedStatement stmt = _conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        System.out.println("Parameters: ");
        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
            System.out.println(parameters[i - 1]);
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

    /**
     * @param query      query to be sent to the database
     * @param parameters parameters for the query
     * @return numbers of rows updated
     * @throws SQLException
     */
    public int runPreparedUpdate(String query, Object... parameters) throws SQLException {

        System.out.println(query);

        PreparedStatement stmt = _conn.prepareStatement(query);

        System.out.println("Parameters: ");
        for (int i = 1; i <= parameters.length; i++) {
            stmt.setString(i, parameters[i - 1].toString());
            System.out.println(parameters[i - 1]);
        }

        return stmt.executeUpdate();

    }


}




