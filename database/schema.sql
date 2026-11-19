-- =============================================================================
-- HỆ THỐNG ĐẶT ĐỒ ĂN TRỰC TUYẾN MAMZI - "THÈM LÀ MĂM"
-- BƯỚC 1: CƠ SỞ DỮ LIỆU CHUẨN QUAN HỆ (MYSQL DDL SCHEMA)
-- =============================================================================

CREATE DATABASE IF NOT EXISTS `mamzi_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `mamzi_db`;

-- Tắt kiểm tra khóa ngoại tạm thời để xóa/tạo bảng sạch sẽ
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `loyalty_transactions`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `order_details`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `drivers`;
DROP TABLE IF EXISTS `vouchers`;
DROP TABLE IF EXISTS `foods`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `ranks`;
DROP TABLE IF EXISTS `roles`;

SET FOREIGN_KEY_CHECKS = 1;

-- -----------------------------------------------------------------------------
-- 1. BẢNG PHÂN QUYỀN (ROLES)
-- -----------------------------------------------------------------------------
CREATE TABLE `roles` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT 'ROLE_CUSTOMER, ROLE_ADMIN, ROLE_DRIVER',
    `description` VARCHAR(255) NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. BẢNG PHÂN HẠNG KHÁCH HÀNG THÂN THIẾT (RANKS)
-- -----------------------------------------------------------------------------
CREATE TABLE `ranks` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT 'Đồng, Bạc, Vàng, Kim Cương',
    `min_points` INT NOT NULL DEFAULT 0 COMMENT 'Số điểm tối thiểu để đạt hạng',
    `discount_percent` DECIMAL(5, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tỉ lệ giảm giá cố định cho hạng (%)',
    `freeship_monthly` INT NOT NULL DEFAULT 0 COMMENT 'Số lượt freeship tặng mỗi tháng',
    `icon_url` VARCHAR(500) NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. BẢNG NGƯỜI DÙNG (USERS)
-- -----------------------------------------------------------------------------
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(120) NULL UNIQUE,
    `phone` VARCHAR(20) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã mã hóa BCrypt',
    `avatar_url` VARCHAR(500) DEFAULT 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80',
    `role_id` INT NOT NULL DEFAULT 1,
    `rank_id` INT NOT NULL DEFAULT 1,
    `points` INT NOT NULL DEFAULT 0 COMMENT 'Điểm tích lũy hiện có',
    `address` VARCHAR(255) NULL,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_users_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
    CONSTRAINT `fk_users_ranks` FOREIGN KEY (`rank_id`) REFERENCES `ranks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 4. BẢNG DANH MỤC MÓN ĂN (CATEGORIES)
-- -----------------------------------------------------------------------------
CREATE TABLE `categories` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `icon_url` VARCHAR(500) NOT NULL,
    `display_order` INT NOT NULL DEFAULT 0,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 5. BẢNG MÓN ĂN (FOODS)
-- -----------------------------------------------------------------------------
CREATE TABLE `foods` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category_id` INT NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT NULL,
    `ingredients` VARCHAR(500) NULL COMMENT 'Nguyên liệu chính (vd: Bò tái, bánh phở, hành hoa...)',
    `calories` INT NULL COMMENT 'Hàm lượng calo ước tính (Kcal)',
    `price` DECIMAL(12, 2) NOT NULL COMMENT 'Giá gốc (VND)',
    `discount_price` DECIMAL(12, 2) NULL COMMENT 'Giá sau giảm nếu có Flash sale',
    `image_url` VARCHAR(500) NOT NULL,
    `is_flash_sale` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Có trong đợt Flash sale không',
    `flash_sale_end_time` DATETIME NULL COMMENT 'Thời gian kết thúc đếm ngược Flash sale',
    `is_best_seller` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_available` BOOLEAN NOT NULL DEFAULT TRUE,
    `rating_avg` DECIMAL(2, 1) NOT NULL DEFAULT 5.0,
    `total_reviews` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_foods_categories` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 6. BẢNG MÃ GIẢM GIÁ (VOUCHERS)
-- -----------------------------------------------------------------------------
CREATE TABLE `vouchers` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) NOT NULL UNIQUE,
    `title` VARCHAR(150) NOT NULL,
    `description` VARCHAR(255) NULL,
    `discount_type` ENUM('PERCENT', 'FIXED', 'FREESHIP') NOT NULL DEFAULT 'PERCENT',
    `discount_value` DECIMAL(10, 2) NOT NULL COMMENT 'Giá trị giảm (% hoặc số tiền VND)',
    `min_order_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    `max_discount_amount` DECIMAL(12, 2) NULL COMMENT 'Số tiền giảm tối đa nếu tính theo %',
    `points_required` INT NOT NULL DEFAULT 0 COMMENT 'Số điểm đổi nếu dùng điểm quy đổi',
    `start_date` DATETIME NOT NULL,
    `end_date` DATETIME NOT NULL,
    `usage_limit` INT NOT NULL DEFAULT 100,
    `used_count` INT NOT NULL DEFAULT 0,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 7. BẢNG TÀI XẾ (DRIVERS) - PHỤC VỤ THEO DÕI GOOGLE MAPS
