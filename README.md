# 🌤️ WeatherWise - Your Personalized Weather Companion 🌦️  

![ic_launcher_round](https://github.com/user-attachments/assets/bfdf14b9-abf3-4933-8f3e-63d2e3d58acd)

**WeatherWise** is a dynamic weather application designed to make weather forecasting effortless and user-centric. Powered by OpenWeatherMap APIs and backed with state-of-the-art Android libraries, this app offers precise weather data, personalized user features, and a seamless UI/UX.

---

## ✨ Features  

- 🌍 **Search by City or Location**: Instantly fetch real-time weather details for your current location or any city worldwide.  
- 📌 **Bookmark Locations**: Save your favorite places for quick access to their weather updates.  
- 📅 **5-Day Forecast**: Plan ahead with detailed weather predictions for the next five days.  
- 🌦️ **Today’s Forecast**: Stay updated with accurate, real-time weather data tailored to your location.  
- 👤 **Profile Management**: Customize your profile by updating your photo and name anytime.  

---

## 📸 Screenshots  

| **Feature**               | **Screenshot**                                                                                                            |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------|
| **Home Screen**           | <img src="https://github.com/user-attachments/assets/1ee683d8-d223-433c-b5bb-234ee51fede7" alt="Home 1" width="150"> <img src="https://github.com/user-attachments/assets/8673f849-c07f-4bbf-9db7-be6980ae3e95" alt="Home 2" width="150"> <img src="https://github.com/user-attachments/assets/df5d5688-e65f-49be-995e-3d4bd8011938" alt="Home 3" width="150"> <img src="https://github.com/user-attachments/assets/bae499a0-4008-41e7-8700-c78ecda6f84c" alt="Home 4" width="150"> |
| **Search Locations**      | <div align="center"><img src="https://github.com/user-attachments/assets/a4e62c02-bb77-4cdc-a922-ecd7a47fca59" alt="Search Locations" width="150"></div> |
| **Bookmarks**             | <div align="center"><img src="https://github.com/user-attachments/assets/655ff16c-e1fb-4d3d-9f4a-7a983957f261" alt="Bookmarks" width="150"></div>       |
| **5-Day Forecast**        | <div align="center"><img src="https://github.com/user-attachments/assets/74b1b927-4749-4b68-bce0-f524d1d572a6" alt="5-Day Forecast" width="150"></div> |
| **Profile Customization** | <div align="center"><img src="https://github.com/user-attachments/assets/b30796cc-0b46-4f0e-8104-ef6007cd5f6b" alt="Profile Customization" width="150"></div> |




---

## 🛠️ Dependencies and Plugins  

### **Plugins**  
The following plugins are used to build and manage the project efficiently:  
- **Android Application Plugin**: `libs.plugins.android.application`  
- **Kotlin Android Plugin**: `libs.plugins.kotlin.android`  
- **Kotlin Kapt**: `org.jetbrains.kotlin.kapt`  
- **Kotlin Parcelize**: `kotlin-parcelize`  

---

### **Dependencies**  
#### **Core Android Libraries**  
- `androidx.core:core-ktx`  
- `androidx.appcompat:appcompat`  
- `com.google.android.material:material`  
- `androidx.activity:activity-ktx`  
- `androidx.constraintlayout:constraintlayout`  
- `androidx.cardview:cardview`  

#### **Networking**  
- **Retrofit**:  
  - `com.squareup.retrofit2:retrofit:2.9.0`  
  - `com.squareup.retrofit2:converter-gson:2.9.0`  
- **OkHttp**:  
  - `com.squareup.okhttp3:okhttp:4.12.0`  
  - `com.squareup.okhttp3:logging-interceptor:4.9.3`  

#### **Data Binding and Lifecycle**  
- `androidx.lifecycle:lifecycle-runtime-ktx:2.6.2`  
- `androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2`  
- `androidx.lifecycle:lifecycle-livedata-ktx:2.6.2`  
- `androidx.lifecycle:lifecycle-extensions:2.2.0`  

#### **UI Enhancements**  
- **Glide**: For efficient image loading and caching.  
- **Chip Navigation Bar**: `libs.chip.navigation.bar`  
- **BlurView**: `com.github.Dimezis:BlurView:version-2.0.3`  

#### **JSON Parsing**  
- **Gson**: `com.google.code.gson:gson:2.9.1`  

#### **Location Services**  
- `com.google.android.gms:play-services-location:21.0.1`  

---

## 🔧 Build Configurations  

### **Project Configuration**  
- **Namespace**: `com.android.Weather`  
- **Compile SDK**: 35  
- **Min SDK**: 26  
- **Target SDK**: 35  
- **Java Version**: 17  

### **Build Features**  
- **View Binding**: Enabled  
- **Data Binding**: Enabled  

### **ProGuard**  
- Default ProGuard files are included for code optimization in the release build.  

---

## 🚀 How to Build & Run  

1. Clone the repository:  
   ```bash
   git clone https://github.com/AyushKumar-Codes/WeatherWise-App.git
   cd WeatherWise-App
