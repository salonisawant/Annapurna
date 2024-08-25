package com.annapurna.test;

import com.annapurna.dao.SubscriptionDAOImpl;
import com.annapurna.dao.AddressDAOImpl;
import com.annapurna.dao.UserDAOImpl;
import com.annapurna.model.Subscription;
import com.annapurna.service.SubscriptionService;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class SubscriptionServiceTest {

    private SubscriptionDAOImpl subscriptionDAO;
    private SubscriptionService subscriptionService;
    private UserDAOImpl userDAO;
    private AddressDAOImpl addressDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // MySQL database connection setup
        String url = "jdbc:mysql://localhost:3306/annapurnadb";
        String username = "root";
        String password = "1234";
        connection = DriverManager.getConnection(url, username, password);

        // Initialize DAOs and Services
        subscriptionDAO = new SubscriptionDAOImpl(connection);
        subscriptionService = new SubscriptionService(subscriptionDAO);
        userDAO = new UserDAOImpl(connection);
        addressDAO = new AddressDAOImpl(connection);

        // Create tables if not exists
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "first_name VARCHAR(255), " +
            "last_name VARCHAR(255), " +
            "email VARCHAR(255), " +
            "password VARCHAR(255), " +
            "phone VARCHAR(255), " +
            "role VARCHAR(255))";

        String createAddressesTableSQL = "CREATE TABLE IF NOT EXISTS addresses (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "user_id INT, " +
            "address_line1 VARCHAR(255), " +
            "address_line2 VARCHAR(255), " +
            "city VARCHAR(255), " +
            "state VARCHAR(255), " +
            "postal_code VARCHAR(255), " +
            "country VARCHAR(255), " +
            "FOREIGN KEY (user_id) REFERENCES users(id))";

        String createSubscriptionsTableSQL = "CREATE TABLE IF NOT EXISTS subscriptions (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "user_id INT, " +
            "product_id INT, " +
            "subscription_plan VARCHAR(255), " +
            "subscription_date DATE, " +
            "subscription_end_date DATE, " +
            "number_of_weeks INT, " +
            "number_of_days INT, " +
            "days_of_week VARCHAR(255), " +
            "address_id INT, " +
            "FOREIGN KEY (user_id) REFERENCES users(id), " +
            "FOREIGN KEY (address_id) REFERENCES addresses(id))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createUsersTableSQL);
            statement.execute(createAddressesTableSQL);
            statement.execute(createSubscriptionsTableSQL);
        }

        // Clear existing data
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM subscriptions");
            statement.execute("DELETE FROM addresses");
            statement.execute("DELETE FROM users");
        }

        // Insert dummy data into users table
        String insertUsersSQL = "INSERT INTO users (id, first_name, last_name, email, password, phone, role) VALUES " +
            "(8, 'Priya', 'Desai', 'priya@gmail.com', '789p', '8765123489', 'ADMIN'), " +
            "(9, 'Saloni', 'Sawant', 'saloni@gmail.com', '1234', '9876543210', 'CUSTOMER'), " +
            "(10, 'Prachi', 'Naik', 'prachi@gmail.com', '1234', '9876543216', 'CUSTOMER'), " +
            "(11, 'Jay', 'Pawar', 'jay@gmail.com', 'Jay@1234', '9876543219', 'CUSTOMER')";
        try (Statement statement = connection.createStatement()) {
            statement.execute(insertUsersSQL);
        }

        // Insert dummy data into addresses table
        String insertAddressesSQL = "INSERT INTO addresses (id, user_id, address_line1, address_line2, city, state, postal_code, country) VALUES " +
            "(5, 11, 'A 205 Amanora Gold Towers', 'Hadapsar', 'Pune', 'Maharashtra', '411028', 'India')";
        try (Statement statement = connection.createStatement()) {
            statement.execute(insertAddressesSQL);
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up data from tables
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM subscriptions");
            statement.execute("DELETE FROM addresses");
            statement.execute("DELETE FROM users");
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testCreateSubscription() throws SQLException {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        subscriptionService.createSubscription(11, 23, "Weekly", startDate, endDate, 2, 2, "Monday, Tuesday", 5);

        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(11);
        assertNotNull(subscriptions);
        assertFalse(subscriptions.isEmpty());

        Subscription subscription = subscriptions.get(0);
        assertEquals(11, subscription.getUserId());
        assertEquals(23, subscription.getProductId());
        assertEquals("Weekly", subscription.getSubscriptionPlan());
        assertEquals(2, subscription.getNumberOfWeeks());
        assertEquals(2, subscription.getNumberOfDays());
        assertEquals("Monday, Tuesday", subscription.getDaysOfWeek());
    }

    @Test
    public void testGetActiveSubscriptions() throws SQLException {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        Date pastEndDate = new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000));

        subscriptionService.createSubscription(11, 23, "Weekly", startDate, endDate, 2, 2, "Monday, Tuesday", 5);
        subscriptionService.createSubscription(9, 24, "Biweekly", startDate, pastEndDate, 2, 4, "Tuesday, Thursday, Saturday, Sunday", 6);

        List<Subscription> activeSubscriptions = subscriptionService.getActiveSubscriptions();
        assertNotNull(activeSubscriptions);
        assertEquals(1, activeSubscriptions.size());
        assertEquals(11, activeSubscriptions.get(0).getUserId());
    }

    @Test
    public void testCancelSubscription() throws SQLException {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));

        subscriptionService.createSubscription(11, 23, "Weekly", startDate, endDate, 2, 2, "Monday, Tuesday", 5);

        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(11);
        assertFalse(subscriptions.isEmpty());

        int subscriptionId = subscriptions.get(0).getId();
        subscriptionService.cancelSubscription(subscriptionId);

        subscriptions = subscriptionService.getSubscriptionsByUserId(11);
        assertTrue(subscriptions.isEmpty());
    }

    @Test
    public void testGetSubscriptionsByUserId() throws SQLException {
        Date startDate = new Date();
        Date endDate1 = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        Date endDate2 = new Date(System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000));

        subscriptionService.createSubscription(11, 23, "Weekly", startDate, endDate1, 1, 2, "Monday, Wednesday", 5);
        subscriptionService.createSubscription(11, 24, "Biweekly", startDate, endDate2, 2, 1, "Tuesday", 6);

        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(11);
        assertNotNull(subscriptions);
        assertEquals(2, subscriptions.size());
    }
}
