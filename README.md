# Marketplace Petani - Aplikasi Mobile Android

Aplikasi marketplace untuk para petani dimana mereka dapat menjual hasil panen mereka. Aplikasi ini dibuat menggunakan Kotlin dan Firebase.

## 🌾 Fitur Utama

- **Homepage**: Menampilkan kategori produk (TukuBuah, TukuSayur, TukuBumbu, TukuBenih) dan daftar produk
- **Search**: Halaman pencarian dengan rekomendasi dan hasil pencarian
- **Product Display**: Tampilan produk dalam grid dengan informasi harga dan tombol favorit/cart
- **Firebase Integration**: Penyimpanan data produk menggunakan Firebase Firestore

## 📱 Tampilan Aplikasi

Aplikasi ini memiliki 3 halaman utama:

1. **Homepage** - Menampilkan greeting, saldo TukuPay, kategori produk, dan grid produk
2. **Search Input** - Halaman pencarian dengan placeholder dan rekomendasi
3. **Search Results** - Menampilkan hasil pencarian dalam format grid

## 🛠 Teknologi yang Digunakan

- **Kotlin** - Bahasa pemrograman utama
- **Android SDK** - Platform development
- **Firebase Firestore** - Database NoSQL
- **Firebase Auth** - Autentikasi pengguna
- **Firebase Storage** - Penyimpanan gambar
- **Glide** - Image loading library
- **RecyclerView** - Untuk menampilkan list dan grid
- **CardView** - UI components
- **Material Design** - Design system

## 📁 Struktur Project

```
app/src/main/
├── java/com/example/pasar_project_bootcamp/
│   ├── MainActivity.kt                 # Activity utama (Homepage)
│   ├── SearchActivity.kt              # Activity pencarian
│   ├── RecommendationAdapter.kt       # Adapter untuk rekomendasi
│   ├── adapter/
│   │   ├── ProductAdapter.kt          # Adapter untuk produk
│   │   └── CategoryAdapter.kt         # Adapter untuk kategori
│   ├── data/
│   │   ├── Product.kt                 # Data class produk
│   │   └── Category.kt                # Data class kategori
│   └── repository/
│       └── ProductRepository.kt       # Repository untuk Firebase
├── res/
│   ├── layout/
│   │   ├── activity_main.xml          # Layout homepage
│   │   ├── activity_search.xml        # Layout pencarian
│   │   ├── item_product.xml           # Layout item produk
│   │   ├── item_category.xml          # Layout item kategori
│   │   └── item_recommendation.xml    # Layout item rekomendasi
│   ├── drawable/
│   │   ├── ic_*.xml                   # Icon vector drawables
│   │   └── *.xml                      # Background drawables
│   └── values/
│       └── colors.xml                 # Definisi warna
└── AndroidManifest.xml                # Manifest aplikasi
```

## 🚀 Cara Setup dan Menjalankan

### Prerequisites

1. **Android Studio** (versi terbaru)
2. **JDK 11** atau lebih tinggi
3. **Android SDK** dengan minimum API level 24
4. **Firebase Project** (opsional untuk development)

### Langkah Setup

1. **Clone atau Download Project**
   ```bash
   git clone <repository-url>
   cd marketplace-petani
   ```

2. **Buka di Android Studio**
   - Buka Android Studio
   - Pilih "Open an existing project"
   - Pilih folder project

