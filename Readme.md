# Fake Store Challenge: App Architecture

This document outlines the architecture of the Fake Store Challenge application, highlighting the chosen patterns, layers, components, and key principles.

## Architectural Patterns

We've implemented a robust architecture leveraging the following patterns:

*   **MVVM (Model-View-ViewModel):** Separates the UI from the business logic, promoting testability and maintainability.
*   **Clean Architecture:** Organizes the code into distinct layers, ensuring independence and flexibility.
*   **Unidirectional Data Flow (UDF):** Data flows in a single direction, simplifying state management and debugging.
*   **Hybrid Approach (Imperative API Call, Reactive UI Updates):** Combines the imperative nature of API calls with the reactive nature of UI updates using Kotlin Flow.

## Layers

The application is structured into the following distinct layers:

### 1. Presentation Layer (UI)

**Responsibility:** Handles all aspects related to the user interface.

**Components:**

*   **`LoginFragment`:**
    *   Displays the login form.
    *   Collects user input (username, password).
    *   Observes the `LoginUiState`.
    *   Shows error dialogs.
    *   Navigates to the next screen upon successful login.
*   **`LoginUiState`:**
    *   A data class representing the current state of the login screen.
    *   **Properties:**
        *   `username`: The current username.
        *   `password`: The current password.
        *   `isUsernameValid`: Indicates if the username is valid.
        *   `isPasswordValid`: Indicates if the password is valid.
        *   `isLoading`: Indicates if a login operation is in progress.
        *   `isSuccess`: Indicates if the login was successful.
        *   `errorMessage`: Contains an error message if the login failed.

**Key Principles:**

*   **Passive View:** The `LoginFragment` is a passive view, containing no business logic.
*   **Observing `UiState`:** The `LoginFragment` observes the `LoginUiState` and updates the UI accordingly.
*   **Showing Dialogs:** The `LoginFragment` is responsible for displaying dialogs (e.g., error dialogs) using `MaterialAlertDialogBuilder`.
*   **Navigation:** The `LoginFragment` handles navigation between screens.

### 2. ViewModel Layer

**Responsibility:** Acts as an intermediary between the UI and the domain/data layers. It prepares data for the UI and handles user interactions.

**Components:**

*   **`LoginViewModel`:**
    *   Holds the `LoginUiState`.
    *   Validates user input.
    *   Makes the API call through the `FakeStoreUseCase`.
    *   Handles the `Result` of the API call.
    *   Updates the `LoginUiState` based on the `Result`.
    *   Saves the token using `TokenManager`.
    *   Clears the error message.
    *   **Properties:**
        *   `username`: `MutableStateFlow` holding the username.
        *   `password`: `MutableStateFlow` holding the password.
        *   `isLoginEnabled`: `LiveData` indicating if the login button is enabled.

**Key Principles:**

*   **UI-Agnostic:** The `LoginViewModel` is UI-agnostic and doesn't know about the `LoginFragment` or UI components.
*   **Data Preparation:** The `LoginViewModel` prepares data for the UI.
*   **Handling User Input:** The `LoginViewModel` handles user input and triggers actions in the domain/data layers.
*   **Exposing `UiState`:** The `LoginViewModel` exposes the `LoginUiState` as a `StateFlow`.
*   **No UI Logic:** The `LoginViewModel` does not contain any UI logic.

### 3. Domain Layer (Use Cases)

**Responsibility:** Contains the core business logic of the application.

**Components:**

*   **`FakeStoreUseCase`:**
    *   Encapsulates the business logic for the login operation.
    *   Takes a `FakeStoreRepository` as a dependency.
    *   Calls the `login()` function from the `FakeStoreRepository`.

**Key Principles:**

*   **Independent of Frameworks:** The domain layer is independent of any UI frameworks or data sources.
*   **Business Logic:** It contains the core business logic of the application.
*   **Testable:** The domain layer is easily testable.

### 4. Data Layer (Repositories, Data Sources)

**Responsibility:** Handles data retrieval and storage.

**Components:**

*   **`FakeStoreRepository`:**
    *   An interface defining the contract for data access.
