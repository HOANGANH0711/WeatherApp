# 🌤️ WeatherApp – Ứng dụng xem thời tiết Android

WeatherApp là ứng dụng xem thời tiết được phát triển bằng **Kotlin** và **Jetpack Compose** trên nền tảng Android.

Ứng dụng sử dụng **OpenWeatherMap API** để lấy dữ liệu thời tiết thực tế, cung cấp thông tin về nhiệt độ, độ ẩm, tốc độ gió, nhiệt độ cảm nhận và dự báo thời tiết theo từng mốc thời gian.

Dự án được thực hiện nhằm tìm hiểu quy trình phát triển ứng dụng Android, tích hợp REST API và xây dựng giao diện hiện đại bằng Jetpack Compose.

## 📱 Chức năng chính

### 🌡️ 1. Xem thời tiết hiện tại
- Hiển thị thời tiết tại TP. Hồ Chí Minh.
- Nhiệt độ hiện tại (°C).
- Mô tả tình trạng thời tiết.
- Biểu tượng thời tiết tương ứng với dữ liệu API.

### 🕒 2. Dự báo thời tiết
- Hiển thị các mốc dự báo thời tiết.
- Hiển thị nhiệt độ và biểu tượng thời tiết của từng mốc.
- Sử dụng API dự báo 5 ngày / 3 giờ của OpenWeatherMap.
- Phiên bản hiện tại lấy tối đa 8 mốc dự báo.

### 🌍 3. Thời tiết nhiều thành phố
Theo dõi thời tiết tại:
- Hà Nội
- Đà Nẵng
- Đà Lạt

Mỗi thành phố được hiển thị bằng một thẻ thông tin gồm nhiệt độ và biểu tượng thời tiết.

### 💧 4. Thông tin thời tiết chi tiết
- Độ ẩm (%).
- Tốc độ gió (m/s).
- Nhiệt độ cảm nhận (°C).

### 🔄 5. Tự động cập nhật
- Tự động tải dữ liệu khi mở màn hình.
- Thời tiết TP. Hồ Chí Minh được cập nhật theo chu kỳ 90 giây.
- Thời tiết các thành phố khác được cập nhật theo chu kỳ 15 phút.
- Không cần nhấn nút làm mới thủ công.

*Lưu ý: Dự báo theo giờ hiện chỉ được tải khi màn hình được khởi tạo.*

## 🛠️ Công nghệ sử dụng

| Công nghệ | Mục đích |
|---|---|
| Kotlin | Ngôn ngữ lập trình chính |
| Jetpack Compose | Xây dựng giao diện Android |
| Material Design 3 | Thiết kế thành phần giao diện |
| Ktor Client | Gửi HTTP request |
| Kotlinx Serialization | Chuyển đổi dữ liệu JSON |
| Coil | Hỗ trợ tải hình ảnh |
| Kotlin Coroutines | Xử lý tác vụ bất đồng bộ |
| OpenWeatherMap API | Cung cấp dữ liệu thời tiết |
| Gradle Kotlin DSL | Quản lý cấu hình và thư viện |

## 📂 Cấu trúc dự án

```text
WeatherApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/weatherapp/
│   │   │   ├── MainActivity.kt
│   │   │   ├── WeatherDashboard.kt
│   │   │   ├── WeatherScreen.kt
│   │   │   ├── WeatherIconMapper.kt
│   │   │   ├── model/
│   │   │   │   ├── WeatherResponse.kt
│   │   │   │   └── ForecastResponse.kt
│   │   │   ├── network/
│   │   │   │   └── WeatherApi.kt
│   │   │   └── ui/theme/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

## ⚙️ Hướng dẫn cài đặt

### Bước 1: Clone repository

```bash
git clone https://github.com/HOANGANH0711/WeatherApp.git
```

### Bước 2: Mở dự án

1. Khởi động Android Studio.
2. Chọn **Open**.
3. Chọn thư mục `WeatherApp`.
4. Chờ Gradle đồng bộ các thư viện.

### Bước 3: Cấu hình API Key

Truy cập [OpenWeatherMap](https://openweathermap.org/api) để đăng ký và lấy API key.

Trong thư mục gốc của dự án, mở file `local.properties` và thêm:

```properties
OPENWEATHER_API_KEY=YOUR_API_KEY
```

Thay `YOUR_API_KEY` bằng API key của bạn.

Ứng dụng sử dụng `BuildConfig` để lấy giá trị cấu hình từ Gradle.

**Lưu ý:** Không đưa file `local.properties` chứa API key lên GitHub. API key được nhúng vào ứng dụng Android vẫn có thể bị trích xuất từ APK.

### Bước 4: Chạy ứng dụng

1. Mở Android Emulator hoặc kết nối điện thoại Android.
2. Đảm bảo thiết bị có kết nối Internet.
3. Nhấn **Run ▶** trong Android Studio.

Yêu cầu Android tối thiểu: **Android 8.0 (API 26)**.

## 🔌 API sử dụng

Ứng dụng sử dụng hai endpoint chính của OpenWeatherMap.

**Thời tiết hiện tại**

```http
GET /data/2.5/weather
```

**Dự báo thời tiết**

```http
GET /data/2.5/forecast
```

Các tham số:
- `q`: Tên thành phố.
- `appid`: API key.
- `units=metric`: Sử dụng đơn vị nhiệt độ Celsius.
- `cnt=8`: Giới hạn 8 mốc dự báo trong yêu cầu dự báo hiện tại.

## 📸 Hình ảnh ứng dụng

*Chưa cập nhật ảnh chụp màn hình.*

## 📚 Kiến thức áp dụng

Qua dự án này, các nội dung được thực hành bao gồm:

- Xây dựng ứng dụng Android bằng Kotlin.
- Thiết kế giao diện với Jetpack Compose.
- Tổ chức giao diện thành các Composable.
- Tích hợp REST API thông qua Ktor Client.
- Xử lý JSON bằng Kotlinx Serialization.
- Quản lý trạng thái giao diện với `remember` và `mutableStateOf`.
- Thực hiện tác vụ bất đồng bộ bằng Kotlin Coroutines.
- Hiển thị dữ liệu từ API lên giao diện.
- Cấu hình API key thông qua Gradle.

## 🚀 Hướng phát triển

- [ ] Cho phép tìm kiếm thành phố.
- [ ] Tự động xác định vị trí bằng GPS.
- [ ] Cập nhật lại dữ liệu dự báo theo giờ.
- [ ] Bổ sung dự báo nhiều ngày.
- [ ] Cải thiện xử lý lỗi kết nối mạng.
- [ ] Thêm hiệu ứng chuyển động thời tiết.
- [ ] Cải thiện giao diện và chế độ tối.
- [ ] Bổ sung Unit Test.

## 👨‍💻 Tác giả

**HOANGANH0711**

GitHub: https://github.com/HOANGANH0711

---

**WeatherApp** – Dự án học tập phát triển ứng dụng Android với Kotlin và Jetpack Compose.
