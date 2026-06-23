# Prezto

Marketplace P2P de herramientas para urbanismo y optimización industrial. Conecta a propietarios con herramientas subutilizadas con arrendatarios que las necesitan temporalmente, sobre un ecosistema de confianza radical, pagos blindados (Escrow) y flujos de cero fricción.

---

## Stack Tecnológico

| Capa | Tecnología |
| :--- | :--- |
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Inyección de dependencias | Hilt (Dagger) |
| Persistencia local | DataStore (Preferences) |
| Red | Retrofit + OkHttp |
| Asincronía | Coroutines / Flow |
| Imágenes | Coil |
| Geolocalización | Google Play Services Location |

## Arquitectura

**Clean Architecture + Domain-Driven Design (DDD)** con un **Shared Kernel** para conceptos transversales.

```
app/src/main/java/com/prezto/prezto/
├── core/                         # Shared Kernel
│   ├── domain/model/             # TrustScore, TrustLevel
│   ├── domain/location/          # GeoLocation (Haversine)
│   ├── domain/validation/        # ValidateEmail/Password/Dni/Phone/Name
│   ├── data/security/            # CryptoManager (Keystore AES-256)
│   ├── data/session/             # SessionManager (DataStore cifrado)
│   ├── data/network/             # AuthInterceptor, TokenAuthenticator, ErrorMapper
│   ├── data/location/            # LocationProvider (FusedLocation)
│   ├── designsystem/             # Tema, componentes reutilizables
│   └── util/logging/             # PreztoLogger (Debug / Crashlytics)
├── feature_auth/                 # Splash, Login, Register, OTP, Forgot
├── feature_explore/              # Home (búsqueda + geo), Detail, Publish
├── feature_profile/              # Perfil, Edit, KYC
├── feautre_rental/               # Checkout (Escrow), Success
├── feature_chat/                 # Chat seguro, Inbox, ciclo de vida
├── feature_support/              # Centro de ayuda, reportar incidencia
└── di/                           # Módulos Hilt
```

Cada feature respeta la separación **Presentation (MVVM + MVI) / Domain / Data**, con State Hoisting estricto (los ViewModels solo se inyectan a nivel de ruta).

## Estado

**MVP Frontend finalizado.**

- **Seguridad:** sesión persistida con tokens cifrados en reposo (Android Keystore, AES-256/GCM); `AuthInterceptor` inyecta el Bearer token; `TokenAuthenticator` listo para el refresh en 401.
- **UX:** geolocalización con filtro por radio (Haversine), Chat Seguro con Escudo Anti-Fuga y ciclo de vida del alquiler, verificación KYC, OTP por celular, Happy Paths animados, manejo de errores y estados vacíos.
- **Validaciones de dominio** reutilizables y testeables en JVM puro.

## Reglas de negocio implementadas (UI)

- **Escrow:** el pago se retiene; el propietario cobra solo tras la devolución.
- **Tarifa de Protección Prezto:** micro-seguro obligatorio (8% del subtotal).
- **Garantía Dinámica:** monto congelado (no debitado) que baja según el Trust Score.
- **Escudo Anti-Fuga:** el chat bloquea números y enlaces.
- **Comisión Prezto:** 10% sobre el alquiler (transparente en Publish).

---

## Roadmap Backend (TODOs)

> Todo el frontend consume interfaces; conectar el backend implica reemplazar las implementaciones mock por las reales sin tocar la presentación.

- [ ] **`AuthRepository` + `AuthApi`** (Retrofit): reemplazar los mocks de Login/Register/OTP/Forgot.
- [ ] **Refresh de tokens (P0):** completar `TokenAuthenticator.authenticate()` con `authApi.refresh(refreshToken)` + `sessionManager.saveTokens()` / `clearSession()`.
- [ ] **Tokens reales:** sustituir `mock_access_token` / `mock_refresh_token` por los del backend en Login y OTP.
- [ ] **`ExploreRepository` real:** sustituir `MockExploreRepositoryImpl` por la API de items.
- [ ] **`MapRepository` con PostGIS:** reemplazar `MockMapRepositoryImpl` (Haversine en cliente) por consulta server-side (`ST_DWithin`).
- [ ] **`ProfileRepository` real:** persistir edición de perfil y verificación KYC.
- [ ] **Chat real:** persistencia de mensajes, `sendMessage`/`markAsRead`, websockets/push.
- [ ] **`SupportRepository`:** envío real de incidencias (tipo, descripción, foto).
- [ ] **Subida de imágenes:** reemplazar URIs locales por upload a almacenamiento (items, avatar, evidencias KYC/incidencias).
- [ ] **Pagos:** integrar Culqi (tokenización PCI-DSS, Visa/Mastercard/Yape).
- [ ] **Rate limiting / anti-brute-force** en login (HTTP 429 + `Retry-After`).
- [ ] **Crashlytics:** aplicar el plugin de Firebase y descomentar `CrashlyticsLogger`.
- [ ] **Hardening release:** habilitar R8/ProGuard + `shrinkResources`; mover secrets a `local.properties` + `BuildConfig`; HTTPS + certificate pinning.
- [ ] **Tests:** ampliar cobertura de ViewModels y validadores.

## Build

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

Requisitos: `minSdk 24`, `compileSdk 36`, JDK 11.

---

Ver el contrato de API y los esquemas en [`docs/API_CONTRACT.md`](docs/API_CONTRACT.md).
