create database AirlineManagement;
use AirlineManagement;
CREATE TABLE flights (
    id INTEGER PRIMARY KEY auto_increment,
    flight_number TEXT,
    origin TEXT,
    destination TEXT
);
CREATE TABLE seats (
    id INTEGER PRIMARY KEY auto_increment,
    flight_id INTEGER,
    seat_number TEXT,
    is_booked BOOLEAN DEFAULT 0,
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY auto_increment,
    customer_name TEXT,
    seat_id INTEGER,
    flight_id INTEGER,
    FOREIGN KEY (seat_id) REFERENCES seats(id),
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);
INSERT INTO flights (flight_number, origin, destination) VALUES
('AI101', 'Delhi', 'Mumbai'),
('AI102', 'Mumbai', 'Delhi'),
('AI103', 'Chennai', 'Kolkata'),
('AI104', 'Bangalore', 'Hyderabad'),
('AI105', 'Hyderabad', 'Bangalore'),
('AI106', 'Delhi', 'Chennai'),
('AI107', 'Kolkata', 'Delhi'),
('AI108', 'Mumbai', 'Chennai'),
('AI109', 'Chennai', 'Delhi'),
('AI110', 'Bangalore', 'Kolkata');
INSERT INTO seats (flight_id, seat_number) VALUES
(1, '1A'), (1, '1B'), (1, '1C'), (1, '1D'), (1, '1E'),
(1, '2A'), (1, '2B'), (1, '2C'), (1, '2D'), (1, '2E'),
(2, '1A'), (2, '1B'), (2, '1C'), (2, '1D'), (2, '1E'),
(2, '2A'), (2, '2B'), (2, '2C'), (2, '2D'), (2, '2E'),
(3, '1A'), (3, '1B'), (3, '1C'), (3, '1D'), (3, '1E'),
(3, '2A'), (3, '2B'), (3, '2C'), (3, '2D'), (3, '2E'),
(4, '1A'), (4, '1B'), (4, '1C'), (4, '1D'), (4, '1E'),
(4, '2A'), (4, '2B'), (4, '2C'), (4, '2D'), (4, '2E'),
(5, '1A'), (5, '1B'), (5, '1C'), (5, '1D'), (5, '1E'),
(5, '2A'), (5, '2B'), (5, '2C'), (5, '2D'), (5, '2E'),
(6, '1A'), (6, '1B'), (6, '1C'), (6, '1D'), (6, '1E'),
(6, '2A'), (6, '2B'), (6, '2C'), (6, '2D'), (6, '2E'),
(7, '1A'), (7, '1B'), (7, '1C'), (7, '1D'), (7, '1E'),
(7, '2A'), (7, '2B'), (7, '2C'), (7, '2D'), (7, '2E'),
(8, '1A'), (8, '1B'), (8, '1C'), (8, '1D'), (8, '1E'),
(8, '2A'), (8, '2B'), (8, '2C'), (8, '2D'), (8, '2E'),
(9, '1A'), (9, '1B'), (9, '1C'), (9, '1D'), (9, '1E'),
(9, '2A'), (9, '2B'), (9, '2C'), (9, '2D'), (9, '2E'),
(10, '1A'), (10, '1B'), (10, '1C'), (10, '1D'), (10, '1E'),
(10, '2A'), (10, '2B'), (10, '2C'), (10, '2D'), (10, '2E');
Select * from flights;
select * from seats;
select * from bookings;




