package segerfast.philip.todoey;

import express.Express;
import express.middleware.Middleware;

import java.io.IOException;
import java.nio.file.Paths;

public class WebServer {

    private static WebServer instance = new WebServer();

    private Database database = Database.getInstance();
    private Express webServer = new Express();

    private WebServer() {



        // Connect the webserver with the DOM.
        try {
            webServer.use(Middleware.statics(Paths.get("src/www").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        webServer.listen(3000); // defaults to port 80
        System.out.println("Server started on port 3000");
    }

    public static WebServer getInstance() {
        return instance;
    }
}
