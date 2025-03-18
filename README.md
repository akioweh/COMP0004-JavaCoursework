# COMP0004-JavaCoursework

Notes app thing

## Specification (Excerpt)

Implement a program for as many of the requirements listed below that you have time to complete.
You can use any of the classes provided in the standard JDK, plus the Tomcat and the Jakarta EE libraries for servlets
and JSPs that are provided via the example code (see links at the end).
You should not add any other additional third-party libraries, except for reading/writing CSV, JSON, or XML format
files, and do not need to use a database.
Use Java 23.

The requirements form a specification for the required program, so they don’t have to be answered in the order they are
written, and you may well find it easier to work on each requirement in stages as you develop your program.
You should submit the final version of your program only.

### Overview

The program is for storing, searching, and viewing collections of notes.
While this is implemented as a web application assume it is for your own personal use and only accessible by yourself,
so no user management or logging on is required.
The data should be stored in files; you decide the
format of those files (e.g., text, JSON, etc.).

The user interface should be implemented as web pages viewed in a web browser, using standard HTML and CSS.
You don't need to implement a sophisticated user interface or start using CSS libraries like Bootstrap, but you might
want to experiment.

This module is primarily about Java programming, not creating complex front-ends for web applications!
Don’t get side-tracked into spending too much time on the appearance of web pages.

It is up to you to interpret the requirements and decide the details of what you actually implement.

### Requirement 1.

The program can store one or more kinds of note. Each note has a name or label, might hold text,
a URL, an image, some other kind of data, or a combination of these such as a note with text and
an image. A basic note just holds text.

### Requirement 2.

An index is used to hold the collection of notes and the index can be displayed. Clicking on an
index entry displays the corresponding note.

### Requirement 3.

Notes can be created, deleted, edited, and renamed. Notes can be viewed in one or more way, for
example, in sorted order, in the order added, a summary, or the full note. When a note is created it
is added to the index, and removed when deleted.

### Requirement 4.

The index and the notes in the index can be searched to display a list of matches. Clicking on a
match displays the content of the relevant note. Ideally, a free-form text search is supported,
meaning that the search string matches any text in a note. If you store images or other kinds of
data think about how they can be searched.

### Requirement 5.

Notes and the index are automatically saved to files in some format (you decide), so that the user
does not have to manually load or save to a file. When an index or a note is modified the change(s)
are immediately written to the files.

### Requirement 6.

Add the functionality to group notes into named categories, for example, reminders, appointments
or birthdays. Update the way notes are displayed, indexed and searched accordingly. A category
can be represented as index, so that an index entry can now be another index of notes. Notes can
be in more than one index.

### Requirement 7 (Optional challenge)

Add your own functionality to the application, for example more advanced formatting of notes,
additional kinds of notes, and so on. This is a chance to demonstrate your programming abilities
but stick to the use of standard Java, Jarkata EE, etc. as outlined earlier. It must be possible to
build your program using Maven, without having to install any other software. 


