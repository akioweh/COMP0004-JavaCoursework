<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Notes App</title>
</head>
<body>
<h1>Notes App</h1>
<h2>Notes Index</h2>
<ul>
    <li><a href="note.jsp?id=1">Note 1</a></li>
    <li><a href="note.jsp?id=2">Note 2</a></li>
    <li><a href="note.jsp?id=3">Note 3</a></li>
</ul>
<h2>Create a New Note</h2>
<form action="createNote" method="post">
    <label for="noteName">Note Name:</label>
    <input type="text" id="noteName" name="noteName" required>
    <br>
    <label for="noteContent">Note Content:</label>
    <textarea id="noteContent" name="noteContent" required></textarea>
    <br>
    <button type="submit">Create Note</button>
</form>
</body>
</html>