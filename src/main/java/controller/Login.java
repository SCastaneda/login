package controller;

import Utils.SQLUtils;
import dao.User;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class Login {

    private Request _request;
    private Response _response;

    private String loginFailMessage = "Could not login";

    public Login(Request request, Response response) {
        _request = request;
        _response = response;
    }

    public String execute() {

        JSONObject response = new JSONObject();

        String username = "";
        String password = "";

        try {
            JSONObject body = new JSONObject(_request.body());
            username = body.getString("username");
            password = body.getString("password");
        } catch (Exception e) {
            _response.status(401); // unauthorized status code
            return failResponse(response);
        }

        System.out.println("Received login request for user: " + username);

        SQLUtils conn = new SQLUtils();

        if (User.validateLogin(conn, username, password)) {
            String session = User.createSession(conn, username);
            if (!session.isEmpty()) {
                response.put("session", session);
                return response.toString();
            }
        }

        _response.status(401); // unauthorized status code
        return failResponse(response);

    }

    private String failResponse(JSONObject response) {
        response.put("error_message", loginFailMessage);

        return response.toString();
    }
}
