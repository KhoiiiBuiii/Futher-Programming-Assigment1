package system.filehandler;
/**
 * @author <Bui Minh Khoi - s3929015>
 */
import system.entity.Claim;
import system.entity.Customer;
import system.entity.InsuranceCard;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileHandler {
    public List<Customer> readCustomersFromFile(String filePath) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Using "|" as the delimiter
                if (parts.length >= 4) { // Ensure there are at least 4 parts
                    Customer customer = new Customer();
                    customer.setId(parts[0]);
                    customer.setFullName(parts[1]);

                    InsuranceCard insuranceCard = new InsuranceCard();
                    insuranceCard.setCardNumber(parts[2]);
                    insuranceCard.setCardHolder(parts[1]);
                    insuranceCard.setPolicyOwner(parts[3]);
                    customer.setInsuranceCard(insuranceCard);


                    if (parts.length > 4 && !parts[4].isEmpty()) {
                        insuranceCard.setExpirationDate(parseDate(parts[4]));
                    }


                    if (parts.length > 5 && !parts[5].isEmpty()) {
                        String[] claims = parts[5].split(","); // Assuming claims are separated by ","
                        for (String claimData : claims) {
                            String[] claimParts = claimData.split(",");
                            if (claimParts.length >= 8) {
                                Claim claim = new Claim();
                                claim.setId(claimParts[0]);

                                claim.setInsuredPerson(claimParts[2]);
                                claim.setCardNumber(claimParts[3]);

                                claim.setClaimAmount(Double.parseDouble(claimParts[5]));
                                claim.setStatus(claimParts[6]);
                                claim.setReceiverBankingInfo(claimParts[7]);

                                customer.getClaims().add(claim);
                            } else {
                                System.err.println("Invalid claim data: " + claimData);
                            }
                        }
                    }

                    customers.add(customer);
                } else {
                    System.err.println("Invalid customer data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<InsuranceCard> readInsuranceCardsFromFile(String filePath) {
        List<InsuranceCard> insuranceCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Using "|" as the delimiter
                if (parts.length >= 3) { // Ensure there are at least 3 parts
                    InsuranceCard insuranceCard = new InsuranceCard();
                    insuranceCard.setCardNumber(parts[0]);
                    insuranceCard.setCardHolder(parts[1]);
                    insuranceCard.setPolicyOwner(parts[2]);

                    if (parts.length > 3 && !parts[3].isEmpty()) {
                        insuranceCard.setExpirationDate(parseDate(parts[3]));
                    }
                    insuranceCards.add(insuranceCard);
                } else {
                    System.err.println("Invalid insurance card data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insuranceCards;
    }

    public List<Claim> readClaimsFromFile(String filePath) {
        List<Claim> claims = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Using "|" as the delimiter
                if (parts.length >= 8) { // Ensure there are at least 8 parts
                    Claim claim = new Claim();
                    claim.setId(parts[0]);
                    claim.setClaimDateFromString(parts[1]); // Parse claim date
                    claim.setInsuredPerson(parts[2]);
                    claim.setCardNumber(parts[3]);
                    claim.setExamDateFromString(parts[4]); // Parse exam date
                    claim.setClaimAmount(Double.parseDouble(parts[5]));
                    claim.setStatus(parts[6]);
                    claim.setReceiverBankingInfo(parts[7]);

                    claims.add(claim);
                } else {
                    System.err.println("Invalid claim data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return claims;
    }


    public void writeCustomersToFile(List<Customer> customers, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer customer : customers) {
                StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(customer.getId()).append("|").append(customer.getFullName()).append("|")
                        .append(customer.getInsuranceCard().getCardNumber()).append("|")
                        .append(customer.getInsuranceCard().getPolicyOwner());

                InsuranceCard insuranceCard = customer.getInsuranceCard();
                if (insuranceCard.getExpirationDate() != null) {
                    lineBuilder.append("|").append(formatDate(insuranceCard.getExpirationDate()));
                }
                if (!customer.getClaims().isEmpty()) {
                    lineBuilder.append("|");
                    for (Claim claim : customer.getClaims()) {
                        lineBuilder.append(claim.getId()).append(",").append(formatDate(claim.getClaimDate())).append(",")
                                .append(claim.getInsuredPerson()).append(",").append(claim.getCardNumber()).append(",")
                                .append(formatDate(claim.getExamDate())).append(",").append(claim.getClaimAmount()).append(",")
                                .append(claim.getStatus()).append(",").append(claim.getReceiverBankingInfo()).append(",");
                    }
                    lineBuilder.deleteCharAt(lineBuilder.length() - 1); // Remove the trailing comma
                }
                writer.write(lineBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInsuranceCardsToFile(List<InsuranceCard> insuranceCards, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (InsuranceCard insuranceCard : insuranceCards) {
                StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(insuranceCard.getCardNumber()).append("|")
                        .append(insuranceCard.getCardHolder()).append("|")
                        .append(insuranceCard.getPolicyOwner());
                if (insuranceCard.getExpirationDate() != null) {
                    lineBuilder.append("|").append(formatDate(insuranceCard.getExpirationDate()));
                }
                writer.write(lineBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeClaimsToFile(List<Claim> claims, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Claim claim : claims) {
                String line = claim.getId() + "|" +
                        formatDate(claim.getClaimDate()) + "|" +
                        claim.getInsuredPerson() + "|" +
                        claim.getCardNumber() + "|" +
                        formatDate(claim.getExamDate()) + "|" +
                        claim.getClaimAmount() + "|" +
                        claim.getStatus() + "|" +
                        claim.getReceiverBankingInfo();
                // Write other claim fields if needed
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClaimToCustomer(String customerId, Claim claim, String filePath) {
        List<Customer> customers = readCustomersFromFile(filePath);
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                customer.getClaims().add(claim);
                break;
            }
        }
        writeCustomersToFile(customers, filePath);
    }

    // Method to parse date string to Date object
    private Date parseDate(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat[] dateFormats = {
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy/MM/dd"),
                    new SimpleDateFormat("dd/MM/yyyy"),
                    new SimpleDateFormat("MM/dd/yyyy"),
                    new SimpleDateFormat("dd-MM-yyyy"),
                    new SimpleDateFormat("MM-dd-yyyy")
            };

            for (SimpleDateFormat dateFormat : dateFormats) {
                try {
                    return dateFormat.parse(dateString);
                } catch (ParseException ignored) {
                    // Try the next date format
                }
            }

            System.err.println("Error parsing date: " + dateString + ". Date format not recognized.");
        }
        return null;
    }


    // Method to format Date object to string
    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
            return dateFormat.format(date);
        } else {
            return "";
        }
    }



}
