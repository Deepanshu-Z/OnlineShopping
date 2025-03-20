import java.util.Scanner;

public class Shopdisplay {
        public static void main(String[] args) {
            System.out.println("Welcome to shop \nPlease specify the operations: ");
            System.out.println("1 for Admin \n2 for Customer Panel \n3 for Exit: \n");
            Scanner scn = new Scanner(System.in);
            int choice = scn.nextInt();

            switch(choice){
                case 1:
                    Admin admin = new Admin();
                    admin.adminDiplay();
                    break;
                case 2:
                    Customer cust = new Customer();
                    cust.custDiplay();
                    customer();
                    break;
                case 3:
                    System.out.println("Thank you for using our shop");
                    scn.close();
                    System.exit(0);
                default:
                    System.out.println("Please enter a valid choice");
            }
        }
}
