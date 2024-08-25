# Annapurna
# Subscription E-Commerce Application

This is a  Java application designed for managing a subscription-based e-commerce platform. It allows customers to subscribe to products with various plans (weekly, bi-weekly, monthly), and enables administrators to manage products and view active subscriptions.

## Project Structure

The project follows a simple structure with the following key packages and classes:

- `com.annapurna.app`: Contains the main application class `SubscriptionEcommerceApp.java`.
- `com.annapurna.model`: Contains model classes representing the entities like `User`, `Product`, `Subscription`, `Address`.
- `com.annapurna.service`: Contains service classes like `UserService`, `ProductService`, `SubscriptionService`, `AddressService` for business logic.
- `com.annapurna.dao`: Contains DAO interfaces and implementations like `UserDAO`, `ProductDAO`, `SubscriptionDAO`, `AddressDAO`, and their implementations (`UserDAOImpl`, `ProductDAOImpl`, etc.) for database interaction.
- `com.annapurna.test`: Contains junit test

## Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **MySQL Database** 
- **Eclipse IDE** 

## Database Configuration

1. **Database Setup**:
    - Create a MySQL database named `annapurnadb`.
    - The required SQL commands are in the documents-> Annapurna.sql file.


2. **Database Connection Configuration**:
    - The application connects to the MySQL database using the JDBC URL: `jdbc:mysql://localhost:3306/annapurnadb`.
    - Update the username and password in the `initializeServices()` method inside `SubscriptionEcommerceApp.java` as per your MySQL configuration:
      ```java
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/annapurnadb", "your_username", "your_password");
      ```

## Running the Application

1. **Import the Project**:

2. **Build the Project**:

3. **Run the Application**:
    - Right-click on `SubscriptionEcommerceApp.java`.
    - Select `Run As -> Java Application`.

4. **Using the Application**:
    - The application starts in a console window.
    - Follow the prompts to register, log in, manage products, or subscribe to products.
## Frontend
The Frontend for Annapurna is implemented using HTML, **CSS (with Tailwind CSS), and **JavaScript.
The frontend is built with responsive design principles, utilizing Tailwind CSS for styling and JavaScript for dynamic interactions. The UI is designed to be clean and user-friendly, focusing on an aesthetic and minimalist approach.

### *gitignore file*
The following files and directories are ignored:

- node_modules/
- tailwind.config.js

### *Dependencies*
To install the necessary dependencies, run the following command:

bash
npm install


TAILWIND CSS SETUP
To build Tailwind CSS, use the following command:

npx tailwindcss -i ./src/input.css -o ./dist/output.css --watch

RUNNING THE PROJECT
To start and run the project, use the following steps:

Install Node Modules:

npm install
Build Tailwind CSS:

Copy code
npx tailwindcss -i ./src/input.css -o ./dist/output.css --watch
Run the Development Server:

VERSION
Node.js: v14.x
Tailwind CSS: v2.x


The frontend is built with responsive design principles, utilizing Tailwind CSS for styling and JavaScript for dynamic interactions. The UI is designed to be clean and user-friendly, focusing on an aesthetic and minimalist approach.


## Notes

- **Default Admin User**:
    - You may manually insert an admin user in the `users` table if you want to access the admin functionality:
      ```sql
      INSERT INTO users (first_name, last_name, email, password, phone, role) 
      VALUES ('Admin', 'User', 'admin@example.com', 'admin_password_hash', '1234567890', 'ADMIN');
      ```
