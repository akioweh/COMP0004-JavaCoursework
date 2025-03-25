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

    // send PUT request to update element
    fetch(getContextPath() + '/api/element/' + noteUuid + '/' + elementUuid, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(formData).toString()
    }).then(response => {
        if (response.ok) {
            // reload the page to see the changes
            location.reload();
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
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

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            elementType: elementType
        }).toString()
    }).then(response => {
        if (response.ok) {
            // reload the page to see the new element
            location.reload();
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
                // reload the page to see the changes
                location.reload();
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
                // redirect to home page
                window.location.href = getContextPath() + '/';
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

    // send PUT request to update note
    fetch(getContextPath() + '/api/note/' + noteUuid, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(formData).toString()
    }).then(response => {
        if (response.ok) {
            // reload the page to see the changes
            location.reload();
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    });
}
