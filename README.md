# Pasar Petani - Marketplace untuk Petani

Aplikasi marketplace mobile yang memungkinkan petani untuk menjual hasil panen mereka langsung kepada konsumen. Aplikasi ini dibangun menggunakan Kotlin dan Firebase untuk backend.

## Fitur Utama

### 🛒 Sistem Pembelian Lengkap
- **Preview Produk**: Melihat detail produk dengan informasi lengkap termasuk harga, deskripsi, dan informasi toko
- **Keranjang Belanja**: Menambahkan produk ke keranjang dan melihat ringkasan pesanan
- **Inspirasi Resep**: Fitur unik yang memberikan inspirasi resep masakan berdasarkan produk yang dibeli
- **Pembayaran**: Sistem pembayaran dengan berbagai metode (COD, Transfer Bank, E-Wallet)

### 🏪 Informasi Toko
- Profil petani/toko lengkap dengan rating dan jumlah produk
- Alamat dan informasi kontak toko
- Sistem review dan rating produk

### 🔥 Firebase Integration
- **Firestore**: Database untuk menyimpan produk, pesanan, dan data pengguna
- **Firebase Auth**: Sistem autentikasi pengguna
- **Firebase Storage**: Penyimpanan gambar produk

## Struktur Aplikasi

### 🎨 UI/UX Design
Aplikasi ini menggunakan desain yang modern dan user-friendly dengan:
- **Material Design**: Menggunakan Material Components untuk konsistensi
- **Green Theme**: Tema hijau yang mencerminkan produk pertanian
- **Responsive Layout**: Desain yang responsif untuk berbagai ukuran layar

### 📱 Screen Flow
1. **Product Detail Screen**: Menampilkan detail produk dengan gambar, harga, dan informasi toko
2. **Order Detail Screen**: Ringkasan pesanan dengan fitur inspirasi resep
3. **Payment Screen**: Form pembayaran dengan berbagai metode pembayaran

### 🏗 Arsitektur
```
app/
├── data/                   # Data classes
│   ├── Product.kt
│   ├── CartItem.kt
│   └── Order.kt
├── ui/                     # UI Components
│   ├── ProductDetailFragment.kt
│   ├── OrderDetailFragment.kt
│   ├── PaymentFragment.kt
│   └── adapter/
│       └── CartAdapter.kt
├── firebase/               # Firebase Integration
│   └── FirebaseHelper.kt
└── MainActivity.kt
```

## Setup dan Instalasi

### Prerequisites
- Android Studio Arctic Fox atau yang lebih baru
- Kotlin 1.9+
- Minimum SDK 24 (Android 7.0)
- Target SDK 36

### Firebase Setup
1. Buat project baru di [Firebase Console](https://console.firebase.google.com/)
2. Tambahkan aplikasi Android dengan package name: `com.example.pasar_project_bootcamp`
3. Download file `google-services.json` dan letakkan di folder `app/`
4. Enable Firestore Database dan Firebase Authentication
5. Setup Firebase Storage untuk gambar produk

### Dependencies
Aplikasi ini menggunakan dependencies berikut:
- **Firebase BOM**: Untuk manajemen versi Firebase
- **Navigation Component**: Untuk navigasi antar fragment
- **ViewBinding**: Untuk binding view yang type-safe
- **Glide**: Untuk loading gambar
- **Material Components**: Untuk UI components

### Build dan Run
1. Clone repository ini
2. Buka project di Android Studio
3. Sync project dengan Gradle files
4. Setup Firebase configuration
5. Run aplikasi di emulator atau device

## Fitur Khusus

### 🍳 Inspirasi Resep
Fitur unik yang memberikan inspirasi resep masakan berdasarkan produk yang dibeli. Ini membantu konsumen untuk memanfaatkan produk pertanian dengan lebih kreatif.

### 💳 Multiple Payment Methods
- **COD (Cash on Delivery)**: Bayar di tempat
- **Transfer Bank**: Transfer melalui rekening bank
- **E-Wallet**: GoPay, OVO, DANA

### 📍 Delivery Address Management
Sistem manajemen alamat pengiriman yang memungkinkan pengguna untuk:
- Menambah alamat pengiriman
- Mengubah alamat default
- Menyimpan multiple alamat

## Data Structure

### Product
```kotlin
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val farmerId: String,
    val farmerName: String,
    val farmerAddress: String,
    val category: String,
    val stock: Int,
    val rating: Float,
    val totalReviews: Int
)
```

### Order
```kotlin
data class Order(
    val id: String,
    val userId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val deliveryAddress: String,
    val orderDate: Date,
    val status: OrderStatus,
    val paymentMethod: String
)
```

## Pengembangan Selanjutnya

### 🚀 Fitur yang Dapat Ditambahkan
- **Chat System**: Komunikasi langsung antara pembeli dan petani
- **GPS Tracking**: Tracking pengiriman real-time
- **Push Notifications**: Notifikasi status pesanan
- **Product Categories**: Kategorisasi produk yang lebih detail
- **Seasonal Recommendations**: Rekomendasi produk berdasarkan musim
- **Farmer Dashboard**: Dashboard untuk petani mengelola produk dan pesanan

### 🔧 Improvements
- **Offline Support**: Caching data untuk akses offline
- **Performance Optimization**: Image compression dan lazy loading
- **Security**: Enkripsi data sensitif
- **Analytics**: Google Analytics untuk tracking user behavior

## Kontribusi

Jika Anda ingin berkontribusi pada project ini:
1. Fork repository
2. Buat feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

## License

Project ini menggunakan MIT License. Lihat file `LICENSE` untuk detail lebih lanjut.

## Support

Untuk pertanyaan atau dukungan, silakan buat issue di repository ini atau hubungi tim development.

---

**Pasar Petani** - Menghubungkan petani dengan konsumen untuk produk pertanian yang lebih segar dan berkualitas! 🌱