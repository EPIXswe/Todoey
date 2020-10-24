
completeTodoItems();


let todoItems = { id, description, isCompleted }


function add_todo_item() {

    /*
    console.log("Attemting to add todo-item...");

    let todo_description = $("#todo-desc").val();

    // Check if textbox is empty
    if(todo_description == "") {
        alert("The todo-description cannot be empty! That doesn't make sense!");
        return;
    }
    */
}

function completeTodoItems() {

    console.log("Inside completeTodoItems()");
    
    // 1. Get all the items in the todo-item-container
    // 2. Add checkmark-buttons and remove-buttons

    // Get all the items in the todo-item-container
    let todoItems = document.getElementById("todo-list").getElementsByClassName("todo-item");

    // Add checkmark- and remove-buttons
    for(let item of todoItems) {
        // Append checkmarker
        let checkmarkerImage = document.createElement("img");
        checkmarkerImage.setAttribute("src", "img/check_box_outline_blank-black-18dp.svg");
        checkmarkerImage.setAttribute("alt", "Todo-item undone");
        let checkmarker = document.createElement("div");
        checkmarker.className = "checkmarker";
        checkmarker.appendChild(checkmarkerImage);

        let removeButtonImage = document.createElement("img");
        removeButtonImage.setAttribute("src", "img/delete-white-24dp.svg");
        checkmarkerImage.setAttribute("alt", "Remove todo-item");
        let removeButton = document.createElement("div");
        removeButton.className = "remove-button";
        removeButton.appendChild(removeButtonImage);

        item.prepend(checkmarker);
        item.appendChild(removeButton);

        item.addEventListener("click", function() { todoItemOnClick(item); });
    }
}

function todoItemOnClick(todoItem) {
    let description = todoItem.getElementsByClassName("description")[0].innerHTML;

    console.log("Clicked: " + description);
}