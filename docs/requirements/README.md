# 📋 Đặc Tả Yêu Cầu & Thiết Kế UI/UX MamZi (`docs/requirement`)

Thư mục dành riêng cho vị trí **Business Analyst (BA) / Requirement Engineer**.

---

## 1. Định Vị Thương Hiệu & Phong Cách Thiết Kế
* **Tên ứng dụng:** **MamZi**
* **Slogan:** "Thèm là măm"
* **Bảng màu chủ đạo:**
  * **Primary (Chủ đạo):** Xanh lá cây `#2E7D32` hoặc `#10B981` (Tươi mát, thực phẩm sạch, an tâm).
  * **Secondary / Background:** Trắng tinh khiết `#FFFFFF` & Xám sáng `#F8F9FA` (Không gian sang trọng, thoáng đãng).
  * **Accent (Điểm nhấn):** Vàng cam `#FFA000` (Dùng cho Badge Flash Sale, Đánh giá sao ⭐).
* **Nguyên tắc UI/UX:** Material Design, bo góc mềm mại (`cornerRadius = 16dp - 24dp`), elevation đổ bóng tinh tế, hiệu ứng chuyển động Lottie mượt mà.

---

## 2. Chi Tiết Tính Năng Phân Hệ

### A. Phân hệ Khách hàng (User App)
1. **Màn hình Splash & Chào mừng:**
   * Animation Lottie logo MamZi chuyển động mượt mà cùng slogan "Thèm là măm".
   * Kiểm tra phiên đăng nhập (JWT token), tự động chuyển vào Home nếu đã đăng nhập.
2. **Xác thực (Authentication):**
   * Đăng ký / Đăng nhập bằng Số điện thoại hoặc Email.
   * Chọn avatar mặc định hoặc tải ảnh cá nhân.
3. **Màn hình Home (Trang chủ hiện đại):**
   * *Section 1:* Banner quảng cáo Auto-slider cuộn ngang.
   * *Section 2:* **Flash Sale trong ngày** có đồng hồ đếm ngược (Countdown Timer `HH:mm:ss`).
   * *Section 3:* **Món ngon bán chạy** (Best Sellers) hiển thị dạng thẻ card nổi bật.
   * *Section 4:* Danh mục món ăn dạng lưới icon tròn bo góc.
4. **Chi tiết món ăn (Food Details):**
   * Ảnh to sắc nét bo tròn góc dưới, mô tả nguyên liệu, lượng calo ước tính.
   * Danh sách đánh giá sao và bình luận của khách hàng.
5. **Giỏ hàng & Thanh toán (Checkout):**
   * Tăng giảm số lượng món, áp dụng Voucher giảm giá/Freeship.
   * Phương thức thanh toán: Tiền mặt (COD), Chuyển khoản ngân hàng, Ví điện tử.
   * Hiển thị hóa đơn (Invoice) điện tử ngay sau khi đặt hàng thành công.
6. **Theo dõi đơn hàng & Bản đồ (Tracking & Google Maps):**
   * Hiển thị tuyến đường (Route) từ nhà hàng đến vị trí khách hàng.
   * Card thông tin tài xế: Họ tên, ảnh, số điện thoại, biển số xe, trạng thái di chuyển.
7. **Khách hàng thân thiết (Loyalty & Rank):**
   * Phân cấp: Đồng -> Bạc -> Vàng -> Kim Cương.
   * Điểm thưởng tích lũy sau mỗi hóa đơn, đổi voucher ưu đãi.

### B. Phân hệ Quản trị (Admin Panel)
1. **Quản lý thực đơn:** Thêm, sửa, xóa món, bật/tắt Flash Sale.
2. **Quản lý đơn hàng:** Tiếp nhận đơn, cập nhật tiến độ (Chờ duyệt -> Đang nấu -> Đang giao -> Hoàn tất).
3. **Báo cáo doanh thu:** Biểu đồ doanh thu theo ngày/tháng, top món ăn sinh lời cao nhất.
