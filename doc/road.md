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
