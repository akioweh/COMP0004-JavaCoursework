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
  - JSTL (JSP Standard Tag Library): For common JSP tasks
  - EL (Expression Language): For accessing data in JSPs
- **RESTful API**: For client-server communication following REST principles
- **Java Serialization**: For binary serialization/deserialization of objects
- **Maven**: For project management and build automation

## Project Structure

The project follows the standard Maven project structure:

```
COMP0004-JavaCoursework/
├── .idea/                  # IntelliJ IDEA configuration files
├── .mvn/                   # Maven wrapper configuration
├── data/                   # Application data files (binary serialized)
├── src/
│   ├── main/
│   │   ├── java/           # Java source code
│   │   │   └── com/
│   │   │       └── akioweh/
│   │   │           └── comp0004javacoursework/
│   │   │               ├── api/        # RESTful API endpoints
│   │   │               ├── engine/     # Core functionality
│   │   │               ├── main/       # Application initialization
│   │   │               ├── models/     # Data models (beans)
│   │   │               ├── renderers/  # Content rendering
│   │   │               ├── util/       # Utility classes
│   │   │               └── view/       # View controllers
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

routes.md documents the server-client communication structure.
refer to it and update it accordingly.

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
- Using binary serialization for efficient storage and retrieval
- Storing each object in a separate binary file named after its UUID
- Providing a caching mechanism to improve performance

### API and View Servlets

The application follows a clear separation between API endpoints and view controllers:

#### API Servlets
The API servlets handle data operations and follow RESTful principles:
- **ApiServlet**: Base class for all API servlets, providing common functionality
- **IndexApiServlet**: Handles CRUD operations for indexes
- **NoteApiServlet**: Handles CRUD operations for notes
- **ElementApiServlet**: Handles operations for note elements (text, links, media)

#### View Servlets
The view servlets handle rendering and user interface:
- **ViewServlet**: Base class for all view servlets, providing common functionality
- **RootViewServlet**: Renders the home page
- **IndexViewServlet**: Renders indexes
- **NoteViewServlet**: Renders notes

This separation allows for a clean implementation of the MVC pattern, with API servlets acting as controllers for data operations and View servlets handling the presentation layer.

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

The application follows the Model-View-Controller (MVC) pattern:

- **Model**: The model classes (beans) represent the data and business logic
  - Located in the `models` package
  - Implemented as Java beans with properties and getters/setters
  - Serializable for persistence

- **View**: The views render the data for the user
  - Implemented using JSPs with custom tags
  - Located in the `webapp/WEB-INF/jsp` directory
  - Use JSTL and EL (Expression Language) for dynamic content

- **Controller**: The controllers handle user input and update the model
  - Split into two types:
    - API controllers (`api` package): Handle data operations via RESTful endpoints
    - View controllers (`view` package): Handle rendering and user interface

This separation ensures:
- Models are independent of the presentation layer
- Controllers focus on request handling and delegation
- Views focus on presentation without business logic
- Clear separation of concerns for maintainability

### Error Handling

- Use exceptions for error handling
- Log errors using the Java logging API
- Provide meaningful error messages to users
