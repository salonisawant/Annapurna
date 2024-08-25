package com.annapurna.test;

import com.annapurna.dao.ProductDAOImpl;
import com.annapurna.model.Product;
import com.annapurna.service.ProductService;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {

    private ProductDAOImpl productDAO;
    private ProductService productService;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // MySQL database connection setup
        String url = "jdbc:mysql://localhost:3306/annapurnadb";
        String username = "root";
        String password = "1234";
        connection = DriverManager.getConnection(url, username, password);

        // Initialize DAO and Service
        productDAO = new ProductDAOImpl(connection);
        productService = new ProductService(productDAO);

        // Create and clear table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "description TEXT, " +
                "price DOUBLE, " +
                "category VARCHAR(255), " +
                "available BOOLEAN)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }

        // Clear existing data
        String clearTableSQL = "DELETE FROM products";
        try (Statement statement = connection.createStatement()) {
            statement.execute(clearTableSQL);
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Clear the products table to ensure isolation
            try (Statement statement = connection.createStatement()) {
                statement.execute("DELETE FROM products");
            }
            connection.close();
        }
    }

    @Test
    public void testAddProduct() throws SQLException {
        Product product = new Product(0, "Organic Tomatoes", "Fresh organic tomatoes from Nashik", 50.0, "Vegetables", true);
        productService.addProduct(product);

        // Retrieve the product by its ID
        Product fetchedProduct = productService.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals("Organic Tomatoes", fetchedProduct.getName());
        assertEquals("Fresh organic tomatoes from Nashik", fetchedProduct.getDescription());
        assertEquals(50.0, fetchedProduct.getPrice(), 0.0);
        assertEquals("Vegetables", fetchedProduct.getCategory());
        assertTrue(fetchedProduct.isAvailable());
    }

    @Test
    public void testGetAllProducts() throws SQLException {
        // Add products for testing
        productService.addProduct(new Product(0, "Organic Tomatoes", "Fresh organic tomatoes from Nashik", 50.0, "Vegetables", true));
        productService.addProduct(new Product(0, "Basmati Rice", "Premium basmati rice from Punjab", 120.0, "Grains", true));
        productService.addProduct(new Product(0, "Paneer", "Fresh homemade paneer", 80.0, "Dairy", true));

        // Retrieve all products
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(3, products.size());
    }

    @Test
    public void testUpdateProduct() throws SQLException {
        Product product = new Product(0, "Organic Tomatoes", "Fresh organic tomatoes from Nashik", 50.0, "Vegetables", true);
        productService.addProduct(product);

        // Update the product details
        product.setName("Premium Organic Tomatoes");
        product.setPrice(60.0);
        productService.updateProduct(product);

        // Retrieve the updated product
        Product updatedProduct = productService.getProductById(product.getProductId());
        assertNotNull(updatedProduct);
        assertEquals("Premium Organic Tomatoes", updatedProduct.getName());
        assertEquals(60.0, updatedProduct.getPrice(), 0.0);
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        Product product = new Product(0, "Basmati Rice", "Premium basmati rice from Punjab", 120.0, "Grains", true);
        productService.addProduct(product);

        // Delete the product
        int productId = product.getProductId();
        productService.deleteProduct(productId);

        // Attempt to retrieve the deleted product
        Product deletedProduct = productService.getProductById(productId);
        assertNull(deletedProduct);
    }

    @Test
    public void testGetProductById() throws SQLException {
        Product product = new Product(0, "Paneer", "Fresh homemade paneer", 80.0, "Dairy", true);
        productService.addProduct(product);

        // Retrieve the product by its ID
        Product fetchedProduct = productService.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals(product.getProductId(), fetchedProduct.getProductId());
        assertEquals("Paneer", fetchedProduct.getName());
    }
}