-- -----------------------------------------------------------------------------
CREATE TABLE `drivers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `vehicle_name` VARCHAR(100) NOT NULL COMMENT 'Tên phương tiện (vd: Honda Wave Alpha)',
    `license_plate` VARCHAR(20) NOT NULL COMMENT 'Biển số xe (vd: 59-P1 999.88)',
    `current_latitude` DECIMAL(10, 7) NOT NULL COMMENT 'Vĩ độ GPS hiện tại',
    `current_longitude` DECIMAL(10, 7) NOT NULL COMMENT 'Kinh độ GPS hiện tại',
    `status` ENUM('AVAILABLE', 'BUSY', 'OFFLINE') NOT NULL DEFAULT 'AVAILABLE',
    `rating` DECIMAL(2, 1) NOT NULL DEFAULT 5.0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_drivers_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 8. BẢNG ĐƠN HÀNG (ORDERS)
-- -----------------------------------------------------------------------------
CREATE TABLE `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_code` VARCHAR(30) NOT NULL UNIQUE COMMENT 'Mã đơn định dạng: MZ-YYYYMMDD-XXXX',
    `user_id` BIGINT NOT NULL,
    `driver_id` BIGINT NULL,
    `voucher_id` INT NULL,
    `status` ENUM('PENDING', 'PREPARING', 'DELIVERING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    `payment_method` ENUM('COD', 'BANKING', 'EWALLET') NOT NULL DEFAULT 'COD',
    `payment_status` ENUM('UNPAID', 'PAID') NOT NULL DEFAULT 'UNPAID',
    `subtotal` DECIMAL(12, 2) NOT NULL COMMENT 'Tổng tiền món ăn',
    `discount_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    `shipping_fee` DECIMAL(12, 2) NOT NULL DEFAULT 15000.00,
    `total_amount` DECIMAL(12, 2) NOT NULL COMMENT 'Số tiền thanh toán cuối cùng',
    `delivery_address` VARCHAR(255) NOT NULL,
    `delivery_latitude` DECIMAL(10, 7) NULL,
    `delivery_longitude` DECIMAL(10, 7) NULL,
    `customer_note` VARCHAR(255) NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `completed_at` DATETIME NULL,
    CONSTRAINT `fk_orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_orders_drivers` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`),
    CONSTRAINT `fk_orders_vouchers` FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 9. BẢNG CHI TIẾT ĐƠN HÀNG (ORDER_DETAILS)
-- -----------------------------------------------------------------------------
CREATE TABLE `order_details` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `food_id` BIGINT NOT NULL,
    `food_name` VARCHAR(150) NOT NULL,
    `price` DECIMAL(12, 2) NOT NULL,
    `quantity` INT NOT NULL,
    `total_price` DECIMAL(12, 2) NOT NULL,
    `note` VARCHAR(255) NULL COMMENT 'Ghi chú món: Ít đường, cay, không hành...',
    CONSTRAINT `fk_order_details_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_details_foods` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 10. BẢNG ĐÁNH GIÁ MÓN ĂN (REVIEWS)
-- -----------------------------------------------------------------------------
CREATE TABLE `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `food_id` BIGINT NOT NULL,
    `rating` INT NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
    `comment` TEXT NULL,
    `image_url` VARCHAR(500) NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_reviews_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `fk_reviews_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_reviews_foods` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 11. BẢNG GIAO DỊCH ĐIỂM THƯỞNG (LOYALTY_TRANSACTIONS)
-- -----------------------------------------------------------------------------
CREATE TABLE `loyalty_transactions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `order_id` BIGINT NULL,
    `points_change` INT NOT NULL COMMENT '+ điểm khi hoàn tất đơn, - điểm khi đổi voucher',
    `reason` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_loyalty_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_loyalty_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- TẠO CHỈ MỤC (INDEXES) TỐI ƯU HIỆU NĂNG TÌM KIẾM
-- -----------------------------------------------------------------------------
CREATE INDEX `idx_foods_flash_sale` ON `foods` (`is_flash_sale`, `flash_sale_end_time`);
CREATE INDEX `idx_foods_best_seller` ON `foods` (`is_best_seller`);
CREATE INDEX `idx_orders_user_status` ON `orders` (`user_id`, `status`);
CREATE INDEX `idx_orders_code` ON `orders` (`order_code`);
