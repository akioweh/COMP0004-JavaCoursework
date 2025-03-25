# Routes Documentation

This document provides an overview of the URL path structure for all operations in the application.

## View Endpoints

These endpoints render HTML pages for user interaction.

### Root View
- **URL Pattern**: `/` or empty path
- **Servlet**: `RootViewServlet`
- **HTTP Methods**:
  - `GET`: Renders the home page

### Index View
- **URL Pattern**: `/index/*`
- **Servlet**: `IndexViewServlet`
- **HTTP Methods**:
  - `GET`: Renders an index
    - `/index`: Renders the root index
    - `/index/{uuid}`: Renders the index with the specified UUID
  - `POST`, `PUT`, `DELETE`: Forwards to the corresponding API endpoint

### Note View
- **URL Pattern**: `/note/*`
- **Servlet**: `NoteViewServlet`
- **HTTP Methods**:
  - `GET`: Renders a note
    - `/note/{uuid}`: Renders the note with the specified UUID
  - `POST`, `PUT`, `DELETE`: Forwards to the corresponding API endpoint

## API Endpoints

These endpoints handle data operations and follow RESTful principles.

### Index API
- **URL Pattern**: `/api/index/*`
- **Servlet**: `IndexApiServlet`
- **HTTP Methods**:
  - `GET`: Forwards to the Index View endpoint
    - `/api/index`: Forwards to `/index` to avoid redundancy
    - `/api/index/{uuid}`: Forwards to `/index/{uuid}` to avoid redundancy
  - `POST`: Creates a new index
  - `PUT`: Updates an existing index
    - `/api/index/{uuid}`: Updates the index with the specified UUID
  - `DELETE`: Deletes an index
    - `/api/index/{uuid}`: Deletes the index with the specified UUID

### Index Entry API
- **URL Pattern**: `/api/index/{indexUuid}/entry/{entryUuid}`
- **Servlet**: `IndexApiServlet`
- **HTTP Methods**:
  - `GET`: Retrieves an entry from an index
  - `POST`: Adds an entry to an index
  - `PUT`: Updates an entry in an index
  - `DELETE`: Removes an entry from an index

### Note API
- **URL Pattern**: `/api/note/*`
- **Servlet**: `NoteApiServlet`
- **HTTP Methods**:
  - `GET`: Forwards to the Note View endpoint
    - `/api/note/{uuid}`: Forwards to `/note/{uuid}` to avoid redundancy
  - `POST`: Creates a new note
  - `PUT`: Updates an existing note
    - `/api/note/{uuid}`: Updates the note with the specified UUID
  - `DELETE`: Deletes a note
    - `/api/note/{uuid}`: Deletes the note with the specified UUID

### Element API
- **URL Pattern**: `/api/element/*`
- **Servlet**: `ElementApiServlet`
- **HTTP Methods**:
  - `POST`: Creates a new element
    - `/api/element/{noteUuid}`: Creates a new element in the note with the specified UUID
    - `/api/element/{noteUuid}/{elementUuid}`: Creates a new element before the element with the specified UUID
  - `PUT`: Updates an existing element
    - `/api/element/{noteUuid}/{elementUuid}`: Updates the element with the specified UUID
  - `DELETE`: Deletes an element
    - `/api/element/{noteUuid}/{elementUuid}`: Deletes the element with the specified UUID

Note: The Element API does not support GET requests as elements are viewed through the Note View endpoint. The API package is designed for modification requests only, while views are handled by the view servlets.

## URL Path Structure

The application follows a consistent URL path structure:

1. View endpoints:
   - `/`: Home page
   - `/index[/{uuid}]`: Index view
   - `/note/{uuid}`: Note view

2. API endpoints:
   - `/api/index[/{uuid}]`: Index operations
   - `/api/index/{indexUuid}/entry/{entryUuid}`: Index entry operations
   - `/api/note[/{uuid}]`: Note operations
   - `/api/element/{noteUuid}[/{elementUuid}]`: Element operations

All UUIDs are in the standard UUID format (e.g., `123e4567-e89b-12d3-a456-426614174000`).
