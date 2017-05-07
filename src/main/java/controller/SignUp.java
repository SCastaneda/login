package controller;

import Utils.SQLUtils;
import dao.User;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class SignUp {

    private Request _request;
    private Response _response;

    public SignUp(Request request, Response response) {
        _request = request;
        _response = response;
    }

    public String execute() {

        System.out.println("Received Sign-up Request...");

        JSONObject response = new JSONObject();
        String username = "";
        String password = "";
        String info = "";

        try {
            JSONObject body = new JSONObject(_request.body());
            username = body.getString("username");
            password = body.getString("password");

            if (body.has("info")) {
                info = body.getString("info");
            }
        } catch (Exception e) {
            response.put("error_message", "Invalid sign-up request");
            return response.toString();
        }


        SQLUtils conn = new SQLUtils();

        // make sure the username and password are not empty, the info is optional
        boolean success = !username.isEmpty() && !password.isEmpty() && User.addUser(conn, username, password, info);

        if (!success) {
            response.put("error_message", "Could not create user");
        }

        System.out.println("Response:\n" + response.toString());

        return response.toString();

    }
}
