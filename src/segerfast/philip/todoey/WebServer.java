package segerfast.philip.todoey;

import express.Express;
import express.middleware.Middleware;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class WebServer {

    private static WebServer instance = new WebServer();

    private Database database = Database.getInstance();
    private Express webServer = new Express();

    private WebServer() {
    }

    public void start() {

        webServer.get("/rest/todo-items", (request, response) -> {
            List<TodoItem> todoItems = database.updateAndGetTodoItems();
            response.json(todoItems);
        });

        webServer.delete("/delete", (req, res) -> {

            System.out.println("Deleting");
            String message = "";
            int id = -1;

            try {
                id = Integer.parseInt((String) req.getBody().get("id"));
                message = database.deleteTodoItemWithId(id);

                System.out.println("ID: " + id);
                System.out.println("Message: " + message);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                message = "The ID you sent is not an integer.";
            }

            res.json(message);
        });

        webServer.post("/add-todo-item", (request, response) -> {
            var body = request.getBody();
            String desc = (String) body.get("description");
            System.out.println("Description: " + desc);
            boolean ok = database.addTodoItem(desc);

            response.send(ok ? "true" : "false");
        });

        webServer.put("/toggle-completed", (request, response) -> {
            var body = request.getBody();
            int id = Integer.parseInt((String)body.get("id"));
            String toggleValue = database.toggleListItemCompleted(id);

            response.send(toggleValue);
        });

        webServer.get("/stop-server", (request, response) -> {
            System.out.println("Stopping server...");
            response.send("Stopping server...");
            database.close();
            webServer.stop();
        });

        // Connect the webserver with the DOM.
        try {
            webServer.use(Middleware.statics(Paths.get("src/www").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int port = 3000;
        webServer.listen(port); // defaults to port 80
        System.out.println("Server started on port " + port);
    }

    public static WebServer getInstance() {
        return instance;
    }
}
