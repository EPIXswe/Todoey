package segerfast.philip.todoey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance = new Database();
    private Connection conn;

    private Database() {

        // 1. connect to database
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:express.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     *
     * @return The database instance.
     */
    public static Database getInstance() {
        return instance;
    }

    private class TodoItem {

        private int id;
        private String description;
        private boolean isChecked;

        public TodoItem(int id, String description, boolean isChecked) {
            this.id = id;
            this.description = description;
            this.isChecked = isChecked;
        }

        /*////////// GETTERS //////////*/
        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public boolean isChecked() {
            return isChecked;
        }

        /*////////// SETTERS //////////*/
        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

}
