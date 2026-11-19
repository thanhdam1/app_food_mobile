-- =============================================================================
-- DỮ LIỆU MẪU BAN ĐẦU (SEED DATA) CHO MAMZI
-- =============================================================================

USE `mamzi_db`;

-- 1. Thêm Roles
INSERT INTO `roles` (`id`, `name`, `description`) VALUES
(1, 'ROLE_CUSTOMER', 'Khách hàng sử dụng ứng dụng đặt đồ ăn'),
(2, 'ROLE_ADMIN', 'Quản trị viên hệ thống MamZi'),
(3, 'ROLE_DRIVER', 'Tài xế giao hàng MamZi Delivery');

-- 2. Thêm Ranks
INSERT INTO `ranks` (`id`, `name`, `min_points`, `discount_percent`, `freeship_monthly`, `icon_url`) VALUES
(1, 'Đồng', 0, 0.00, 1, 'https://cdn-icons-png.flaticon.com/512/2583/2583342.png'),
(2, 'Bạc', 200, 2.00, 3, 'https://cdn-icons-png.flaticon.com/512/2583/2583319.png'),
(3, 'Vàng', 500, 5.00, 5, 'https://cdn-icons-png.flaticon.com/512/2583/2583434.png'),
(4, 'Kim Cương', 1200, 10.00, 10, 'https://cdn-icons-png.flaticon.com/512/2583/2583448.png');

-- 3. Thêm Users mẫu (Mật khẩu mặc định là: 123456 -> BCrypt hash bên dưới)
-- $2a$10$wWv352yJmFw5H9bC4Xg3hOqHqT3e7Fq0K6jA6t0q3U4nKk1rX5B4G
INSERT INTO `users` (`id`, `full_name`, `email`, `phone`, `password`, `role_id`, `rank_id`, `points`, `address`) VALUES
(1, 'Lê An (Khách Hàng)', 'anle64437@gmail.com', '0901234567', '$2a$10$wWv352yJmFw5H9bC4Xg3hOqHqT3e7Fq0K6jA6t0q3U4nKk1rX5B4G', 1, 2, 250, '123 Đường Nguyễn Huệ, Quận 1, TP.HCM'),
(2, 'Admin MamZi', 'admin@mamzi.vn', '0999888777', '$2a$10$wWv352yJmFw5H9bC4Xg3hOqHqT3e7Fq0K6jA6t0q3U4nKk1rX5B4G', 2, 4, 9999, 'Tòa nhà MamZi Tower, Quận 3, TP.HCM'),
(3, 'Nguyễn Văn Tài (Tài Xế)', 'driver.tai@mamzi.vn', '0912345678', '$2a$10$wWv352yJmFw5H9bC4Xg3hOqHqT3e7Fq0K6jA6t0q3U4nKk1rX5B4G', 3, 1, 50, 'Quận Bình Thạnh, TP.HCM');

-- 4. Thêm Danh mục món ăn
INSERT INTO `categories` (`id`, `name`, `icon_url`, `display_order`) VALUES
(1, 'Cơm & Mì', 'https://cdn-icons-png.flaticon.com/512/1046/1046784.png', 1),
(2, 'Phở & Bún', 'https://cdn-icons-png.flaticon.com/512/2722/2722574.png', 2),
(3, 'Đồ Ăn Nhanh', 'https://cdn-icons-png.flaticon.com/512/3075/3075977.png', 3),
(4, 'Trà Sữa & Đồ Uống', 'https://cdn-icons-png.flaticon.com/512/3081/3081162.png', 4),
(5, 'Ăn Vặt', 'https://cdn-icons-png.flaticon.com/512/2515/2515261.png', 5),
(6, 'Healthy & Salad', 'https://cdn-icons-png.flaticon.com/512/706/706164.png', 6);

