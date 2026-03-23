# Hotel Management System

A full-stack Hotel Management System built with Node.js, Express, MySQL, and standard HTML/CSS/JS. This application allows hotel staff to manage room bookings, customers, and checkouts quickly and efficiently.

## Features

- **User Authentication:** Login capabilities for different user roles (e.g., admin, receptionist).
- **Room Management:** View all hotel rooms and their current statuses in real-time.
- **Customer Tracking:** Register new guests, capturing details such as Name, Phone, Email, and Aadhaar ID.
- **Booking System:** Seamlessly assign rooms to guests. The system automatically marks booked rooms to prevent double-booking.
- **Checkout & Billing:** Quickly process checkouts. The system automatically calculates total bills based on room rent and length of stay, unassigning the room for future bookings.

## Tech Stack

- **Frontend:** HTML5, CSS, Vanilla JavaScript
- **Backend:** Node.js, Express.js
- **Database:** MySQL
- **Key Modules:** `body-parser`, `cors`, `express`, `mysql2`

## Prerequisites

Make sure you have the following software installed on your machine:
- [Node.js](https://nodejs.org/) (which includes npm)
- A local [MySQL Server](https://dev.mysql.com/downloads/mysql/) or a stack like [XAMPP](https://www.apachefriends.org/index.html)

## Database Configuration

1. Open your MySQL environment (like MySQL Workbench, phpMyAdmin, or your terminal).
2. Create a new database named `hotel_management`:
   ```sql
   CREATE DATABASE hotel_management;
   USE hotel_management;
   ```
3. Create the required tables (if not already provided in a `.sql` file): `users`, `rooms`, `customers`, and `bookings`. 
4. Verify your database connection settings in `server.js`. Update the password if needed to match yours:
   ```javascript
   const db = mysql.createConnection({
       host: 'localhost',
       user: 'root',
       password: 'Dhruv@4567', // <-- Update if your DB password is different
       database: 'hotel_management'
   });
   ```

## Running the Application

1. Open a terminal in the project directory (e.g., `c:\HotelManagementSystem`).
2. Install the necessary Node dependencies:
   ```bash
   npm install
   ```
3. Start the application backend server:
   ```bash
   npm start
   # or run: node server.js
   ```
4. The API and Frontend server will boot up. Open your preferred web browser and navigate to:
   [http://localhost:3000](http://localhost:3000)

Your application is now successfully running locally! Enjoy managing your hotel!
