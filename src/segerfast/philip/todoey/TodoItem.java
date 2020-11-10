package segerfast.philip.todoey;

public class TodoItem {

    private int id;
    private String description;
    private boolean completed;

    // For reflection
    private TodoItem() {}

    public TodoItem(int id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    /*////////// GETTERS //////////*/
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        String idDesc = String.format("ID: %s%n", id);
        String descriptionDesc = String.format("Description: %s%n", description);
        String completedDesc = String.format("Completed: %b%n", completed);

        StringBuilder sb = new StringBuilder();
        sb
                .append(idDesc)
                .append(descriptionDesc)
                .append(completedDesc);

        return sb.toString();
    }
}