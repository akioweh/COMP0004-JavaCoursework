<%--
  Author: akioweh 
  24/03/2025 5:58 am
--%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/style.css">
<script>
    function newNote() {
        // send POST request to create a new note
        fetch('${pageContext.request.contextPath}/api/note', {
            method: 'POST',
        }).then(response => {
            if (response.ok) {
                // redirect to the new note page
                window.location.href = response.url;
            } else {
                console.error('Failed to create new note');
            }
        });
    }
</script>
