# Internshala Projects

**A Modern Android Hotel Discovery Application**

[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-3DDC84?logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?logo=firebase&logoColor=black)](https://firebase.google.com/)
[![API](https://img.shields.io/badge/API-Android-green)](https://developer.android.com/)

---

## 📱 Overview

Internshala Projects is a feature-rich Android application that helps users discover hotels near their location or in any city. Built with modern Android development best practices using Jetpack Compose, the app integrates multiple APIs and Firebase services to provide a seamless hotel search and browsing experience.

The app supports multiple authentication methods and fetches real-time hotel data from OpenTripMap and Foursquare APIs, displaying comprehensive information including ratings, prices, distance, and categories.

---

## ✨ Features

### 🔐 Authentication System
- **Phone Number Authentication** - OTP-based login using Firebase Auth
- **Email/Password Authentication** - Traditional email sign-up and login
- **Google Sign-In** - One-tap Google authentication using Credential Manager
- **Firebase Realtime Database** - User data storage and management

### 🏨 Hotel Discovery
- **Location-Based Search** - Find hotels near you using OpenTripMap API
- **City Search** - Search hotels in any city by name
- **Dual Data Sources**:
  - Static sample hotel data for demonstration
  - Real-time API data from OpenTripMap
- **Detailed Hotel Cards** displaying:
  - Hotel name and category
  - Star ratings
  - Distance from search location
  - Interactive cards with click actions

### 🎨 Modern UI/UX
- **Jetpack Compose** - Declarative UI toolkit
- **Material Design 3** - Latest Material components
- **Lazy Vertical Grid** - Responsive grid layout for hotel cards
- **Navigation Component** - Seamless screen transitions
- **Edge-to-Edge** - Immersive full-screen experience
- **Loading & Error States** - Comprehensive state handling
- **Promotional Banner** - Dynamic offer screen

### 🔧 Technical Features
- **MVVM Architecture** - Clean separation of concerns
- **StateFlow & LiveData** - Reactive state management
- **Coroutines** - Async operations with proper error handling
- **Retrofit + OkHttp** - Type-safe HTTP client with logging
- **Kotlin Serialization** - Efficient JSON parsing
- **Coil** - Image loading library for Compose
- **ViewModel** - UI state preservation

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Navigation** | Navigation Compose |
| **Authentication** | Firebase Auth, Google Identity Services |
| **Database** | Firebase Realtime Database |
| **Networking** | Retrofit 2, OkHttp 3, Gson |
| **Image Loading** | Coil Compose |
| **Maps/Location** | OpenTripMap API, OSMDroid |
| **Build System** | Gradle (KTS) |
| **Minimum SDK** | Android 10 (API 29) |
| **Target SDK** | Android 15 (API 36) |

---

## 📂 Project Structure

```
app/src/main/java/com/example/internshalaprojects/
├── MainActivity.kt                    # Main activity and UI composition
├── AppViewModel.kt                    # ViewModel with business logic
├── data/
│   ├── Hotel.kt                      # Hotel data model
│   ├── Internetitem.kt               # Data model for Firebase
│   └── ListOfHotels.kt               # Static sample data
├── network/
│   ├── foursquareAPI.kt              # Foursquare API interface (unused)
│   ├── OpenTripMapApi.kt             # OpenTripMap API client
│   └── OtmProperties.kt              # API response models
├── otpScreens/
│   └── sendOTPScreen.kt              # OTP authentication UI
├── ui/
│   └── theme/                        # Material 3 theme configuration
└── NavigationController.kt           # Navigation setup
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Arctic Fox or newer)
- **JDK 11** or higher
- **Android SDK** with API 36
- **Git**

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/internshala-projects.git
   cd internshala-projects
   ```

2. **Configure API Keys**

   Open `local.properties` in the project root and add your OpenTripMap API key:
   ```properties
   OPEN_TRIP_MAP_API_KEY=your_api_key_here
   ```

   To get an API key:
   - Visit [OpenTripMap](https://opentripmap.com/) and sign up for a free account
   - Get your API key from the dashboard

3. **Firebase Configuration**

   This project requires Firebase services. To set up:

   a. Create a new project in [Firebase Console](https://console.firebase.google.com/)

   b. Enable the following services:
      - Authentication (Phone, Email/Password, Google)
      - Realtime Database
      - Analytics

   c. Download `google-services.json` and place it in `app/` directory

   d. Update the Google Sign-In Client ID in `AppViewModel.kt`:
      ```kotlin
      setServerClientId("YOUR_CLIENT_ID.apps.googleusercontent.com")
      ```

4. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Connect an Android device or start an emulator (API 29+)
   - Click **Run** or press `Shift+F10`

---

## 🔑 Required API Keys & Configuration

| Service | Purpose | Setup |
|---------|---------|-------|
| **OpenTripMap API** | Hotel geolocation and search | Get free key from [opentripmap.com](https://opentripmap.com) |
| **Firebase** | Authentication & Database | Enable services in Firebase Console |
| **Google Sign-In** | Google OAuth | Configure OAuth 2.0 in Google Cloud Console |

---

## 📱 Screenshots

| Home Screen | Hotel Search | Authentication |
|-------------|--------------|----------------|
| *Home with offers* | *Results grid* | *OTP/Email login* |
| *Sample hotels* | *Loading states* | *Google Sign-In* |

---

## 🏗️ Architecture

This application follows **MVVM (Model-View-ViewModel)** architecture:

- **Model**: Data classes (`Hotel`, `OtmProperties`, `Internetitem`) and repository pattern
- **View**: Composable functions in `MainActivity.kt` and screen files
- **ViewModel**: `AppViewModel` holds UI state and business logic

### State Management
- **UI State**: Exposed via `StateFlow` for Compose UI
- **One-time Events**: Use `LiveData` for navigation/toasts
- **Separation of Concerns**: Network, database, and UI logic are decoupled

---

## 📦 Dependencies

Key libraries used:
- **AndroidX Core KTX** - Kotlin extensions
- **Activity Compose** - Compose integration
- **Compose BOM** - Bill of Materials for consistent versions
- **Material 3** - Modern design system
- **Navigation Compose** - In-app navigation
- **Lifecycle ViewModel** - ViewModel support
- **Retrofit** - HTTP client
- **OkHttp Logging** - Debug network calls
- **Kotlinx Serialization** - JSON parsing
- **Coil Compose** - Image loading
- **Firebase BOM** - Firebase BoM for version management
  - Firebase Auth
  - Firebase Database
  - Firebase Analytics
- **Google Play Services Auth** - Google Sign-In

---

## 🧪 Testing

The project includes basic instrumentation tests:
- `ExampleInstrumentedTest.kt` - Sample Android test

To run tests:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 🔧 Build Configuration

- **Build Type**: Debug/Release
- **Minify**: Disabled for debug builds
- **ProGuard**: Optimized rules configured for release
- **BuildConfig Fields**: API keys injected at build time

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **OpenTripMap** - Free geolocation and POI API
- **Firebase** - Backend-as-a-Service platform
- **Google** - Material Design and Android platform
- **JetBrains** - Kotlin programming language

---

## 📧 Contact

For questions, suggestions, or contributions, please open an issue or reach out:

- **GitHub Issues**: [Create an issue](https://github.com/yourusername/internshala-projects/issues)
- **Email**: your.email@example.com

---

## 🚧 Future Enhancements

- [ ] Add map view with hotel locations
- [ ] Implement hotel booking functionality
- [ ] Add reviews and ratings from users
- [ ] Support multiple languages (i18n)
- [ ] Add favorites/wishlist feature
- [ ] Implement offline caching with Room DB
- [ ] Add push notifications for deals
- [ ] Payment gateway integration

---

**Made with ❤️ using Kotlin & Jetpack Compose**