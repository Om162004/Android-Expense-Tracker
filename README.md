# Expense Tracker

An Android-based personal finance application to manage income and expenses.

## Features
- User Registration and Login
- JWT Authentication
- Forgot Password with Email OTP Verification
- Dashboard with Income and Expense Summary
- Add Transactions and View Transaction History
- Search and Date-wise Transaction Grouping
- User Profile and Logout
- REST API Integration
- Local Data Storage using Room Database and LiveData

## Technologies Used
- Android: Java, XML, ViewBinding
- Backend: ASP.NET Core Web API
- Database: SQL Server and Room Database
- API Integration: Retrofit
- Authentication: JWT
- Email Service: MailKit and SMTP

## Setup Instructions

### Prerequisites
- Android Studio
- .NET 9 SDK
- SQL Server

### Backend Setup
1. Open the ExpenseTrackAPI project in Visual Studio.
2. Configure the SQL Server connection string and JWT settings.
3. Configure SMTP email settings for OTP delivery.
4. Apply database migrations:
   `dotnet ef database update`
5. Run the API:
   `dotnet run`

### Android Setup
1. Open the Android project in Android Studio.
2. Sync Gradle dependencies.
3. Update the API base URL in `RetrofitClient.java` to your backend address.
4. Build and run the application.

## Project Status

🚧 **In Progress** — Core features are implemented, and development and testing are ongoing.