3. **Setup Firebase (Opsional)**
   - Buat project baru di [Firebase Console](https://console.firebase.google.com)
   - Tambahkan aplikasi Android dengan package name: `com.example.pasar_project_bootcamp`
   - Download file `google-services.json` dan letakkan di folder `app/`
   - Enable Firestore Database dan Authentication di Firebase Console

4. **Sync Project**
   - Klik "Sync Now" ketika diminta oleh Android Studio
   - Tunggu hingga semua dependencies terdownload

5. **Build dan Run**
   - Pilih device atau emulator
   - Klik tombol "Run" (▶️) atau tekan Shift + F10

### Setup Firebase (Langkah Detail)

1. **Buat Firebase Project**
   - Kunjungi https://console.firebase.google.com
   - Klik "Create a project"
   - Ikuti wizard setup

2. **Tambahkan Android App**
   - Di project Firebase, klik "Add app" → Android
   - Package name: `com.example.pasar_project_bootcamp`
   - Download `google-services.json`

3. **Enable Services**
   - **Firestore Database**: Database → Create database → Start in test mode
   - **Authentication**: Authentication → Sign-in method → Enable Email/Password
   - **Storage**: Storage → Get started

4. **Setup Firestore Rules**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /{document=**} {
         allow read, write: if true; // Untuk development
       }
     }
   }
   ```

## 📊 Struktur Data Firebase

### Collection: `products`
```json
{
  "id": "auto-generated",
  "name": "Buah Apel Malang",
  "price": 150000.0,
  "category": "Buah",
  "description": "Buah apel segar dari Malang",
  "imageUrl": "https://...",
  "farmerId": "farmer123",
  "farmerName": "Petani A",
  "location": "Malang, Jawa Timur",
  "quantity": 10,
  "unit": "kg",
  "createdAt": 1640995200000
}
```

### Collection: `categories`
```json
{
  "id": "auto-generated",
  "name": "TukuBuah",
  "iconRes": "ic_fruit",
  "backgroundColor": "green_light"
}
```

## 🎨 Design System

### Warna Utama
- **Primary Green**: `#4CAF50`
- **Green Dark**: `#388E3C`
- **Green Light**: `#C8E6C9`
- **Background**: `#F5F5F5`
- **White**: `#FFFFFF`

### Typography
- **Heading**: 18sp, Bold
- **Body**: 16sp, Regular
- **Caption**: 14sp, Regular
- **Small**: 12sp, Regular

## 🔧 Kustomisasi

### Menambah Kategori Baru
1. Tambahkan icon di `res/drawable/`
2. Update fungsi `getCategories()` di `MainActivity.kt`
3. Tambahkan warna background di `colors.xml`

### Menambah Field Produk
1. Update data class `Product.kt`
2. Update layout `item_product.xml`
3. Update `ProductAdapter.kt`

### Mengubah Tampilan
1. Edit file layout di `res/layout/`
2. Update warna di `res/values/colors.xml`
3. Ganti icon di `res/drawable/`

## 🐛 Troubleshooting

### Build Errors
- **Sync failed**: Pastikan koneksi internet stabil, coba sync ulang
- **Missing dependencies**: Periksa `build.gradle.kts`, pastikan semua dependencies ada
- **Firebase error**: Pastikan `google-services.json` ada di folder `app/`

### Runtime Errors
- **App crash on start**: Periksa logcat, biasanya karena missing resources
- **Firebase not working**: Pastikan google-services.json sesuai dengan package name
- **Images not loading**: Periksa permission INTERNET di AndroidManifest.xml

### Common Issues
1. **Minimum SDK**: Pastikan device/emulator minimal API 24
2. **Java Version**: Gunakan JDK 11
3. **Firebase Rules**: Untuk development, set rules ke allow all

## 📝 TODO / Future Enhancements

- [ ] Implementasi autentikasi pengguna
- [ ] Fitur keranjang belanja
- [ ] Sistem rating dan review
- [ ] Notifikasi push
- [ ] Chat antara petani dan pembeli
- [ ] Sistem pembayaran
- [ ] Tracking pengiriman
- [ ] Profile petani dan pembeli

## 👥 Kontribusi

Untuk berkontribusi pada project ini:

1. Fork repository
2. Buat branch feature (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

## 📄 Lisensi

Project ini menggunakan lisensi MIT. Lihat file `LICENSE` untuk detail.

## 📞 Kontak

Jika ada pertanyaan atau issue, silakan buat issue di repository ini atau hubungi developer.

---

**Happy Coding! 🌾📱**