Create Database MoneyBank;
use Database Moneybank;


CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE AccountTypes (
      account_type_id INT PRIMARY KEY AUTO_INCREMENT,
    account_type_name VARCHAR(50) UNIQUE NOT NULL,
    minimum_balance DECIMAL(10,2) NOT NULL
);

CREATE TABLE Accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    type_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    CONSTRAINT fk_account_type
        FOREIGN KEY (type_id) REFERENCES AccountTypes(type_id)
);

CREATE TABLE Transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)REFERENCES Accounts(account_id)
);

CREATE TABLE Transfers (
    transfer_id INT PRIMARY KEY AUTO_INCREMENT,
    from_account INT NOT NULL,
    to_account INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_from_account
        FOREIGN KEY (from_account) REFERENCES Accounts(account_id),
    CONSTRAINT fk_to_account
        FOREIGN KEY (to_account) REFERENCES Accounts(account_id)
);


INSERT INTO Customers (first_name, last_name, email, phone, address)
VALUES
('Kamohelo', 'Mokoena', 'Kamo@gmail.com', '0812345678', 'Pretoria Black Prince Street'),
('Sarah', 'Brown', 'sarah@gmail.com', '0634567899', 'Jhannesburg Houton Estate'),
('Tony', 'Stark', 'Irongmail.com', '0723456789', 'Johannesburg Maboa Street'),
('Bruce', 'Wayn', 'Batman@gmail.com', '0612345678', 'Durban Ethekwini'),
('David', 'Goliath', 'davG@gmail.com', '0712345678', 'Polokwane Gamashashane');

INSERT INTO AccountTypes (type_name, minimum_balance)
VALUES
('Student', 100.00),
('Cheque', 50.00),
('Business', 500.00),
('Savings', 10000.00),
('Fixed Deposit', 1000.00);

INSERT INTO Accounts (customer_id, type_id, account_number, balance)
VALUES
(1, 1, 'ACC1001', 5000.00),
(2, 2, 'ACC1002', 3500.00),
(3, 1, 'ACC1003', 9000.00),
(4, 4, 'ACC1004', 1200.00),
(5, 3, 'ACC1005', 25000.00);

INSERT INTO Transactions (account_id, transaction_type, amount, description)
VALUES
(1, 'Deposit', 1000.00, 'Cash deposit'),
(2, 'Withdrawal', 500.00, 'ATM withdrawal'),
(3, 'Deposit', 2000.00, 'Salary deposit'),
(4, 'Withdrawal', 300.00, 'Card payment'),
(5, 'Deposit', 5000.00, 'Business payment');

INSERT INTO Transfers (from_account, to_account, amount)
VALUES
(1, 2, 500.00),
(2, 3, 250.00),
(3, 4, 700.00),
(4, 5, 100.00),
(5, 1, 1200.00);

SELECT * FROM Customers;

SELECT * FROM Accounts
WHERE balance > 3000;

SELECT * FROM Accounts
WHERE type_id = 1;

SELECT * FROM Transactions
WHERE transaction_type = 'Deposit';

SELECT * FROM Customers
WHERE address = 'Johannesburg Houton Estate';

UPDATE Customers
SET phone = '0645678934'
WHERE customer_id = 1;

UPDATE Accounts
SET balance = 6000.00
WHERE account_id = 1;

DELETE FROM Transactions
WHERE transaction_id = 4;

DELETE FROM Transfers
WHERE transfer_id = 4;
