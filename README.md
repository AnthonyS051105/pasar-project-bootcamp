# Pasar Petani Marketplace

Aplikasi marketplace untuk menghubungkan petani dengan konsumen, dengan fitur autentikasi Firebase, Firestore database, dan integrasi AI untuk rekomendasi resep.

## Setup Instructions

### 1. Firebase Setup

1. **Buat Project Firebase**:
   - Pergi ke [Firebase Console](https://console.firebase.google.com/)
   - Klik "Add project" dan ikuti instruksi
   - Aktifkan Authentication dan Firestore Database

2. **Konfigurasi Authentication**:
   - Di Firebase Console, pergi ke Authentication > Sign-in method
   - Aktifkan "Email/Password" provider

3. **Setup Firestore Database**:
   - Di Firebase Console, pergi ke Firestore Database
   - Klik "Create database" dan pilih mode "Start in test mode"

4. **Download google-services.json**:
   - Di Firebase Console, pergi ke Project Settings
   - Klik "Add app" dan pilih Android
   - Masukkan package name: `com.example.pasar_project_bootcamp`
   - Download file `google-services.json`
   - Replace file `app/google-services.json` dengan file yang baru didownload

### 2. OpenRouter API Setup

1. **Daftar di OpenRouter**:
   - Pergi ke [OpenRouter.ai](https://openrouter.ai/)
   - Buat akun dan dapatkan API key

2. **Konfigurasi API Key**:
   - Buka file `local.properties` di root project
   - Ganti `sk-or-v1-your-actual-api-key-here` dengan API key asli Anda:
   ```
   OPENROUTER_API_KEY=sk-or-v1-your-actual-api-key-here
   ```

### 3. Build dan Run

1. **Sync Project**:
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **Run Aplikasi**:
   - Buka di Android Studio
   - Sync project dengan Gradle files
   - Run aplikasi di emulator atau device fisik

## Fitur Aplikasi

### ✅ Sudah Diperbaiki:

1. **Autentikasi Firebase**:
   - Login dan Sign up dengan email/password
   - Error handling yang lebih baik
   - Validasi input
   - Auto-redirect jika sudah login

2. **Firestore Database**:
   - Integrasi dengan Firestore
   - Sample data otomatis (Apel, Jeruk, Bayam, Cabai)
   - CRUD operations untuk produk, cart, dan orders

3. **REST API**:
   - Integrasi dengan OpenRouter API untuk rekomendasi resep
   - Error handling dan timeout configuration
   - Proper API key management

4. **UI Improvements**:
   - Ukuran lingkaran kategori diperkecil (60dp → 50dp)
   - Padding dan margin yang lebih proporsional
   - Text size yang lebih sesuai

### 🔄 Fitur yang Tersedia:

- **Product Management**: Browse produk berdasarkan kategori
- **Search**: Pencarian produk berdasarkan nama, deskripsi, kategori
- **Cart System**: Tambah produk ke keranjang
- **Recipe AI**: Generate resep berdasarkan bahan yang dipilih
- **User Profile**: Profil pengguna tersimpan di Firestore

## Troubleshooting

### Login/Signup Gagal:
1. Pastikan internet connection aktif
2. Check Firebase Authentication settings
3. Verify google-services.json sudah benar
4. Check Logcat untuk error details

### API Recipe Tidak Bekerja:
1. Pastikan OpenRouter API key valid
2. Check internet connection
3. Verify API key di local.properties

### Data Produk Tidak Muncul:
1. Check Firestore rules (harus allow read/write)
2. Verify internet connection
3. Check Logcat untuk Firestore errors

## Firebase Rules

Untuk development, gunakan rules berikut di Firestore:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Tech Stack

- **Android**: Kotlin, ViewBinding, Navigation Component
- **Backend**: Firebase Authentication, Firestore Database
- **API**: OpenRouter AI API
- **UI**: Material Design, CardView, RecyclerView
- **Image Loading**: Glide
- **HTTP Client**: OkHttp3

## Contact

Jika ada pertanyaan atau masalah, silakan buat issue di repository ini.