*   **`FakeStoreRepositoryImpl`:**
    *   An implementation of the `FakeStoreRepository` interface.
    *   Takes a `FakeStoreService` as a dependency.
    *   Calls the `login()` function from the `FakeStoreService`.
*   **`FakeStoreService`:**
    *   An interface defining the contract for the API service.
*   **`FakeStoreServiceImpl`:**
    *   An implementation of the `FakeStoreService` interface.
    *   Uses Retrofit to make the API call.
*   **`RepoUtils`:**
    *   A utility class containing the `toResult()` function to handle API responses.
*   **`TokenManager`:**
    *   A class that handles the token.

**Key Principles:**

*   **Data Abstraction:** The data layer abstracts the data source from the rest of the application.
*   **Data Retrieval:** It's responsible for retrieving data from various sources (e.g., API).
*   **Data Storage:** It's responsible for storing data.
*   **Testable:** The data layer is easily testable.

### 5. Security Layer

**Responsibility:** Handles the security of the application.

**Components:**

*   **`TokenManager`:**
    *   A class that handles the token.

**Key Principles:**

*   **Security:** It's responsible for the security of the application.
*   **Testable:** The security layer is easily testable.

## Dependency Injection (Hilt)

We're using Hilt for dependency injection. This makes our code:

*   **Testable:** It's easy to inject mock dependencies for testing.
*   **Maintainable:** It's easy to manage dependencies.
*   **Scalable:** It's easy to add new dependencies.

## Key Technologies

*   **Kotlin:** The programming language.
*   **Android Jetpack:** A suite of libraries to help build great Android apps.
*   **MVVM:** The architectural pattern.
*   **Clean Architecture:** The architectural principles.
*   **Retrofit:** For making network requests.
*   **Coroutines:** For asynchronous programming.
*   **Flow:** For reactive streams.
*   **Hilt:** For dependency injection.
*   **MaterialAlertDialogBuilder:** For showing dialogs.
*   **LiveData:** For observing data.
*   **StateFlow:** For observing data.

## Flow of Data (Login Process)

1.  **User Input:** The user enters their credentials in the `LoginFragment`.
2.  **ViewModel Action:** The `LoginFragment` calls the `login()` function in the `LoginViewModel`.
3.  **API Call:** The `LoginViewModel` calls the `login()` function in the `FakeStoreUseCase`.
4.  **Repository:** The `FakeStoreUseCase` calls the `login()` function in the `FakeStoreRepository`.
5.  **Service:** The `FakeStoreRepository` calls the `login()` function in the `FakeStoreService`.
6.  **Network Request:** The `FakeStoreService` makes the network request using Retrofit.
7.  **Response:** The `FakeStoreService` receives the response from the API.
8.  **Result:** The `RepoUtils` converts the response to a `Result` object.
9.  **Result Back to ViewModel:** The `Result` object is passed back through the layers to the `LoginViewModel`.
10. **ViewModel Handles Result:** The `LoginViewModel` handles the `Result` (success or error).
11. **Token Saved:** If successful, the `LoginViewModel` saves the token using the `TokenManager`.
12. **`UiState` Updated:** The `LoginViewModel` updates the `LoginUiState` based on the `Result`.
13. **Fragment Observes `UiState`:** The `LoginFragment` observes the `LoginUiState`.
14. **UI Update:** The `LoginFragment` updates the UI based on the `LoginUiState`:
    *   **Success:** Navigates to the next screen.
    *   **Error:** Shows the error dialog.

## Key Improvements

*   **Separation of Concerns:** Each layer has a clear and distinct responsibility, making the code easier to understand, maintain, and modify.
*   **Testability:** Each layer is designed to be easily testable, allowing for thorough unit and integration testing.
*   **Maintainability:** The code is well-structured and easy to maintain.
*   **Scalability:** The architecture is scalable.
*   **Reusability:** The components are reusable.
*   **No memory leaks:** There is no risk of memory leaks.
*   **Challenges I Overcame Imperative vs. Reactive:** I had to carefully balance the imperative nature of the API call