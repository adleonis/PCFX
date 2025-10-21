# Software Bill of Materials (SBOM) - PCF-X Android Adapter v0.1.0

**Generated:** 2025-10-20
**Project:** org.pcfx.adapter.android
**Version:** 0.1.0

## Direct Dependencies

### Android Framework
- `androidx.core:core:1.12.0` - Android core library
- `androidx.appcompat:appcompat:1.6.1` - Backward compatibility support
- `androidx.constraintlayout:constraintlayout:2.1.4` - Layout engine
- `com.google.android.material:material:1.11.0` - Material Design components

### Database
- `androidx.room:room-runtime:2.6.1` - Android persistence library
- `androidx.room:room-ktx:2.6.1` - Kotlin extensions for Room
- `androidx.room:room-compiler:2.6.1` - Room annotation processor (kapt)

### HTTP & Networking
- `com.squareup.okhttp3:okhttp:4.11.0` - HTTP client library
- `com.squareup.okhttp3:logging-interceptor:4.11.0` - OkHttp logging

### JSON & Serialization
- `com.google.code.gson:gson:2.10.1` - JSON serialization library
- `com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0` - Kotlin support for Jackson

### Validation
- `com.github.everit-org.json-schema:org.everit.json.schema:1.14.4` - JSON Schema validation

### Cryptography & Security
- `androidx.security:security-crypto:1.1.0-alpha06` - Android security library
- `org.bouncycastle:bcprov-jdk15on:1.70` - Bouncy Castle cryptography provider

### Kotlin & Coroutines
- `org.jetbrains.kotlin:kotlin-stdlib:1.9.0` - Kotlin standard library
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3` - Android coroutines
- `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3` - Core coroutines library

### Logging
- `org.slf4j:slf4j-api:2.0.9` - Simple Logging Facade
- `ch.qos.logback:logback-android:0.1.5` - Android logging backend

## Test Dependencies

### Unit Testing
- `junit:junit:4.13.2` - Unit testing framework
- `org.mockito:mockito-core:5.5.0` - Mocking framework
- `org.mockito.kotlin:mockito-kotlin:5.1.0` - Kotlin support for Mockito
- `org.json:json:20231013` - JSON library for testing

### Android Testing
- `androidx.test:runner:1.5.2` - Android test runner
- `androidx.test.espresso:espresso-core:3.5.1` - UI testing framework
- `androidx.room:room-testing:2.6.1` - Room testing utilities

## Security Considerations

- **Cryptography:** Uses ECDSA (P-256) for event signing via Bouncy Castle
- **Storage:** Sensitive data (private keys) stored in Android KeyStore
- **Networking:** Uses HTTPS for PDV communication (user-configurable)
- **Permissions:** Requests minimal permissions for accessibility and notification monitoring

## License

This project is licensed under Apache License 2.0 and CC-BY-SA 4.0 (see LICENSE_APACHE2, LICENSE_CC-BY-SA in root).

## Maintenance Status

This SBOM is a stub for v0.1.0 and should be updated when:
- New dependencies are added
- Versions are updated
- Vulnerability advisories are published

---

For detailed vulnerability information, refer to:
- [CVE Database](https://www.cvedetails.com/)
- [Android Security Advisories](https://source.android.com/security/bulletin)
