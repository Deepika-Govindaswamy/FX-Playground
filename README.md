# FX-Playground - A Forex Transaction Simulator 💸

<br/>

The Forex Transaction Simulator is a microservices-based learning application designed to simulate the lifecycle of real-world foreign exchange (FX) transactions.
The goal of this project is to understand:

* How multi-currency wallets operate
* How FX quotes are generated
* How forex transactions are executed
* How external financial APIs (Wise & Stripe in test/sandbox mode) are integrated
* How distributed microservices communicate to complete an FX order

This is **NOT** a production-grade trading platform, but an educational project aimed at understanding FX infrastructure, multi-currency systems, and distributed backend design using a real-world approach.

**Key Learning Outcomes**

* Understanding liquidity, quotes, and spreads
* FX order lifecycle
* Multi-currency accounting
* Distributed systems in fintech

<br/>

## ⚙️ Application Workflow

**1. User Onboarding**

* New users register and automatically receive a multi-currency wallet (GBP, USD, EUR).
* User data is managed through a dedicated User Management Service.

**2. Wallet Recharge (Add Money)**

* Users can “top up” their wallet using a Stripe-based payment simulation.
* The Wallet Recharge Service records all top-up transactions.

**3. Forex Conversion Simulation**

Users can initiate a currency conversion such as GBP → EUR or USD → GBP, using the following flow:

* Select base currency, quote currency, and amount.
* System fetches live FX rates using Wise’s API.
* User sees the generated quote and can approve the transaction.
* The Forex Simulator service executes a simulated conversion using Wise’s Test API.
* On success:
  * Base currency is deducted from the user’s wallet
  * Quote currency is credited
* The Order Management Service logs the entire conversion history.

<br/>

## 🧱 High-Level System Architecture 

The application follows a microservices architecture, where each service has its own database and responsibility:

#### 1. User Management Service
* Handles User Registration, Login, and OAuth 2.0 Authentication
* Creates a multi-currency wallet (GBP, USD, EUR) on successful signup
* Database: customers-db

#### 2. Wallet Service
* Manages wallet creation, balance tracking, and multi-currency holdings
* No money movement logic — only stores wallet states
* Database: wallet-db

#### 3. Wallet Recharge Service
* Allows users to add funds (GBP / USD / EUR) to their wallet
* Payment simulation is done using the Stripe API in test/sandbox mode
* Database: transactions-db

#### 4. Order Management Service
Responsible for the entire Forex Order Lifecycle:

* User selects base currency, target currency, and amount
* Fetches live FX quotes via Wise API
* Generates a conversion order
* Waits for user approval
* Coordinates with the FX Simulator for execution
* Logs completed FX conversions
Databases:
* order-list-db
* forex-txns-db

#### 5. Forex Transaction Simulator Service
* Core component that interacts directly with Wise’s API in test/sandbox mode
* Handles:
    * Getting current FX rates
    * Creating a Wise Quote
    * Approving and simulating conversion
    * Polling the Wise API for transaction status
* Returns final settlement results to the Order Management Service

<br/>

## 🛠️ Tech Stack
  
### Backend
<div> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/java.png" alt="Java" title="Java"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/spring_boot.png" alt="Spring Boot" title="Spring Boot"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/hibernate.png" alt="Hibernate" title="Hibernate"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/postgresql.png" alt="PostgreSQL" title="PostgreSQL"/></code> 
</div> 
<br/>
<p> Java • Spring Boot • Hibernate/JPA • PostgreSQL • Microservices Architecture </p>

### Frontend
<div> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/javascript.png" alt="JavaScript" title="JavaScript"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/react.png" alt="React" title="React"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/vite.png" alt="Vite" title="Vite"/></code> 
</div>
<br/>
<p> JavaScript • ReactJS • Vite </p>

### DevOps & Tooling
<div> <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/docker.png" alt="Docker" title="Docker"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/git.png" alt="Git" title="Git"/></code> 
  <code><img width="55" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/postman.png" alt="Postman" title="Postman"/></code> 
</div> 
<br/>
<p> Docker • Git & GitHub • Postman (API Testing) </p>
  
### External APIs: 
* Wise API (Simulate Forex conversions)
* Stripe API (Simulate wallet top-up transactions)

<br/>

**Project is in development process.**

*****Note*****: This is not a production FX trading system and does not replace regulated financial services.
