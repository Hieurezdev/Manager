D3: MODULE THỐNG KÊ CHUYẾN TÀU THEO DOANH THU 
I. Pha xác định yêu cầu
Mục đích: Module cho phép người quản lý (QL) thực hiện việc thống kê và đánh giá doanh thu của các chuyến tàu trong một khoảng thời gian nhất định, bao gồm:
Chọn khoảng thời gian cần thực hiện thống kê (ngày bắt đầu - kết thúc)
Xem danh sách chi tiết các chuyến tàu
Sắp xếp danh sách kết quả tự động theo doanh thu từ cao đến thấp Hệ thống hỗ trợ quản lý nắm bắt chính xác tình hình kinh doanh, đánh giá hiệu suất khai thác các chuyến tàu và đưa ra các quyết định điều phối, tối ưu hóa doanh thu hiệu quả.
1.1. Mô tả nghiệp vụ module “Quản lý thống kê chuyến tàu theo doanh thu“: 
Hệ thống phải cho phép người quản lý (QL) chọn chức năng thống kê các chuyến tàu theo doanh thu từ danh mục chức năng quản lý.
Hệ thống phải hiển thị giao diện chọn thời gian thống kê, cho phép QL lựa chọn ngày bắt đầu và ngày kết thúc.
Sau khi QL chọn xong thời gian và bấm nút thống kê, hệ thống phải thực hiện tính toán và hiển thị danh sách các chuyến tàu chi tiết thỏa mãn điều kiện thời gian lọc.
Tại danh sách kết quả hiển thị, hệ thống bắt buộc cung cấp đầy đủ các thông tin chi tiết của từng chuyến tàu
Hệ thống phải tự động sắp xếp danh sách chuyến tàu trả về theo tiêu chí doanh thu theo thứ tự giảm dần (từ cao đến thấp).
1.2. Mô tả usecase:
Tra cứu: UC cho phép tìm kiếm các thông tin chuyến tàu trong khoảng thời gian xác định
Nhập ngày bắt đầu: UC cho phép đặt thời gian tìm kiếm bắt đầu
Nhập ngày kết thúc: UC cho phép đặt thời gian tìm kiếm kết thúc
II. Pha phân tích hệ thống 
2.1. Kịch bản:
Mục tiêu: Giúp cán bộ quản lý đánh giá hiệu quả kinh doanh của từng chuyến tàu dựa trên các chỉ số hiệu suất trung bình trong một khoảng thời gian nhất định, từ đó đưa ra quyết định điều chỉnh lịch trình hoặc giá vé.
Luồng nghiệp vụ chính:

Quản lý truy cập vào menu và chọn chức năng "Thống kê chuyến tàu theo doanh thu".
Hệ thống hiển thị giao diện thiết lập tham số thời gian bao gồm: Ngày bắt đầu và Ngày kết thúc.
Quản lý chọn khoảng thời gian cần xem báo cáo (Ví dụ: từ 01/06/2026 đến 31/06/2026) và nhấn nút "Thống kê Doanh Thu".
Hệ thống thực hiện quét toàn bộ các chuyến tàu đã hoàn thành hành trình trong khoảng thời gian trên.
Hệ thống sắp xếp danh sách theo cột "Doanh thu chuyến" giảm dần 
Hệ thống hiển thị bảng kết quả thống kê lên màn hình:

Mã lịch trình
Tên tàu
Ga đầu
Ga cuối
Ngày khởi hành
Số vé bán
Tỉ lệ lấp đầy
Doanh thu chuyến (VNĐ)
LT-SE1-01
Thống Nhất 1
Hà Nội 
Đà Nẵng
20/05/2026
452
94.1%
560,000,000
LT-SE3-01
Thống Nhất 3
Hải Phòng 
Lào Cai
21/05/2026
320
80.0%
410,000,000
LT-NA1-01
Tàu Vinh
Vinh 
Huế
22/05/2026
290
90.6%
120,000,000




