# 🔥 **FIREBASE SETUP & DEBUG GUIDE**

## 🚨 **MASALAH: "Signup gagal. Silahkan coba lagi"**

### **LANGKAH-LANGKAH DEBUGGING:**

## 1. **CHECK LOGCAT OUTPUT**
Jalankan aplikasi dan lihat logcat untuk error detail:
```bash
# Filter untuk Firebase logs
adb logcat | grep -E "(SignUpActivity|FirebaseConfigChecker|Firebase)"

# Atau di Android Studio
# Logcat → Filter → "SignUpActivity"
```

**Yang harus dicari:**
- `Firebase initialized successfully` ✅
- `Firebase configuration check passed` ✅  
- `Firebase createUser task completed` ✅
- Error codes seperti `ERROR_OPERATION_NOT_ALLOWED` ❌

---

## 2. **SETUP FIREBASE PROJECT YANG BENAR**

### **A. Buat Firebase Project Baru:**
1. Buka [Firebase Console](https://console.firebase.google.com)
2. Click **"Add project"**
3. Nama project: `pasar-petani-marketplace`
4. Enable Google Analytics (optional)

### **B. Enable Authentication:**
1. Di Firebase Console → **Authentication**
2. Click **"Get started"**
3. Tab **"Sign-in method"**
4. Enable **"Email/Password"** ✅
5. Save changes

### **C. Setup Firestore Database:**
1. Di Firebase Console → **Firestore Database**
2. Click **"Create database"**
3. Choose **"Start in test mode"** (untuk development)
4. Select location (asia-southeast1 untuk Indonesia)

### **D. Download google-services.json:**
1. Di Firebase Console → **Project Settings** (⚙️)
2. Tab **"Your apps"**
3. Click **"Add app"** → **Android**
4. Package name: `com.example.pasar_project_bootcamp`
5. Download **google-services.json**
6. Replace file di `app/google-services.json`

---

## 3. **FIRESTORE SECURITY RULES**

Buat rules yang allow read/write untuk development:

```javascript
// Firestore Rules - Development Mode
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow read/write for authenticated users
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
    
    // Allow public read for products
    match /products/{productId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

---

## 4. **COMMON ERROR SOLUTIONS**

### **Error: "ERROR_OPERATION_NOT_ALLOWED"**
```
❌ Problem: Email/Password auth not enabled
✅ Solution: Enable Email/Password in Firebase Console
```

### **Error: "ERROR_NETWORK_REQUEST_FAILED"**
```
❌ Problem: No internet or Firebase blocked
✅ Solution: Check internet, try different network
```

### **Error: "Firebase not initialized"**
```
❌ Problem: google-services.json incorrect
✅ Solution: Download correct file from Firebase Console
```

### **Error: "ERROR_INVALID_API_KEY"**
```
❌ Problem: Wrong API key in google-services.json
✅ Solution: Re-download google-services.json
```

---

## 5. **TESTING CHECKLIST**

### **✅ Pre-Signup Checks:**
- [ ] Internet connection active
- [ ] Firebase project created
- [ ] Email/Password auth enabled
- [ ] Correct google-services.json in place
- [ ] App rebuilt after adding google-services.json

### **✅ During Signup:**
- [ ] Check logcat for detailed errors
- [ ] Try different email addresses
- [ ] Use password longer than 6 characters
- [ ] Check Firebase Console → Authentication → Users

### **✅ Post-Signup Verification:**
- [ ] User appears in Firebase Console
- [ ] User profile created in Firestore
- [ ] Can login with created account

---

## 6. **QUICK TEST COMMANDS**

```bash
# Clean and rebuild
./gradlew clean
./gradlew build

# Install and run
./gradlew installDebug

# Check logs
adb logcat | grep SignUpActivity
```

---

## 7. **SAMPLE WORKING CONFIGURATION**

### **File: `app/google-services.json`**
```json
{
  "project_info": {
    "project_number": "YOUR_PROJECT_NUMBER",
    "project_id": "pasar-petani-marketplace",
    "storage_bucket": "pasar-petani-marketplace.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:YOUR_PROJECT_NUMBER:android:YOUR_APP_ID",
        "android_client_info": {
          "package_name": "com.example.pasar_project_bootcamp"
        }
      },
      "api_key": [
        {
          "current_key": "YOUR_ACTUAL_API_KEY"
        }
      ]
    }
  ]
}
```

---

## 8. **EMERGENCY FALLBACK**

Jika semua gagal, gunakan Firebase Emulator:

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Init emulator
firebase init emulators

# Start emulator
firebase emulators:start
```

---

## 🎯 **EXPECTED SUCCESSFUL LOG OUTPUT:**

```
D/SignUpActivity: Firebase initialized successfully
D/FirebaseConfigChecker: Firebase configuration check passed
D/SignUpActivity: Starting signup process for email: test@example.com
D/SignUpActivity: Firebase createUser task completed. Success: true
D/SignUpActivity: Signup successful. User ID: xyz123, Email: test@example.com
D/SignUpActivity: User profile created successfully in Firestore
```

---

## 📞 **SUPPORT:**

Jika masih error, share:
1. **Logcat output** (filter SignUpActivity)
2. **Firebase Console screenshot** (Authentication settings)
3. **google-services.json** (project_id dan package_name)
4. **Error message** yang muncul di app