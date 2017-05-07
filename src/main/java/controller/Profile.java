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

        JSONObject response = new JSONObject();

        String session = "";
        String userName = "";
        String newUserName = "";
        String newInfo = "";

        try {
            JSONObject body = new JSONObject(_request.body());
            userName = body.getString("username");
            session = body.getString("session");

            if (body.has("new_username")) {
                newUserName = body.getString("new_username");
            }

            if (body.has("new_info")) {
                newInfo = body.getString("new_info");
            }

        } catch (Exception e) {
            response.put("error_message", "Could not update profile");
            _response.status(400);
            return response.toString();
        }


        SQLUtils conn = new SQLUtils();

        if (User.validateSession(conn, session, userName, MAX_SESSION_DURATION_SECS)) {
            if (User.updateProfile(conn, userName, newUserName, newInfo)) {
                return response.toString();
            } else {
                response.put("error_message", "Could not update profile");
                return response.toString();
            }
        } else {
            _response.status(401);
            return response.toString();
        }
    }

    public String changePassword() {
        JSONObject response = new JSONObject();

        String userName = "";
        String password = "";
        String newPassword = "";
        String newPasswordConfirm = "";
        String session = "";

        boolean useSession = false;

        try {
            JSONObject body = new JSONObject(_request.body());
            userName = body.getString("username");
            newPassword = body.getString("new_password");
            newPasswordConfirm = body.getString("new_password_confirm");

            if (!newPassword.equals(newPasswordConfirm)) {
                response.put("error_message", "New Passwords don't match");
                _response.status(400);
                return response.toString();
            }

            if (body.has("session")) {
                session = body.getString("session");
                useSession = true;
            }

            if (session.isEmpty()) {
                password = body.getString("password");
                useSession = false;
            }

        } catch (Exception e) {
            response.put("error_message", "Could not change password");
            _response.status(401);
            return response.toString();
        }

        SQLUtils conn = new SQLUtils();

        boolean changePassword = false;

        if (useSession) {
            if (User.validateSession(conn, session, userName, MAX_SESSION_DURATION_SECS)) {
                changePassword = true;
            } else {
                response.put("error_message", "Session expired. Please login.");
                _response.status(401);
                return response.toString();
            }
        } else {
            if (User.validateLogin(conn, userName, password)) {
                changePassword = true;
            } else {
                _response.status(401);
                return response.toString();
            }
        }

        if (changePassword) {
            if (User.changePassword(conn, userName, newPassword)) {
                return response.toString();
            } else {
                response.put("error_message", "Could not update password");
                _response.status(500);
                return response.toString();
            }
        } else {
            _response.status(500);
            return response.toString();
        }

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