Luồng ngoại lệ:
Tại bước 3 (Ngày không hợp lệ): Nếu Ngày kết thúc nhỏ hơn Ngày bắt đầu, hệ thống báo lỗi: "Ngày kết thúc không được nhỏ hơn ngày bắt đầu. Vui lòng chọn lại!"
Tại bước 4 (Không có dữ liệu): Nếu không có chuyến tàu nào vận hành trong kỳ, hệ thống hiển thị thông báo: "Không có dữ liệu vận hành trong khoảng thời gian đã chọn."
2.2. Phân tích chi tiết chức năng
Phân tích chi tiết chức năng thống kê (bỏ qua giai đoạn đăng nhập) diễn ra như sau:
Click vào nút thống kê doanh thu chuyến tàu -> giao diện thống kê hiện lên -> đề xuất lớp ManHinhThongKe, có các ô nhập Ngày bắt đầu, Ngày kết thúc, nút Thống Kê Doanh Thu, và bảng danh sách kết quả gồm các cột: Mã lịch trình, Tên tàu, Ga đầu, Ga cuối, Ngày khởi hành, Số vé bán, Tỉ lệ lấp đầy, Doanh thu chuyến (VNĐ).
Nhập Ngày bắt đầu, Ngày kết thúc và click nút Thống Kê Doanh Thu -> hệ thống thực hiện xác thực khoảng thời gian nhập vào -> cần chức năng kiemTraHopLe(ngayBD, ngayKT) -> chức năng này là hành động của đối tượng BaoCao.
Khoảng thời gian hợp lệ -> hệ thống khởi tạo thực thể báo cáo rỗng để lưu trữ thời gian thống kê -> cần chức năng taoMoiBaoCao(ngayBD, ngayKT) -> chức năng này là hành động của đối tượng BaoCao.
Hệ thống truy xuất danh sách toàn bộ các đoàn tàu đang hoạt động để lấy thông tin Tên tàu -> cần chức năng layDanhSachDoanTau() -> chức năng này là hành động của đối tượng DoanTau.
Với từng đoàn tàu tìm thấy, hệ thống lấy ra danh sách các lịch trình chạy thực tế đã hoàn thành trong kỳ để làm cơ sở thống kê -> cần chức năng layDanhSachLichTrinhTrongKy(maTau, ngayBD, ngayKT) -> chức năng này là hành động của đối tượng LichTrinh.
Với mỗi lịch trình hợp lệ, hệ thống truy xuất toàn bộ các vé tàu có trạng thái "Đã bán" -> cần chức năng layDanhSachVeTheoLichTrinh(maLichTrinh) -> chức năng này là hành động của đối tượng VeTau.
Để hiển thị thông tin lộ trình và ga đi, ga đến của từng chuyến đi:
Hệ thống truy xuất thông tin chi tiết của lịch trình -> cần chức năng layThongTinLichTrinh(maTau) -> chức năng này là hành động của đối tượng LichTrinh. Lịch trình truy xuất thông tin hành trình tương ứng -> cần chức năng layThongTinHanhTrinh(maHanhTrinh) -> chức năng này là hành động của đối tượng HanhTrinh. Hành trình truy xuất thông tin ga đi và ga về để xác định Ga đầu, Ga cuối -> cần chức năng layThongTinGaDauGaCuoi(maGaDi, maGaDen) -> chức năng này là hành động của đối tượng NhaGa.
Hệ thống tính tổng doanh thu của kỳ báo cáo -> cần chức năng tinhDoanhThu() -> chức năng này là hành động nội bộ của đối tượng BaoCao (tính thuộc tính tongDoanhThu của thực thể BaoCao bằng tổng cộng các doanh thu của từng chuyến).
Hệ thống thực hiện lưu trữ thông tin báo cáo tổng hợp và chi tiết hiệu suất từng chuyến chạy vào cơ sở dữ liệu -> cần chức năng luuKetQuaThongKe(maBaoCao, dsKetQua) -> chức năng này là hành động của đối tượng BaoCao (đối tượng BaoCao ghi nhận dữ liệu chi tiết từng chuyến qua lớp thực thể liên kết ChiTietBaoCao tương ứng gồm: soVeBan, doanhThuChuyen, tiLeLapDay).
Hệ thống thực hiện sắp xếp danh sách chuyến chạy dựa trên doanh thu của chuyến giảm dần -> cần chức năng sapXepDoanhThuGiamDan() -> chức năng này là hành động của đối tượng BaoCao.
Sắp xếp và lưu xong, bảng báo cáo kết quả chi tiết từng chuyến chạy sẽ hiện lên giao diện thống kê ManHinhThongKeView.




