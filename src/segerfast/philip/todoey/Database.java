package segerfast.philip.todoey;

import com.fasterxml.jackson.core.JsonProcessingException;
import express.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final Database instance = new Database();
    private Connection conn;

    private List<TodoItem> todoItems = new ArrayList<>();

    /** @return The database instance. */
    public static Database getInstance() {
        return instance;
    }

    private Database() {
        // 1. connect to database
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            System.err.println("Couldn't close database connection.");
            throwables.printStackTrace();
        }
    }

    public boolean addTodoItem(String description) {
        try {
            // No duplicates allowed
            if(containsTodoItemWithDescription(description)) {
                System.out.println("Database already contains item with description: " + description);
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO todo_items (description) VALUES (?)");
            stmt.setString(1, description);
            int rowsAdded = stmt.executeUpdate();
            if(rowsAdded > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean containsTodoItemWithDescription(String description) {
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    SELECT EXISTS(
                        SELECT 1 FROM todo_items 
                        WHERE description = ?)
                    """);
            stmt.setString(1, description);
            ResultSet result = stmt.executeQuery();
            return result.getBoolean(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String deleteTodoItemWithId(int id) {

        String sql = """
                DELETE FROM todo_items
                WHERE ID = ?
                """;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int updatedRows = stmt.executeUpdate();
            if(updatedRows > 0) {
                return "success";
            } else {
                return "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "fail";
    }

    public List<TodoItem> updateAndGetTodoItems() {
        List<TodoItem> items = null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo_items");
            ResultSet rs = stmt.executeQuery();

            TodoItem[] todoItemsTmp = (TodoItem[]) Utils.readResultSetToObject(rs, TodoItem[].class);
            items = List.of(todoItemsTmp);

        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
        }

        // Updating the instance variable and returning it.
        this.todoItems = items;
        return todoItems;
    }

    public String toggleListItemCompleted(int id) {

        // 1. toggle the value in the database
        String toggleCompleted = """
                UPDATE `todo_items` 
                SET completed = ((completed | 1) - (completed & 1)) 
                WHERE id = ?
                """;

        try {
            PreparedStatement stmt_toggle = conn.prepareStatement(toggleCompleted);
            stmt_toggle.setInt(1, id);
            stmt_toggle.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TodoItem item = getTodoItemWithID(id);

        return Boolean.toString(item.isCompleted());
    }

    private TodoItem getTodoItemWithID(int id) {
        String sql = "SELECT * FROM todo_items WHERE id = ?";
        TodoItem item = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            item = ((TodoItem[])Utils.readResultSetToObject(res, TodoItem[].class))[0];
        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }

        if(item == null) {
            throw new NullPointerException("The supplied ID doesn't have any corresponding Todo-Item.");
        }

        return item;
    }
}


