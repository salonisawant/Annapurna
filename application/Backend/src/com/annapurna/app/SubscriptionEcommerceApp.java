package com.annapurna.app;

import java.io.Console;
import java.sql.Connection;		
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.annapurna.model.Address;
import com.annapurna.model.Product;
import com.annapurna.model.Subscription;
import com.annapurna.model.User;
import com.annapurna.service.AddressService;
import com.annapurna.service.ProductService;
import com.annapurna.service.SubscriptionService;
import com.annapurna.service.UserService;
import com.annapurna.dao.UserDAO;
import com.annapurna.dao.UserDAOImpl;
import com.annapurna.dao.SubscriptionDAO;
import com.annapurna.dao.SubscriptionDAOImpl;
import com.annapurna.dao.ProductDAO;
import com.annapurna.dao.ProductDAOImpl;
import com.annapurna.dao.AddressDAO;
import com.annapurna.dao.AddressDAOImpl;

public class SubscriptionEcommerceApp {
	private static UserService userService;
    private static SubscriptionService subscriptionService;
    private static ProductService productService;
    private static AddressService addressService;
    private static Scanner scanner;
    private static User currentUser;
    private static Address address;
    private static Subscription subscription;

    public static void main(String[] args) {
        try {
            initializeServices();
            scanner = new Scanner(System.in);

            while (true) {
                if (currentUser == null) {
                    displayMainMenu();
                } else if ("ADMIN".equals(currentUser.getRole())) {
                    displayAdminMenu();
                } else {
                    displayCustomerMenu();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void initializeServices() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/annapurnadb", "root", "1234");
        UserDAO userDAO = new UserDAOImpl(connection);
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl(connection);
        ProductDAO productDAO = new ProductDAOImpl(connection);
        AddressDAO addressDAO = new AddressDAOImpl(connection);

        userService = new UserService(userDAO);
        subscriptionService = new SubscriptionService(subscriptionDAO);
        productService = new ProductService(productDAO);
        addressService = new AddressService(addressDAO);
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. View All Products");
        System.out.println("5. View Active Subscriptions");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    viewAllProducts();
                    break;
                case 5:
                    viewActiveSubscriptions();
                    break;
                case 6:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayCustomerMenu() {
        System.out.println("\n--- Customer Menu ---");
        System.out.println("1. View Products");
        System.out.println("2. Subscribe to Product");
        System.out.println("3. View My Subscriptions");
        System.out.println("4. Cancel Subscription");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    subscribeToProduct();
                    break;
                case 3:
                    viewMySubscriptions();
                    break;
                case 4:
                    cancelSubscription();
                    break;
                case 5:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static String getHiddenPassword(String prompt) {
        Console console = System.console();
        if (console == null) {
            // If console is not available, fall back to scanner
            System.out.print(prompt);
            return scanner.nextLine();
        } else {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        }
    }

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]{2,}");
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pat = Pattern.compile(passwordRegex);
        return pat.matcher(password).matches();
    }

    private static boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private static void registerUser() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        String password = getHiddenPassword("Enter password: ");

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();

        if (!isValidName(firstName) || !isValidName(lastName)) {
            System.out.println("Invalid name. Names should only contain letters and be at least 2 characters long.");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        if (!isValidPassword(password)) {
            System.out.println("Invalid password. Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
            return;
        }

        if (!isValidPhone(phone)) {
            System.out.println("Invalid phone number. Please enter a 10-digit number.");
            return;
        }

        try {
            User newUser = userService.registerUser(firstName, lastName, email, password, phone, "CUSTOMER");
            System.out.println("Registration successful!");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void loginUser() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        String password = getHiddenPassword("Enter password: ");

        try {
            User user = userService.loginUser(email, password);
            if (user != null) {
                currentUser = user;
                System.out.println("Login successful! Welcome, " + user.getFirstName() + "!");
            } else {
                System.out.println("Login failed. Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
    
    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private static void addProduct() throws SQLException {
        System.out.println("\n--- Add Product ---");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter product category: ");
        String category = scanner.nextLine();
        System.out.print("Is product available? (true/false): ");
        boolean available = scanner.nextBoolean();

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setCategory(category);
        newProduct.setAvailable(available);

        productService.addProduct(newProduct);
        System.out.println("Product added successfully!");
    }

    private static void updateProduct() throws SQLException {
        System.out.println("\n--- Update Product ---");
        viewAllProducts(); // Show all products before updating

        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product product = productService.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter new product name (or press Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            product.setName(name);
        }

        System.out.print("Enter new product description (or press Enter to keep current): ");
        String description = scanner.nextLine().trim();
        if (!description.isEmpty()) {
            product.setDescription(description);
        }

        System.out.print("Enter new product price (or -1 to keep current): ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        if (price != -1) {
            if (price < 0) {
                System.out.println("Price cannot be negative.");
                return;
            }
            product.setPrice(price);
        }

        System.out.print("Enter new product category (or press Enter to keep current): ");
        String category = scanner.nextLine().trim();
        if (!category.isEmpty()) {
            product.setCategory(category);
        }

        System.out.print("Is product available? (true/false, or press Enter to keep current): ");
        String availableInput = scanner.nextLine().trim();
        if (!availableInput.isEmpty()) {
            product.setAvailable(Boolean.parseBoolean(availableInput));
        }

        productService.updateProduct(product);
        System.out.println("Product updated successfully!");
    }
    private static void deleteProduct() throws SQLException {
        System.out.println("\n--- Delete Product ---");
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        productService.deleteProduct(productId);
        System.out.println("Product deleted successfully!");
    }

    private static void viewAllProducts() throws SQLException {
        System.out.println("\n--- All Products ---");
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            System.out.println(product.getProductId() + ": " + product.getName() + " - ₹" + product.getPrice());
        }
    }

    private static void viewActiveSubscriptions() throws SQLException {
        System.out.println("\n--- Active Subscriptions ---");
        List<Subscription> activeSubscriptions = subscriptionService.getActiveSubscriptions();
        if (activeSubscriptions.isEmpty()) {
            System.out.println("There are no active subscriptions.");
        } else {
            for (Subscription subscription : activeSubscriptions) {
                System.out.println("Subscription ID: " + subscription.getId() + 
                                   ", User ID: " + subscription.getUserId() + 
                                   ", Product ID: " + subscription.getProductId() + 
                                   ", Plan: " + subscription.getSubscriptionPlan());
            }
        }
    }

    private static void subscribeToProduct() throws SQLException {
        System.out.println("\n--- Subscribe to Product ---");
        viewAllProducts();
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Choose the plan: Weekly/Biweekly/Monthly");
        String plan = scanner.next();

        int duration = 0;
        if(plan.equals("Weekly")) {
            System.out.print("Enter number of weeks: ");
            duration = scanner.nextInt();
        }
        else if(plan.equals("Biweekly")) {
            System.out.print("Enter number of Biweeks: ");
            duration = scanner.nextInt()*2;
        }
        else if(plan.equals("Monthly")){
            System.out.print("Enter number of Months: ");
            duration = scanner.nextInt()*4;
        }
        scanner.nextLine(); // Consume newline

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + duration * 7 * 24 * 60 * 60 * 1000L); // Assuming weeks

        System.out.println("Enter total number of service days in a week: ");
        int days = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter service days in a week");
        String days_str = "";
        for(int i = 0; i < days; i++) {
            String var = scanner.next();
            days_str += var;
            if(i < days - 1) {
                days_str += ",";
                days_str += " ";
            }
        }

        // Handle address selection or creation
        int address_id = handleAddressSelection();

        // Proceed with creating the subscription
        subscriptionService.createSubscription(currentUser.getId(), productId, plan, startDate, endDate, duration, days, days_str, address_id);
        System.out.println("Subscription created successfully!");
    }

    private static int handleAddressSelection() throws SQLException {
        System.out.println("Address Status: ");
        List<Address> myAddress = checkAddresses();
        int address_id;

        if (myAddress.isEmpty()) {
            System.out.println("No addresses found. Please add an address.");
            address_id = addAddress();
        } else {
            System.out.println("1. Add New Address");
            System.out.println("2. Choose Existing Address");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                address_id = addAddress();
            } else {
                for (Address address : myAddress) {
                    System.out.println(address.toString());
                }
                System.out.println("Please choose an address by entering the ID:");
                address_id = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            }
        }

        return address_id;
    }
    
    private static int getMaxId() throws SQLException{
    	int address_id = addressService.getMaxId();
    	return address_id;
    }
    
    private static List<Address> checkAddresses() throws SQLException {
        List<Address> myAddresses = addressService.getAddressesByUserId(currentUser.getId());
        return myAddresses;
    }

    private static void viewMySubscriptions() throws SQLException {
        System.out.println("\n--- My Subscriptions ---");
        List<Subscription> mySubscriptions = subscriptionService.getSubscriptionsByUserId(currentUser.getId());
        if (mySubscriptions.isEmpty()) {
            System.out.println("You have no active subscriptions.");
        } else {
            for (Subscription subscription : mySubscriptions) {
                System.out.println(subscription.toString());
            }
        }
    }

    private static void cancelSubscription() throws SQLException {
        System.out.println("\n--- Cancel Subscription ---");
        List<Subscription> mySubscriptions = subscriptionService.getSubscriptionsByUserId(currentUser.getId());
        
        if (mySubscriptions.isEmpty()) {
            System.out.println("You have no active subscriptions to cancel.");
            return;
        }
        
        for (Subscription subscription : mySubscriptions) {
            System.out.println(subscription.toString());
        }
        
        System.out.print("Enter subscription ID to cancel: ");
        int subscriptionId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the subscription belongs to the current user
        boolean isValidSubscription = mySubscriptions.stream()
                .anyMatch(sub -> sub.getId() == subscriptionId);

        if (isValidSubscription) {
            subscriptionService.cancelSubscription(subscriptionId);
            System.out.println("Subscription cancelled successfully!");
        } else {
            System.out.println("Invalid subscription ID. Please enter a valid ID from your subscriptions.");
        }
    }

    private static int addAddress() throws SQLException {
        System.out.println("\n--- Add Address ---");
        scanner.nextLine();
        
        System.out.print("Enter address line 1: ");
        String addressLine1 = scanner.nextLine();  

        System.out.print("Enter address line 2: ");
        String addressLine2 = scanner.nextLine();  

        System.out.print("Enter city: ");
        String city = scanner.nextLine();

        System.out.print("Enter state: ");
        String state = scanner.nextLine();

        System.out.print("Enter postal code: ");
        String postalCode = scanner.nextLine();

        System.out.print("Enter country: ");
        String country = scanner.nextLine();

        // Create a new Address object
        Address newAddress = new Address();
        newAddress.setUserId(currentUser.getId());
        newAddress.setAddressLine1(addressLine1);
        newAddress.setAddressLine2(addressLine2);
        newAddress.setCity(city);
        newAddress.setState(state);
        newAddress.setPostalCode(postalCode);
        newAddress.setCountry(country);

        // Add the address and get the new address ID
        int newAddressId = addressService.addAddress(newAddress);
        System.out.println("Address added successfully!");
        
        return newAddressId;
    }



}