Luồng xử lý chi tiết:
Cán bộ quản lý (QuanLy) truy cập vào giao diện chính, click chọn chức năng thống kê -> lớp biên ManHinhQuanLyView gọi kích hoạt khởi tạo lớp biên ManHinhThongKeView.
Giao diện ManHinhThongKeView hiển thị, cán bộ quản lý thực hiện nhập khoảng thời gian vào các trường nhập liệu (inNgayBD, inNgayKT) và nhấn nút thống kê.
Lớp biên ManHinhThongKeView gọi trực tiếp lớp thực thể BaoCao thông qua phương thức kiemTraHopLe(ngayBD, ngayKT) để xác thực khoảng thời gian nhập vào (ví dụ: ngày kết thúc không được nhỏ hơn ngày bắt đầu).
Lớp biên ManHinhThongKeView gọi lớp thực thể BaoCao thông qua phương thức taoMoiBaoCao(ngayBD, ngayKT) để lưu trữ thông tin khoảng thời gian thống kê và khởi tạo một thực thể báo cáo rỗng.
Lớp biên ManHinhThongKeView gọi lớp thực thể DoanTau thông qua phương thức layDanhSachDoanTau().
Lớp DoanTau thực hiện truy vấn cơ sở dữ liệu và trả về danh sách toàn bộ các thực thể đoàn tàu hiện có (gồm thông tin maTau và tenTau) cho lớp biên ManHinhThongKeView.
Với mỗi đoàn tàu (DoanTau) được tìm thấy, lớp biên ManHinhThongKeView gọi lớp thực thể LichTrinh thông qua phương thức layDanhSachLichTrinhTrongKy(maTau, ngayBD, ngayKT).
Lớp LichTrinh thực hiện truy vấn và lọc ra danh sách các lịch trình chạy thực tế hoàn thành trong khoảng thời gian yêu cầu (lấy ra thông tin maLichTrinh và ngayKhoiHanh), sau đó trả kết quả về cho lớp biên ManHinhThongKeView.
Với mỗi lịch trình (LichTrinh) hợp lệ, lớp biên ManHinhThongKeView gọi lớp thực thể VeTau thông qua phương thức layDanhSachVeTheoLichTrinh(maLichTrinh).
Lớp VeTau lọc ra danh sách các vé tàu có trạng thái "Đã bán" (không tính các vé đã bị hủy hoặc trả) thuộc lịch trình tương ứng và trả kết quả về cho lớp biên ManHinhThongKeView.
Lớp biên ManHinhThongKeView tự thực hiện vòng lặp đếm số vé bán được của chuyến đi để gán vào thuộc tính soVeBan của thực thể liên kết ChiTietBaoCao tương ứng.
Lớp biên ManHinhThongKeView tự thực hiện cộng dồn thuộc tính giaVe của danh sách vé bán được của chuyến đi đó để tính ra giá trị Doanh thu chuyến (thuộc tính doanhThuChuyen trong ChiTietBaoCao).
Lớp ManHinhThongKeView gọi lớp thực thể LichTrinh thông qua phương thức layThongTinLichTrinh(maTau).
Lớp LichTrinh gọi lớp thực thể HanhTrinh thông qua phương thức layThongTinHanhTrinh(maHanhTrinh).
Lớp HanhTrinh tự thực hiện duyệt danh sách các thực thể liên kết ChiTietHanhTrinh của mình nhằm xác định cụ thể hai mã ga đầu và cuối:
Tìm bản ghi ChiTietHanhTrinh có thuộc tính thuTuGa = 1 (ga xuất phát), đi theo đường tham chiếu sang đối tượng NhaGa liên kết để lấy giá trị maGa và gán làm mã ga đi (maGaDi).
Tìm bản ghi ChiTietHanhTrinh có thuộc tính thuTuGa đạt giá trị lớn nhất (ga kết thúc), đi theo đường tham chiếu sang đối tượng NhaGa liên kết để lấy giá trị maGa và gán làm mã ga đến (maGaDen).
Lớp HanhTrinh trực tiếp gọi lớp thực thể NhaGa thông qua phương thức layThongTinGaDauGaCuoi(maGaDi, maGaDen) với hai mã ga vừa tìm được.
Lớp NhaGa nhận hai mã ga, tiến hành truy vết lấy thuộc tính tenNhaGa tương ứng rồi trả về tên chi tiết của ga đầu (ứng với maGaDi) và ga cuối (ứng với maGaDen) cho lớp HanhTrinh.
Lớp HanhTrinh phản hồi thông tin tên ga đầu và tên ga cuối về cho lớp LichTrinh.
Lớp LichTrinh tổng hợp và trả về thông tin ga đầu, ga cuối (thông qua HanhTrinh), số toa thiết kế (thông qua đoàn tàu và toa xe liên kết để hiển thị cột Số toa) và tổng số ghế/giường tối đa của đoàn tàu chạy chuyến đó (thông qua ghế ngồi liên kết để hiển thị cột TB ghế/giường) cho lớp ManHinhThongKeView.
Lớp biên ManHinhThongKeView tự thực hiện tính toán Tỉ lệ lấp đầy của chuyến chạy đó (thuộc tính tiLeLapDay trong ChiTietBaoCao)
Sau khi xử lý xong tất cả các chuyến đi, lớp biên ManHinhThongKeView tự thực hiện phương thức cộng dồn doanh thu (tinhDoanhThu) của tất cả các chuyến đi lại, gán vào thuộc tính tổng của báo cáo (tongDoanhThu trong BaoCao).
Lớp biên ManHinhThongKeView gọi lớp thực thể BaoCao thông qua phương thức luuKetQuaThongKe(maBaoCao, dsKetQua) để lưu thông tin tổng hợp của báo cáo (tongDoanhThu, ngayLapBaoCao...) và lưu chi tiết từng dòng hiệu suất chuyến chạy vào các thực thể liên kết ChiTietBaoCao tương ứng.
Lớp biên ManHinhThongKeView tự thực hiện phương thức nội bộ sapXepDoanhThuGiamDan() để sắp xếp danh sách các thực thể liên kết ChiTietBaoCao dựa trên chỉ số Doanh thu chuyến từ cao xuống thấp.
Lớp biên ManHinhThongKeView phản hồi và trả kết quả danh sách báo cáo chi tiết đã sắp xếp trực tiếp về cho lớp biên ManHinhThongKeView (tự hiển thị trên giao diện hiện hành).
Lớp biên ManHinhThongKeView nhận dữ liệu và kết xuất hiển thị bảng báo cáo chi tiết trực quan lên màn hình thông qua các thuộc tính hiển thị đầu ra (outMaLichTrinh, outTenTau, outGaDau, outGaCuoi, outNgayKhoiHanh, outSoVeBan, outTiLeLapDay, outDoanhThuChuyen) cho cán bộ quản lý (QuanLy) đánh giá.


