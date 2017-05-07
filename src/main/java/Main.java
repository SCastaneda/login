import controller.Login;
import controller.Profile;
import controller.SignUp;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        registerRoutes();
    }

    protected static void registerRoutes() {

        post("/sign-up", (req, res) -> new SignUp(req, res).execute());

        /* returns a session for the logged in user */
        post("/login", (req, res) -> new Login(req, res).execute());

        /* allow user to change any of current info user_name/info */
        put("/profile", (req, res) -> new Profile(req, res).update());

        /* allow user to delete his profile */
        delete("/profile", (req, res) -> new Profile(req, res).delete());

        get("/profile/:username", (req, res) -> new Profile(req, res).get());

        /* requires either active session or password */
        put("/password", (req, res) -> new Profile(req, res).changePassword());
    }

}