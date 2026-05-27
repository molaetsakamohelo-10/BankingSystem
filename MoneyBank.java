package moneybank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MoneyBank{

    static Scanner input = new Scanner(System.in);

    // DATABASE DETAILS
    static final String URL = "jdbc:mysql://localhost:3306/MoneyBank";
    static final String USER = "root";
    static final String PASSWORD = "";

    // MAIN METHOD
    public static void main(String[] args) {

        int option;

        do {

            System.out.println("===== Welcome to Money Bank =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Check Balance");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            option = input.nextInt();

            switch(option) {

                case 1 -> registerCustomer();

                case 2 -> createAccount();

                case 3 -> depositMoney();

                case 4 -> withdrawMoney();

                case 5 -> transferMoney();

                case 6 -> checkBalance();

                case 7 -> System.out.println("Thank you for using the Banking System.");

                default -> System.out.println("Invalid choice!");
            }

        } while(option != 7);
    }

    // DATABASE CONNECTION METHOD
    public static Connection getConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch(SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }

        return conn;
    }

    // REGISTER CUSTOMER METHOD
    public static void registerCustomer() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter First Name: ");
            String firstName = input.next();

            System.out.print("Enter Last Name: ");
            String lastName = input.next();

            System.out.print("Enter Email: ");
            String email = input.next();

            System.out.print("Enter Phone Number: ");
            String phone = input.next();

            String sql = "INSERT INTO Customers(first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, email);
            pst.setString(4, phone);

            pst.executeUpdate();

            System.out.println("Customer registered successfully!");

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CREATE ACCOUNT METHOD
    public static void createAccount() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter Customer ID: ");
            int customerId = input.nextInt();

            System.out.print("Enter Account Type ID: ");
            int typeId = input.nextInt();

            System.out.print("Enter Account Number: ");
            String accountNumber = input.next();

            System.out.print("Enter Opening Balance: ");
            double balance = input.nextDouble();

            String sql = "INSERT INTO Accounts(customer_id, type_id, account_number, balance) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, customerId);
            pst.setInt(2, typeId);
            pst.setString(3, accountNumber);
            pst.setDouble(4, balance);

            pst.executeUpdate();

            System.out.println("Account created successfully!");

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // DEPOSIT MONEY METHOD
    public static void depositMoney() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter Account ID: ");
            int accountId = input.nextInt();

            System.out.print("Enter Deposit Amount: ");
            double amount = input.nextDouble();

            if(amount <= 0) {
                System.out.println("Invalid amount!");
                return;
            }

            String sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setDouble(1, amount);
            pst.setInt(2, accountId);

            pst.executeUpdate();

            System.out.println("Deposit successful!");

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // WITHDRAW MONEY METHOD
    public static void withdrawMoney() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter Account ID: ");
            int accountId = input.nextInt();

            System.out.print("Enter Withdrawal Amount: ");
            double amount = input.nextDouble();

            String checkSql = "SELECT balance FROM Accounts WHERE account_id = ?";

            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setInt(1, accountId);

            ResultSet rs = checkPst.executeQuery();

            if(rs.next()) {

                double balance = rs.getDouble("balance");

                if(balance < amount) {
                    System.out.println("Insufficient funds!");
                    return;
                }
            }

            String sql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setDouble(1, amount);
            pst.setInt(2, accountId);

            pst.executeUpdate();

            System.out.println("Withdrawal successful!");

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // TRANSFER MONEY METHOD
    public static void transferMoney() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter Sender Account ID: ");
            int fromAccount = input.nextInt();

            System.out.print("Enter Receiver Account ID: ");
            int toAccount = input.nextInt();

            System.out.print("Enter Transfer Amount: ");
            double amount = input.nextDouble();

            String checkSql = "SELECT balance FROM Accounts WHERE account_id = ?";

            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setInt(1, fromAccount);

            ResultSet rs = checkPst.executeQuery();

            if(rs.next()) {

                double balance = rs.getDouble("balance");

                if(balance < amount) {
                    System.out.println("Insufficient funds!");
                    return;
                }
            }

            String withdrawSql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";

            PreparedStatement withdrawPst = conn.prepareStatement(withdrawSql);

            withdrawPst.setDouble(1, amount);
            withdrawPst.setInt(2, fromAccount);

            withdrawPst.executeUpdate();

            String depositSql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";

            PreparedStatement depositPst = conn.prepareStatement(depositSql);

            depositPst.setDouble(1, amount);
            depositPst.setInt(2, toAccount);

            depositPst.executeUpdate();

            System.out.println("Transfer successful!");

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CHECK BALANCE METHOD
    public static void checkBalance() {

        try {

            Connection conn = getConnection();

            System.out.print("Enter Account ID: ");
            int accountId = input.nextInt();

            String sql = "SELECT balance FROM Accounts WHERE account_id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, accountId);

            ResultSet rs = pst.executeQuery();

            if(rs.next()) {

                double balance = rs.getDouble("0.00");

                System.out.println("Current Balance: R" + balance);

            }
            else {

                System.out.println("Account not found!");
            }

        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}