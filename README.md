# Mini Grocery Delivery App 🍎🥦

A modern, clean, and functional Android application built with **XML Views**, **View Binding**, and **MVVM Architecture** as part of the OceanX Agency Internship Assignment.

## 🚀 Features

- **Authentication:** Simple Login Screen to start the app flow.
- **Product Discovery:** Home screen featuring a categorized list of fresh grocery products.
- **Detailed View:** Product Detail Screen with rich descriptions and quantity management.
- **Cart Management:** Dynamic Cart Screen using **Room Database** for persistence, allowing real-time quantity updates.
- **Order Flow:** Seamless Checkout process with address management and order success confirmation.
- **Profile & Settings:** User profile editing and dark mode support via `PreferenceManager`.
- **Responsive UI:** Built with Material 3 components and optimized for various screen sizes.

## 🛠️ Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** XML Views & Material 3
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** [Room](https://developer.android.com/training/data-storage/room) (SQLite)
- **Navigation:** [Jetpack Navigation Component](https://developer.android.com/guide/navigation)
- **Asynchronous Logic:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- **Dependency Management:** Gradle Version Catalog (libs.versions.toml)

## 📁 Project Structure

```text
com.example.minigrocerydeliveryapp
│
├── data/
│   ├── local/                     # Room Database, DAOs, and Entities
│   ├── PreferenceManager.kt       # SharedPrefs for User & Theme settings
│   └── ProductRepository.kt       # In-memory product data
├── model/                         # Domain models (Product, Order, UserProfile)
├── ui/
│   ├── adapters/                  # RecyclerView Adapters for Lists (Products, Cart, Orders)
│   ├── fragments/                 # UI Controllers (Home, Cart, Checkout, Profile, etc.)
│   └── navigation/                # Navigation-related helpers (if any)
├── viewmodel/
│   └── GroceryViewModel.kt        # Centralized business logic & state management
└── MainActivity.kt                # App Entry Point & Navigation Host
```

## 📸 Demo
*(Include your screen recording link or GIF here)*

## 🛠️ How to Run
1. Clone the repository.
2. Open the project in **Android Studio (Hedgehog or newer)**.
3. Sync Gradle and run on an Emulator or Physical device (API 24+).

---

## 👨‍💻 Submission for OceanX Agency
- **GitHub Repository:** [https://github.com/codingWiz-rick/MiniGroceryDeliveryApp](https://github.com/codingWiz-rick/MiniGroceryDeliveryApp)
- **Developer:** Shubham Chakraborty
