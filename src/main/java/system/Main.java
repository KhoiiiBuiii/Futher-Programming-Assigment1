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

    private static void addClaim(List<Customer> customers, List<InsuranceCard> insuranceCards, List<Claim> claims) {
        System.out.println("Adding a new claim...");
        Claim newClaim = new Claim();

        newClaim.setId(generateClaimId(claims));
        newClaim.setClaimDate(parseDate("Enter claim date (YYYY-MM-DD): "));
        System.out.print("Enter insured person: ");
        String insuredPerson = scanner.nextLine();
        newClaim.setInsuredPerson(insuredPerson);
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();
        newClaim.setCardNumber(cardNumber);
        newClaim.setExamDate(parseDate("Enter exam date (YYYY-MM-DD): "));
        System.out.print("Enter claim amount: ");
        double claimAmount = scanner.nextDouble();
        newClaim.setClaimAmount(claimAmount);
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter status (New, Processing, Done): ");
        String status = scanner.nextLine();
        newClaim.setStatus(status);
        System.out.print("Enter receiver banking info (Bank - Name - Number): ");
        String receiverBankingInfo = scanner.nextLine();
        newClaim.setReceiverBankingInfo(receiverBankingInfo);

        claims.add(newClaim);
        claimProcessManager.add(newClaim);

        System.out.println("Claim added successfully.");

        // Update customer and insurance card information
        updateCustomerAndInsuranceCard(customers, insuranceCards, newClaim);
    }
    private static String generateClaimId(List<Claim> claims) {
        String prefix = "f-";
        StringBuilder idBuilder = new StringBuilder(prefix);

        boolean uniqueId = false;
        while (!uniqueId) {
            // Generate 10 random numbers
            for (int i = 0; i < 10; i++) {
                idBuilder.append((int) (Math.random() * 10));
            }

            // Check if the generated ID is unique
            String newClaimId = idBuilder.toString();
            if (!isClaimIdExists(claims, newClaimId)) {
                uniqueId = true;
            } else {
                // Reset StringBuilder for the next attempt
                idBuilder.delete(prefix.length(), idBuilder.length());
            }
        }

        return idBuilder.toString();
    }

    private static boolean isClaimIdExists(List<Claim> claims, String newClaimId) {
        for (Claim claim : claims) {
            if (claim.getId().equals(newClaimId)) {
                return true;
            }
        }
        return false;
    }

    private static void updateCustomerAndInsuranceCard(List<Customer> customers, List<InsuranceCard> insuranceCards, Claim newClaim) {
        boolean customerExists = false;
        for (Customer customer : customers) {
            if (customer.getInsuranceCard().getCardNumber().equals(newClaim.getCardNumber())) {
                // Customer exists, update the customer's claims list
                customerExists = true;
                customer.getClaims().add(newClaim);
                break;
            }
        }

        if (!customerExists) {
            // Customer does not exist, create a new customer and insurance card entry
            Customer newCustomer = new Customer();
            newCustomer.setId(generateCustomerId());
            newCustomer.setFullName(newClaim.getInsuredPerson());
            InsuranceCard newInsuranceCard = new InsuranceCard();
            newInsuranceCard.setCardNumber(newClaim.getCardNumber());
            newInsuranceCard.setCardHolder(newClaim.getInsuredPerson());
            newCustomer.setInsuranceCard(newInsuranceCard);
            newCustomer.getClaims().add(newClaim);

            customers.add(newCustomer);
            insuranceCards.add(newInsuranceCard);
        }
    }

    private static String generateCustomerId() {
        StringBuilder idBuilder = new StringBuilder("c-");
        for (int i = 0; i < 7; i++) {
            idBuilder.append((int) (Math.random() * 10));
        }
        return idBuilder.toString();
    }

    private static void updateClaim(List<Claim> claims) {
        System.out.println("Updating a claim...");
        System.out.print("Enter the ID of the claim you want to update: ");
        String claimId = scanner.nextLine();
        Claim claimToUpdate = null;

        // Find the claim with the given ID
        for (Claim claim : claims) {
            if (claim.getId().equals(claimId)) {
                claimToUpdate = claim;
                break;
            }
        }

        if (claimToUpdate != null) {
            System.out.println("Claim found. Enter the information you want to update:");
            System.out.print("New claim date (YYYY-MM-DD): ");
            String newClaimDate = scanner.nextLine();
            if (!newClaimDate.isEmpty()) {
                claimToUpdate.setClaimDateFromString(newClaimDate);
            }

            System.out.print("New insured person: ");
            String newInsuredPerson = scanner.nextLine();
            if (!newInsuredPerson.isEmpty()) {
                claimToUpdate.setInsuredPerson(newInsuredPerson);
            }

            System.out.print("New card number: ");
            String newCardNumber = scanner.nextLine();
            if (!newCardNumber.isEmpty()) {
                claimToUpdate.setCardNumber(newCardNumber);
            }

            System.out.print("New exam date (YYYY-MM-DD): ");
            String newExamDate = scanner.nextLine();
            if (!newExamDate.isEmpty()) {
                claimToUpdate.setExamDateFromString(newExamDate);
            }

            System.out.print("New claim amount: ");
            String newClaimAmountStr = scanner.nextLine();
            if (!newClaimAmountStr.isEmpty()) {
                double newClaimAmount = Double.parseDouble(newClaimAmountStr);
                claimToUpdate.setClaimAmount(newClaimAmount);
            }

            System.out.print("New status (New, Processing, Done): ");
            String newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) {
                claimToUpdate.setStatus(newStatus);
            }

            System.out.print("New receiver banking info: ");
            String newReceiverBankingInfo = scanner.nextLine();
            if (!newReceiverBankingInfo.isEmpty()) {
                claimToUpdate.setReceiverBankingInfo(newReceiverBankingInfo);
            }

            System.out.println("Claim updated successfully.");
        } else {
            System.out.println("Claim not found.");
        }
    }




    private static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        } else {
            return "N/A";
        }
    }

    private static Date parseDate(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        boolean validDate = false;
        do {
            System.out.print(message);
            String dateString = scanner.nextLine();
            try {
                dateFormat.setLenient(false); // Disable lenient parsing
                date = dateFormat.parse(dateString);
                validDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
            }
        } while (!validDate);
        return date;
    }
}
