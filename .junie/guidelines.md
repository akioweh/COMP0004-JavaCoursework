# COMP0004-JavaCoursework Developer Guidelines

## Project Overview

This project is a Java web application for storing, searching, and viewing collections of notes. It follows the Model-View-Controller (MVC) pattern and is implemented using Java servlets and JSPs.

The application allows users to:
- Create, edit, delete, and rename notes
- Organize notes in indexes (collections)
- Search notes and indexes
- View notes in different ways (sorted, in order added, etc.)
- Group notes into categories
- Store different types of content in notes (text, URLs, images, etc.)

The application automatically saves changes to files, so users don't need to manually save their work.

## Tech Stack

- **Java 23**: The programming language used for the backend
- **Jakarta EE**: The enterprise edition of Java for web applications
  - Jakarta Servlet API 6.1.0: For handling HTTP requests
  - Jakarta MVC API 2.1.0: For implementing the MVC pattern
- **Tomcat 11.0.4**: The embedded web server
- **JSP (JavaServer Pages)**: For generating dynamic HTML content
- **Jackson 2.18.3**: For JSON serialization/deserialization
- **Maven**: For project management and build automation

## Project Structure

The project follows the standard Maven project structure:

```
COMP0004-JavaCoursework/
├── .idea/                  # IntelliJ IDEA configuration files
├── .mvn/                   # Maven wrapper configuration
├── data/                   # Application data files (JSON)
├── src/
│   ├── main/
│   │   ├── java/           # Java source code
│   │   │   └── com/
│   │   │       └── akioweh/
│   │   │           └── comp0004javacoursework/
│   │   │               ├── engine/     # Core functionality
│   │   │               ├── main/       # Application initialization
│   │   │               ├── models/     # Data models
│   │   │               ├── renderers/  # Content rendering
│   │   │               ├── servlets/   # HTTP request handlers
│   │   │               └── util/       # Utility classes
│   │   ├── resources/      # Application resources
│   │   └── webapp/         # Web application files
│   │       ├── static/     # Static resources (CSS, JS, etc.)
│   │       └── WEB-INF/    # Web application configuration
│   │           ├── jsp/    # JSP files (views)
│   │           └── web.xml # Web application deployment descriptor
│   └── test/               # Test code
├── target/                 # Compiled output
├── .gitignore              # Git ignore file
├── COMP0004-JavaCoursework.iml # IntelliJ IDEA module file
├── logfile.txt             # Application log file
├── mvnw                    # Maven wrapper script (Unix)
├── mvnw.cmd                # Maven wrapper script (Windows)
├── pom.xml                 # Maven project configuration
└── README.md               # Project documentation
```

## Key Components and Their Responsibilities

### Models

The application uses several model classes to represent the data:

- **UUIO**: Base class for all model objects, providing UUID-based identification
- **Index**: Represents a collection of notes and other indexes
- **Note**: Represents a note with a title, brief description, and content elements
- **NoteElement**: Base class for note content elements
  - **TextElement**: Represents plain text content
    - **HTMLElement**: Represents HTML content
  - **LinkElement**: Represents a link to a resource
    - **MediaElement**: Represents embedded media (images, videos, etc.)

### Engine

The **Engine** class is the core of the application, responsible for:
- Managing the in-memory cache of notes and indexes
- Coordinating with the StorageHandler to read and write data to disk
- Providing methods for adding, retrieving, saving, and deleting notes and indexes
- Maintaining the root index as the top-level container for all notes and indexes

The Engine is implemented as a singleton, accessible via `Engine.getInstance()`.

### StorageHandler

The **StorageHandler** class is responsible for:
- Reading and writing data to and from the filesystem
- Using Jackson for JSON serialization/deserialization
- Storing each object in a separate JSON file named after its UUID

### Servlets

The application uses servlets as controllers to handle HTTP requests:
- **NoteServlet**: Handles requests related to notes (create, edit, delete, view)
- Other servlets handle different aspects of the application

### JSPs

The application uses JSPs as views to generate HTML content:
- **index.jsp**: The main page of the application
- **render_index.jsp**: Renders the index of notes
- **render_note.jsp**: Renders individual notes
- **meta.jsp**: Contains metadata for the web pages
- **footer.jsp**: Contains the footer section of the web pages

## Coding Conventions

### Java Code Style

- Use 4 spaces for indentation
- Follow the Java naming conventions:
  - `camelCase` for variables and methods
  - `PascalCase` for classes and interfaces
  - `UPPER_SNAKE_CASE` for constants
- Use meaningful names for variables, methods, and classes
- Add Javadoc comments for classes, methods, and fields
- Use `@NotNull` and `@Nullable` annotations to indicate nullability
- Use `final` for immutable fields

### MVC Pattern

- Keep the MVC layers separate:
  - Models should not depend on controllers or views
  - Controllers should not contain business logic
  - Views should not contain business logic
- Use servlets as controllers
- Use JSPs as views
- Use model classes for data representation

### Error Handling

- Use exceptions for error handling
- Log errors using the Java logging API
- Provide meaningful error messages to users
