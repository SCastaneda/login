package controller;

import Utils.SQLUtils;
import dao.User;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class Profile {

    Request _request;
    Response _response;

    private final static int MAX_SESSION_DURATION_SECS = 3600;

    public Profile(Request request, Response response) {
        _request = request;
        _response = response;
    }

    public String update() {
        return "update";
    }

    public String delete() {

        JSONObject response = new JSONObject();

        String userName = "";
        String password = "";
        String session = "";

        try {
            JSONObject body = new JSONObject(_request.body());
            userName = body.getString("username");
            password = body.getString("password");
            session = body.getString("session");

        } catch (Exception e) {
            response.put("error_message", "Invalid delete request");
            return response.toString();
        }

        System.out.println("Received Delete Request for user: " + userName);

        SQLUtils conn = new SQLUtils();

        boolean deleted = false;

        if (User.validateLogin(conn, userName, password) &&
                User.validateSession(conn, session, userName, MAX_SESSION_DURATION_SECS)) {
            deleted = User.deleteUser(conn, userName);
        }

        if (!deleted) {
            _response.status(400);
            response.put("error_message", "Could not delete user");
        }

        return response.toString();
    }
}
