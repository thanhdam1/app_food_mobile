# 🥗 MamZi - "Thèm là măm"
> **Hệ thống đặt đồ ăn trực tuyến (Khách hàng & Quản trị)**  
> **Chủ đề thiết kế:** Xanh lá (Green - Thực phẩm tươi sạch) & Trắng (White - Sang trọng, tinh tế).  
> **Phong cách UI/UX:** Material Design hiện đại, bo góc mềm mại, đổ bóng elevation tinh tế, chuyển động Lottie mượt mà.

---

## 🛠️ Stack Công Nghệ Bắt Buộc

* **Frontend (Android Native):** **100% Java + XML Layouts** (Tuyệt đối KHÔNG dùng Kotlin hay Jetpack Compose).
  * Kiến trúc: **MVVM (Model - View - ViewModel)**.
  * Thư viện chính: **Retrofit2** (Networking), **Glide** (Image Loader), **Lottie** (Animation), **Google Maps SDK** (Tracking vị trí & chỉ đường).
* **Backend:** **Java Spring Boot**, **Spring Security (JWT)**, **Spring Data JPA**.
* **Database:** **MySQL** (Quan hệ chuẩn hóa, phân vùng bảng rõ ràng).

---

## 🌳 Phân Chia 7 Nhánh Git & 5 Vị Trí Thành Viên

| STT | Nhánh Git | Vai trò đảm nhiệm | Phạm vi thư mục & Trách nhiệm chính |
| :---: | :--- | :--- | :--- |
| 1 | `main` | Bản chính thức (Release) | Nhánh nộp bài / deploy sản phẩm hoàn chỉnh |
| 2 | `develop` | Tích hợp (Integration) | Nơi merge mã nguồn của cả 5 thành viên |
| 3 | `feat/fe-android` | **Frontend Developer** | Thư mục `app/`: Lập trình Android bằng **Java + XML**, cấu trúc MVVM, giao diện Xanh/Trắng, Retrofit2, Lottie, Google Maps. |
| 4 | `feat/be-api` | **Backend Developer** | Thư mục `backend/`: Xây dựng API bằng **Java Spring Boot**, bảo mật Spring Security JWT, Spring Data JPA, viết tài liệu Swagger. |
| 5 | `feat/database` | **Data & Database** | Thư mục `database/`: Thiết kế CSDL **MySQL**, file `schema.sql`, dữ liệu mẫu `seed_data.sql`, quản lý bảng Ranks & Vouchers. |
| 6 | `docs/requirement` | **Requirement (BA)** | Thư mục `docs/requirements/`: Viết đặc tả SRS, User Stories, biểu đồ Use Case, luồng Activity, thiết kế **Figma** chuẩn Xanh/Trắng. |
| 7 | `docs/report` | **QA / Tester & Report** | Thư mục `docs/reports/`: Lập kế hoạch dự án, bảng Test Cases, kiểm thử app MamZi, viết báo cáo Word/PDF và Slide bảo vệ. |

---

## 📁 Cấu Trúc Thư Mục Dự Án

```text
app_food_mobile/
├── app/                  # [Frontend] Ứng dụng Android Native (Java + XML, MVVM)
├── backend/              # [Backend] API Server (Java Spring Boot, Spring Security JWT)
├── database/             # [Data] Lược đồ CSDL MySQL (schema.sql, seed_data.sql)
├── docs/
│   ├── requirements/     # [BA] Tài liệu đặc tả yêu cầu, link Figma thiết kế
│   └── reports/          # [QA/Report] Báo cáo đồ án, Slide thuyết trình, Test cases
└── README.md             # Tổng quan dự án MamZi
```

---

## 🗺️ Lộ Trình Triển Khai (Step-by-Step)
- **Bước 1:** Thiết kế Database MySQL (`schema.sql` + `seed_data.sql`).
- **Bước 2:** Xây dựng Backend Spring Boot (Cấu hình `application.yml`, JWT Auth, Flash Sale & Food API).
- **Bước 3:** Khởi tạo Android Java + XML (MVVM, Lottie Splash, XML Layout Home tông Xanh/Trắng).
- **Bước 4:** Kết nối Retrofit2 vào Android (Gọi API hiển thị RecyclerView Home, Food Details).
- **Bước 5:** Tích hợp Google Maps (Vẽ đường giao hàng, Tài xế di chuyển) & Thanh toán/Hóa đơn.
