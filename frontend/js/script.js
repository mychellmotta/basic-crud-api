const apiUrl = "http://localhost:8080/api/v1/things";
const searchInput = document.getElementById("searchBar");
const errorMessage = document.getElementById("errorMessage");
const descriptionInput = document.getElementById("description");
const imageUrlInput = document.getElementById("imageUrl");
const newFormWrapper = document.getElementById("newFormWrapper");

document.addEventListener("DOMContentLoaded", () => {
  fetchThings();

  searchInput.addEventListener("input", () => {
    const searchTerm = searchInput.value.trim();
    fetchThings(searchTerm);
  });

  function fetchThings(searchTerm = "") {
    let url = apiUrl;
    if (searchTerm !== "") {
      url += `/findByDescription/${encodeURIComponent(searchTerm)}`;
    }

    fetch(url)
      .then((response) => response.json())
      .then((things) => {
        const thingList = document.querySelector(".thing-list");
        thingList.innerHTML = "";
        things.forEach((thing) => {
          const card = createThingCard(thing);
          addCardToList(card);
        });
        hideErrorMessage(); // Hide error message on successful fetch
      })
      .catch((error) => {
        console.log("Error fetching things:", error);
        // Display an error message on the page
        errorMessage.textContent = "Failed to fetch things. Please try again.";
        errorMessage.style.display = "block"; // Show the error message
      });
  }

  function createThingCard(thing) {
    const card = document.createElement("div");
    card.className = "thing-card";

    const description = document.createElement("p");
    description.textContent = thing.description;

    const image = document.createElement("img");
    image.title = thing.id;
    image.addEventListener("error", () => {
      image.src = "static/img/noimage.jpg";
    });
    image.src = thing.imageUrl;

    card.appendChild(description);
    card.appendChild(image);

    card.addEventListener("click", () => {
      handleCardSelection(card);
    });

    return card;
  }

  function handleCardSelection(card) {
    const thingCards = document.querySelectorAll(".thing-card");
    thingCards.forEach((c) => {
      c.classList.remove("selected");
    });
    card.classList.add("selected");
  }

  function addCardToList(card) {
    const thingList = document.querySelector(".thing-list");
    thingList.appendChild(card);
  }

  const newForm = document.getElementById("newForm");
  newForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const description = descriptionInput.value;
    const imageUrl = imageUrlInput.value;
    const thing = {
      description,
      imageUrl,
    };

    // Validate form input
    if (validateForm(description, imageUrl)) {
      fetch(apiUrl + "/save", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(thing),
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error("Failed to save thing.");
          }
        })
        .then((savedThing) => {
          newForm.reset();
          const card = createThingCard(savedThing);
          addCardToList(card);
          newFormWrapper.style.display = "none";
          searchInput.style.display = "block";
          document.querySelector(".thing-list").style.display = "flex";
          hideErrorMessage(); // Hide error message on successful save
        })
        .catch((error) => {
          console.error("Error saving thing:", error);
          // Display an error message on the page
          errorMessage.textContent = "Failed to save thing. Please try again.";
          errorMessage.style.display = "block"; // Show the error message
        });
    }
  });

  function handleThingDeletion() {
    const selectedCard = document.querySelector(".thing-card.selected");

    if (selectedCard) {
      const thingId = selectedCard.querySelector("img").getAttribute("title");
      const confirmDelete = confirm("Are you sure you want to delete?");
      if (confirmDelete) {
        fetch(apiUrl + "/delete/" + encodeURIComponent(thingId), {
          method: "DELETE",
        })
          .then((response) => {
            if (response.ok) {
              alert("Delete successful.");
              selectedCard.remove();
              hideErrorMessage(); // Hide error message on successful delete
            } else {
              throw new Error("Failed to delete thing.");
            }
          })
          .catch((error) => {
            console.error("Error deleting thing:", error);
            // Display an error message on the page
            errorMessage.textContent = "Failed to delete thing. Please try again.";
            errorMessage.style.display = "block"; // Show the error message
          });
      }
    } else {
      alert("Please select a Thing to delete.");
    }
  }

const newButton = document.getElementById("newButton");
  newButton.addEventListener("click", () => {
    hideErrorMessage();
    newFormWrapper.style.display = "block";
    searchInput.style.display = "none";
    document.querySelector(".thing-list").style.display = "none";
  });

  const returnButton = document.getElementById("returnButton");
  returnButton.addEventListener("click", (event) => {
    event.preventDefault();
    hideErrorMessage();
    newFormWrapper.style.display = "none";
    searchInput.style.display = "block";
    document.querySelector(".thing-list").style.display = "flex";
    newForm.reset(); // Clear form input values
  });

  function hideErrorMessage() {
      errorMessage.style.display = "none";
    }

  document.getElementById("deleteButton").addEventListener("click", handleThingDeletion);

    // Function to validate form input
    function validateForm(description, imageUrl) {
      if (description.trim() === "" || imageUrl.trim() === "") {
        errorMessage.textContent = "Please enter description and image URL.";
        errorMessage.style.display = "block"; // Show the error message
        return false;
      }
      return true;
    }
});
