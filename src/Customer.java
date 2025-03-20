import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Customer {
    private Scanner scanner = new Scanner(System.in);

    public void customerMenu() {
        while (true) {
            System.out.println("\n==== Customer Panel ====");
            System.out.println("1. View Products");
            System.out.println("2. Buy Product");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewProducts();
                    break;
                case 2:
                    buyProduct();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void viewProducts() {
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            System.out.println("\n=== Available Products ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Price: $" + rs.getDouble("price") +
                        ", Stock: " + rs.getInt("stock"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buyProduct() {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter your email: ");
        String email = scanner.next();

        // Register customer
        int customerId = registerCustomer(name, email);
        if (customerId == -1) return;

        // Select product to buy
        System.out.print("Enter Product ID to buy: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        try (Connection conn = Databaseconnection.getConnection()) {
            // Check product availability
            PreparedStatement checkStockStmt = conn.prepareStatement("SELECT stock, price FROM products WHERE id = ?");
            checkStockStmt.setInt(1, productId);
            ResultSet rs = checkStockStmt.executeQuery();

            if (!rs.next() || rs.getInt("stock") < quantity) {
                System.out.println("Insufficient stock or product not found.");
                return;
            }

            double price = rs.getDouble("price");
            double totalPrice = price * quantity;

            // Update product stock
            PreparedStatement updateStockStmt = conn.prepareStatement("UPDATE products SET stock = stock - ? WHERE id = ?");
            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            // Insert order
            PreparedStatement insertOrderStmt = conn.prepareStatement("INSERT INTO orders (customer_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)");
            insertOrderStmt.setInt(1, customerId);
            insertOrderStmt.setInt(2, productId);
            insertOrderStmt.setInt(3, quantity);
            insertOrderStmt.setDouble(4, totalPrice);
            insertOrderStmt.executeUpdate();

            System.out.println("Purchase successful! Total price: $" + totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int registerCustomer(String name, String email) {
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error registering customer. Email might already exist.");
        }
        return -1;
    }
}
