# 🕌 Islami App

A native Android Islamic application built with **Kotlin** and traditional Android Views.

The application provides access to Quran content, Hadith, Tasbeh functionality, and Islamic radio through a tab-based user interface.

## 🚀 Features

* 📖 Quran section
* 📚 Hadith section
* 📿 Tasbeh counter
* 📻 Islamic Radio section
* 🏠 Tab-based home screen
* 🎬 Splash screen
* 📱 Native Android UI
* 🔗 ViewBinding
* 📦 Kotlin Parcelize
* 🎨 Material Components

---

## 📖 Quran

The application includes a dedicated Quran section where users can browse Quran-related content.

The Quran feature is isolated in its own UI package to keep the application structure organized.

```text
Home
 ↓
Quran Tab
 ↓
Quran Content
```

---

## 📚 Hadith

A separate Hadith section provides Islamic Hadith content through its own dedicated feature package.

```text
Home
 ↓
Hadith Tab
 ↓
Hadith Content
```

---

## 📿 Tasbeh

The Tasbeh feature provides a simple digital counter experience for dhikr.

It is implemented as a separate tab inside the application's main navigation.

---

## 📻 Islamic Radio

The application also includes a Radio section designed for Islamic radio content.

The radio feature is organized independently from the Quran, Hadith, and Tasbeh sections.

---

## 🏠 Home Navigation

The application's main screen organizes the primary features into separate tabs:

```text
HomeActivity
│
├── Quran
├── Hadith
├── Radio
└── Tasbeh
```

This keeps the major sections easily accessible from one screen.

---

## 🏗️ Application Structure

The project follows a simple Android structure separating models from UI functionality.

```text
Application
   ↓
UI Layer
   ↓
Feature Screens
   ↓
Models / Local Content
```

The project is organized around feature-specific UI packages.

---

## 📂 Project Structure

```text
com.example.islamiapp/
│
├── model/
│   └── Application data models
│
└── ui/
    ├── Constants.kt
    │
    ├── splash/
    │
    └── home/
        ├── HomeActivity.kt
        │
        └── tabs/
            ├── quran/
            ├── hadeth/
            ├── radio/
            └── tasbeh/
```

---

## 🛠️ Tech Stack

### Language

* **Kotlin**

### Android

* Android SDK
* XML Layouts
* ViewBinding
* Material Components
* ConstraintLayout

### Kotlin

* Kotlin Parcelize

### Testing

* JUnit
* AndroidX Test
* Espresso

---

## 🎨 UI

The application uses the traditional Android View system.

Main UI technologies include:

* XML layouts
* ViewBinding
* Material Components
* ConstraintLayout

This project focuses on Android UI fundamentals and feature-based screen organization.

---

## 🚀 Getting Started

### Prerequisites

You will need:

* Android Studio
* Android SDK
* JDK
* Android device or emulator

---

### Clone the Repository

```bash
git clone -b development https://github.com/MohamedAbbas289/Islami-app.git
cd Islami-app
```

The main application implementation is available on the:

```text
development
```

branch.

---

## 🔨 Build

Linux / macOS:

```bash
./gradlew assembleDebug
```

Windows:

```bash
gradlew.bat assembleDebug
```

Or open the project in Android Studio and run the `app` module.

---

## 🧪 Testing

Run tests using:

```bash
./gradlew test
```

---

## 🎯 Project Purpose

This project was built to practice Android development concepts including:

* Kotlin
* XML UI development
* ViewBinding
* Activities and Fragments
* Feature-based UI organization
* RecyclerView-style content presentation
* Tab-based navigation
* Android project structure

It also demonstrates building a multi-section application around Quran, Hadith, Tasbeh, and Radio functionality.

---

## 👨‍💻 Developer

**Mohamed Ibrahim Abbas**

Android & Kotlin Multiplatform Developer

GitHub: `MohamedAbbas289`