III. Pha thiết kế 
Dưới đây là sơ đồ phân lớp cấu trúc và kịch bản tương tác tuần tự chi tiết của chức năng Thống kê chuyến tàu theo doanh thu, được thiết kế theo mô hình 3 lớp ở pha thiết kế, đảm bảo đồng bộ hoàn toàn với sơ đồ thực thể chi tiết toàn hệ thống.
3.1. Thiết kế kiến trúc MVC: 
Các lớp tầng giao diện (Boundary):
ManHinhQuanLyFrm: Giao diện trang chủ điều hướng của cán bộ quản lý (QuanLy).
ManHinhThongKeFrm: Giao diện thiết lập khoảng thời gian và hiển thị báo cáo thống kê các chuyến tàu theo doanh thu.
Các lớp tầng điều khiển / truy cập dữ liệu (Control/DAO):
BaoCaoDAO: Xử lý lưu trữ, tính toán các chỉ số và khởi tạo báo cáo thống kê doanh thu 
DoanTauDAO: Xử lý thông tin danh mục đoàn tàu, toa xe và ghế ngồi thiết kế.
LichTrinhDAO: Xử lý thông tin lịch trình, ngày giờ vận hành thực tế và các ga dừng.
HanhTrinhDAO: Xử lý thông tin lộ trình hành trình và các điểm dừng liên quan.
NhaGaDAO: Xử lý thông tin nhà ga đường sắt.
VeTauDAO: Xử lý thông tin các vé tàu trong hệ thống.
Các lớp tầng thực thể (Entity):
BaoCao	
ChiTietBaoCao
LichTrinh
ChiTietLichTrinh
DoanTau
HanhTrinh
ChiTietHanhTrinh
NhaGa
VeTau 
  3.2. Thiết kế giao diện
	
