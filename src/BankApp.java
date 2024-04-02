import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.Map;


public class BankApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, IDBalance> idMap = loadAccountsFromFile();
        if (idMap.isEmpty()) {
            addAccount(scanner, idMap);
        }

        System.out.println("Welcome to bankApp");

        int choice;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Add Account");
            System.out.println("2. Operations With Account (by ID)");
            System.out.println("3. View All Accounts");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            try {
                choice = scanner.nextInt();
            } catch (NoSuchElementException e) {
                System.out.println("Invalid input. Please enter a number (1-4).");
                scanner.nextLine();
                choice = -1;
            }

            switch (choice) {
                case 1:
                    addAccount(scanner, idMap);
                    break;
                case 2:
                    searchAccount(scanner, idMap);
                    break;
                case 3:
                    viewAllAccounts(idMap);
                    break;
                case 4:
                    saveAccountsToFile(idMap);
                    System.out.println("Exiting bankApp. Thank you for using our services!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        } while (choice != 4);

        scanner.close();
    }


    public static void addAccount(Scanner scanner, HashMap<String, IDBalance> idMap) {
        System.out.print("Enter ID for new account: ");
        String id = scanner.next();
        System.out.print("Enter balance for new account: ");
        double balance = scanner.nextDouble();

        // Check for duplicate ID (optional)
        if (idMap.containsKey(id)) {
            System.out.println("Error: Account ID already exists.");
            return;
        }

        idMap.put(id, new IDBalance(id, balance));
        System.out.println("Account created successfully!");
    }

    public static void viewAllAccounts(HashMap<String, IDBalance> idMap) {
        if (idMap.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("\nList of Accounts:");
        System.out.println("ID\t\tBalance");
        System.out.println("---------\t-------");
        for (Map.Entry<String, IDBalance> entry : idMap.entrySet()) {
            IDBalance account = entry.getValue();
            System.out.println(entry.getKey() + "\t\t$" + account.balance);
        }
    }

    public static void searchAccount(Scanner scanner, HashMap<String, IDBalance> idMap) {
        System.out.print("Enter account ID to search: ");
        String searchID = scanner.next();

        IDBalance account = idMap.get(searchID);

        if (account != null) {
            System.out.println("Account Found:");
            System.out.println("ID: " + account.id);
            System.out.println("Balance: $" + account.balance);

            accountMenu(scanner, account, idMap);
        } else {
            System.out.println("Account not found.");
        }
    }

    public static void accountMenu(Scanner scanner, IDBalance account, HashMap<String, IDBalance> idMap) {
        idMap = loadAccountsFromFile();
        int choice;
        do {
            System.out.println("\nAccount Menu (" + account.id + "):");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice (1-5): ");

            try {
                choice = scanner.nextInt();
            } catch (NoSuchElementException e) {
                System.out.println("Invalid input. Please enter a number (1-5).");
                scanner.nextLine(); // Clear scanner buffer after invalid input
                choice = -1; // Set choice to avoid unintended actions
            }

            switch (choice) {
                case 1:
                    System.out.println("Balance: $" + account.balance);
                    break;
                case 2:
                    deposit(scanner, idMap);
                    break;
                case 3:
                    withdraw(scanner, idMap, account);
                    break;
                case 4:
                    transfer(scanner, idMap, account);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        } while (choice != 5);
    }

    public static void viewBalance(Scanner scanner, HashMap<String, IDBalance> idMap) {
        IDBalance selectedAccount = callID(scanner, idMap);
        if (selectedAccount != null) {
            System.out.println("Balance for " + selectedAccount.id + ": $" + selectedAccount.balance);
        }
    }

    public static void deposit(Scanner scanner, HashMap<String, IDBalance> idMap) {
        IDBalance selectedAccount = callID(scanner, idMap);
        if (selectedAccount != null) {
            System.out.print("Enter deposit amount: ");
            double depositAmount = scanner.nextDouble();
            selectedAccount.deposit(depositAmount);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Invalid account selection. Please try again.");
        }
    }

    public static void withdraw(Scanner scanner, HashMap<String, IDBalance> idMap, IDBalance selectedAccount) {
        if (selectedAccount != null) {
            System.out.print("Enter withdrawal amount: ");
            double withdrawAmount = scanner.nextDouble();
            if (selectedAccount.withdraw(withdrawAmount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Invalid account selection. Please try again.");
        }
    }

    public static void transfer(Scanner scanner, HashMap<String, IDBalance> idMap, IDBalance fromAccount) {
        if (fromAccount != null) {
            System.out.print("Enter ID to which to transfer: ");
            IDBalance toAccount = callID(scanner, idMap);

            if (toAccount != null && !toAccount.equals(fromAccount)) { // Prevent transferring to the same account
                System.out.print("Enter transfer amount: ");
                double transferAmount = scanner.nextDouble();

                if (fromAccount.withdraw(transferAmount)) {
                    toAccount.deposit(transferAmount);
                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("Insufficient funds for transfer.");
                }
            } else {
                System.out.println("Invalid transfer selection.");
            }
        }
    }

    public static IDBalance callID(Scanner scanner, HashMap<String, IDBalance> idMap) {
        while (true) {
            System.out.print("Enter account ID: ");
            String searchID = scanner.next();

            if (idMap.containsKey(searchID)) {
                return idMap.get(searchID);
            } else {
                System.out.println("Invalid account ID. Please try again.");
            }
        }
    }


    public static void saveAccountsToFile(HashMap<String, IDBalance> idMap) {
        String csvFileName = "accounts.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            // Writing header
            writer.write("ID,Balance");
            writer.newLine();

            // Writing account data
            for (IDBalance account : idMap.values()) {
                writer.write(account.id + "," + account.balance);
                writer.newLine();
            }
            System.out.println("Accounts data saved to CSV file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving accounts to CSV: " + e.getMessage());
        }
    }

    // Method to load accounts from CSV file
    public static HashMap<String, IDBalance> loadAccountsFromFile() {
        HashMap<String, IDBalance> idMap = new HashMap<>();
        String csvFileName = "accounts.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            // Skip header line
            reader.readLine();
            // Reading account data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String id = parts[0];
                    double balance = Double.parseDouble(parts[1]);
                    idMap.put(id, new IDBalance(id, balance));
                }
            }
            System.out.println("Accounts data loaded from CSV file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading accounts from CSV: " + e.getMessage());
        }
        return idMap;
    }
}


