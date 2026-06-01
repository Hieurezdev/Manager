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

## Build toàn bộ mã nguồna
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

## Chạy toàn bộ hệ thống bằng Docker (Cho Windows/Mac)

Dự án có sẵn cấu hình `docker-compose.yml` để bạn khởi tạo nhanh môi trường mà không cần cài đặt MySQL thủ công.

**Cách 1: Chỉ chạy Database MySQL (Khuyên dùng để dev/test trên máy tính cá nhân)**
Nếu bạn muốn code và chạy giao diện trực tiếp trên phần mềm (IntelliJ/Eclipse) nhưng không muốn cài đặt MySQL server nặng nề:
1. Mở Terminal (PowerShell hoặc CMD) tại thư mục gốc của dự án.
2. Chạy lệnh dựng riêng container MySQL:
   ```bash
   docker compose up -d mysql
   ```
3. Database sẽ tự động khởi tạo với cấu hình `root` / `Hch301105#` và nạp sẵn dữ liệu.
*(Lưu ý: Theo cấu hình hiện tại, cổng ngoài của DB là `3307`. Nếu bạn chạy code ở ngoài Docker, hãy trỏ port về `3307` trong code hoặc đổi port ở file docker-compose.yml thành `3306:3306`).*

**Cách 2: Chạy toàn bộ hệ thống (App + Database)**
Lệnh này sẽ build ứng dụng Java thành ảnh Docker và chạy song song cùng database:
```bash
docker compose up --build
```
*(Lưu ý đối với Windows: Việc chạy giao diện Java Swing GUI bên trong Docker Container đòi hỏi bạn phải cài đặt thêm X-Server cho Windows (như VcXsrv) và cấu hình biến môi trường `DISPLAY` thì cửa sổ ứng dụng mới có thể hiện lên màn hình. Do đó, khuyến khích sử dụng **Cách 1**).*

Để tắt hệ thống Docker khi không sử dụng nữa:
```bash
docker compose down
```
