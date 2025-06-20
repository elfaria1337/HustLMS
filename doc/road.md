# Xây dựng lần lượt theo luồng: MainApp → FXML + Controller → Model → Repository → Kết nối DB.

# Chức năng ứng dụng cho nhân viên thư viện

## Quản lý độc giả (Reader)

Tìm kiếm, xem danh sách độc giả

Thêm mới, sửa thông tin độc giả (họ tên, ngày sinh, địa chỉ, điện thoại, email)

Khóa/mở khóa tài khoản độc giả theo trạng thái (vd: khóa nếu không thanh toán phạt)

Xem lịch sử mượn, phạt, thanh toán của độc giả

Hỗ trợ đăng ký độc giả mới tại quầy

## Quản lý sách và kho

Quản lý đầu sách (book_title): thêm/sửa/xóa thông tin sách (tên sách, tác giả, thể loại, nhà xuất bản, năm xuất bản)

Quản lý bản sao sách (book_copy): quản lý tình trạng từng bản sao, vị trí kho (inventory), trạng thái (sẵn sàng, mượn, hỏng,...)

Quản lý kho sách (inventory): quản lý các vị trí lưu kho sách

## Quản lý nhập sách mới (Book Import)

Tạo phiếu nhập sách mới (book_import) từ nhà cung cấp (supplier)

Quản lý chi tiết nhập sách (book_import_detail) gồm số lượng, giá, sách nhập

Quản lý thông tin nhà cung cấp: tên công ty, liên hệ, địa chỉ, email, điện thoại

Xem lịch sử nhập sách và báo cáo tồn kho

## Quản lý mượn - trả sách (Loan)

Tạo phiếu mượn sách cho độc giả, nhập ngày mượn, ngày đến hạn (loan)

Quản lý chi tiết mượn từng bản sao sách (loan_detail), ngày trả, trạng thái trả

Xác nhận trả sách, cập nhật trạng thái bản sao sách

Gia hạn mượn sách (nếu có)

Quản lý đặt trước sách (reservation): duyệt yêu cầu đặt trước, thông báo sẵn sàng lấy sách

## Quản lý phạt và thanh toán (Fine & Invoice)

Tạo phạt cho độc giả khi trả sách quá hạn hoặc hư hỏng (fine)

Quản lý hóa đơn thanh toán phạt, phí mượn sách (invoice)

Xác nhận thanh toán qua các hình thức (tiền mặt, chuyển khoản, ví điện tử, thẻ,...)

Gửi thông báo phạt, hoá đơn điện tử cho độc giả

## Báo cáo & thống kê (mở rộng)

Thống kê doanh thu từ phí mượn, phạt

Báo cáo hoạt động nhân viên (số lượt mượn, trả, nhập sách,...)

Phân tích xu hướng mượn sách theo thể loại, tác giả

Cảnh báo tồn kho, sách hư hỏng, phạt chưa thanh toán

Xuất báo cáo ra PDF, Excel

# Chức năng dành cho Độc giả (Reader)

## Đăng ký tài khoản

Đăng ký trực tuyến với các thông tin cá nhân (họ tên, số điện thoại, email, ngày sinh, địa chỉ) và thông tin đăng nhập (username, password).

Xác minh đăng ký qua email hoặc SMS.

Hoặc đăng ký trực tiếp với nhân viên thư viện.

## Đăng nhập và quản lý tài khoản

Đăng nhập bằng username/password.

Cập nhật thông tin cá nhân (địa chỉ, số điện thoại, email, ...).

Đổi mật khẩu.

## Tìm kiếm và tra cứu sách

Tìm kiếm đầu sách theo tên, tác giả, thể loại, nhà xuất bản,...

Xem chi tiết đầu sách, thông tin bản sao sách còn trong kho.

## Đặt trước sách (Reservation)

Thực hiện đặt trước các đầu sách còn hạn chế.

Quản lý trạng thái đặt trước (chờ xử lý, đã chấp nhận, từ chối).

## Mượn sách (Loan)

Xem lịch sử phiếu mượn của mình.

Thực hiện mượn sách thông qua phiếu mượn (Loan).

Xem chi tiết phiếu mượn, thời hạn trả sách.

## Trả sách và phạt (Fine)

Xem thông tin phạt (nếu có) do trả sách muộn hoặc hư hỏng.

Thanh toán phạt và các hóa đơn liên quan (Invoice).

## Thông báo và nhắc nhở

Nhận thông báo qua email hoặc SMS về:

Sách sắp đến hạn trả.

Phạt tiền hoặc hóa đơn thanh toán.

Thông tin đặt trước sách.

## Xem lịch sử hoạt động

Xem lịch sử mượn sách.

Xem lịch sử vi phạm (phạt).

Xem lịch sử thanh toán hóa đơn.


