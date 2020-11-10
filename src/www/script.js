let todoItems = {};
init();

function init() {
    addListener();
    updateAndRenderTodoItems();
}

function addListener() {

    let itemList = document.querySelector("#todo-list");

    itemList.addEventListener("click", function(e) {
        let target = e.target;

        // e.target is the clicked element!
        if(typeof target !== 'undefined') {
            let targetClassName = target.className;

            let itemID = targetClassName.includes("todo-item") ? target.dataset.id : target.parentNode.dataset.id;

            if(targetClassName == "checkmarker" || targetClassName == "description" || targetClassName.includes("todo-item")) {
                checkItem(itemID);
            } else if(targetClassName == "remove-button") {
                removeTodoItem(target.parentNode);
            }
        }
    });
}

async function removeTodoItem(item) {
    await removeFromDatabase(item.dataset.id);
    await updateAndRenderTodoItems();
}

async function removeFromDatabase(id) {
    let theBody = JSON.stringify({ id: id });
    let response = await fetch(`/delete`, {
        method: 'delete', 
        body: theBody
    });
    let answer = await response.json();
    console.log("Answer from remove-request: " + answer);
}

async function updateAndRenderTodoItems() {
    await update();
    render();

    function render() {
        // The DOM-element containing all the todo-items.
        let itemList = document.querySelector("#todo-list");
        
        // Clear the contents of the list.
        itemList.innerHTML = "";
    
        for(let item of todoItems) {
            
            let itemContainer = document.createElement("div");
            itemContainer.classList.add("todo-item");

            itemContainer.dataset.id = item.id; // metadata containing the id of the todo-list-object. Used to easily retrieve corresponding object.

            let checkmarker = createCheckmarkerHTMLElement();
            let description = createDescriptionHTMLElement(item.description);
            let removeButton = createRemoveButtonHTMLElement();
    
            itemContainer.appendChild(checkmarker);
            itemContainer.appendChild(description)
            itemContainer.appendChild(removeButton);

            if(item.completed) {
                itemContainer.classList.add("completed");
                let checkmarker = itemContainer.getElementsByClassName("checkmarker")[0];
                checkmarker.getElementsByTagName("img")[0].src = "img/check_box-black-18dp.svg";
            } else {
                itemContainer.classList.add("uncompleted");
            }
            
            //itemList.innerHTML += itemContainer.outerHTML;
            itemList.appendChild(itemContainer);
        }
    }
}

/**
 * DON'T NORMALLY CALL THIS METHOD.
 */
async function update() {
    let result = await fetch('/rest/todo-items');
    todoItems = await result.json();
}

async function createTodoItem() {
    let descriptionHTMLElement = document.getElementById("todo-desc");
    let htmlTextfieldDescriptionValue = descriptionHTMLElement.value;
    let theBody = JSON.stringify({ description: htmlTextfieldDescriptionValue });
    
    let result = await fetch("/add-todo-item", {
        method: "POST",
        body: theBody
    });

    let successful = await result.text();
    console.log("Item added: " + successful);
    descriptionHTMLElement.value = "";

    updateAndRenderTodoItems();
}

async function checkItem(itemID) {
    let theBody = JSON.stringify({
        id: itemID
    });
    
    let result = await fetch("/toggle-completed", {
        method: "PUT",
        body: theBody
    });

    let answer = await result.text();
    console.log("Answer", answer);

    let htmlItem = getTodoItemElementById(itemID);
    let checkmarkerIMG = htmlItem.getElementsByTagName("img")[0];

    if(answer == "true") {
        htmlItem.className = "todo-item completed";
        checkmarkerIMG.setAttribute("src", "img/check_box-black-18dp.svg");
        checkmarkerIMG.setAttribute("alt", "Todo-item completed");
    } else {
        htmlItem.className = "todo-item uncompleted";
        checkmarkerIMG.setAttribute("src", "img/check_box_outline_blank-black-18dp.svg");
        checkmarkerIMG.setAttribute("alt", "Todo-item uncompleted");
    }

    update();
}

function getTodoItemObjectById(id) {
    for(let item of todoItems) {
        if(item.id == id) {
            return item;
        }
    }
    return null;
}

function getTodoItemElementById(id) {
    let items = document.getElementsByClassName("todo-item");
    for(let item of items) {
        if(item.dataset.id == id) {
            return item;
        }
    }
}

///////// FACTORIES /////////

function createCheckmarkerHTMLElement(checked) {
    let checkmarkerImageHTML = document.createElement("img");
    checkmarkerImageHTML.setAttribute("src", "img/check_box_outline_blank-black-18dp.svg");
    checkmarkerImageHTML.setAttribute("alt", "Todo-item uncompleted");
    let checkmarkerDiv = document.createElement("div");
    checkmarkerDiv.className = "checkmarker";
    checkmarkerDiv.appendChild(checkmarkerImageHTML);

    return checkmarkerDiv;
}

function createDescriptionHTMLElement(description) {
    let descriptionDiv = document.createElement("div");
    descriptionDiv.className = "description";
    descriptionDiv.innerHTML = description;

    return descriptionDiv;
}

function createRemoveButtonHTMLElement() {
    let removeButtonImage = document.createElement("img");
    removeButtonImage.setAttribute("src", "img/delete-white-24dp.svg");
    removeButtonImage.setAttribute("alt", "Remove todo-item");
    let removeButtonDiv = document.createElement("div");
    removeButtonDiv.className = "remove-button";
    removeButtonDiv.appendChild(removeButtonImage);

    return removeButtonDiv;
}

