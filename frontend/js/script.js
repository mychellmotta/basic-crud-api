document.addEventListener("DOMContentLoaded", function () {
    // Fetch the list of things from the controller
    fetch("http://localhost:8080/api/v1/things")
        .then(response => response.json())
        .then(things => {
            const thingList = document.querySelector(".thing-list");
            const line = document.querySelector(".line");

            // Iterate over each thing and create a card
            things.forEach(thing => {
                const card = document.createElement("div");
                card.className = "thing-card";

                const description = document.createElement("p");
                description.textContent = thing.description;

                const image = document.createElement("img");
                image.src = thing.imageUrl;
                image.title = thing.id; // Set the Thing ID as the title attribute of the image

                card.appendChild(description);
                card.appendChild(image);
                thingList.appendChild(card);
            });

            // Toggle the visibility of the line and form wrapper based on the number of things
            line.style.display = things.length > 0 ? "block" : "none";
            document.getElementById("newFormWrapper").style.display = things.length > 0 ? "none" : "block";

            // Add event listeners to the Thing cards for selection
            const thingCards = document.querySelectorAll(".thing-card");
            thingCards.forEach(function (card) {
                card.addEventListener("click", function () {
                    // Remove the "selected" class from all cards
                    thingCards.forEach(function (c) {
                        c.classList.remove("selected");
                    });

                    // Add the "selected" class to the clicked card
                    card.classList.add("selected");
                });
            });
        })
        .catch(error => console.log("Error fetching things:", error));

    // Add event listener to the "New" button
    const newButton = document.getElementById("newButton");
    const newFormWrapper = document.getElementById("newFormWrapper");

    newButton.addEventListener("click", function () {
        newFormWrapper.style.display = "block";
        document.querySelector(".thing-list").style.display = "none";
    });

    // Add event listener to the form submit event
    const newForm = document.getElementById("newForm");

    newForm.addEventListener("submit", function (event) {
        event.preventDefault();

        // Get the form input values
        const description = document.getElementById("description").value;
        const imageUrl = document.getElementById("imageUrl").value;

        // Create a new Thing object
        const newThing = {
            description: description,
            imageUrl: imageUrl
        };

        // Perform the POST request to save the new Thing
        fetch("http://localhost:8080/api/v1/things/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newThing)
        })
            .then(response => response.json())
            .then(savedThing => {
                // Clear the form inputs
                newForm.reset();

                // Create a new card for the saved Thing and add it to the thingList
                const card = document.createElement("div");
                card.className = "thing-card";

                const description = document.createElement("p");
                description.textContent = savedThing.description;

                const image = document.createElement("img");
                image.src = savedThing.imageUrl;
                image.title = savedThing.id;

                card.appendChild(description);
                card.appendChild(image);
                document.querySelector(".thing-list").appendChild(card);

                const thingCards = document.querySelectorAll(".thing-card");

                // Hide the new form and show the thing list
                newFormWrapper.style.display = "none";
                document.querySelector(".thing-list").style.display = "block";

                // Show or hide the line based on the number of things
                const line = document.querySelector(".line");
                line.style.display = thingCards.length > 0 ? "block" : "none";
            })
            .catch(error => console.log("Error saving thing:", error));
    });

    // Add event listener to the "Return" button
    const returnButton = document.getElementById("returnButton");
    returnButton.addEventListener("click", function () {
        newFormWrapper.style.display = "none";
        document.querySelector(".thing-list").style.display = "block";
    });
});


// ----------------------------------------------------------------------------------- //


// Add an event listener to the delete button
document.getElementById("deleteButton").addEventListener("click", function () {
    // Get the selected card
    var selectedCard = document.querySelector(".thing-card.selected");

    if (selectedCard) {
        // Get the Thing ID from the card's title attribute
        var thingId = selectedCard.querySelector("img").getAttribute("title");

        // Confirm before deleting
        var confirmDelete = confirm("Are you sure you want to delete?");
        if (confirmDelete) {
            // Call the delete endpoint
            fetch("http://localhost:8080/api/v1/things/delete/" + encodeURIComponent(thingId), { method: "DELETE" })
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
});

// Add event listeners to the Thing cards for selection
var thingCards = document.querySelectorAll(".thing-card");
thingCards.forEach(function(card) {
    card.addEventListener("click", function() {
        // Remove the "selected" class from all cards
        thingCards.forEach(function(c) {
            c.classList.remove("selected");
        });

        // Add the "selected" class to the clicked card
        card.classList.add("selected");
    });
});