3.3. Kịch bản tương tác tuần tự chi tiết 
Quản lý click chọn chức năng "Thống kê chuyến tàu theo doanh thu" trên giao diện ManHinhQuanLyFrm
Phương thức actionPerformed() của lớp ManHinhQuanLyFrm tự động được gọi để bắt sự kiện.
Lớp ManHinhQuanLyFrm tự gọi phương thức call nội bộ để xử lý logic chuyển màn hình.
Lớp ManHinhQuanLyFrm gọi hàm khởi tạo ManHinhThongKeFrm(quanLy) để tạo một form giao diện mới (tham số quanLy được truyền vào theo đúng nguyên tắc thuộc tính ẩn).
Phương thức call nội bộ của lớp ManHinhThongKeFrm được kích hoạt để chuẩn bị hiển thị form nhập liệu.
Lớp ManHinhThongKeFrm hiển thị giao diện nhập khoảng thời gian thống kê ra màn hình cho Quản lý.
Quản lý thực hiện nhập Ngày bắt đầu, Ngày kết thúc.
Phương thức actionPerformed() của lớp ManHinhThongKeFrm được gọi để bắt sự kiện.
Lớp ManHinhThongKeFrm tự gọi phương thức call nội bộ để chuẩn bị thực hiện quy trình kiểm tra và xử lý.
Lớp ManHinhThongKeFrm gọi phương thức kiemTraHopLe(ngayBD, ngayKT) của lớp BaoCaoDAO để kiểm tra tính hợp lệ của thời gian nhập vào.
Lớp BaoCaoDAO tự gọi phương thức call nội bộ để thực thi việc xác thực logic khoảng thời gian (ngày kết thúc không được nhỏ hơn ngày bắt đầu).
Lớp BaoCaoDAO trả kết quả xác thực (hợp lệ) về cho lớp ManHinhThongKeFrm.
Quản lý click nút "Thống Kê Doanh Thu"
Lớp ManHinhThongKeFrm gọi phương thức taoMoiBaoCao(ngayBD, ngayKT) của lớp BaoCaoDAO.
Lớp BaoCaoDAO tự gọi phương thức call nội bộ để khởi tạo đối tượng lưu trữ thông tin thời gian báo cáo.
Lớp BaoCaoDAO gọi hàm khởi tạo BaoCao() của thực thể BaoCao để đóng gói dữ liệu thời gian thống kê và tạo thực thể báo cáo rỗng.
Thực thể BaoCao trả đối tượng đã đóng gói về cho lớp BaoCaoDAO.
Lớp BaoCaoDAO trả đối tượng BaoCao về cho lớp ManHinhThongKeFrm.
Lớp ManHinhThongKeFrm gọi phương thức layDanhSachDoanTau() của lớp DoanTauDAO.
Lớp DoanTauDAO tự gọi phương thức call nội bộ để lấy danh sách tàu đang hoạt động.
Lớp DoanTauDAO gọi hàm khởi tạo DoanTau() của thực thể DoanTau để đóng gói dữ liệu tàu (gồm mã tàu maTau và tên tàu tenTau).
Thực thể DoanTau trả đối tượng tàu đã đóng gói về cho lớp DoanTauDAO.
Lớp DoanTauDAO trả danh sách tàu về cho lớp ManHinhThongKeFrm.
 Lớp ManHinhThongKeFrm gọi phương thức layDanhSachLichTrinhTrongKy(maTau, ngayBD, ngayKT) của lớp LichTrinhDAO.
