package system;
/**
 * @author <Bui Minh Khoi - s3929015>
 */
import system.entity.Claim;
import system.entity.Customer;
import system.entity.InsuranceCard;
import system.filehandler.FileHandler;
import system.manager.ClaimProcessManager;
import system.manager.ClaimProcessManagerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileHandler fileHandler = new FileHandler();
    private static final ClaimProcessManager claimProcessManager = new ClaimProcessManagerImpl();

    public static void main(String[] args) {
        List<Customer> customers = fileHandler.readCustomersFromFile("src/resource/customer.txt");
        List<InsuranceCard> insuranceCards = fileHandler.readInsuranceCardsFromFile("src/resource/InsuranceCard.txt");
        List<Claim> claims = fileHandler.readClaimsFromFile("src/resource/claim.txt");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addClaim(customers, insuranceCards, claims);
                    break;
                case 2:
                    updateClaim(claims);
                    break;
                case 3:
                    deleteClaim(claims);
                    break;
                case 4:
                    viewClaim(claims);
                    break;
                case 5:
                    viewAllClaims(claims);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        fileHandler.writeCustomersToFile(customers, "src/resource/customer.txt");
        fileHandler.writeInsuranceCardsToFile(insuranceCards, "src/resource/InsuranceCard.txt");
        fileHandler.writeClaimsToFile(claims, "src/resource/claim.txt");
        fileHandler.addClaimToCustomer(customers,"src/resource/customer.txt");

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. Add Claim");
        System.out.println("2. Update Claim");
        System.out.println("3. Delete Claim");
        System.out.println("4. View Claim");
        System.out.println("5. View All Claims");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }


}
