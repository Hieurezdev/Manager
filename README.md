# Manager - Hướng dẫn chạy và test

## Yêu cầu
- JDK 11+ (Khuyên dùng Java 17+)
- Hệ quản trị CSDL MySQL (Port 3306)
- Thư viện kết nối: `mysql-connector-j-8.0.33.jar`

## Cài đặt Cơ sở dữ liệu MySQL
1. Tải thư viện JDBC MySQL: [mysql-connector-j-8.0.33.jar](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar) và đặt file `.jar` vào thư mục gốc của dự án này. *(Thư viện này đã được cấu hình `.gitignore` nên sẽ không bị đẩy lên Git)*.
2. Đảm bảo MySQL server đang chạy trên máy của bạn với tài khoản và mật khẩu đã cấu hình trong tệp `src/com/example/manager/dao/DBConnection.java`.
3. Chạy lệnh sau để tự động khởi tạo database và nạp toàn bộ dữ liệu mẫu (chạy trên PowerShell):
   ```bash
   javac -encoding UTF-8 -d out -sourcepath src src/com/example/manager/AddSampleData.java
   java -cp "out;mysql-connector-j-8.0.33.jar" com.example.manager.AddSampleData
   ```

## Build toàn bộ mã nguồn
Từ thư mục gốc dự án (dành cho Windows PowerShell):
```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | javac -encoding UTF-8 -d out @-
```
*(Lưu ý: Luôn biên dịch với tùy chọn `-encoding UTF-8` để tránh lỗi vỡ font tiếng Việt).*

## Chạy Unit Test & Integration Test
Hệ thống hiện tại hỗ trợ cơ chế chạy song song (Dual-Mode): Nếu kết nối được MySQL thì tự động chạy tương tác với CSDL thật; nếu MySQL offline thì tự động dùng dữ liệu giả lập (Mock).

**1. Kiểm thử phân hệ Thống kê (TestRunner)**
```bash
java -cp "out;mysql-connector-j-8.0.33.jar" com.example.manager.TestRunner
```

**2. Kiểm thử phân hệ Trả vé (TestTraVe)**
Bộ kiểm thử này tự động dọn dẹp và khôi phục (reset) dữ liệu trước và sau khi chạy, do đó bạn có thể chạy nhiều lần liên tục (idempotent).
```bash
java -cp "out;mysql-connector-j-8.0.33.jar" com.example.manager.TestTraVe
```

## Ghi chú cho Team
- **Tuyệt đối không push file `.jar` lên Git** để tránh làm phình dung lượng. Kho lưu trữ chỉ chứa Code. Mọi thành viên khi clone code về cần tự thực hiện Bước 1 trong phần Cài đặt ở trên.
