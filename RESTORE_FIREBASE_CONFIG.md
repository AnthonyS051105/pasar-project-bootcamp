# 🔄 **RESTORE FIREBASE CONFIGURATION**

## 🚨 **MASALAH:** 
File `google-services.json` menggunakan placeholder values, menyebabkan error "API key not valid"

## ✅ **SOLUSI:**

### **LANGKAH 1: Download File Asli dari Firebase Console**

1. Buka **Firebase Console**: https://console.firebase.google.com
2. Pilih **project Anda yang sudah ada**
3. Klik **⚙️ Project Settings**
4. Tab **"Your apps"**
5. Cari app dengan package name: `com.example.pasar_project_bootcamp`
6. Klik **"google-services.json"** untuk download
7. **Replace** file di `app/google-services.json`

### **LANGKAH 2: Verifikasi File Benar**

File `google-services.json` yang benar harus memiliki:
- `project_id`: **Project ID asli Anda** (bukan "pasar-petani-marketplace")
- `current_key`: **API key asli** (bukan "AIzaSyBvOiIS28MoTVd_BcVueRnrdnYjAmJHiSI")
- `mobilesdk_app_id`: **App ID asli Anda**
- `package_name`: `com.example.pasar_project_bootcamp` ✅

### **LANGKAH 3: Clean & Rebuild**

```bash
./gradlew clean
./gradlew build
```

### **LANGKAH 4: Test Signup**

Sekarang signup seharusnya bekerja dengan error message yang lebih informatif.

---

## 📋 **CHECKLIST:**

- [ ] Download google-services.json asli dari Firebase Console
- [ ] Replace file di app/google-services.json  
- [ ] Clean & rebuild project
- [ ] Test signup dengan email baru
- [ ] Check logcat untuk konfirmasi

---

## 🎯 **EXPECTED SUCCESS LOG:**

```
D/SignUpActivity: Firebase initialized successfully
D/FirebaseConfigChecker: Firebase configuration check passed
D/SignUpActivity: Firebase createUser task completed. Success: true
D/SignUpActivity: Signup successful. User ID: [actual_user_id]
```

## ❌ **JIKA MASIH ERROR:**

Share informasi berikut:
1. **Project ID** dari Firebase Console
2. **Package name** di Firebase Console  
3. **Error message** dari logcat
4. **Screenshot** Firebase Console → Authentication settings