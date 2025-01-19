# TicTacToeServer

This application is a Tic Tac Toe game server that allows users to play Tic Tac Toe against each other online. 
The server provides a simple interface with buttons to start and stop the server, as well as a real-time bar chart that displays the status of online and offline users.

Steps to Use the Application:  
Start the Server:  
Click the "Start Server" button to initialize and launch the Tic Tac Toe game server. This will allow players to connect and start playing.

Stop the Server:  
Once you're done or want to shut down the server, click the "Stop Server" button to stop the server and disconnect any active players.

View Online/Offline Users:  
A bar chart will dynamically update to show the number of online and offline users.
Online Users: Represented in one color (e.g., green), indicating the number of users currently connected to the server.
Offline Users: Represented in another color (e.g., red), showing the number of users who are currently offline.

Running the Application:  
Start the Application:  

Launch the application and open the user interface.
Ensure that your environment (such as Java or other necessary dependencies) is set up correctly.  
Interact with the UI:  

Click the "Start Server" button to activate the server.
Once the server is running, users can connect, and the bar chart will reflect the number of active (online) players.
When you want to stop the server, click the "Stop Server" button, which will also disconnect any players and show the updated status in the bar chart.  

## Client Repository

The client code for this project is available at the following GitHub repository:

[Client GitHub Repository](https://github.com/ahmedsaad207/TicTacToe)

## JAR Dependencies  
The project requires the following JAR files for proper functionality:  

**Gson** (version 2.2.4): A library for converting Java objects to JSON and vice versa.
File: Gson-2.2.4.jar  
**JSON** (version 1.0.4): A lightweight library for working with JSON data in Java.
File: json-1.0.4.jar  

 **Services:**
  - **Java DB**: The project utilizes **Java DB** for storing user data, handling game sessions, and maintaining game states. Ensure Java DB is installed and set up correctly to manage data persistence.

## Database Schema

The following schema represents the structure of the database for the Tic Tac Toe game server:

| Column Name  | Data Type | Size   | Description                          |
|--------------|-----------|--------|--------------------------------------|
| `username`   | `VARCHAR` | 20     | Stores the username of the user.     |
| `email`      | `VARCHAR` | 40     | Stores the email (Primary Key).      |
| `score`      | `INTEGER` | -      | Stores the user's current score.     |
| `isonline`   | `BOOLEAN` | -      | Indicates whether the user is online.|
| `isavailable`| `BOOLEAN` | -      | Indicates whether the user is available for a game. |
| `password`   | `VARCHAR` | 250    | Stores the user's hashed password.   |

### Notes:
- The `email` column is the **Primary Key** for the table.
- The `password` column stores a hashed version of the user's password for security purposes.

## Project Structure

This project follows the MVC (Model-View-Controller) architecture with the following key directories and files:

controller/: Contains the logic for handling user interactions and controlling the flow of the application.

DashBoardController.java: Manages the interaction between the user interface (view) and the underlying game logic (model). Handles the start/stop server actions and updates the view accordingly.
model/: Represents the data and business logic of the application.

User.java: Represents a user in the game, containing properties such as user status (online/offline).
UserDao.java: Handles data access for managing users, such as storing or retrieving user information from a database 
server/: Contains files related to handling the server-side logic and connections.

Server.java: Responsible for managing the server connection, starting the server, and listening for incoming client connections.
handler/: Handles individual client connections in separate threads to ensure that each player can interact with the server independently.

ClientHandler.java: Manages communication with a single client. Each client is handled in a separate thread to allow simultaneous gameplay for multiple users.
view/: Manages the graphical user interface (GUI) and user interaction elements.

DashBoard.java: The main dashboard that provides the user interface with buttons to start and stop the server and a bar chart to display online/offline user status.

## The following individuals contributed to this project:

Ahmed Saad – comm.ahmedsaad@alexu.edu.eg  
Ali Kotb – Alikotb38@gmail.com  
Nour Agami – nuralquds123@gmail.com  
Yousef Adel – youssefadelfayad@gmail.com  
