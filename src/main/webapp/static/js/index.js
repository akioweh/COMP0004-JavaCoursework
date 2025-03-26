/**
 * JavaScript functions for index operations
 */

/**
 * Deletes the current index
 */
function deleteIndex() {
    if (confirm('Are you sure you want to delete this index?')) {
        // send DELETE request to delete index
        fetch(getContextPath() + '/api/index/' + indexUuid, {
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
 * Shows the edit form for the index
 */
function editIndex() {
    // Check if this is the root index
    const rootIndexUuid = document.querySelector('meta[name="rootIndexUuid"]').getAttribute('content');
    if (indexUuid === rootIndexUuid) {
        alert('The root index cannot be edited.');
        return;
    }

    document.getElementById('index-view').style.display = 'none';
    document.getElementById('index-edit').style.display = 'block';
}

/**
 * Hides the edit form for the index
 */
function cancelEdit() {
    document.getElementById('index-view').style.display = 'block';
    document.getElementById('index-edit').style.display = 'none';
}

/**
 * Removes an entry from the index
 * @param {string} entryUuid - UUID of the entry to remove
 */
function removeEntry(entryUuid) {
    // Check if this is the root index
    const rootIndexUuid = document.querySelector('meta[name="rootIndexUuid"]').getAttribute('content');
    if (indexUuid === rootIndexUuid) {
        alert('Entries cannot be removed from the root index.');
        return;
    }

    if (confirm('Are you sure you want to remove this entry from the index?')) {
        // send DELETE request to remove entry
        fetch(getContextPath() + '/api/index/' + indexUuid + '/entry/' + entryUuid, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                // reload the page to see the changes
                location.href = location.href.split('?')[0];
            } else {
                response.text().then(text => {
                    alert('Error: ' + text);
                });
            }
        });
    }
}

/**
 * Updates the index with the form data
 * @param {Event} event - The form submit event
 */
function updateIndex(event) {
    event.preventDefault();

    // Check if this is the root index
    const rootIndexUuid = document.querySelector('meta[name="rootIndexUuid"]').getAttribute('content');
    if (indexUuid === rootIndexUuid) {
        alert('The root index cannot be edited.');
        return;
    }

    const form = document.getElementById('edit-index-form');
    const formData = new FormData(form);


    // Convert FormData to URLSearchParams manually for better browser compatibility
    const params = new URLSearchParams();
    for (const [key, value] of formData.entries()) {
        params.append(key, value.toString());
    }

    const url = getContextPath() + '/api/index/' + indexUuid;
    const requestBody = params.toString();

    // send PUT request to update index
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: requestBody
    }).then(response => {
        if (response.ok) {
            // reload the page to see the changes
            location.href = location.href.split('?')[0];
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    }).catch();
}

/**
 * Adds an entry to the index
 * @param {string} entryUuid - UUID of the entry to add
 */
function addEntry(entryUuid) {
    if (!entryUuid) {
        alert('Please select an entry to add');
        return;
    }

    // Check if this is the root index
    const rootIndexUuid = document.querySelector('meta[name="rootIndexUuid"]').getAttribute('content');
    if (indexUuid === rootIndexUuid) {
        alert('Entries cannot be manually added to the root index. The root index automatically contains all existing notes and indexes.');
        return;
    }

    // send POST request to add entry
    fetch(getContextPath() + '/api/index/' + indexUuid + '/entry/' + entryUuid, {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            // reload the page to see the changes
            location.href = location.href.split('?')[0];
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    });
}
