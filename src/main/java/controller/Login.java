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
            response.put("status", "FAIL");
            response.put("error_message", loginFailMessage);
            _response.status(401); // unauthorized status code
        }

        SQLUtils conn = new SQLUtils();

        User.validateLogin(conn, username, password);

        return "{}";
    }
}
