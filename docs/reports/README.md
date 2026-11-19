# 📊 Báo Cáo Đồ Án, Kiểm Thử & Quản Trị Dự Án MamZi (`docs/report`)

Thư mục dành riêng cho vị trí **QA / Tester / Project Manager & Technical Writer**.

---

## 1. Nhiệm Vụ Trọng Tâm
1. **Quản lý tiến độ dự án:**
   * Lập sơ đồ phân rã công việc (WBS) và theo dõi tiến độ các thành viên trên Trello / GitHub Projects.
   * Đảm bảo tiến độ bàn giao đúng lộ trình 5 bước.
2. **Kế hoạch Kiểm thử chất lượng (QA/QC Testing):**
   * Lập bảng kịch bản kiểm thử (Test Cases) chi tiết cho cả 2 phân hệ.
   * Thực hiện test trên thiết bị thật và máy ảo Android, ghi lại Bug Report cho bạn FE và BE khắc phục.
3. **Soạn thảo tài liệu báo cáo & thuyết trình:**
   * Soạn thảo Báo cáo đồ án hoàn chỉnh (file Word / PDF) theo mẫu chuẩn học viện/trường đại học.
   * Thiết kế Slide trình chiếu bảo vệ đồ án tông màu Xanh lá & Trắng đồng bộ nhận diện MamZi.

---

## 2. Danh Mục Test Cases Bắt Buộc Cần Kiểm Tra
* **TC-AUTH-01:** Đăng nhập thành công với tài khoản hợp lệ, nhận đúng JWT Token.
* **TC-HOME-01:** Đồng hồ Flash Sale đếm ngược chính xác theo thời gian thực (realtime).
* **TC-CART-01:** Thêm món vào giỏ hàng, cập nhật tổng tiền chính xác khi thay đổi số lượng.
* **TC-PAY-01:** Áp dụng mã Voucher giảm giá hợp lệ, kiểm tra trừ tiền đúng điều kiện.
* **TC-MAP-01:** Tải Google Maps API, hiển thị Marker quán, Marker khách hàng và vẽ Route chỉ đường.
* **TC-TRACK-01:** Hiển thị đúng thông tin tài xế đang giao đơn hàng.
