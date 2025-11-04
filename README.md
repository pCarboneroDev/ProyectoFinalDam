# 🚀 Android Guitar Tuner

App for real-time audio analysis for pitch detection. Built with Kotlin and Jetpack Compose

---

# Architecture
The application follows the design pattern known as "Clean Architecture". Below we will detail the folder structure chosen for this architecture:

Data: Contains the folders and files related to external data sources, such as Firebase or local databases. It also contains the implementation of the repositories defined in the domain layer.

Domain (Domain layer): Includes the models used exclusively in the views. This layer represents the entities and business rules of the application.
It also contains the use cases: responsible for acting as intermediaries between the ViewModels and data sources. Domain also contains the repository interfaces that define the available actions.

Di (Dependency Injection): Groups the modules responsible for providing the necessary dependencies for each class through dependency injection.

Features: Represents each functionality or section of the app. Each feature includes its view, ViewModel, and reusable components.

Utils: Stores utility classes that can be used in different parts of the application.

# How pitch detection works
The app implements the YIN algorithm for fundamental frequency detection. Here's how it works:

Audio Input: The app captures real-time audio input from the device's microphone

Pre-processing: Audio signals are processed through a series of steps:

Difference Function: Computes the difference between the signal and its delayed version

Cumulative Mean Normalized Difference: Creates a function that emphasizes periodicity

Absolute Threshold: Sets a threshold to find the first minimum below it

Peak Detection: Identifies the fundamental frequency by finding the period with the lowest difference value

Frequency Conversion: Converts the detected period into frequency (Hz) for musical note identification

The YIN algorithm is particularly effective for guitar tuning as it handles the harmonic nature of string instruments well and provides accurate pitch detection even in noisy environments.

⚙️ Technologies Used

## ⚙️ Technologies used

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/compose)
- [SQLite](https://sqlite.org/)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419) (For dependency injection)
- [Room](https://sqlite.org/) (For local database management)
  
