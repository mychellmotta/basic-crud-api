const apiUrl = "http://localhost:8080/api/v1/things";

document.addEventListener("DOMContentLoaded", function () {

    // Fetch the list of things from the controller
    fetch(apiUrl)
        .then(response => response.json())
        .then(things => {
            document.querySelector(".thing-list");

            // Iterate over each thing and create a card
            things.forEach(thing => {
                const card = createThingCard(thing);
                addCardToList(card);
            });

            document.getElementById("newFormWrapper").style.display = things.length > 0 ? "none" : "block";
        })
        .catch(error => console.log("Error fetching things:", error));

    // Function to create a new Thing card
    function createThingCard(thing) {
        const card = document.createElement("div");
        card.className = "thing-card";

        const description = document.createElement("p");
        description.textContent = thing.description;

        const image = document.createElement("img");
        image.title = thing.id;

        // Add an event listener for the error event
        image.addEventListener("error", function () {
            // Set the src attribute to the path of the static image
            image.src = "static/img/noimage.jpg";
        });

        // Set the src attribute to the provided image URL
        image.src = thing.imageUrl;

        card.appendChild(description);
        card.appendChild(image);

        // Add event listener for card selection
        card.addEventListener("click", function () {
            handleCardSelection(card);
        });

        return card;
    }

    // Function to handle card selection
    function handleCardSelection(card) {
        // Remove the "selected" class from all cards
        const thingCards = document.querySelectorAll(".thing-card");
        thingCards.forEach(function (c) {
            c.classList.remove("selected");
        });

        // Add the "selected" class to the clicked card
        card.classList.add("selected");
    }

    // Function to add a new Thing card to the thing list
    function addCardToList(card) {
        const thingList = document.querySelector(".thing-list");
        thingList.appendChild(card);
    }

    // Add event listener to the form submit event
    const newForm = document.getElementById("newForm");

    newForm.addEventListener("submit", function (event) {
        event.preventDefault();

        // Get the form input values
        const description = document.getElementById("description").value;
        const imageUrl = document.getElementById("imageUrl").value;

        // Create a new Thing object
        const thing = {
            description,
            imageUrl
        };

        // Save the new Thing to the database
        fetch(apiUrl + "/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(thing)
        })
            .then(response => response.json())
            .then(savedThing => {
                // Clear the form inputs
                newForm.reset();

                // Create a new card for the saved Thing and add it to the thingList
                const card = createThingCard(savedThing);
                addCardToList(card);

                // Hide the new form and show the thing list
                newFormWrapper.style.display = "none";
                document.querySelector(".thing-list").style.display = "flex";
            });
    });

    // Function to handle Thing deletion
    function handleThingDeletion() {
        // Get the selected card
        const selectedCard = document.querySelector(".thing-card.selected");

        if (selectedCard) {
            // Get the Thing ID from the card's title attribute
            const thingId = selectedCard.querySelector("img").getAttribute("title");

            // Confirm before deleting
            const confirmDelete = confirm("Are you sure you want to delete?");
            if (confirmDelete) {
                // Call the delete endpoint
                fetch(apiUrl + "/delete/" + encodeURIComponent(thingId), {
                    method: "DELETE"
                })
                    .then(response => {
                        if (response.ok) {
                            alert("Delete successful.");
                            // Remove the selected card from the UI
                            selectedCard.remove();
                        } else {
                            alert("Delete failed.");
                        }
                    })
                    .catch(error => {
                        console.error("Error deleting thing:", error);
                        alert("Delete failed. Please try again.");
                    });
            }
        } else {
            alert("Please select a Thing to delete.");
        }
    }

    // Add event listener to the "New" button
    const newButton = document.getElementById("newButton");
    const newFormWrapper = document.getElementById("newFormWrapper");

    newButton.addEventListener("click", function () {
        newFormWrapper.style.display = "block";
        document.querySelector(".thing-list").style.display = "none";
    });

    // Add event listener to the "Return" button
    const returnButton = document.getElementById("returnButton");
    returnButton.addEventListener("click", function () {
        newFormWrapper.style.display = "none";
        document.querySelector(".thing-list").style.display = "flex";
    });

    // Add event listener to the delete button
    document.getElementById("deleteButton").addEventListener("click", handleThingDeletion);

});