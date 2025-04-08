# 📈 Crypto Trading Simulator

## Overview
This is a **web application** that simulates cryptocurrency trading using **real-time prices** from the Kraken exchange.  
It allows users to:

- Manage a **virtual portfolio**
- **Buy/Sell** crypto assets
- Track **account balance** and **transaction history**
- **Reset** their account at any time

## Tech Stack

---

### Frontend
- **HTML** – Defines the structure and layout of the web pages.
- **CSS** – Handles the visual styling and layout of the user interface.
- **JavaScript** – Implements the client-side logic, including WebSocket handling, buying/selling, and dynamic DOM updates.
- **Fetch API** – Used to perform HTTP requests to the backend for actions like fetching assets, balances, and transactions.

---

### Backend
- **Java** – The primary programming language used for backend development.
- **Spring Boot** – Powers the REST APIs and manages the application's business logic, request handling, and database interactions. Organized in layers (Controller, Service, DAO) for maintainability.

---

### API Source
- **Kraken WebSocket API v2** – Provides real-time data for cryptocurrency prices, including buy and sell values for the top 20 cryptocurrencies.

---

### Database
- **MySQL** – Stores all persistent data including:
  - Registered users and their account balances
  - User-owned cryptocurrency assets
  - Complete transaction history (BUY/SELL)
- **SQL Script** – Used to create the necessary schema without relying on any ORM tools.

## ER diagram
![image](https://github.com/user-attachments/assets/6e3fc6ce-81f8-4235-a956-586651e36501)


## Features

### Real-time Price Display
The application connects to the Kraken WebSocket API to receive live market updates. A dynamically updating table displays each cryptocurrency’s symbol, current buy price (ask), and sell price (bid), ensuring users see the most recent values in real time.

### Virtual Account
Every user starts with a virtual balance of $10,000. When a cryptocurrency is purchased, the cost is automatically deducted from this balance. Selling crypto increases the balance accordingly. The system prevents invalid transactions such as trying to buy more than the available balance or selling more than owned.

### Transactions
All BUY and SELL operations are saved in the database. The application keeps a full transaction history for each user, including the transaction type, symbol, price, quantity, timestamp, and calculated profit or loss when applicable.

### Reset Account
Users can reset their account at any time. This action restores the balance to the initial $10,000, removes all currently held assets, and deletes the transaction history — providing a clean slate for new simulations.


## API Endpoints

### User Endpoints
- `POST /user/create`  
  Registers a new user with a unique username and initializes their balance.

- `GET /user/login?username=...`  
  Logs in a user by verifying their username and returning their account information.

- `GET /user/balance/{id}`  
  Retrieves the current balance of a specific user by their user ID.

- `GET /user/reset/by/id/{id}`  
  Resets the user's account by restoring the balance to the initial value and clearing all assets and transactions.

### UserAsset Endpoints
- `GET /user-assets/{userId}`  
  Returns a list of all cryptocurrencies held by a specific user, including quantities.

### Transaction Endpoints
- `POST /transaction/buy`  
  Simulates the purchase of cryptocurrency. Deducts the total cost from the user’s balance and records the transaction.

- `POST /transaction/sell`  
  Simulates selling cryptocurrency. Adds the proceeds to the user’s balance, calculates profit/loss, and records the transaction.

- `GET /transaction/history/by/user-id/{id}`  
  Returns the full transaction history for a user, including each transaction’s type, quantity, price, and timestamp.


## Screenshots

Below are key screens that illustrate the functionality of the application:

- **Live Crypto Prices:**  
  A dynamic table displaying real-time cryptocurrency prices for the top 20 coins.
![image](https://github.com/user-attachments/assets/33c2d179-cdde-4824-8bdc-3e8e8d704d2e)

- **Buy/Sell Interface:**  
  Buy
  ![image](https://github.com/user-attachments/assets/248765f5-dee9-4889-b6db-8c13e2ce3231)
  Sell
  ![image](https://github.com/user-attachments/assets/cad2321a-4c1a-4dac-b4b2-51af9f672df4)

- **Transaction History:**  
  A detailed table showing the user's full transaction history, including symbol, type (BUY/SELL), quantity, price, profit/loss, and timestamp.
  ![image](https://github.com/user-attachments/assets/8931f0e0-b273-4de7-b735-89b9f8343d48)

- ✅ **Reset Portfolio Button:**  
  A button that resets the user’s account to the initial state by restoring the default balance and clearing all crypto holdings and transactions.
  ![image](https://github.com/user-attachments/assets/448739df-1bff-444c-be8b-782f3801f6b3)

Video:

