# Shuttle Management System (Backend)
A backend solution for managing shuttle services within a campus. This system allows students and staff to book shuttles, manage user authentication, dynamically discover nearby shuttle options, calculate fares, and manage wallet-based transactions.

## 📌 Features
- User Registration & Authentication (JWT-based)
- Shuttle Booking with Proximity Discovery - Uses the Haversine formula to calculate the nearest available shuttles.
- Fare Calculation - Based on the distance between nearest stops to user's start and end locations.
- Seat Capacity Management - Ensures accurate seat availability during bookings.
- Wallet Integration - Users can recharge their wallet and fare is deducted upon successful booking.
- Trip Completion & History Tracking - Completed trips are scheduled and stored in a trip history database.
- Admin-only Shuttle & Stop Management

## 🧩 System Architecture
### Entity Relationship Diagram (ERD)

```
+----------------+      +----------------+      +----------------+
| User           |      | Shuttle        |      | Stop           |
+----------------+      +----------------+      +----------------+
| id             |      | id             |      | id             |
| fullName       |      | vehicleNumber  |      | name           |
| email          |      | capacity       |      | latitude       |
| password       |      | currentStatus  |      | longitude      |
| role           |      | currentLatitude|      | address        |
| walletBalance  |      | currentLongitude|     +----------------+
| createdAt      |      | avgSpeed       |              ^
| updatedAt      |      +----------------+              |
+----------------+              ^                       |
        ^                       |                       |
        |                       |                       |
        |       +---------------+-------+               |
        |       |                       |               |
        +-------+      Booking         +--------------+
                |                       |
                +-----------------------+
                | id                    |
                | bookingTime           |
                | estimatedEndTime      |
                | travelDate            |
                | fare                  |
                | pointsDeducted        |
                | status                |
                +-----------------------+
```

## ⚙ Technologies Used
- Backend: Java, Spring Boot
- Security: Spring Security, JWT
- Database: MySQL
- Scheduler: Spring's @Scheduled for trip completion
- Distance Calculation: Haversine Formula

## 📁 Project Structure
```
ShuttleManagementSystem/
│
├── controller/ # REST API Controllers
├── dto/ # Data Transfer Objects
├── model/ # JPA Entities
│ ├── User.java # User entity with wallet integration
│ ├── Shuttle.java # Shuttle entity with location tracking
│ ├── Stop.java # Shuttle stop locations
│ ├── Booking.java # Booking records with trip details
│ └── ...
├── repository/ # JPA Repositories
├── service/ # Business Logic
├── config/ # JWT and Security Configurations
└── util/ # Utility classes
```

## 🔄 Code Flow

### 1. User Registration & Authentication Flow
- **User Registration:**
  - User sends registration request to `/api/auth/register` with `UserRegistrationDTO`.
  - `AuthController` receives the request and delegates to `UserService`.
  - `UserService` validates the email (must end with `@gla.ac.in`) and checks for existing users.
  - `UserService` encodes the password using `PasswordEncoder`.
  - `UserService` saves the user to the database via `UserRepository`.
  - `UserService` sets the initial wallet balance to 100.0.
  - `UserService` throws `RuntimeException` for errors.
- **User Login:**
  - User sends login request to `/api/auth/login` with `LoginDTO`.
  - `AuthController` delegates to `UserService`.
  - `UserService` uses `AuthenticationManager` to authenticate the user.
  - On successful authentication, `UserService` calls `UserTokenService` to generate a JWT token.
  - `UserTokenService` uses `JwtTokenUtil` to generate the token.
  - The generated token is returned to the user in a `TokenResponse`.
  - `UserTokenService` throws `RuntimeException` if a given token is expired.
- **Token Validation:**
  - `UserTokenService` is used to validate tokens, and extract user information.

### 2. Shuttle Booking Flow
- **Shuttle Options Retrieval:**
  - User sends a request to `/api/booking/options` with `BookingRequestDTO` containing location data.
  - `BookingController` delegates to `BookingService`.
  - `BookingService` validates the user authentication.
  - `BookingService` retrieves all stops from `StopRepository`.
  - `BookingService` uses helper method `findNearestStop` to determine nearest pickup and destination stops.
  - `BookingService` calculates the route distance using the Haversine formula.
  - `BookingService` retrieves available shuttles from `ShuttleRepository` and filters shuttles based on proximity and capacity.
  - `BookingService` calculates fare estimates and travel times.
  - `BookingService` returns a list of `ShuttleOptionDTO` objects, sorted by cost.
  - `BookingService` throws `RuntimeException` for errors.
- **Booking Confirmation:**
  - User sends a booking confirmation request to `/api/booking/confirm` with `ConfirmBookingRequestDTO`.
  - `BookingController` delegates to `BookingService`.
  - `BookingService` validates the user and shuttle.
  - `BookingService` checks the user's wallet balance.
  - `BookingService` deducts the fare from the user's wallet and updates the user's balance in `UserRepository`.
  - `BookingService` decreases the shuttle capacity and updates the shuttle's status in `ShuttleRepository`.
    - If the capacity becomes zero or less, the shuttle status is set to "UNAVAILABLE".
    - Else the shuttle Status is set to "AVAILABLE".
  - `BookingService` creates a `Booking` record and saves it to `BookingRepository`.
  - `BookingService` returns `ConfirmedBookingResponseDTO` with booking details.
  - `BookingService` throws `RuntimeException` for errors.
- **Trip History:**
  - `BookingService` has a method to retrieve a users trip history, based on the user ID.
  - The data is returned in a list of `TripHistoryDTO`.

### 3. Trip Completion Flow (Scheduled Task)
- **Automatic Trip Completion:**
  - `BookingService` has a scheduled task (`autoCompleteRides`) that runs at fixed intervals (e.g., every minute).
  - `BookingService` retrieves bookings with "CONFIRMED" status from `BookingRepository`.
  - `BookingService` checks if the estimated end time of each booking has passed.
  - If the end time has passed, `BookingService` updates the booking status to "COMPLETED" in `BookingRepository`.
  - `BookingService` retrieves the associated `Shuttle` from `ShuttleRepository`.
  - `BookingService` increments the shuttle's `capacity` by 1.
  - `BookingService` sets the shuttle's `currentStatus` to "AVAILABLE".
  - `BookingService` updates the `Shuttle` record in `ShuttleRepository`.

### 4. Admin Wallet Recharge
- Admin sends a request to `/api/users/recharge` with `WalletRechargeDTO` containing admin ID, user ID, and recharge amount.
- `UserService` validates the admin's role and the existence of the admin and user.
- `UserService` adds the recharge amount to the user's wallet balance and updates the user's record in `UserRepository`.
- `UserService` returns `UserWalletResponseDTO` with the updated wallet balance.
- `UserService` throws `RuntimeException` for errors.

### 5. Stop and Shuttle creation
- Admin sends request to create either a new stop, or a new shuttle.
- `StopService` and `ShuttleService` store new data into their respective repositories.

### 6. User Details Service
- `UserDetailService` is used by Spring security to load user details.
- It retrieves user data from the `UserRepository`.
- It creates a `UserDetails` object.
- It throws `UsernameNotFoundException` if the user is not found.

## 🔐 Authentication
- Users must register and log in to receive a JWT.
- The JWT must be included in the header (`Authorization: Bearer <token>`) for accessing protected endpoints.

## 🔁 Key Endpoints
### 🔑 Authentication
- POST `/api/auth/register` – Register user
- POST `/api/auth/login` – Login and receive JWT
- POST `/api/auth/recharge` – Recharge user wallet

### 🚏 Stops (Admin only)
- POST `/api/stop/create` – Create new stop

### 🚐 Shuttle (Admin only)
- POST `/api/shuttle/create` – Add new shuttle

### 📍 Booking
- POST `/api/booking/options` – Get nearby shuttle options
- POST `/api/booking/confirm` – Confirm a booking
- GET `/api/booking/history` – View trip history

### 💳 Wallet
- GET `/api/user/{userId}/wallet` – Check user balance

## 💾 Database Schema
### User Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| full_name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| role | VARCHAR(255) | NOT NULL |
| wallet_balance | DECIMAL(10,2) | |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

### Shuttle Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| vehicle_number | VARCHAR(255) | NOT NULL, UNIQUE |
| capacity | INT | NOT NULL |
| current_status | VARCHAR(255) | NOT NULL |
| current_latitude | DOUBLE | |
| current_longitude | DOUBLE | |
| avg_speed | DOUBLE | |

### Stop Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(255) | |
| latitude | DOUBLE | |
| longitude | DOUBLE | |
| address | VARCHAR(255) | |

### Booking Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| user_id | BIGINT | FOREIGN KEY (User) |
| start_stop_id | BIGINT | NOT NULL, FOREIGN KEY (Stop) |
| end_stop_id | BIGINT | NOT NULL, FOREIGN KEY (Stop) |
| shuttle_id | BIGINT | NOT NULL, FOREIGN KEY (Shuttle) |
| booking_time | DATETIME | NOT NULL |
| estimated_end_time | DATETIME | |
| travel_date | DATE | NOT NULL |
| fare | DECIMAL(10,2) | NOT NULL |
| points_deducted | DECIMAL(10,2) | NOT NULL |
| status | VARCHAR(255) | NOT NULL |

### Relationships
| Relationship | Type | Description |
|--------------|------|-------------|
| User → Booking | One-to-Many | A user can have multiple bookings |
| Shuttle → Booking | One-to-Many | A shuttle can be used for multiple bookings |
| Stop → Booking (as start) | One-to-Many | A stop can be the starting point for multiple bookings |
| Stop → Booking (as end) | One-to-Many | A stop can be the destination for multiple bookings |

## 🧠 Advanced Logic
### 🛰 Shuttle Discovery
- Applies Haversine Formula to calculate distance between user's start location and shuttle stops.

### 💲 Fare Calculation
- Trip fare is calculated based on the distance between the nearest stops to the user's start and end locations.
- System identifies the closest shuttle stops to the user's specified locations using the Haversine formula.

### 📦 Scheduled Tasks
- Trip completion is handled via a Spring @Scheduled task that:
  - Marks trips as completed
  - Updates seat availability
  - Logs trips in history

## 🚀 Future Enhancements
- Dynamic admin dashboard
- Integration with real-time GPS tracking
- Route optimization based on traffic and distance
- SMS/Email notifications
- Google Maps route display (via frontend layer)
 
