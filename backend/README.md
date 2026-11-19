# 🖥️ Phân Hệ Backend MamZi (`feat/be-api`)

Thư mục dành riêng cho **Backend Developer** phát triển hệ thống RESTful API cho ứng dụng **MamZi**.

---

## 1. Công Nghệ Bắt Buộc
- **Ngôn ngữ:** Java (JDK 17 hoặc 21).
- **Framework:** Spring Boot 3.x.
- **Bảo mật:** Spring Security + JWT (JSON Web Token) cho xác thực không trạng thái (stateless).
- **Thao tác dữ liệu:** Spring Data JPA + Hibernate.
- **Cơ sở dữ liệu:** MySQL Server.
- **Tài liệu API:** Springdoc OpenAPI (Swagger UI).

---

## 2. Cấu Trúc Mã Nguồn Tiêu Chuẩn
```text
backend/
├── src/main/java/com/mamzi/
│   ├── config/          # Cấu hình Spring Security, JWT Filter, CORS, Swagger
│   ├── controller/      # REST API Controllers (AuthController, FoodController, OrderController...)
│   ├── dto/             # Request & Response Data Transfer Objects
│   ├── entity/          # JPA Entities tương ứng các bảng MySQL
│   ├── repository/      # Spring Data JPA Repositories
│   ├── service/         # Business logic interface & implementation
│   └── MamziApplication.java
├── src/main/resources/
│   └── application.yml  # Cấu hình port, datasource MySQL, JWT secret key
└── pom.xml (hoặc build.gradle)
```

---

## 3. Danh Sách Endpoint Cần Triển Khai

### A. Phân hệ Khách hàng (User API):
* `POST /api/v1/auth/register`: Đăng ký tài khoản (Số điện thoại/Email).
* `POST /api/v1/auth/login`: Đăng nhập & trả về JWT Token.
* `GET /api/v1/foods/flash-sale`: Lấy danh sách món Flash Sale kèm thời gian kết thúc.
* `GET /api/v1/foods/best-sellers`: Lấy danh sách món bán chạy nhất.
* `GET /api/v1/categories`: Lấy danh mục món ăn (kèm icon).
* `GET /api/v1/foods/{id}`: Chi tiết món ăn, nguyên liệu, calo, đánh giá sao.
* `POST /api/v1/orders`: Tạo đơn hàng mới (tính phí ship, áp dụng voucher).
* `GET /api/v1/orders/{id}/tracking`: Lấy tọa độ quán, tọa độ khách, tọa độ tài xế để hiển thị bản đồ.
* `GET /api/v1/loyalty/profile`: Xem điểm tích lũy, phân hạng (Đồng/Bạc/Vàng/Kim Cương) và voucher khả dụng.

### B. Phân hệ Quản trị (Admin API):
* `POST /api/v1/admin/foods`: Thêm món ăn mới, gắn cờ Flash Sale / Best Seller.
* `PUT /api/v1/admin/foods/{id}`: Cập nhật thông tin, giá, trạng thái còn/hết hàng.
* `PUT /api/v1/admin/orders/{id}/status`: Cập nhật trạng thái đơn (Chờ xử lý -> Đang giao -> Hoàn thành).
* `GET /api/v1/admin/reports/revenue`: Báo cáo doanh thu theo ngày/tháng và thống kê món bán chạy.
