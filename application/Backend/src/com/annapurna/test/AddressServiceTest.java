package com.annapurna.test;

import com.annapurna.dao.AddressDAOImpl;
import com.annapurna.model.Address;
import com.annapurna.service.AddressService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class AddressServiceTest {

    private AddressDAOImpl addressDAO;
    private AddressService addressService;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // MySQL database connection setup
        String url = "jdbc:mysql://localhost:3306/annapurnadb";
        String username = "root";
        String password = "1234";
        connection = DriverManager.getConnection(url, username, password);

        // Initialize DAO and Service
        addressDAO = new AddressDAOImpl(connection);
        addressService = new AddressService(addressDAO);

        // Create and clear table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS addresses (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "user_id INT, " +
            "address_line1 VARCHAR(255), " +
            "address_line2 VARCHAR(255), " +
            "city VARCHAR(255), " +
            "state VARCHAR(255), " +
            "postal_code VARCHAR(20), " +
            "country VARCHAR(255), " +
            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }

        // Clear the table
        clearTable("addresses");
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up data from tables
        clearTable("addresses");

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void clearTable(String tableName) throws SQLException {
        String sql = "DELETE FROM " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testAddAddress() throws SQLException {
        // Add an address for a valid user with user_id = 1
        Address address = new Address(0, 1, "B2 Ganga Savera", "Fatima Nagar", "Pune", "Maharashtra", "411040", "India");
        addressService.addAddress(address);
        
        List<Address> addresses = addressService.getAddressesByUserId(1);
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        
        Address savedAddress = addresses.get(0);
        assertEquals(1, savedAddress.getUserId());
        assertEquals("B2 Ganga Savera", savedAddress.getAddressLine1());
        assertEquals("Fatima Nagar", savedAddress.getAddressLine2());
        assertEquals("Pune", savedAddress.getCity());
        assertEquals("Maharashtra", savedAddress.getState());
        assertEquals("411040", savedAddress.getPostalCode());
        assertEquals("India", savedAddress.getCountry());
    }

    @Test
    public void testGetAddressesByUserId() throws SQLException {
        // Add multiple addresses for a valid user with user_id = 1
        Address address1 = new Address(0, 1, "B2 Ganga Savera", "Fatima Nagar", "Pune", "Maharashtra", "411040", "India");
        Address address2 = new Address(0, 1, "A 205 Amanora Towers", "Hadapsar", "Pune", "Maharashtra", "411028", "India");
        addressService.addAddress(address1);
        addressService.addAddress(address2);
        
        List<Address> addresses = addressService.getAddressesByUserId(1);
        assertNotNull(addresses);
        assertEquals(2, addresses.size());

        // Verify addresses' details
        boolean address1Found = addresses.stream().anyMatch(a -> a.getAddressLine1().equals("B2 Ganga Savera"));
        boolean address2Found = addresses.stream().anyMatch(a -> a.getAddressLine1().equals("A 205 Amanora Towers"));
        
        assertTrue(address1Found);
        assertTrue(address2Found);
    }
}
