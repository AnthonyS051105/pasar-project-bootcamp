# Pasar Petani - Marketplace untuk Petani

Aplikasi marketplace mobile yang dirancang khusus untuk para petani agar dapat menjual hasil panen mereka secara online. Aplikasi ini dibuat menggunakan Kotlin dan Firebase.

## Fitur Utama

### 🏠 Homepage
- Tampilan welcome dengan saldo TukuPay
- Kategori produk: TukuBuah, TukuSayur, TukuBumbu, TukuBenih
- Search bar untuk mencari produk
- Keranjang belanja

### 🔍 Search Page
- Search bar dengan filter
- Rekomendasi pencarian
- Saran produk berdasarkan input user

### 📱 Product List Page
- Grid layout untuk menampilkan produk
- Search bar yang tidak menutupi status bar (posisi lebih rendah)
- Filter berdasarkan kategori
- Tombol favorite dan add to cart pada setiap produk

### 🛒 Product Detail Page
- Detail lengkap produk
- Informasi petani
- Opsi untuk menambah ke keranjang

## Teknologi yang Digunakan

- **Kotlin** - Bahasa pemrograman utama
- **Android Architecture Components**
  - Navigation Component
  - View Binding
  - LiveData & ViewModel
- **Firebase**
  - Firestore Database
  - Authentication
  - Storage
- **Material Design Components**
- **Glide** - Image loading
- **RecyclerView** - List dan Grid display

## Struktur Project

```
app/src/main/java/com/example/pasar_project_bootcamp/
├── ui/
│   ├── HomeFragment.kt
│   ├── SearchFragment.kt
│   ├── ProductListFragment.kt
│   ├── ProductDetailFragment.kt
│   └── adapter/
│       ├── ProductAdapter.kt
│       └── RecommendationAdapter.kt
├── data/
│   ├── Product.kt
│   ├── CartItem.kt
│   └── Order.kt
├── firebase/
│   └── FirebaseHelper.kt
└── MainActivity.kt
```

## Layout Files

```
app/src/main/res/layout/
├── activity_main.xml
├── fragment_home.xml
├── fragment_search.xml
├── fragment_product_list.xml
├── item_product.xml
└── item_recommendation.xml
```

## Navigasi

Aplikasi menggunakan Navigation Component dengan flow sebagai berikut:

1. **HomeFragment** (Start Destination)
   - Ke SearchFragment (via search bar)
   - Ke ProductListFragment (via kategori)

2. **SearchFragment**
   - Ke ProductListFragment (via search query)

3. **ProductListFragment**
   - Ke ProductDetailFragment (via product click)

4. **ProductDetailFragment**
   - Ke OrderDetailFragment

## Fitur Khusus

### Search Bar Positioning
Search bar pada ProductListFragment ditempatkan lebih rendah dari biasanya untuk menghindari overlap dengan status bar HP, memberikan user experience yang lebih baik.

### Category-Based Product Loading
Produk dimuat berdasarkan kategori yang dipilih:
- **TukuBuah**: Produk buah-buahan
- **TukuSayur**: Produk sayuran
- **TukuBumbu**: Produk bumbu dan rempah
- **TukuBenih**: Produk benih tanaman

### Sample Data
Aplikasi menyediakan sample data untuk demonstrasi dengan harga konsisten Rp 150.000,00 sesuai dengan desain yang diberikan.

## Setup dan Instalasi

1. Clone repository ini
2. Buka project di Android Studio
3. Pastikan Android SDK sudah terinstall
4. Setup Firebase project dan ganti `google-services.json` dengan file yang sebenarnya
5. Build dan run aplikasi

## Firebase Setup

Untuk menggunakan Firebase secara penuh:

1. Buat project baru di Firebase Console
2. Tambahkan aplikasi Android dengan package name: `com.example.pasar_project_bootcamp`
3. Download `google-services.json` dan letakkan di folder `app/`
4. Setup Firestore Database untuk menyimpan data produk
5. Setup Firebase Authentication untuk login user
6. Setup Firebase Storage untuk upload gambar produk

## Kontribusi

Aplikasi ini dibuat sebagai project marketplace untuk membantu petani menjual produk mereka. Kontribusi dan saran pengembangan sangat diterima.

## License

Project ini dibuat untuk keperluan edukasi dan pengembangan marketplace petani.