Lớp LichTrinhDAO tự gọi phương thức call nội bộ để lấy các lịch trình chạy chuyến tàu đã hoàn thành trong kỳ thống kê.
Lớp LichTrinhDAO gọi hàm khởi tạo LichTrinh() của thực thể LichTrinh để đóng gói dữ liệu lịch trình chuyến đi (gồm mã lịch trình maLichTrinh và ngày khởi hành ngayKhoiHanh).
Thực thể LichTrinh trả đối tượng lịch trình về cho lớp LichTrinhDAO.
Lớp LichTrinhDAO trả danh sách lịch trình về cho lớp ManHinhThongKeFrm.
Lớp ManHinhThongKeFrm gọi phương thức layDanhSachVeTheoLichTrinh(maLichTrinh) của lớp VeTauDAO.
Lớp VeTauDAO tự gọi phương thức call nội bộ để lấy danh sách các vé tàu có trạng thái "Đã bán" (không tính vé đã bị hủy hoặc trả).
Lớp VeTauDAO gọi hàm khởi tạo VeTau() của thực thể VeTau để đóng gói dữ liệu vé tàu.
Thực thể VeTau trả đối tượng vé tàu về cho lớp VeTauDAO.
Lớp VeTauDAO trả danh sách vé đã bán về cho lớp ManHinhThongKeFrm.
Lớp ManHinhThongKeFrm tự gọi phương thức call nội bộ để thực hiện vòng lặp đếm tổng số lượng vé bán (soVeBan) và cộng dồn thuộc tính giá vé (giaVe) của danh sách để tính toán doanh thu chuyến chạy (doanhThuChuyen).
Lớp ManHinhThongKeFrm gọi phương thức layThongTinLichTrinh(maTau) của lớp LichTrinhDAO.
Lớp LichTrinhDAO tự gọi phương thức call nội bộ để truy xuất chi tiết hành trình liên kết.
Lớp LichTrinhDAO gọi phương thức layThongTinHanhTrinh(maHanhTrinh) của lớp HanhTrinhDAO.
Lớp HanhTrinhDAO tự gọi phương thức call nội bộ để lấy thông tin điểm dừng trên hành trình.
Lớp HanhTrinhDAO tự thực hiện duyệt danh sách các thực thể liên kết ChiTietHanhTrinh của mình nhằm xác định cụ thể hai mã ga đầu và ga cuối:
Tìm bản ghi ChiTietHanhTrinh có thuộc tính thuTuGa = 1 (ga xuất phát), đi theo đường tham chiếu sang đối tượng NhaGa liên kết để lấy giá trị maGa và gán làm mã ga đi (maGaDi).
Tìm bản ghi ChiTietHanhTrinh có thuộc tính thuTuGa đạt giá trị lớn nhất (ga kết thúc), đi theo đường tham chiếu sang đối tượng NhaGa liên kết để lấy giá trị maGa và gán làm mã ga đến (maGaDen).
Lớp HanhTrinhDAO gọi phương thức layThongTinGaDauGaCuoi(maGaDi, maGaDen) của lớp NhaGaDAO.
Lớp NhaGaDAO tự gọi phương thức call nội bộ để truy xuất tên chi tiết của ga đầu (ứng với maGaDi) và ga cuối (ứng với maGaDen) từ cơ sở dữ liệu.
Lớp NhaGaDAO trả tên ga đầu và ga cuối về cho lớp HanhTrinhDAO.
Lớp HanhTrinhDAO trả kết quả lộ trình về cho lớp LichTrinhDAO.
Lớp LichTrinhDAO tổng hợp thông tin ga đầu, ga cuối (thông qua HanhTrinh), cấu hình số toa thiết kế (thông qua đoàn tàu và toa xe liên kết) và tổng số ghế/giường tối đa của đoàn tàu chạy chuyến đó (thông qua ghế ngồi liên kết) rồi trả về cho lớp ManHinhThongKeFrm.
Lớp ManHinhThongKeFrm tự gọi phương thức call nội bộ để tính toán tỉ lệ lấp đầy ghế/giường của chuyến
Lớp ManHinhThongKeFrm tự gọi phương thức call nội bộ để thực hiện phương thức cộng dồn doanh thu (tinhDoanhThu) của tất cả các chuyến chạy nhằm gán và xác định tổng doanh thu báo cáo (tongDoanhThu của BaoCao).
Lớp ManHinhThongKeFrm gọi phương thức luuKetQuaThongKe(maBaoCao, dsChiTiet) của lớp BaoCaoDAO để tiến hành lưu trữ dữ liệu.
Lớp BaoCaoDAO tự gọi phương thức call nội bộ để lưu trữ thông tin báo cáo tổng hợp (tongDoanhThu, ngayLapBaoCao...) vào bảng BaoCao và các bản ghi chi tiết chuyến chạy tương ứng vào bảng ChiTietBaoCao xuống cơ sở dữ liệu.
Lớp BaoCaoDAO trả về kết quả lưu trữ thành công cho lớp ManHinhThongKeFrm.
Lớp ManHinhThongKeFrm tự gọi phương thức nội bộ sapXepDoanhThuGiamDan() để sắp xếp danh sách kết quả chi tiết theo thứ tự doanh thu chuyến chạy giảm dần.
Lớp ManHinhThongKeFrm kết xuất dữ liệu hiển thị bảng báo cáo chi tiết trực quan lên màn hình thông qua các thuộc tính hiển thị đầu ra.


