import java.util.*;

class Account {
    private String userId;
    private String userPin;
    private double balance;
    private List<String> transactionHistory;

    public Account(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: ₹" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrawn: ₹" + amount);
            return true;
        } else {
            transactionHistory.add("Failed Withdrawal Attempt of ₹" + amount);
            return false;
        }
    }

    public boolean transfer(Account receiver, double amount) {
        if (amount <= balance) {
            balance -= amount;
            receiver.deposit(amount);
            transactionHistory.add("Transferred ₹" + amount + " to " + receiver.getUserId());
            return true;
        } else {
            transactionHistory.add("Failed Transfer Attempt of ₹" + amount + " to " + receiver.getUserId());
            return false;
        }
    }

    public void changePin(String newPin) {
        this.userPin = newPin;
        transactionHistory.add("PIN changed successfully");
    }

    public void calculateInterest(double rate, int years) {
        double interest = (balance * rate * years) / 100;
        balance += interest;
        transactionHistory.add("Interest added: ₹" + interest + " (" + rate + "% for " + years + " years)");
    }

    public void printTransactionHistory() {
        System.out.println("\nTransaction History for " + userId + ":");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
        System.out.println("Current Balance: ₹" + balance);
    }
}

// Class for ATM functionalities
class ATM {
    private Account currentAccount;
    private Scanner sc;
    private Map<String, Account> accounts;

    public ATM() {
        sc = new Scanner(System.in);
        accounts = new HashMap<>();

        // Default sample users
        accounts.put("user1", new Account("user1", "1234", 5000));
        accounts.put("user2", new Account("user2", "5678", 8000));
        accounts.put("user3", new Account("user3", "2468", 12000));
        accounts.put("user4", new Account("user4", "1357", 2500));
        accounts.put("user5", new Account("user5", "4321", 10000));
        accounts.put("nishant", new Account("nishant", "2025", 15000));
        accounts.put("student", new Account("nitin", "1010", 3000));
    }

    public void start() {
        while (true) {
            System.out.println("\n===== Welcome to Java ATM Interface =====");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    System.out.println("Thank you for visiting Java ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void login() {
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter PIN: ");
        String userPin = sc.nextLine();

        if (authenticateUser(userId, userPin)) {
            System.out.println("\nLogin Successful! Welcome, " + userId + ".");
            mainMenu();
        } else {
            System.out.println("Invalid User ID or PIN. Please try again.");
        }
    }

    private boolean authenticateUser(String userId, String userPin) {
        if (accounts.containsKey(userId)) {
            Account acc = accounts.get(userId);
            if (acc.getUserPin().equals(userPin)) {
                currentAccount = acc;
                return true;
            }
        }
        return false;
    }

    private void createNewAccount() {
        System.out.print("Enter new User ID: ");
        String newUserId = sc.nextLine();

        if (accounts.containsKey(newUserId)) {
            System.out.println("User ID already exists! Please choose a different one.");
            return;
        }

        System.out.print("Set a 4-digit PIN: ");
        String newPin = sc.nextLine();
        System.out.print("Enter initial deposit amount: ₹");
        double initialBalance = sc.nextDouble();

        Account newAccount = new Account(newUserId, newPin, initialBalance);
        accounts.put(newUserId, newAccount);
        System.out.println("Account created successfully! You can now login using your credentials.");
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Change PIN");
            System.out.println("6. Calculate Interest");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    currentAccount.printTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    changePin();
                    break;
                case 6:
                    calculateInterest();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: ₹");
        double amount = sc.nextDouble();
        if (currentAccount.withdraw(amount)) {
            System.out.println("Withdrawal successful!");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ₹");
        double amount = sc.nextDouble();
        currentAccount.deposit(amount);
        System.out.println("Deposit successful!");
    }

    private void transfer() {
        sc.nextLine(); // clear buffer
        System.out.print("Enter receiver User ID: ");
        String receiverId = sc.nextLine();
        if (!accounts.containsKey(receiverId)) {
            System.out.println("Receiver not found!");
            return;
        }
        System.out.print("Enter amount to transfer: ₹");
        double amount = sc.nextDouble();
        if (currentAccount.transfer(accounts.get(receiverId), amount)) {
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Insufficient balance.");
        }
    }
    private void changePin() {
        sc.nextLine(); // clear buffer
        System.out.print("Enter your current PIN: ");
        String oldPin = sc.nextLine();

        if (oldPin.equals(currentAccount.getUserPin())) {
            System.out.print("Enter your new PIN: ");
            String newPin = sc.nextLine();
            currentAccount.changePin(newPin);
            System.out.println("PIN changed successfully!");
        } else {
            System.out.println("Incorrect current PIN!");
        }
    }
    private void calculateInterest() {
        System.out.print("Enter annual interest rate (%): ");
        double rate = sc.nextDouble();
        System.out.print("Enter number of years: ");
        int years = sc.nextInt();

        currentAccount.calculateInterest(rate, years);
        System.out.println("Interest added successfully!");
    }
}
// Main class
public class ATMInterface {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
