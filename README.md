# Inday Rental Backend

Inday Rental Backend is a Spring Boot application designed to manage rental properties, tenants, rooms, and billing efficiently. It provides RESTful APIs for handling tenant information, tracking room occupancy, and calculating monthly utility and rent bills (including electricity and water consumption).

## Features

* **Tenant Management**: Keep track of tenant details, move-in dates, and contact information.
* **Room Management**: Manage available, occupied, and maintenance status of rental rooms.
* **Billing System**: 
  * Automatically calculate electricity bills based on previous and current meter readings.
  * Consolidate rent, electricity, and water bills into a single monthly invoice.
  * Track payment statuses (`UNPAID`, `PAID`, `OVERDUE`).
* **Image Upload Integration**: Support for uploading and storing electricity meter reading images (e.g., via Supabase).

## Technologies Used

* **Java** 
* **Spring Boot** (Web, Data JPA)
* **Hibernate / JPA**
* **Maven**

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven
* A relational database (PostgreSQL, MySQL, etc.)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/inday-rental-backend.git
   cd inday-rental-backend
   ```

2. **Configure the Database:**
   Update the `src/main/resources/application.properties` or `application.yml` file with your database credentials.
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/inday_rental
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build and Run the Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

## API Endpoints

The backend exposes several REST endpoints under `/api`. Key endpoints include:
* `/api/rooms` - Room management
* `/api/tenants` - Tenant management
* `/api/billing` - Billing and invoice generation

*(You can integrate Swagger/OpenAPI for detailed endpoint documentation).*

## Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## License

This project is open-source and available under the [MIT License](LICENSE).
