# 🏗️ MVVM Clean Architecture — Android Template

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024.12-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen)](https://developer.android.com/about/versions/nougat)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A **production-ready** Android project template implementing **MVVM + Clean Architecture** with multi-module setup, offline support, encrypted storage, and type-safe navigation.

Built to be a **starting point** for scalable Android apps — just clone, customize, and ship.

---

## ✨ Features

| Feature | Implementation |
|---|---|
| 🏛️ **Clean Architecture** | Data → Domain → Presentation per feature module |
| 🧩 **Multi-Module** | 7 modules with clear dependency boundaries |
| 🧭 **Type-Safe Navigation** | `@Serializable` routes (Navigation 2.8+) |
| 📡 **Offline Mode** | `ConnectivityMonitor` + `OfflineBanner` + cache strategies |
| 🔐 **Encrypted Storage** | Tink AES256-GCM + Proto DataStore |
| 💾 **Local Database** | Room with Flow-based reactive queries |
| 🌐 **HTTP Client** | Ktor 3.x + OkHttp engine + auto error mapping |
| 💉 **Dependency Injection** | Koin 4.x with per-layer modules |
| 🎨 **Material3 Theme** | Full light/dark palette + dynamic color (Android 12+) |
| 🛡️ **Proguard Ready** | Consumer rules for Tink, Ktor, Room |
| ⚙️ **Convention Plugins** | Centralized Gradle config via `buildSrc` |

---

## 🏛️ Architecture

```
┌──────────────────────────────────────────────────────┐
│                     app                               │
│  MainActivity · NavHost · Koin Setup                  │
├──────────────────────────────────────────────────────┤
│                 feature:auth                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐   │
│  │   Data   │→ │  Domain  │← │  Presentation    │   │
│  │ RemoteDS │  │ UseCase  │  │ ViewModel+Screen │   │
│  │ LocalDS  │  │ Model    │  │ UiState+Event    │   │
│  │ RepoImpl │  │ Repo(IF) │  │                  │   │
│  └──────────┘  └──────────┘  └──────────────────┘   │
├──────────────────────────────────────────────────────┤
│                   core modules                        │
│  ┌────────┐ ┌─────────┐ ┌────┐ ┌──────────┐ ┌────┐  │
│  │  base  │ │ network │ │ ui │ │ security │ │ db │  │
│  └────────┘ └─────────┘ └────┘ └──────────┘ └────┘  │
└──────────────────────────────────────────────────────┘
```

### Module Responsibilities

| Module | Purpose | Key Classes |
|---|---|---|
| `app` | Entry point, navigation, DI wiring | `MainActivity`, `AppNavGraph`, `AppRoutes` |
| `core:base` | Foundation classes | `BaseViewModel`, `UseCase`, `Result`, `AppError` |
| `core:network` | HTTP client & connectivity | `KtorClient`, `SafeApiCall`, `ConnectivityMonitor` |
| `core:ui` | Design system & components | `Theme`, `Color`, `Typography`, `OfflineBanner` |
| `core:security` | Encrypted preferences | `CryptoManager`, `SecurePreferencesManager` |
| `core:database` | Room database | `AppDatabase`, `UserDao`, `UserEntity` |
| `feature:auth` | Authentication feature | `LoginScreen`, `LoginViewModel`, `LoginUseCase` |

---

## 🛠️ Tech Stack

| Category | Library | Version |
|---|---|---|
| Language | Kotlin | 2.1.0 |
| UI | Jetpack Compose (BOM) | 2024.12.01 |
| Navigation | Navigation Compose | 2.8.4 |
| HTTP | Ktor + OkHttp | 3.0.2 |
| DI | Koin | 4.0.0 |
| Database | Room | 2.6.1 |
| Encryption | Tink | 1.16.0 |
| DataStore | Proto DataStore | 1.1.1 |
| Serialization | kotlinx.serialization | 1.7.3 |
| Coroutines | kotlinx.coroutines | 1.9.0 |
| Build | AGP | 8.7.0 |
| Min SDK | | 24 (Android 7.0) |
| Compile SDK | | 35 |

---

## 📁 Project Structure

```
MVVMCleanArchitecture/
├── app/                              # Application module
│   └── src/main/java/.../
│       ├── MainActivity.kt           # Scaffold + OfflineBanner + NavHost
│       ├── MainApplication.kt        # Koin DI initialization
│       └── navigation/
│           ├── AppRoutes.kt          # @Serializable route objects
│           └── AppNavGraph.kt        # Type-safe NavHost
│
├── core/
│   ├── base/                         # Foundation (no Android UI deps)
│   │   ├── BaseViewModel.kt          # StateFlow + SharedFlow + coroutine helpers
│   │   ├── UiState.kt                # UiState & UiEvent interfaces
│   │   ├── UseCase.kt                # UseCase, NoParamUseCase, FlowUseCase
│   │   └── wrapper/
│   │       ├── Result.kt             # Success / Error / Loading
│   │       └── AppError.kt           # 12 error subtypes
│   │
│   ├── network/                      # HTTP & connectivity
│   │   ├── KtorClient.kt             # HttpClient factory
│   │   ├── SafeApiCall.kt            # Error mapping + retry
│   │   ├── connectivity/
│   │   │   └── ConnectivityMonitor.kt # NetworkCallback-based monitoring
│   │   └── di/NetworkModule.kt
│   │
│   ├── ui/                           # Design system
│   │   ├── theme/
│   │   │   ├── Color.kt              # Material3 light/dark palette
│   │   │   ├── Theme.kt              # MaterialTheme config
│   │   │   └── Type.kt               # Typography + Spacing
│   │   └── component/
│   │       └── OfflineBanner.kt      # Animated offline indicator
│   │
│   ├── security/                     # Encrypted storage
│   │   ├── CryptoManager.kt          # Tink AES256-GCM
│   │   ├── AuthPreferencesSerializer.kt
│   │   ├── SecurePreferencesManager.kt
│   │   ├── di/SecurityModule.kt
│   │   └── proto/auth_prefs.proto
│   │
│   └── database/                     # Room persistence
│       ├── AppDatabase.kt
│       ├── dao/UserDao.kt
│       ├── entity/UserEntity.kt
│       └── di/DatabaseModule.kt
│
├── feature/
│   └── auth/                         # Auth feature (Clean Architecture)
│       ├── data/
│       │   ├── remote/               # RemoteDataSource, DTOs, Mappers
│       │   ├── local/                # LocalDataSource (Room + DataStore)
│       │   └── repository/           # AuthRepositoryImpl (cache strategies)
│       ├── domain/
│       │   ├── model/User.kt
│       │   ├── repository/AuthRepository.kt (interface)
│       │   └── usecase/              # LoginUseCase, ObserveCurrentUserUseCase
│       ├── presentation/
│       │   ├── screen/LoginScreen.kt
│       │   ├── viewmodel/LoginViewModel.kt
│       │   └── state/LoginUiState.kt
│       └── di/AuthModule.kt
│
├── buildSrc/                         # Convention plugins
│   ├── AndroidApplicationConventionPlugin.kt
│   ├── AndroidLibraryConventionPlugin.kt
│   └── AndroidLibraryComposeConventionPlugin.kt
│
└── gradle/
    └── libs.versions.toml            # Centralized version catalog
```

---

## 🎯 Design Patterns

### UseCase Pattern
```kotlin
// Parameterized UseCase
abstract class UseCase<in P, out R> {
    abstract suspend operator fun invoke(params: P): Result<R>
}

// Usage — wrap params in data class
data class LoginParams(val email: String, val password: String)

class LoginUseCase(private val repo: AuthRepository) : UseCase<LoginParams, User>() {
    override suspend fun invoke(params: LoginParams): Result<User> {
        // Validation + business logic here
        return repo.login(params.email, params.password)
    }
}
```

### BaseViewModel Pattern
```kotlin
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginUiState, LoginEvent>(LoginUiState()) {

    fun onLoginClicked() {
        launchWithLoading(
            setLoading = { copy(isLoading = it) },
            setError = { copy(error = it) },
            onSuccess = { user -> sendEvent(LoginEvent.NavigateToHome) }
        ) {
            loginUseCase(LoginParams(currentState.email, currentState.password))
        }
    }
}
```

### Type-Safe Navigation (Nav 2.8+)
```kotlin
// Routes as @Serializable objects
@Serializable object Login
@Serializable object Home
@Serializable data class Detail(val id: String)

// Usage
composable<AppRoutes.Login> { LoginScreen(...) }
navController.navigate(AppRoutes.Home)
```

### Offline Cache Strategy
```kotlin
// NETWORK ONLY — login, register
// CACHE FIRST — getCurrentUser (try cache → refresh from network)
// CACHE ONLY — observeCurrentUser (Room Flow, works offline)
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2+)
- JDK 17+
- Android SDK 35

### Setup
```bash
# Clone
git clone https://github.com/user/MVVMCleanArchitecture.git
cd MVVMCleanArchitecture

# Open in Android Studio → Sync Gradle → Run
```

### Customization Checklist

1. **API Base URL** — Update in `NetworkModule.kt`
2. **Colors** — Edit `core/ui/theme/Color.kt` (look for `TODO`)
3. **Package name** — Rename via Android Studio refactor
4. **App name** — Update `strings.xml`

---

## 🗺️ Roadmap

- [ ] Register & Forgot Password screens
- [ ] Ktor Auth plugin (automatic token refresh)
- [ ] Unit tests & UI tests
- [ ] Navigation animations
- [ ] Splash / onboarding screen
- [ ] CI/CD pipeline (GitHub Actions)

---

## 📄 License

```
MIT License

Copyright (c) 2026 Elan Yudho

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
