package com.annapurna.test;

import com.annapurna.dao.UserDAOImpl;
import com.annapurna.dao.AddressDAOImpl;
import com.annapurna.model.User;
import com.annapurna.model.Address;
import com.annapurna.service.UserService;
import com.annapurna.service.AddressService;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {

    private UserDAOImpl userDAO;
    private AddressDAOImpl addressDAO;
    private UserService userService;
    private AddressService addressService;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // Set up MySQL database connection
        String url = "jdbc:mysql://localhost:3306/annapurnadb";
        String username = "root";
        String password = "1234";
        connection = DriverManager.getConnection(url, username, password);

        // Initialize DAOs and Services
        userDAO = new UserDAOImpl(connection);
        addressDAO = new AddressDAOImpl(connection);
        userService = new UserService(userDAO);
        addressService = new AddressService(addressDAO);

        // Create tables if they don't exist
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "first_name VARCHAR(255), "
            + "last_name VARCHAR(255), "
            + "email VARCHAR(255) UNIQUE, "
            + "password VARCHAR(255), "
            + "phone VARCHAR(255), "
            + "role VARCHAR(255))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createUsersTableSQL);
        }

        String createAddressesTableSQL = "CREATE TABLE IF NOT EXISTS addresses ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "user_id INT, "
            + "address_line1 VARCHAR(255), "
            + "address_line2 VARCHAR(255), "
            + "city VARCHAR(255), "
            + "state VARCHAR(255), "
            + "postal_code VARCHAR(20), "
            + "country VARCHAR(255), "
            + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createAddressesTableSQL);
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up tables
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM addresses");
            statement.execute("DELETE FROM users");
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testRegisterUser() throws SQLException {
        User user = userService.registerUser("Rahul", "Sharma", "rahul@example.com", "password123", "9876543210", "CUSTOMER");
        
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("Rahul", user.getFirstName());
        assertEquals("Sharma", user.getLastName());
        assertEquals("rahul@example.com", user.getEmail());
    }

    @Test
    public void testLoginUser() throws SQLException {
        userService.registerUser("Priya", "Patel", "priya@example.com", "password123", "9876543211", "CUSTOMER");
        
        User loggedInUser = userService.loginUser("priya@example.com", "password123");
        
        assertNotNull(loggedInUser);
        assertEquals("Priya", loggedInUser.getFirstName());
        assertEquals("Patel", loggedInUser.getLastName());
    }

    @Test
    public void testUpdateUser() throws SQLException {
        User user = userService.registerUser("Amit", "Kumar", "amit@example.com", "password123", "9876543212", "CUSTOMER");
        
        user.setFirstName("Amitabh");
        userDAO.updateUser(user);
        
        User updatedUser = userDAO.getUserById(user.getId());
        assertNotNull(updatedUser);
        assertEquals("Amitabh", updatedUser.getFirstName());
        assertEquals("Kumar", updatedUser.getLastName());
    }

    @Test
    public void testDeleteUser() throws SQLException {
        User user = userService.registerUser("Neha", "Gupta", "neha@example.com", "password123", "9876543213", "CUSTOMER");
        
        userDAO.deleteUser(user.getId());
        
        User retrievedUser = userDAO.getUserById(user.getId());
        assertNull(retrievedUser);
    }
}
