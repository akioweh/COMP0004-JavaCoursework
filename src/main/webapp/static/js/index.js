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
 * Shows the edit form for the index
 */
function editIndex() {
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
    if (confirm('Are you sure you want to remove this entry from the index?')) {
        // send DELETE request to remove entry
        fetch(getContextPath() + '/api/index/' + indexUuid + '/entry/' + entryUuid, {
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
 * Updates the index with the form data
 * @param {Event} event - The form submit event
 */
function updateIndex(event) {
    event.preventDefault();
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
            // reload the page to see the changes, with cache-busting parameter
            location.href = location.href.split('?')[0] + '?t=' + new Date().getTime();
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    }).catch(error => {
    });
}
