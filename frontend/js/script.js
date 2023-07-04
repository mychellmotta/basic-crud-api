const apiUrl = "http://localhost:8080/api/v1/things";
const searchInput = document.getElementById("searchBar");
const errorMessage = document.getElementById("errorMessage");
const descriptionInput = document.getElementById("description");
const imageUrlInput = document.getElementById("imageUrl");
const newFormWrapper = document.getElementById("newFormWrapper");
const buttonWrapper = document.getElementById("buttonWrapper");
const fileInput = document.getElementById('fileInput');

document.addEventListener("DOMContentLoaded", () => {
  fetchThings();

  searchInput.addEventListener("input", () => {
    const searchTerm = searchInput.value.trim();
    fetchThings(searchTerm);
  });

  function fetchThings(searchTerm = "") {
    let url = apiUrl;
    if (searchTerm !== "") {
      url += `/findAllWithDescription/${encodeURIComponent(searchTerm)}`;
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

    const editButton = document.createElement("img");
      editButton.src = "static/img/edit-btn.svg";
      editButton.className = "edit-button";
      editButton.title = "Edit";
      editButton.addEventListener("click", (event) => {
        event.stopPropagation(); // Prevent the card click event from triggering
        handleEditButtonClick(thing);
      });

    const description = document.createElement("p");
    description.textContent = thing.description;

    const image = document.createElement("img");
    image.title = thing.id;
    card.setAttribute("data-thing-id", thing.id);
    image.addEventListener("error", () => {
      image.src = "static/img/noimage.jpg";
    });
    image.src = thing.imageUrl;

    card.appendChild(editButton);
    card.appendChild(description);
    card.appendChild(image);

    card.addEventListener("click", () => {
      handleCardSelection(card);
    });

    return card;
  }

  function handleEditButtonClick(thing) {
    // Populate form fields with Thing data
    descriptionInput.value = thing.description;
    imageUrlInput.value = thing.imageUrl;

    // Set the Thing ID as a data attribute on the form
    newForm.setAttribute("data-thing-id", thing.id);

    // Show the form for editing
    newFormWrapper.style.display = "block";
    searchInput.style.display = "none";
    document.querySelector(".thing-list").style.display = "none";
    buttonWrapper.style.display = "none"; // Hide the button wrapper
  }

  function handleCardSelection(card) {
    const isSelected = card.classList.contains("selected");

    const thingCards = document.querySelectorAll(".thing-card");
    thingCards.forEach((c) => {
      c.classList.remove("selected");
    });

    if (!isSelected) {
      card.classList.add("selected");
    }
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
    const thingId = newForm.getAttribute("data-thing-id");

    // Validate form input
    if (validateForm(description, imageUrl)) {
      const thing = {
        description,
        imageUrl,
      };

      if (thingId) {
        // Update existing Thing
        fetch(apiUrl + `/update/${encodeURIComponent(thingId)}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(thing),
        })
            .then((response) => {
              if (response.ok) {
                return response.json();
              } else {
                throw new Error("Failed to update thing.");
              }
            })
            .then((updatedThing) => {
              // Update the card in the UI with the updated Thing data
              const card = createThingCard(updatedThing);
              const existingCard = document.querySelector(
                  `.thing-card[data-thing-id="${thingId}"]`
              );
              existingCard.replaceWith(card);
              resetForm();
              newFormWrapper.style.display = "none";
              searchInput.style.display = "block";
              document.querySelector(".thing-list").style.display = "flex";
              buttonWrapper.style.display = "flex"; // Show the button wrapper
              hideErrorMessage(); // Hide error message on successful update
            })
            .catch((error) => {
              console.error("Error updating thing:", error);
              // Display an error message on the page
              errorMessage.textContent =
                  "Failed to update thing. Please try again.";
              errorMessage.style.display = "block"; // Show the error message
            });
      } else {
        // Create new Thing
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
              const card = createThingCard(savedThing);
              addCardToList(card);
              resetForm();
              newFormWrapper.style.display = "none";
              searchInput.style.display = "block";
              document.querySelector(".thing-list").style.display = "flex";
              buttonWrapper.style.display = "flex"; // Show the button wrapper
              hideErrorMessage(); // Hide error message on successful save
            })
            .catch((error) => {
              console.error("Error saving thing:", error);
              // Display an error message on the page
              errorMessage.textContent =
                  "Failed to save thing. Please try again.";
              errorMessage.style.display = "block"; // Show the error message
            });
      }
    }
  });

  function resetForm() {
    newForm.removeAttribute("data-thing-id");
    newForm.reset(); // Clear form input values
  }

  function handleThingDeletion() {
    const selectedCard = document.querySelector(".thing-card.selected");

    if (selectedCard) {
      const thingId = selectedCard.getAttribute("data-thing-id");
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
    buttonWrapper.style.display = "none"; // Hide the button wrapper
  });

  const returnButton = document.getElementById("returnButton");
  returnButton.addEventListener("click", (event) => {
    event.preventDefault();
    hideErrorMessage();
    newFormWrapper.style.display = "none";
    searchInput.style.display = "block";
    document.querySelector(".thing-list").style.display = "flex";
    resetForm(); // Clear form input values
    buttonWrapper.style.display = "flex"; // Show the button wrapper
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

  // Add an event listener to the saveFromSheetButton
    document.getElementById('saveFromSheetButton').addEventListener('click', function() {
      // Simulate a click on the file input element
      fileInput.click();
    });

    // Handle the file selection event
    fileInput.addEventListener('change', function(event) {
      const file = event.target.files[0]; // Get the selected file

      // Create a FormData object
      const formData = new FormData();
      formData.append('file', file); // Append the file to the form data

      // Send the file to the backend using an AJAX request
      fetch(apiUrl + '/saveFromExcel', {
        method: 'POST',
        body: formData
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Failed to save things from Excel.');
        }
      })
      .then(things => {
        fetchThings();
        resetForm();
        newFormWrapper.style.display = "none";
        searchInput.style.display = "block";
        document.querySelector(".thing-list").style.display = "flex";
        buttonWrapper.style.display = "flex"; // Show the button wrapper
        hideErrorMessage(); // Hide error message on successful save
      })
      .catch(error => {
        console.error('Error saving things from Excel:', error);
        // Display an error message on the page
        errorMessage.textContent = 'Failed to save things from Excel. Please try again.';
        errorMessage.style.display = 'block'; // Show the error message
      });
    });
});