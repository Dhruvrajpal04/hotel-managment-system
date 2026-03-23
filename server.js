const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bodyParser = require('body-parser');
const path = require('path');

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'frontend')));

// Database Connection
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'Dhruv@4567', // Using the password from the Java code
    database: 'hotel_management'
});

db.connect((err) => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
        return;
    }
    console.log('Connected to MySQL Database: hotel_management');
});

// API Routes

// Login route
app.post('/api/login', (req, res) => {
    const { username, password } = req.body;
    const query = "SELECT role FROM users WHERE username=? AND password=?";
    db.query(query, [username, password], (err, results) => {
        if (err) return res.status(500).json(err);
        
        if (results.length > 0) {
            res.json({ message: 'Login successful', role: results[0].role, username: username });
        } else {
            res.status(401).json({ message: 'Invalid username or password' });
        }
    });
});

// Get all rooms
app.get('/api/rooms', (req, res) => {
    db.query('SELECT * FROM rooms', (err, results) => {
        if (err) return res.status(500).json(err);
        res.json(results);
    });
});

// Get all customers
app.get('/api/customers', (req, res) => {
    db.query('SELECT * FROM customers', (err, results) => {
        if (err) return res.status(500).json(err);
        res.json(results);
    });
});

// Add a new customer
app.post('/api/customers', (req, res) => {
    const { name, phone, email, adhaar } = req.body;
    const query = "INSERT INTO customers(name, phone, email, adhaar) VALUES(?, ?, ?, ?)";
    db.query(query, [name, phone, email, adhaar], (err, results) => {
        if (err) return res.status(500).json(err);
        res.json({ message: 'Customer Added!', customer_id: results.insertId });
    });
});

// Get all bookings with customer details
app.get('/api/bookings', (req, res) => {
    const query = `
        SELECT b.booking_id, c.customer_id, c.name, c.phone, c.email, c.adhaar, b.room_id, b.check_in, b.check_out 
        FROM bookings b 
        JOIN customers c ON b.customer_id = c.customer_id
    `;
    db.query(query, (err, results) => {
        if (err) return res.status(500).json(err);
        res.json(results);
    });
});

// Add a new booking
app.post('/api/bookings', (req, res) => {
    const { customer_id, room_id } = req.body;
    
    // 1. Insert booking
    const bookQuery = "INSERT INTO bookings(customer_id, room_id, check_in) VALUES(?, ?, CURDATE())";
    db.query(bookQuery, [customer_id, room_id], (err, results) => {
        if (err) return res.status(500).json(err);
        
        // 2. Update room status to 'Booked'
        const updateRoom = "UPDATE rooms SET status='Booked' WHERE room_id=?";
        db.query(updateRoom, [room_id], (err2, results2) => {
            if (err2) return res.status(500).json(err2);
            res.json({ message: 'Room Booked!', booking_id: results.insertId });
        });
    });
});

// Checkout room
app.post('/api/checkout', (req, res) => {
    const { room_id, days } = req.body;
    
    // 1. Get room price
    db.query("SELECT price FROM rooms WHERE room_id=?", [room_id], (err, results) => {
        if (err || results.length === 0) return res.status(500).json(err || { message: 'Room not found' });
        
        const price = results[0].price;
        const totalBill = price * days;
        
        // 2. Update booking check_out date
        const updateBooking = "UPDATE bookings SET check_out=DATE_ADD(check_in, INTERVAL ? DAY) WHERE room_id=? AND check_out IS NULL";
        db.query(updateBooking, [days, room_id], (err2, results2) => {
            if (err2) return res.status(500).json(err2);
            
            // 3. Update room status to 'Available'
            db.query("UPDATE rooms SET status='Available' WHERE room_id=?", [room_id], (err3, results3) => {
                if (err3) return res.status(500).json(err3);
                res.json({ message: 'Checkout Done', total_bill: totalBill });
            });
        });
    });
});

// Root route to serve the dashboard
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'frontend', 'index.html'));
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
    console.log(`Dashboard active at http://localhost:${port}`);
});
