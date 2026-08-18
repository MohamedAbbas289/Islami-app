# Islami App

A native Android Islamic app built with Kotlin, XML layouts, ViewBinding, MVVM, and repository-based
clean architecture.

## Features

- Quran chapter catalog with continuous Mushaf-style text and Arabic verse numbers
- Hadeth catalog and details from bundled assets
- Tasbeh counter with state restoration
- Live Quran radio stations from `https://mp3quran.net/api/v3/radios`
- Media3 radio streaming with previous, play/pause, next, buffering, retry, and a scrollable station
  picker
- Background radio playback through `MediaSessionService`, including system notification,
  lock-screen, headset, and external media controls
- Persistent System, Light, and Dark theme selection
- Arabic-first, RTL-friendly interface based on the supplied light/dark designs

## Architecture

The application uses dependency inversion: presentation code depends on domain interfaces, while
Android and network details live in the data layer.

```text
ui (Activities, Fragments, ViewModels)
        ↓ depends on
domain (models, repository/player interfaces)
        ↑ implemented by
data (assets, SharedPreferences, Retrofit, Media3)
```

`DefaultAppContainer` is the composition root. It creates each concrete implementation once and
injects interfaces into ViewModels through small factories. This keeps construction explicit without
coupling screens to Retrofit, assets, preferences, or ExoPlayer.

### SOLID application

- Single responsibility: parsing, storage, API access, playback, state, and rendering are separate
  classes.
- Open/closed: repository and player implementations can be replaced without changing ViewModels.
- Liskov substitution: fake repository/player implementations can stand in for production
  implementations.
- Interface segregation: each domain contract exposes only the operations its consumer needs.
- Dependency inversion: ViewModels depend on `QuranRepository`, `HadethRepository`,
  `RadioRepository`, `ThemeRepository`, and `RadioPlayer` abstractions.

## Main packages

```text
com.example.islamiapp
├── data
│   ├── local       # Quran/Hadeth assets and theme preferences
│   ├── remote      # Retrofit radio API and DTO mapping
│   └── player      # Media3 radio player
├── di              # Application composition root
├── domain
│   ├── model
│   ├── player
│   └── repository
└── ui
    ├── common
    ├── home
    ├── splash
    └── theme
```

## Build and test

The project uses Gradle 8.5 so it can run on Java 21. App bytecode remains Java 8 compatible for the
existing Android Gradle Plugin and Android API range.

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

Run Android lint with:

```powershell
.\gradlew.bat lintDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## CI/CD

GitHub Actions provides two pipelines:

- `Android CI` runs for pull requests, branch pushes, and manual dispatches. It executes unit tests,
  Android lint, and a debug build, then uploads the APK and verification reports.
- `Android Release` runs when a tag matching `v*` is pushed. It repeats verification, builds signed
  APK and AAB files, generates SHA-256 checksums, and publishes them to a GitHub Release.

Configure these repository Actions secrets before creating a release tag:

| Secret                      | Value                                |
|-----------------------------|--------------------------------------|
| `ANDROID_KEYSTORE_BASE64`   | Base64-encoded release keystore file |
| `ANDROID_KEYSTORE_PASSWORD` | Keystore password                    |
| `ANDROID_KEY_ALIAS`         | Signing key alias                    |
| `ANDROID_KEY_PASSWORD`      | Signing key password                 |

On PowerShell, encode an existing keystore and copy it to the clipboard with:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks")) | Set-Clipboard
```

After adding the secrets, publish a release by pushing a version tag:

```powershell
git tag v1.0.0
git push origin v1.0.0
```

Release signing is enabled only when all four environment variables are present. Local debug builds
and unsigned local release builds continue to work without a keystore.
