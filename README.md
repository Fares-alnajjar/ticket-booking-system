<div align="center">

# 🎫 Ticket Booking System

**A comprehensive Spring Boot web application for managing event ticket bookings with user authentication, admin controls, and payment processing.**

[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

[Features](#-features) • [Getting Started](#-getting-started) • [API Documentation](#-api-documentation) • [Contributing](#-contributing) • [License](#-license)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Building for Production](#-building-for-production)
- [Contributing](#-contributing)
- [Roadmap](#-roadmap)
- [License](#-license)
- [Acknowledgments](#-acknowledgments)
- [Contact](#-contact)

---

## 🌟 Overview

Ticket Booking System is a full-stack web application built with Spring Boot that provides a complete solution for event ticket management. It features user authentication, event browsing, ticket booking with a shopping cart, payment processing, and a comprehensive admin dashboard for managing users and events.

---

## ✨ Features

### User Features
- **🔐 User Authentication**: Secure registration and sign-in with role-based access control (User & Admin roles)
- **🎪 Event Management**: Browse events by category, view event details
- **🛒 Ticket Booking**: Book tickets for events with a shopping cart system
- **💳 Payment Processing**: Integrated payment system with card details for ticket purchases
- **📋 Order Confirmation**: Post-payment confirmation page with booking details
- **🎫 My Tickets**: View all purchased tickets with QR code support
- **👤 User Profile**: View and manage user account details
- **🔒 Session Management**: Persistent user sessions with secure session handling

### Admin Features
- **📊 Admin Dashboard**: Complete admin interface with system statistics
- **➕ Event Management**: Add, edit, and delete events
- **👥 User Management**: Manage registered users with full CRUD operations
- **📈 Analytics**: View system statistics and booking trends
- **🎨 Admin UI**: Dedicated admin interface with custom styling

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 25 | Programming language |
| **Spring Boot** | 4.0.5 | Application framework |
| **Spring Security** | 6.x | Authentication and authorization |
| **Spring Data JPA** | 3.x | Data persistence with Hibernate |
| **PostgreSQL** | Latest | Relational database (hosted on Supabase) |
| **Thymeleaf** | 3.x | Server-side HTML templating engine |
| **Thymeleaf Extras Spring Security** | 3.x | Security integration with Thymeleaf |
| **Spring Boot Validation** | 3.x | Input validation |
| **Spring Boot Actuator** | 3.x | Health checks and monitoring |
| **Lombok** | Latest | Reduce boilerplate code |
| **Maven** | 3.6+ | Build and dependency management |
| **HTML/CSS/JS** | Modern | Frontend styling and interactivity |

---

## 📁 Project Structure

```
ticketbookingsystem/
├── src/
│   ├── main/
│   │   ├── java/com/project/ticketbookingsystem/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/          # Web controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── EventController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── SignInController.java
│   │   │   │   ├── SignUpController.java
│   │   │   │   └── TicketController.java
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   │   ├── SignUpDto.java
│   │   │   │   ├── SignInDto.java
│   │   │   │   ├── EventRequest.java
│   │   │   │   └── CartItemDto.java
│   │   │   ├── model/               # JPA entities
│   │   │   │   ├── UserEntity.java
│   │   │   │   ├── EventEntity.java
│   │   │   │   ├── BookingEntity.java
│   │   │   │   ├── TicketEntity.java
│   │   │   │   └── PaymentEntity.java
│   │   │   ├── repository/          # Spring Data JPA repositories
│   │   │   ├── service/             # Business logic layer
│   │   │   └── TicketbookingsystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/           # Thymeleaf HTML templates
│   │       │   ├── home_page.html
│   │       │   ├── sign_up.html
│   │       │   ├── sign_in.html
│   │       │   ├── events.html
│   │       │   ├── Booking.html
│   │       │   ├── cart.html
│   │       │   ├── payment.html
│   │       │   ├── confirmation.html
│   │       │   ├── my-tickets.html
│   │       │   ├── user-profile.html
│   │       │   └── Admin/
│   │       │       ├── Admin.html
│   │       │       ├── adminMenu.html
│   │       │       ├── add event.html
│   │       │       ├── edit-event.html
│   │       │       ├── edit-user.html
│   │       │       ├── manage-events.html
│   │       │       ├── manage-users.html
│   │       │       └── user-profile.html
│   │       └── static/              # CSS, JavaScript, images
│   │           ├── css/
│   │           ├── js/
│   │           ├── adminStyles/
│   │           └── *.css
│   └── test/
│       └── java/
├── .gitignore
├── .gitattributes
├── LICENSE
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 25** or higher ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL** database ([Download](https://www.postgresql.org/download/))
- Git (for cloning the repository)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/ticketbookingsystem.git
cd ticketbookingsystem
```

### 2. Configure Database

Create a PostgreSQL database and update the configuration in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ticketbooking
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> ⚠️ **Security Note**: Never commit production database credentials to version control. Use environment variables or a secrets manager for production deployments.

### 3. Install Dependencies

```bash
# Using Maven wrapper (Linux/Mac)
./mvnw clean install

# Using Maven wrapper (Windows)
mvnw.cmd clean install

# Using Maven directly
mvn clean install
```

### 4. Run the Application

```bash
# Using Maven wrapper (Linux/Mac)
./mvnw spring-boot:run

# Using Maven wrapper (Windows)
mvnw.cmd spring-boot:run

# Using Maven directly
mvn spring-boot:run
```

### 5. Access the Application

Open your browser and navigate to:

```
http://localhost:8080
```

**Default Configuration:**
- Server Port: `8080`
- Context Path: `/`
- Database: PostgreSQL

---

## 📚 API Documentation

### Public Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Home page |
| `/sign_up` | GET | Registration page |
| `/sign_up` | POST | Submit registration form |
| `/sign_in` | GET | Login page |
| `/sign_in` | POST | Submit login credentials |
| `/events` | GET | Browse all events / filter by category |
| `/events/booking/{id}` | GET | Event booking details page |

### Authenticated User Endpoints

| Endpoint | Method | Description | Authentication |
|----------|--------|-------------|----------------|
| `/booking/cart/add` | POST | Add tickets to cart | Required |
| `/cart` | GET | View shopping cart | Required |
| `/payment` | GET | Payment page | Required |
| `/payment` | POST | Process payment | Required |
| `/confirmation` | GET | Booking confirmation page | Required |
| `/my-tickets` | GET | View purchased tickets | Required |
| `/user-profile` | GET | View user profile | Required |

### Admin Endpoints

| Endpoint | Method | Description | Authentication |
|----------|--------|-------------|----------------|
| `/admin` | GET | Admin dashboard | Admin |
| `/admin/add-event` | GET | Add event form | Admin |
| `/admin/add-event` | POST | Submit new event | Admin |
| `/admin/manage-events` | GET | List all events for management | Admin |
| `/admin/edit-event/{id}` | GET | Edit event form | Admin |
| `/admin/update-event` | POST | Submit event update | Admin |
| `/admin/delete-event/{id}` | POST | Delete an event | Admin |
| `/admin/manage-users` | GET | List all users | Admin |
| `/admin/edit-user/{id}` | GET | Edit user form | Admin |
| `/admin/update-user` | POST | Submit user update | Admin |
| `/admin/delete-user/{id}` | POST | Delete a user | Admin |

---

## 🧪 Testing

Run the test suite using Maven:

```bash
# Using Maven wrapper (Linux/Mac)
./mvnw test

# Using Maven wrapper (Windows)
mvnw.cmd test

# Using Maven directly
mvn test
```

To run specific test classes:

```bash
mvn test -Dtest=YourTestClass
```

To generate test coverage report:

```bash
mvn clean test jacoco:report
```

---

## 🏗 Building for Production

### Build the Application

```bash
# Linux/Mac
./mvnw clean package

# Windows
mvnw.cmd clean package

# Using Maven directly
mvn clean package
```

This creates an executable JAR file in the `target/` directory:

```
target/ticketbookingsystem-0.0.1-SNAPSHOT.jar
```

### Run the JAR

```bash
java -jar target/ticketbookingsystem-0.0.1-SNAPSHOT.jar
```

### Production Configuration

For production deployment, consider:

1. **Environment Variables**: Use environment variables for sensitive configuration
2. **Profile-specific Configuration**: Create `application-prod.properties`
3. **Database Connection Pooling**: Configure HikariCP settings
4. **HTTPS**: Enable SSL/TLS for secure connections
5. **Logging**: Configure appropriate logging levels
6. **Monitoring**: Enable Spring Boot Actuator endpoints

Example production profile:

```properties
# application-prod.properties
server.port=8080
spring.profiles.active=prod
logging.level.root=WARN
logging.level.com.project.ticketbookingsystem=INFO
```

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

### How to Contribute

1. **Fork the Repository**
   ```bash
   https://github.com/yourusername/ticketbookingsystem/fork
   ```

2. **Clone Your Fork**
   ```bash
   git clone https://github.com/your-username/ticketbookingsystem.git
   cd ticketbookingsystem
   ```

3. **Create a Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

4. **Make Your Changes**
   - Write clean, readable code
   - Add tests for new features
   - Update documentation as needed

5. **Commit Your Changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```

6. **Push to Your Branch**
   ```bash
   git push origin feature/amazing-feature
   ```

7. **Open a Pull Request**
   - Provide a clear description of your changes
   - Reference any related issues
   - Ensure all tests pass

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add appropriate Javadoc comments for public methods
- Ensure all tests pass before submitting
- Follow the existing code structure and patterns
- Keep methods focused and concise

### Reporting Issues

If you find a bug or have a feature request:

1. Check existing issues to avoid duplicates
2. Use the issue template if available
3. Provide clear steps to reproduce bugs
4. Include screenshots if applicable
5. Describe the expected behavior

---

## 🗺 Roadmap

- [ ] Add email notifications for bookings
- [ ] Implement refund system
- [ ] Add QR code generation for tickets
- [ ] Integrate with payment gateways (Stripe, PayPal)
- [ ] Add multi-language support
- [ ] Implement real-time seat selection
- [ ] Add mobile app support
- [ ] Implement analytics dashboard
- [ ] Add social media sharing
- [ ] Implement discount/coupon system

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Ticket Booking System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **Spring Team** - For the amazing Spring Boot framework
- **PostgreSQL Community** - For the robust database solution
- **All Contributors** - Thanks to everyone who has contributed to this project
- **Open Source Community** - For the invaluable tools and libraries

---

## 📞 Contact

- **Project Repository**: [https://github.com/yourusername/ticketbookingsystem](https://github.com/yourusername/ticketbookingsystem)
- **Issues**: [https://github.com/yourusername/ticketbookingsystem/issues](https://github.com/yourusername/ticketbookingsystem/issues)
- **Discussions**: [https://github.com/yourusername/ticketbookingsystem/discussions](https://github.com/yourusername/ticketbookingsystem/discussions)

---

<div align="center">

**Built with ❤️ using Spring Boot**

[⬆ Back to Top](#-ticket-booking-system)

</div>
