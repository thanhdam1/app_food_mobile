# 🍔 Food Mobile App (Đồ Án Nhóm 5 Người)

Dự án ứng dụng di động đặt món và gợi ý món ăn thông minh tích hợp AI (Gemini).

---

## 🌳 Cấu Trúc Nhánh (Git Branches) & Phân Công

Dự án gồm **7 nhánh** được phân chia theo vai trò để tránh xung đột mã nguồn (Merge Conflict):

| STT | Nhánh Git | Vai trò đảm nhiệm | Phạm vi thư mục phụ trách |
| :---: | :--- | :--- | :--- |
| 1 | `main` | Production / Bản nộp cuối | Chứa phiên bản ổn định nhất của dự án |
| 2 | `develop` | Tích hợp chung | Nhánh trung tâm để các thành viên ghép code |
| 3 | `feat/fe-android` | Frontend (Mobile Dev) | Thư mục `app/` (Jetpack Compose, UI, Gọi API) |
| 4 | `feat/be-api` | Backend (API Dev) | Thư mục `backend/` (Server, RESTful API, Swagger) |
| 5 | `feat/database` | Database & AI | Thư mục `database/` (ERD, SQL, Seed Data, AI Prompt) |
| 6 | `docs/requirement` | Requirement (BA) | Thư mục `docs/requirements/` (SRS, Figma, Usecase) |
| 7 | `docs/report` | QA / Report / PM | Thư mục `docs/reports/` (Báo cáo, Slide, Test Cases) |

---

## 📁 Cấu Trúc Thư Mục Dự Án

```text
app_food_mobile/
├── app/                  # [Frontend] Mã nguồn ứng dụng Android (Jetpack Compose)
├── backend/              # [Backend] Mã nguồn Server API (NodeJS / Python / Spring Boot...)
├── database/             # [Data] Lược đồ CSDL, SQL script, dữ liệu mẫu, AI prompt
├── docs/
│   ├── requirements/     # [Requirement] Tài liệu đặc tả yêu cầu, Figma link, Usecase
│   └── reports/          # [Report] Báo cáo đồ án, Slide thuyết trình, Test cases
├── gradle/               # Cấu hình Gradle cho Android
└── README.md             # Hướng dẫn tổng quan
```

---

## 🚀 Quy Trình Làm Việc Với Git (Git Workflow)

1. **Lấy code mới nhất về trước khi làm việc:**
   ```bash
   git checkout develop
   git pull origin develop
   ```

2. **Chuyển sang nhánh của mình:**
   ```bash
   # Ví dụ bạn làm Frontend:
   git checkout feat/fe-android
   git merge develop   # Cập nhật thay đổi mới nhất từ develop vào nhánh của mình
   ```

3. **Commit và đẩy lên GitHub:**
   ```bash
   git add .
   git commit -m "feat(fe): hoàn thiện màn hình chi tiết món ăn"
   git push origin feat/fe-android
   ```

4. **Tạo Pull Request (PR):**
   * Mở Pull Request từ nhánh của bạn vào nhánh `develop`.
   * Cả nhóm duyệt và merge vào `develop`.
   * **Lưu ý:** Tuyệt đối không commit hoặc push thẳng vào `main`.
