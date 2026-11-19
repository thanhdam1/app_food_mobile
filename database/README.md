# 🗄️ Phân Hệ Cơ Sở Dữ Liệu MamZi (`feat/database`)

Thư mục dành riêng cho vị trí **Data & Database**, chịu trách nhiệm thiết kế, tối ưu hóa và cung cấp dữ liệu cho hệ sinh thái **MamZi**.

---

## 1. Công Nghệ & Tiêu Chuẩn
- **Hệ quản trị CSDL:** MySQL 8.0+.
- **Bảng mã (Charset):** `utf8mb4` và collation `utf8mb4_unicode_ci` (hỗ trợ đầy đủ tiếng Việt có dấu và emoji).
- **Chuẩn hóa:** Đạt chuẩn 3NF, có khóa ngoại (Foreign Key) đảm bảo tính toàn vẹn tham chiếu.

---

## 2. Danh Sách Các Bảng CSDL
1. **`roles`**: Phân quyền hệ thống (`ROLE_CUSTOMER`, `ROLE_ADMIN`, `ROLE_DRIVER`).
2. **`ranks`**: Bảng cấp bậc thành viên (`Đồng`, `Bạc`, `Vàng`, `Kim Cương`) và mức ưu đãi.
3. **`users`**: Tài khoản người dùng, số điện thoại, mật khẩu mã hóa BCrypt, điểm tích lũy.
4. **`categories`**: Danh mục món ăn (Cơm, Phở, Đồ uống, Ăn vặt, v.v.).
5. **`foods`**: Danh sách món ăn, giá bán, giá khuyến mãi, cờ Flash Sale, thời gian đếm ngược, calo, nguyên liệu.
6. **`vouchers`**: Mã giảm giá, loại giảm (phần trăm, tiền mặt, freeship), điều kiện áp dụng.
7. **`drivers`**: Thông tin tài xế, biển số xe, số điện thoại, tọa độ GPS hiện tại (latitude, longitude).
8. **`orders`**: Hóa đơn đặt hàng, trạng thái, phương thức thanh toán (COD, Ngân hàng, Ví điện tử), tổng tiền.
9. **`order_details`**: Chi tiết từng món trong hóa đơn (món ăn, đơn giá, số lượng, ghi chú).
10. **`reviews`**: Đánh giá và bình luận của khách hàng về món ăn (1 - 5 sao).
11. **`loyalty_transactions`**: Lịch sử cộng/trừ điểm tích lũy sau mỗi đơn hàng.

---

## 3. Các Tệp Tin Chính
* [`schema.sql`](file:///c:/Users/KhanhHV/.gemini/antigravity/scratch/app_food_mobile/database/schema.sql): Script tạo toàn bộ bảng DDL, khóa chính, khóa ngoại và chỉ mục (Index).
* [`seed_data.sql`](file:///c:/Users/KhanhHV/.gemini/antigravity/scratch/app_food_mobile/database/seed_data.sql): Dữ liệu mẫu phong phú bằng tiếng Việt cho món ăn, danh mục, flash sale, tài xế và voucher.
