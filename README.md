# Movie Manager Application

Movie Manager is a Spring Boot web application for managing a collection of movies. Users can view, add, edit, and delete movies, with each movie including details like title, director, release year, rating, and genre.

The application uses Spring Data JPA with a PostgreSQL database and includes user authentication via Spring Security, supporting different access levels for users and admins.

A REST API is also provided for accessing movie data in JSON format, enabling integration with other tools or frontends.

The project is deployed online and can be accessed via a public URL.

## Deployment

The application is deployed using Railway. The backend service and PostgreSQL database are hosted on Railway, and environment variables are used to configure the database connection and server port.

**Live:** [Movie Manager Online](https://movie-manager-production.up.railway.app/login)

## Reset Password

Users can request a password reset by entering their email. A secure reset link is sent via email using Brevo API, allowing them to set a new password.