IV. Pha cài đặt + kiểm thử
   4.1. Cài đặt
   
Github
Source code được đặt trong thư mục src\main\java\com\example\manage
Các folder:
dao: Các lớp kết nối với CSDL
entity: Các lớp thực thể có trong module tương ứng với models
view: Các lớp giao diện sử dụng trong module
test/java/unit: Lớp kiểm thử đơn vị
4.2. Kiểm thử đơn vị - JUnit test:
- Lập kế hoạch:
Chức năng
Lớp DAO
Hàm/Phương thức
   Các trường hợp cần kiểm thử
Xác thực khoảng thời gian thống kê
BaoCaoDAO
kiemTraHopLe(Date ngayBD, Date ngayKT)
1. Khoảng thời gian hợp lệ (ngày kết thúc lớn hơn hoặc bằng ngày bắt đầu).
2. Khoảng thời gian không hợp lệ (ngày kết thúc nhỏ hơn ngày bắt đầu).
3. Ngày bắt đầu hoặc ngày kết thúc bị null.
Khởi tạo báo cáo mới
BaoCaoDAO
taoMoiBaoCao(Date ngayBD, Date ngayKT)
1. Khởi tạo thành công đối tượng BaoCao rỗng với khoảng thời gian hợp lệ.
2. Các thuộc tính ban đầu (tongDoanhThu = 0, danh sách chi tiết trống).
Lấy danh sách chuyến tàu chạy trong kỳ
LichTrinhDAO
layDanhSachLichTrinhTrongKy(String maTau, Date ngayBD, Date ngayKT)
1. Có lịch trình hoàn thành trong kỳ (trả về danh sách chứa các thực thể hợp lệ).
2. Không có lịch trình nào hoàn thành trong kỳ (trả về danh sách trống).
Lấy danh sách vé đã bán theo chuyến
VeTauDAO
layDanhSachVeTheoLichTrinh(String maLichTrinh)
1. Chuyến tàu có phát sinh vé bán ở trạng thái "Đã bán" (trả về danh sách vé).
2. Chuyến tàu không có vé bán hoặc chỉ có vé bị hủy/trả (trả về danh sách trống).
Lưu trữ kết quả báo cáo
BaoCaoDAO
luuKetQuaThongKe(BaoCao baoCao, List<ChiTietBaoCao> dsKetQua)
1. Lưu thành công báo cáo tổng hợp và danh sách chi tiết hiệu suất các chuyến tàu chạy xuống cơ sở dữ liệu (trả về true).

4.3. Kiểm thử chức năng - Blackbox testing 
- Lập kế hoạch:
Chức năng
Các trường hợp cần kiểm thử
Thống kê doanh thu chuyến tàu
1. Khoảng thời gian chọn hợp lệ và có dữ liệu vận hành chuyến tàu trong kỳ.
2. Khoảng thời gian chọn không hợp lệ (Ngày kết thúc < Ngày bắt đầu).
3. Khoảng thời gian chọn hợp lệ nhưng không có dữ liệu vận hành chuyến tàu nào trong kỳ.

- Thiết lập Cơ sở dữ liệu (CSDL) trước khi test:
Dữ liệu giả định được thiết lập trong hệ thống quản trị cơ sở dữ liệu trước khi thực hiện các ca kiểm thử:
Bảng NhaGa:

Bảng HanhTrinh & ChiTietHanhTrinh:


Bảng DoanTau:

Bảng LichTrinh:

Bảng VeTau:

