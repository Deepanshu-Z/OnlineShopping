import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Admin{
        public static void adminDiplay(){
                System.out.println("Welcome Admin \nPlease Specify your actions: ");
                Scanner scn = new Scanner(System.in);
                while (true) {
                        System.out.println("1. Add Product");
                        System.out.println("2. View Products");
                        System.out.println("3. Exit to Main Menu");
                        System.out.print("Enter choice: ");

                        int choice = scn.nextInt();
                        switch (choice) {
                                case 1:
                                        addProducts();
                                        break;
                                case 2:
                                        viewProducts();
                                        break;
                                case 3:
                                        return;
                                default:
                                        System.out.println("Invalid choice! Try again.");
                        }
                }


        }

        private static void addProducts(){
                System.out.println("\nPlease specify, how many Products you want to add? ");
                Scanner scn = new Scanner(System.in);
                int entries = scn.nextInt();

                try{
                        Connection con = Databaseconnection.getConnection();
                        String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        String name;
                        int price;
                        int stock;
                        while (entries > 0){
                                System.out.println("Enter product name: ");
                                name= scn.nextLine();
                                System.out.println("Enter product price: ");
                                price= scn.nextInt();
                                System.out.println("Enter product stock: ");
                                stock= scn.nextInt();

                                statement.setString(1, name);
                                statement.setInt(2, price);
                                statement.setInt(3,stock);
                                statement.executeUpdate();
                                entries--;
                        }
                }
                catch(Exception e){
                        e.printStackTrace();
                }
        }

        private static void viewProducts(){
                try{
                        Connection con = Databaseconnection.getConnection();
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM products");
                        System.out.println("\n=== Product List ===");
                        while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("id") +
                                                ", Name: " + rs.getString("name") +
                                                ", Price: $" + rs.getDouble("price") +
                                                ", Stock: " + rs.getInt("stock"));
                        }
                }
                catch(Exception e){
                        e.printStackTrace();
                }
        }
}
