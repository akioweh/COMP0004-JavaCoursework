/**
 * Common JavaScript functions used across multiple pages
 */

/**
 * Creates a new note and redirects to it
 */
function newNote() {
    // send POST request to create a new note
    fetch(getContextPath() + '/api/note', {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            // redirect to the new note
            window.location.href = response.url;
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    });
}

/**
 * Creates a new index and redirects to it
 */
function newIndex() {
    // send POST request to create a new index
    fetch(getContextPath() + '/api/index', {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            // redirect to the new index
            window.location.href = response.url;
        } else {
            response.text().then(text => {
                alert('Error: ' + text);
            });
        }
    });
}

/**
 * Helper function to get the context path
 * @returns {string} The context path
 */
function getContextPath() {
    return window.contextPath || '';
}

/**
 * iframe auto-resize function
 * @param obj
 */
function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.documentElement.scrollHeight + 'px';
}
