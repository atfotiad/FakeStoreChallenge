For this Challenge we chose the following architecture: 
with Patterns: 
    MVVM (Model-View-ViewModel)
    Clean Architecture
    Unidirectional Data Flow (UDF)
    Hybrid Approach (Imperative API Call, Reactive UI Updates)

Presentation Layer (UI)
Responsibility: Handles everything related to the user interface.
Components:
    LoginFragment: The UI component responsible for:
    Displaying the login form.
    Collecting user input (username, password).
    Observing the LoginUiState.
    Showing the error dialog.
    Navigating to the next screen upon successful login.
    LoginUiState: A data class that represents the current state of the login screen. It includes:
        username
        password
        isUsernameValid
        isPasswordValid
        isLoading
        isSuccess
        errorMessage
Key Principles:
    Passive View: The Fragment is a passive view. It doesn't contain any business logic. It simply displays data and reacts to user input.
    Observing UiState: The Fragment observes the LoginUiState and updates the UI accordingly.
    Showing Dialogs: The Fragment is responsible for showing dialogs (e.g., error dialogs).
    Navigation: The Fragment handles navigation between screens.
ViewModel Layer
Responsibility: Acts as an intermediary between the UI and the domain/data layers. It prepares data for the UI and handles user interactions.
    Components:
    LoginViewModel: The ViewModel responsible for:
    Holding the LoginUiState.
    Validating user input.
    Making the API call through the UseCase.
    Handling the Result of the API call.
    Updating the LoginUiState based on the Result.
    Saving the token.
    Clearing the error message.
    username and password: MutableStateFlow that holds the username and password.
    isLoginEnabled: LiveData that indicates if the login button is enabled.
Key Principles:
    UI-Agnostic: The ViewModel is UI-agnostic. It doesn't know anything about the Fragment or the UI components.
    Data Preparation: The ViewModel prepares the data for the UI.
    Handling User Input: The ViewModel handles user input and triggers actions in the domain/data layers.
    Exposing UiState: The ViewModel exposes the LoginUiState as a StateFlow.
    No UI logic: The ViewModel does not contain any UI logic.
Domain Layer (Use Cases)
    Responsibility: Contains the business logic of the application.
    Components:
        FakeStoreUseCase: A class that encapsulates the business logic for the login operation.
        It takes a FakeStoreRepository as a dependency.
        It calls the login() function from the FakeStoreRepository.
    Key Principles:
        Independent of Frameworks: The domain layer is independent of any UI frameworks or data sources.
    Business Logic: It contains the core business logic of the application.
    Testable: The domain layer is easily testable.
    Data Layer (Repositories, Data Sources)
        Responsibility: Handles data retrieval and storage.
        Components:
        FakeStoreRepository: An interface that defines the contract for data access.
        FakeStoreRepositoryImpl: An implementation of the FakeStoreRepository interface.
        It takes a FakeStoreService as a dependency.
        It calls the login() function from the FakeStoreService.
        FakeStoreService: An interface that defines the contract for the API service.
        FakeStoreServiceImpl: An implementation of the FakeStoreService interface.
        It uses Retrofit to make the API call.
        RepoUtils: A utility class that contains the toResult() function to handle the API response.
        TokenManager: A class that handles the token.
            Key Principles:
            Data Abstraction: The data layer abstracts the data source from the rest of the application.
            Data Retrieval: It's responsible for retrieving data from various sources (e.g., API, database).
            Data Storage: It's responsible for storing data.
            Testable: The data layer is easily testable.
        Security Layer
            Responsibility: Handles the security of the application.
            Components:
            TokenManager: A class that handles the token.
            Key Principles:
                Security: It's responsible for the security of the application.
                Testable: The security layer is easily testable. Dependency Injection (Hilt)
                We're using Hilt for dependency injection. This makes our code:
                Testable: It's easy to inject mock dependencies for testing.
                Maintainable: It's easy to manage dependencies.
                Scalable: It's easy to add new dependencies. Key Technologies
            Kotlin: The programming language.
            Android Jetpack: A suite of libraries to help build great Android apps.
            MVVM: The architectural pattern.
            Clean Architecture: The architectural principles.
            Retrofit: For making network requests.
            Coroutines: For asynchronous programming.
            Flow: For reactive streams.
            Hilt: For dependency injection.
            MaterialAlertDialogBuilder:  For showing dialogs.
            LiveData: For observing data.
            StateFlow: For observing data. The Flow of Data
            User Input: The user enters their credentials in the LoginFragment.
            ViewModel Action: The LoginFragment calls the login() function in the LoginViewModel.
            API Call: The LoginViewModel calls the login() function in the FakeStoreUseCase.
            Repository: The FakeStoreUseCase calls the login() function in the FakeStoreRepository.
            Service: The FakeStoreRepository calls the login() function in the FakeStoreService.
            Network Request: The FakeStoreService makes the network request using Retrofit.
            Response: The FakeStoreService receives the response from the API.
            Result: The RepoUtils converts the response to a Result object.
            Result Back to ViewModel: The Result object is passed back through the layers to the LoginViewModel.
            ViewModel Handles Result: The LoginViewModel handles the Result (success or error).
            Token Saved: If successful, the LoginViewModel saves the token using the TokenManager.
            UiState Updated: The LoginViewModel updates the LoginUiState based on the Result.
            Fragment Observes UiState: The LoginFragment observes the LoginUiState.
            UI Update: The LoginFragment updates the UI based on the LoginUiState:
            Success: Navigates to the next screen.
            Error: Shows the error dialog. Key Improvements
Separation of Concerns: Each layer has a clear responsibility.
Testability: Each layer is easily testable.
Maintainability: The code is well-structured and easy to maintain.
Scalability: The architecture is scalable.
Reusability: The components are reusable.
No memory leaks: There is no risk of memory leaks. 
Challenges I Overcame Imperative vs. Reactive: I had to carefully balance the imperative nature of the API call