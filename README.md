# Mini Grocery Delivery App 🍎🥦

A modern, clean, and functional Android application built with **Jetpack Compose** and **MVVM Architecture** as part of the OceanX Agency Internship Assignment.

## 🚀 Features

- **Authentication:** Simple Login Screen to start the app flow.
- **Product Discovery:** Home screen featuring a grid of fresh grocery products with categories.
- **Detailed View:** Product Detail Screen with large imagery, full description, and quantity selection.
- **Cart Management:** Dynamic Cart Screen where users can increase/decrease quantities or remove items in real-time.
- **Order Flow:** Seamless Checkout process with address validation and an Order Success confirmation.
- **Responsive UI:** Built with Material 3 components and optimized for various screen sizes.
- **State Management:** Uses Kotlin Coroutines and StateFlow for reactive UI updates.

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Navigation:** Jetpack Compose Navigation
- **Asynchronous Logic:** Kotlin Coroutines & StateFlow
- **Dependency Management:** Gradle Version Catalog (libs.versions.toml)

## 📁 Project Structure

```text
com.example.minigrocerydeliveryapp
│
├── data/
│   └── ProductRepository.kt       # Local dummy data source
├── model/
│   └── Product.kt                 # Data classes (Product, CartItem)
├── ui/
│   ├── components/                # Reusable UI molecules (ProductCard, CartItemRow)
│   ├── navigation/
│   │   └── Screen.kt              # Type-safe Navigation routes
│   ├── screens/                   # Full-screen Compose UI
│   │   ├── LoginScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── ProductDetailScreen.kt
│   │   ├── CartScreen.kt
│   │   └── CheckoutScreen.kt
│   └── theme/                     # App styling (Colors, Type, Theme)
├── viewmodel/
│   └── GroceryViewModel.kt        # Business logic & State management
└── MainActivity.kt                # App Entry Point & Navigation Host
```

## 📸 Screenshots / Demo
*(Include your screen recording link or GIF here)*

## 🛠️ How to Run
1. Clone the repository.
2. Open the project in **Android Studio (Hedgehog or newer)**.
3. Sync Gradle and run on an Emulator or Physical device (API 24+).

---

## 👨‍💻 Submission for OceanX Agency
- **GitHub Repository:** [Link to Repo]
- **Developer:** [Your Name]
