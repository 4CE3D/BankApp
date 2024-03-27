import java.util.Scanner;

public class BankApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        IDBalance[] idBalances = new IDBalance[]{
                new IDBalance("ID123", 100.50),
                new IDBalance("ID228", 25.75),
                new IDBalance("ID007", 0.00)
        };

        System.out.println("Welcome to bankApp");

        int choice;
        do {
            // Display available IDs and balances
            System.out.println("\nAvailable IDs and Balances:");
            for (int i = 0; i < idBalances.length; i++) {
                System.out.println((i + 1) + ". " + idBalances[i].id + " (Balance: $" + idBalances[i].balance + ")");
            }

            System.out.println("\nMenu:");
            System.out.println("1. View balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewBalance(scanner, idBalances);
                    break;
                case 2:
                    deposit(scanner, idBalances);
                    break;
                case 3:
                    withdraw(scanner, idBalances);
                    break;
                case 4:
                    transfer(scanner, idBalances);
                    break;
                case 5:
                    System.out.println("Exiting bankApp. Thank you for using our services!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        } while (choice != 5); // Exit loop when choice is 5

        scanner.close();
    }

    public static void viewBalance(Scanner scanner, IDBalance[] idBalances) {
        int selectedIDIndex = callID(scanner, idBalances);
        if (selectedIDIndex != -1) {
            System.out.println("Balance for " + idBalances[selectedIDIndex].id + ": $" + idBalances[selectedIDIndex].balance);
        }
    }

    public static void deposit(Scanner scanner, IDBalance[] idBalances) {
        int selectedIDIndex = callID(scanner, idBalances);
        if (selectedIDIndex != -1) {
            System.out.print("Enter deposit amount: ");
            double depositAmount = scanner.nextDouble();
            idBalances[selectedIDIndex].deposit(depositAmount);
            System.out.println("Deposit successful!");
        }
    }

    public static void withdraw(Scanner scanner, IDBalance[] idBalances) {
        int selectedIDIndex = callID(scanner, idBalances);
        if (selectedIDIndex != -1) {
            System.out.print("Enter withdrawal amount: ");
            double withdrawAmount = scanner.nextDouble();
            if (idBalances[selectedIDIndex].withdraw(withdrawAmount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient funds.");
            }
        }
    }

    public static void transfer(Scanner scanner, IDBalance[] idBalances) {
        System.out.print("Enter ID from which to transfer: ");
        int fromIDIndex = callID(scanner, idBalances);

        if (fromIDIndex != -1) {
            System.out.print("Enter ID to which to transfer: ");
            int toIDIndex = callID(scanner, idBalances);

            if (toIDIndex != -1 && toIDIndex != fromIDIndex) { // Avoid transfer to same ID
                System.out.print("Enter transfer amount: ");
                double transferAmount = scanner.nextDouble();

                if (idBalances[fromIDIndex].withdraw(transferAmount)) {
                    idBalances[toIDIndex].deposit(transferAmount);
                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("Insufficient funds for transfer.");
                }
            } else {
                System.out.println("Invalid transfer selection.");
            }
        }
    }

    public static int callID(Scanner scanner, IDBalance[] idBalances) {
        System.out.print("Enter ID number you want to select (1-" + idBalances.length + "): ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= idBalances.length) {
            return choice - 1; // Return index for valid selection (0-based indexing)
        } else {
            System.out.println("Invalid ID choice.");
            return -1; // Indicate invalid selection
        }
    }
}

