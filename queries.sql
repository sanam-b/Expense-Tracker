CREATE DATABASE EXPENSETRACKER;
USE EXPENSETRACKER;

CREATE TABLE users(
user_id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(100) NOT NULL,
pwd VARCHAR(100) NOT NULL,
phone VARCHAR(100) NOT NULL ,
email VARCHAR(100) NOT NULL,
created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
active_yn INT DEFAULT 1
);

CREATE TABLE category(
category_id INT PRIMARY KEY AUTO_INCREMENT,
category_name VARCHAR(100),
transactiontype ENUM('Income','Expense') NOT NULL DEFAULT 'EXPENSE',
icon_url VARCHAR(100),
description VARCHAR(100) ,
user_id INT NOT NULL,
created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE TABLE transaction(
transaction_id INT PRIMARY KEY AUTO_INCREMENT,
notes VARCHAR(100) NOT NULL,
date_of_transaction DATE NOT NULL,
amount DECIMAL(10,2) not null,
created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
user_id INT ,
category_id INT ,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY(category_id) REFERENCES category(category_id) ON DELETE SET NULL
);


INSERT INTO users(username, pwd , phone , email) VALUES ("Sanam@01" , "sanamjb","8265061088" , "sanamjbhatia@gmail.com");
INSERT INTO category(category_name , transactiontype , description) VALUES ("JOB",'INCOME' , "I EARNED SOME MONEY TODAY");
INSERT INTO transaction(notes , date_of_transaction , amount) VALUES("This transaction is from the comapny" , '2025-12-26' ,80000);