-- 5. Thêm Món ăn (Bao gồm Flash Sale & Bán chạy)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `ingredients`, `calories`, `price`, `discount_price`, `image_url`, `is_flash_sale`, `flash_sale_end_time`, `is_best_seller`, `rating_avg`, `total_reviews`) VALUES
(1, 2, 'Phở Bò Tái Lăn Hà Nội', 'Bát phở nóng hổi với thịt bò tái xào tỏi thơm lừng, nước dùng hầm xương 24h đậm đà truyền thống.', 'Bánh phở tươi, thịt bò Úc, hành lá, ớt tươi, rau mùi, quẩy', 550, 65000.00, 39000.00, 'https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?auto=format&fit=crop&w=600&q=80', TRUE, DATE_ADD(NOW(), INTERVAL 6 HOUR), TRUE, 4.9, 128),
(2, 1, 'Cơm Tấm Sườn Bì Chả Đặc Biệt', 'Đĩa cơm tấm chuẩn vị Sài Gòn với miếng sườn nướng mật ong vàng óng, bì dai giòn, chả trứng béo ngậy.', 'Gạo tấm thơm, sườn cốt lết ướp mật ong, bì heo, chả trứng hấp, mỡ hành', 720, 55000.00, 35000.00, 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80', TRUE, DATE_ADD(NOW(), INTERVAL 6 HOUR), TRUE, 4.8, 95),
(3, 4, 'Trà Sữa Trân Châu Đường Đen', 'Trà sữa đậm vị lá trà olong hòa quyện cùng sữa tươi thanh trùng và trân châu thủ công mềm dẻo.', 'Trà đen Ceylon, sữa tươi tiệt trùng, đường nâu tự nhiên, trân châu tapioca', 380, 45000.00, 29000.00, 'https://images.unsplash.com/photo-1558857563-b37cb4b66df2?auto=format&fit=crop&w=600&q=80', TRUE, DATE_ADD(NOW(), INTERVAL 6 HOUR), TRUE, 4.7, 210),
(4, 3, 'Burger Bò Phô Mai Tan Chảy', 'Bánh burger kẹp thịt bò Wagyu nướng lửa than, kèm 2 lát phô mai Cheddar chảy mịn và sốt độc quyền MamZi.', 'Vỏ bánh mì mè, bò xay Wagyu 150g, phô mai Cheddar, xà lách, cà chua, sốt BBQ', 680, 79000.00, NULL, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=600&q=80', FALSE, NULL, TRUE, 4.9, 87),
(5, 6, 'Salad Ức Gà Sốt Mè Rang', 'Món ăn thanh đạm giàu protein cho người ăn kiêng, ức gà áp chảo xé sợi cùng rau củ tươi hữu cơ.', 'Ức gà phi lê, xà lách lolo xanh, cà chua bi, dưa leo, ngô ngọt, sốt mè rang Kewpie', 320, 59000.00, NULL, 'https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80', FALSE, NULL, FALSE, 4.6, 42),
(6, 5, 'Bánh Mì Chảo Xíu Mại Trứng Lòng Đào', 'Chảo gang sôi xèo xèo với pate gan ngỗng béo thơm, xíu mại sốt cà chua và bánh mì nóng giòn.', 'Bánh mì giòn, pate gan, viên xíu mại thịt heo, trứng gà ta, sốt cà chua thảo mộc', 580, 49000.00, 32000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=600&q=80', TRUE, DATE_ADD(NOW(), INTERVAL 6 HOUR), TRUE, 4.8, 76);

-- 6. Thêm Vouchers khuyến mãi
INSERT INTO `vouchers` (`id`, `code`, `title`, `description`, `discount_type`, `discount_value`, `min_order_amount`, `max_discount_amount`, `points_required`, `start_date`, `end_date`, `usage_limit`) VALUES
(1, 'MAMZIWELCOME', 'Giảm 50% cho khách hàng mới', 'Áp dụng cho đơn hàng đầu tiên của bạn tại MamZi', 'PERCENT', 50.00, 50000.00, 40000.00, 0, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1000),
(2, 'FREESHIP15K', 'Miễn phí giao hàng 15.000đ', 'Giảm trực tiếp 15k phí vận chuyển', 'FREESHIP', 15000.00, 70000.00, 15000.00, 50, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 500),
(3, 'MAMZIVIP20', 'Giảm 20.000đ cho đơn từ 100.000đ', 'Ưu đãi dành riêng cho thành viên', 'FIXED', 20000.00, 100000.00, 20000.00, 100, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 200);

-- 7. Thêm Tài xế mẫu (Phục vụ Google Maps Tracking)
-- Tọa độ demo tại trung tâm TP.HCM (Quận 1 / Quận 3)
INSERT INTO `drivers` (`id`, `user_id`, `vehicle_name`, `license_plate`, `current_latitude`, `current_longitude`, `status`, `rating`) VALUES
(1, 3, 'Yamaha Exciter 155', '59-B1 678.99', 10.776889, 106.700806, 'AVAILABLE', 4.9);
