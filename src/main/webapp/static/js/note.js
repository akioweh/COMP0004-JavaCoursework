/**
 * JavaScript functions for note operations
 */

/**
 * Updates an element with the form data
 * @param {Event} event - The form submit event
 * @param {string} noteUuid - UUID of the note
 * @param {string} elementUuid - UUID of the element
 */
function updateElement(event, noteUuid, elementUuid) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    // Debug logging to see what data is being sent
    console.log("[DEBUG_LOG] updateElement: Form data:");
    for (const [key, value] of formData.entries()) {
        console.log("[DEBUG_LOG] " + key + " = " + value);
    }

    // Convert FormData to URLSearchParams manually for better browser compatibility
    const params = new URLSearchParams();
    for (const [key, value] of formData.entries()) {
        params.append(key, value.toString());
    }

    // Debug logging to see the actual request being sent
    const url = getContextPath() + '/api/element/' + noteUuid + '/' + elementUuid;
    const requestBody = params.toString();
    console.log("[DEBUG_LOG] updateElement: Sending request to URL:", url);
    console.log("[DEBUG_LOG] updateElement: Request method: PUT");
    console.log("[DEBUG_LOG] updateElement: Request body:", requestBody);

    // send PUT request to update element
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: requestBody
    }).then(response => {
        console.log("[DEBUG_LOG] updateElement: Response status:", response.status);
        console.log("[DEBUG_LOG] updateElement: Response status text:", response.statusText);

        if (response.ok) {
            console.log("[DEBUG_LOG] updateElement: Response OK, reloading page");
            // reload the page to see the changes, with cache-busting parameter
            location.href = location.href.split('?')[0] + '?t=' + new Date().getTime();
        } else {
            console.log("[DEBUG_LOG] updateElement: Response not OK");
            response.text().then(text => {
                console.log("[DEBUG_LOG] updateElement: Error text:", text);
                alert('Error: ' + text);
            });
        }
    }).catch(error => {
        console.log("[DEBUG_LOG] updateElement: Fetch error:", error);
    });
}

/**
 * Adds a new element to the note
 * @param {string} beforeElementUuid - UUID of the element to insert before, or null to append
 * @param {string} elementType - Type of element to add (text, html, link, media)
 */
function addNewElement(beforeElementUuid, elementType) {
    // send POST request to add new element
    let url = getContextPath() + '/api/element/' + noteUuid;
    if (beforeElementUuid) {
        url += '/' + beforeElementUuid;
    }

    // Create URLSearchParams object manually for better browser compatibility
    const params = new URLSearchParams();
    params.append('elementType', elementType);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    }).then(response => {
        if (response.ok) {
            // reload the page to see the new element, with cache-busting parameter
            location.href = location.href.split('?')[0] + '?t=' + new Date().getTime();
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    });
}

/**
 * Deletes an element from the note
 * @param {string} elementUuid - UUID of the element to delete
 */
function deleteElement(elementUuid) {
    if (confirm('Are you sure you want to delete this element?')) {
        // send DELETE request to delete element
        fetch(getContextPath() + '/api/element/' + noteUuid + '/' + elementUuid, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                // reload the page to see the changes, with cache-busting parameter
                location.href = location.href.split('?')[0] + '?t=' + new Date().getTime();
            } else {
                response.text().then(text => {
                    alert('Error: ' + text);
                });
            }
        });
    }
}

/**
 * Deletes the current note
 */
function deleteNote() {
    if (confirm('Are you sure you want to delete this note?')) {
        // send DELETE request to delete note
        fetch(getContextPath() + '/api/note/' + noteUuid, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                // redirect to home page, with cache-busting parameter
                window.location.href = getContextPath() + '/?t=' + new Date().getTime();
            } else {
                response.text().then(text => {
                    alert('Error: ' + text);
                });
            }
        });
    }
}

/**
 * Shows the edit form for the note
 */
function editNote() {
    document.getElementById('note-view').style.display = 'none';
    document.getElementById('note-edit').style.display = 'block';
}

/**
 * Hides the edit form for the note
 */
function cancelEdit() {
    document.getElementById('note-view').style.display = 'block';
    document.getElementById('note-edit').style.display = 'none';
}

/**
 * Updates the note with the form data
 * @param {Event} event - The form submit event
 */
function updateNote(event) {
    event.preventDefault();
    const form = document.getElementById('edit-note-form');
    const formData = new FormData(form);

    // Debug logging to see what data is being sent
    console.log("[DEBUG_LOG] updateNote: Form data:");
    for (const [key, value] of formData.entries()) {
        console.log("[DEBUG_LOG] " + key + " = " + value);
    }

    // Convert FormData to URLSearchParams manually for better browser compatibility
    const params = new URLSearchParams();
    for (const [key, value] of formData.entries()) {
        params.append(key, value.toString());
    }

    // Debug logging to see the actual request being sent
    const url = getContextPath() + '/api/note/' + noteUuid;
    const requestBody = params.toString();
    console.log("[DEBUG_LOG] updateNote: Sending request to URL:", url);
    console.log("[DEBUG_LOG] updateNote: Request method: PUT");
    console.log("[DEBUG_LOG] updateNote: Request body:", requestBody);

    // send PUT request to update note
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: requestBody
    }).then(response => {
        console.log("[DEBUG_LOG] updateNote: Response status:", response.status);
        console.log("[DEBUG_LOG] updateNote: Response status text:", response.statusText);

        if (response.ok) {
            console.log("[DEBUG_LOG] updateNote: Response OK, reloading page");
            // reload the page to see the changes, with cache-busting parameter
            location.href = location.href.split('?')[0] + '?t=' + new Date().getTime();
        } else {
            console.log("[DEBUG_LOG] updateNote: Response not OK");
            response.text().then(text => {
                console.log("[DEBUG_LOG] updateNote: Error text:", text);
                alert('Error: ' + text);
            });
        }
    }).catch(error => {
        console.log("[DEBUG_LOG] updateNote: Fetch error:", error);
    });
}
