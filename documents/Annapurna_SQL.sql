use annapurnadb;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(50),
    phone VARCHAR(10),
    role VARCHAR(20)
);

desc users;
insert into users values(8, "Priya","Desai", "priya@gmail.com", "789p", "8765123489","ADMIN");
select * from users;



CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30),
    description TEXT,
    price DOUBLE,
    category VARCHAR(30),
    available TINYINT(1)
);

desc products;
insert into products values(10, "Organic Tomatoes", "Fresh organic tomatoes","50","Vegetables",1);
select * from products;

CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    address_line1 VARCHAR(40),
    address_line2 VARCHAR(40),
    city VARCHAR(30),
    state VARCHAR(40),
    postal_code VARCHAR(20),
    country VARCHAR(40),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
desc addresses;
select * from addresses;



CREATE TABLE subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    subscription_plan VARCHAR(20),
    subscription_date TIMESTAMP,
    subscription_end_date TIMESTAMP,
    number_of_weeks INT,
    number_of_days INT,
    days_of_week VARCHAR(500),
    address_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (address_id) REFERENCES addresses(id)
);

desc subscriptions;
select * from subscriptions;



