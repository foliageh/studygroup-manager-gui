# Study Group Manager GUI

JavaFX desktop client and multithreaded server for a shared collection of study groups. Several
users work with the same data at once: a table and an animated map show every group, and changes
made by others appear without a restart.

![Main window](https://github.com/foliageh/studygroup-manager-gui/assets/46216950/ec7bd2e7-a495-4b1b-8dea-8b308d35f227)

**Stack:** Java 17 · JavaFX 20 (FXML, CSS) · TCP sockets · `java.util.concurrent` · JDBC · PostgreSQL · Gradle (multi-project)

## Features

- **Login and registration**, each user edits only their own groups.
- **Table view** with sorting and filtering, and a card view for creating and editing a group with
  field validation.
- **Visualization.** Groups are drawn on an animated coordinate map with the user's own groups
  highlighted. A click on a shape opens the group.
- **Localization** into four languages (Russian, English, German, Albanian) with `ResourceBundle`,
  switched on the fly, with locale-aware date formats.
- **Responsive UI.** Network calls run on a background executor, the UI thread only renders
  results, and an update loop pulls changes made by other users.

## Architecture

The server accepts connections into a cached thread pool, processes requests per client and writes
responses from a ForkJoinPool. Data lives in PostgreSQL behind a DAO layer. Client and server share
a `common` module with the models and the request and response messages.

Previous console version: [studygroup-manager](https://github.com/foliageh/studygroup-manager).

---
Built as coursework for the Programming course at ITMO University, 2023.
