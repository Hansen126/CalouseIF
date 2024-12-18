# CaLouselF

CaLouselF is a marketplace application designed to facilitate the buying and selling of second-hand clothing easily and safely. This platform provides a space for users to sell pre-loved clothing that is still of good quality and wearable, as well as offering buyers the opportunity to find unique and affordable fashion items. With a focus on sustainability and a sustainable lifestyle, CaLouselF aims to promote more sustainable shopping practices by extending the lifespan of clothing.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Registration and Login:** Users can create accounts as buyers or sellers. Admins have special credentials for access.
- **Product Management:** Sellers can upload, edit, and delete their items. Admins review and approve or decline items.
- **Purchasing and Offers:** Buyers can purchase items directly or make offers for better prices.
- **Wishlist and Purchase History:** Buyers can add items to their wishlist and view their purchase history.
- **Secure Transactions:** Strong encryption and responsive customer support ensure user security and satisfaction.
- **Community Interaction:** Users can interact with a community that shares similar interests through social media integration and product reviews.

## Technologies Used

- **Programming Language:** Java
- **Architecture:** MVC (Model-View-Controller)
- **Frontend:** JavaFX
- **Database:** MySQL
- **Version Control:** GitHub

## Installation

Follow these steps to set up and run CaLouselF on your local machine:

### Prerequisites

- **Java Development Kit (JDK):** Ensure you have JDK 8 or higher installed.
- **JavaFX:** Download and set up JavaFX SDK.
- **MySQL Server:** Install MySQL Server on your machine.
- **IDE:** An Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse is recommended.
- **Git:** Install Git to clone the repository.

### Steps

1. **Clone the Repository:**

   Open your terminal or command prompt and run:

   ```bash
   git clone https://github.com/your-username/Calouseif.git

2. **Import JavaFX and MySQL Libraries:**

   - **JavaFX:**
     - Download the JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/).
     - Extract the SDK and add the `lib` folder to your project's module path.
   
   - **MySQL Connector:**
     - Download the MySQL Connector/J from [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/).
     - Add the `mysql-connector-java-x.x.xx.jar` to your project's module path.

3. **Create MySQL Database:**

   - Open **phpMyAdmin** or your preferred MySQL client.
   - Create a new database named `calouseif`.

   ```sql
   CREATE DATABASE calouseif;

4. **Import Database Schema:**

   - In **phpMyAdmin**, select the `calouseif` database.
   - Click on the **Import** tab.
   - Choose the `CalouseIF.sql` file from the cloned repository and import it.

5. **Configure Database Connection:**

   - Locate the database configuration file in the project (e.g., `config.properties` or similar).
   - Update the database URL, username, and password as per your MySQL setup.

     ```properties
     db.url=jdbc:mysql://localhost:3306/calouseif
     db.username=your_mysql_username
     db.password=your_mysql_password
     ```

6. **Run the Application:**

   - Open the project in your IDE.
   - Ensure that JavaFX libraries are correctly referenced in the module path.
   - Compile and run the `Main.java` (or the main entry point) to start the application.

## Database Setup

The `CalouseIF.sql` script sets up all necessary tables and initial data required for the application. Ensure that you import this script into the `calouseif` database before running the application.

## Running the Application

1. **Start MySQL Server:**

   Ensure that your MySQL server is running.

2. **Launch the Application:**

   Run the `Main.java` file from your IDE. The application window should appear, allowing you to register, login, and start using CaLouselF.

## Usage

1. **Register an Account:**

   - Choose to register as a buyer or seller.
   - Fill in the required details and create an account.

2. **Login:**

   - Use your credentials to log in.
   - Admins can log in using the username `admin` and password `admin`.

3. **Manage Items:**

   - **Sellers:** Upload, edit, or delete items.
   - **Admins:** Approve or decline items submitted by sellers.

4. **Purchase and Make Offers:**

   - **Buyers:** Browse items, make purchases, or submit offers for better prices.

5. **Wishlist and History:**

   - Add items to your wishlist and view your purchase history.
