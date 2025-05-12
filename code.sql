
USE eventrix;


CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('user', 'admin') NOT NULL DEFAULT 'user'
);


CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_type ENUM('Farewell Party', 'Concert', 'Wedding Party', 'Birthday', 'Corporate Event', 'Bachelorette Party') NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL
);

CREATE TABLE venues (
    venue_id INT AUTO_INCREMENT PRIMARY KEY,
    venue_name VARCHAR(100) NOT NULL,
    venue_type ENUM('Farewell Hall', 'Concert Ground', 'Wedding Hall', 'Birthday Hall', 'Corporate Hall', 'Bachelorette Hall') NOT NULL,
    capacity INT NOT NULL
);


CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    venue_id INT NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    delete_request TINYINT(1) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES venues(venue_id) ON DELETE CASCADE
);

INSERT INTO user (username, email, password, role) VALUES
('AdminUser', 'admin@example.com', 'admin123', 'admin');

INSERT INTO events (event_name, event_type, event_date, event_time) VALUES
('Spring Festival', 'Concert', '2025-05-10', '18:00:00'),
('Wedding of Alice & Bob', 'Wedding Party', '2025-07-15', '12:00:00');

INSERT INTO venues (venue_name, venue_type, capacity) VALUES
('Sunset Hall', 'Wedding Hall', 200),
('Rock Arena', 'Concert Ground', 5000);

INSERT INTO bookings (user_id, event_id, venue_id, event_date, event_time) VALUES
(1, 1, 2, '2025-05-10', '18:00:00'),
(1, 2, 1, '2025-07-15', '12:00:00');