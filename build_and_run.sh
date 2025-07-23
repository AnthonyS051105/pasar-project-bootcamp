#!/bin/bash

# Marketplace Petani - Build and Run Script
echo "🌾 Marketplace Petani - Build and Run Script"
echo "=============================================="

# Check if Android SDK is available
if ! command -v adb &> /dev/null; then
    echo "❌ Android SDK tidak ditemukan. Pastikan Android SDK sudah terinstall dan ada di PATH."
    exit 1
fi

# Clean project
echo "🧹 Cleaning project..."
./gradlew clean

# Build project
echo "🔨 Building project..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Build berhasil!"
    
    # Check if device is connected
    DEVICES=$(adb devices | grep -v "List of devices" | grep "device$" | wc -l)
    
    if [ $DEVICES -gt 0 ]; then
        echo "📱 Installing APK to device..."
        ./gradlew installDebug
        
        if [ $? -eq 0 ]; then
            echo "✅ Instalasi berhasil!"
            echo "🚀 Launching app..."
            adb shell am start -n com.example.pasar_project_bootcamp/.MainActivity
        else
            echo "❌ Instalasi gagal!"
        fi
    else
        echo "⚠️  Tidak ada device yang terhubung. APK tersedia di: app/build/outputs/apk/debug/"
    fi
else
    echo "❌ Build gagal!"
    exit 1
fi

echo "✨ Selesai!"