- Các test case chi tiết cho chức năng Thống kê doanh thu chuyến tàu:
1. Test thống kê thành công chuyến tàu theo doanh thu (Khoảng thời gian hợp lệ, có dữ liệu).
Các bước thực hiện
Kết quả mong đợi
1. Khởi động phần mềm quản lý đường sắt
Giao diện đăng nhập hệ thống hiện ra đầy đủ các trường nhập dữ liệu.
2. Nhập thông tin đăng nhập tài khoản Quản lý
Click nút Đăng nhập
Đăng nhập thành công. Giao diện trang chủ điều hướng của cán bộ quản lý (ManHinhQuanLyFrm) hiển thị, có menu chức năng
 
3. Click chọn chức năng Thống kê chuyến tàu theo doanh thu trên menu
Giao diện thiết lập báo cáo thống kê (ManHinhThongKeFrm) xuất hiện, gồm các ô chọn ngày (Ngày bắt đầu, Ngày kết thúc), nút hành động và bảng hiển thị kết quả thống kê rỗng.
4. Thiết lập tham số khoảng thời gian thống kê:
- Ngày bắt đầu = 01/06/2026
- Ngày kết thúc = 31/06/2026
Click nút Thống Kê Doanh Thu 
- Hệ thống kiểm tra khoảng thời gian hợp lệ thành công.
- Hệ thống tự động thực hiện truy xuất dữ liệu, tính toán doanh thu, tính tỉ lệ lấp đầy, xác định ga đầu/ga cuối.
- Kết quả tính toán được lưu trữ thành công vào cơ sở dữ liệu.
- Bảng kết quả báo cáo hiển thị trực quan lên màn hình gồm 3 bản ghi chuyến tàu được sắp xếp theo cột Doanh thu chuyến (VNĐ) giảm dần

5. Kiểm tra thông tin hiển thị chi tiết của dòng đầu tiên trên bảng kết quả
Dòng 1 hiển thị chính xác các thông tin cột:


2. Test thống kê thất bại do lỗi nhập ngày không hợp lệ (Ngày kết thúc nhỏ hơn ngày bắt đầu).
Các bước thực hiện
Kết quả mong đợi
1. Khởi động phần mềm và đăng nhập với tài khoản cán bộ quản lý
Truy cập thành công vào giao diện chính và hiển thị màn hình thiết lập thống kê (ManHinhThongKeFrm).
2. Thiết lập tham số khoảng thời gian không hợp lệ:
- Ngày bắt đầu = 31/06/2026
- Ngày kết thúc = 01/06/2026
Click nút Thống Kê Doanh Thu
- Hệ thống phát hiện lỗi kiểm tra logic nghiệp vụ khoảng thời gian (Ngày kết thúc < Ngày bắt đầu).
- Hệ thống dừng quy trình tính toán, không truy vấn cơ sở dữ liệu.
- Một hộp thoại cảnh báo (Message Dialog) hiện lên với nội dung lỗi

3. Click nút OK trên hộp thoại cảnh báo
Hộp thoại cảnh báo đóng lại. Người dùng quay lại giao diện thiết lập thời gian ban đầu để tiến hành chọn lại ngày hợp lệ.

3. Test thống kê không có dữ liệu vận hành chuyến tàu trong kỳ được chọn.
Các bước thực hiện
Kết quả mong đợi
1. Khởi động phần mềm và đăng nhập với tài khoản cán bộ quản lý
Truy cập thành công vào giao diện chính và hiển thị màn hình thiết lập thống kê (ManHinhThongKeFrm).
2. Thiết lập tham số khoảng thời gian không có lịch trình hoạt động nào thực tế:
- Ngày bắt đầu = 01/01/2020
- Ngày kết thúc = 31/01/2020
Click nút Thống Kê Doanh Thu 
- Hệ thống kiểm tra tính hợp lệ của thời gian thành công.
- Hệ thống thực hiện quét các lịch trình và xác định không có chuyến tàu nào đã hoàn thành hành trình trong kỳ này.
- Một hộp thoại thông báo hiện lên màn hình với nội dung

3. Click nút OK trên hộp thoại thông báo
- Hộp thoại thông báo đóng lại.
- Giao diện bảng hiển thị danh sách kết quả trống rỗng.


