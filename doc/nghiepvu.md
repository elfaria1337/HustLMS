**3. Mô tả nghiệp vụ**

**3.1. Quản lý độc giả**

- Với độc giả (reader) mới, hệ thống cho phép độc giả đăng kí tài khoản bằng 2 cách:
  o Đăng kí trực tuyến:
  ▪ Đăng ký tài khoản mới với các thông tin cá nhân yêu cầu (họ tên, số điện thoại, email, ngày sinh, địa chỉ) và thông tin đăng nhập (username, password).
  ▪ Hệ thống xác minh thông tin đăng ký qua email hoặc SMS để đảm bảo hợp lệ.
  o Đăng kí với nhân viên tại quầy: Nhân viên yêu cầu độc giả cung cấp các thông tin cần thiết để nhập vào hệ thống
- Hệ thống sẽ tự động tạo mã độc giả để phục vụ quản lý nội bộ.
- Hệ thống có thể gửi thông báo qua email/SMS về:
  o Sách sắp đến hạn trả.
  o Sách mới nhập về phù hợp với sở thích của độc giả.
  o Khuyến mãi đặc biệt (chương trình ưu đãi thành viên).
  o Xác nhận phạt/miễn giảm.
  o Thông báo đặt trước thành công hoặc sách đã sẵn sàng mượn.
  o Hoá đơn điện tử sau khi thanh toán.
- Hệ thống sẽ phân tích lịch sử mượn của độc giả trong 3 tháng gần nhất, từ đó:
  o Xác định thể loại sách độc giả mượn nhiều nhất.
  o Gợi ý các đầu sách tương tự hoặc liên quan đến sở thích của độc giả.
- Khóa tài khoản tự động:
  o Trạng thái tài khoản tự động cập nhật “bị khóa” nếu độc giả không thanh toán phạt sau 30 ngày (thời gian này có thể cấu hình bởi quản lý).
  o Tài khoản chỉ được mở lại khi độc giả hoàn tất thanh toán các khoản nợ và được nhân viên thư viện xác nhận.

---

**3.2. Quản lý mượn - trả và thanh toán**

- Độc giả có thể mượn sách theo 2 cách:
  o Mượn trực tiếp tại thư viện: Nhân viên sẽ kiểm tra tình trạng sách và nhập giao dịch mượn vào hệ thống nếu sách sẵn sàng để mượn.
  o Gửi yêu cầu mượn online: Độc giả gửi yêu cầu trực tuyến; hệ thống cập nhật trạng thái và gửi thông báo khi yêu cầu được duyệt. Sau khi duyệt, độc giả đến thư viện lấy sách, nhân viên xác nhận giao dịch mượn thực tế.
- Hạn mức:
  o Số sách tối đa được mượn mỗi lần: 5 cuốn
  o Thời hạn mượn tối đa: 30 ngày, có thể gia hạn 1–2 lần, mỗi lần tối đa 7 ngày nếu không có độc giả khác đặt trước
- Phí mượn sách được tính theo 2 cách:
  o Theo số lượng sách: 5000 đồng/cuốn
  o Vé mượn theo tháng: 30.000/tháng (áp dụng cho thành viên thường xuyên).
- Đặt trước & hàng chờ
  o Nếu sách đang có người mượn, độc giả có thể đặt trước.
  o Hệ thống quản lý hàng chờ theo thứ tự đăng ký, tự động thông báo khi đến lượt có sách sẵn.
  o Độc giả có thời gian 3 ngày để đến nhận sách trước khi nhường lượt cho người tiếp theo.
- Khi độc giả trả sách:
  o Nhân viên kiểm tra tình trạng sách, chụp ảnh hoặc thu chữ ký xác nhận để minh bạch.
  o Hệ thống cập nhật ngay trạng thái “Sẵn sàng” hoặc chuyển sách vào khu xử lý nếu hư hỏng.
- Xử lý quá hạn & phạt
  o Hệ thống tự động tính tiền phạt 3.000 đồng/ngày đối với sách quá hạn.
  o Nhân viên có thể đặt mức phạt thêm tùy theo tình trạng hư hỏng sách.
  o Độc giả gửi đơn giải trình xin miễn/giảm phạt kèm lý do hợp lệ, quản lý duyệt online trong vòng 48 giờ và cập nhật mức phạt mới.
- Thanh toán
  o Các khoản thu bao gồm: phí mượn sách và tiền phạt (quá hạn hoặc hư hỏng, mất sách).
  o Độc giả có thể thanh toán trực tiếp tại thư viện hoặc online qua các phương thức:
  ▪ Tiền mặt tại thư viện (nhân viên xác nhận giao dịch)
  ▪ Chuyển khoản qua ngân hàng
  ▪ Ví điện tử
  ▪ Thẻ tín dụng / thẻ ghi nợ
  ▪ QR code (scan mã do hệ thống hoặc cổng thanh toán cung cấp).
  o Hệ thống sẽ phát hành hoá đơn điện tử ngay sau khi thanh toán.
  o Toàn bộ lịch sử giao dịch được lưu trữ phục vụ quản lý thư viện và độc giả.

---

**3.3. Quản lý kho & nhập sách**

- Kiểm tra và cập nhật số lượng sách
  o Hệ thống tự động cập nhật số lượng tồn kho khi có độc giả mượn hoặc trả sách.
  o Nhân viên thư viện định kỳ (hàng tháng) kiểm tra số lượng sách thực tế trong kho và đối chiếu với dữ liệu trên hệ thống. Hệ thống phát hiện và cảnh báo khi có sai lệch để xử lý kịp thời.
  o Khi hệ thống phát hiện sai lệch số lượng giữa kho thực tế và dữ liệu, nhân viên lập biên bản và báo cáo quản lý để xử lý kịp thời.
- Báo cáo tồn kho và nhập sách mới
  o Khi có sách gần hết hoặc cần nhập mới, nhân viên sẽ gửi báo cáo tồn kho cho quản lý thư viện.
  o Quản lý thư viện phê duyệt quyết định nhập sách mới. Nhân viên thư viện chịu trách nhiệm nhập thông tin sách vào hệ thống, hoặc theo phân công của quản lý.
  o Quản lý gửi đơn đặt hàng hoặc yêu cầu báo giá đến nhà cung cấp, trong đó có thông tin về tên sách, tác giả, số lượng, v.v.
  o Nhà cung cấp xác nhận đơn hàng và gửi hóa đơn, bao gồm thông tin về giá thành, thuế VAT và tổng tiền cần thanh toán.
  o Hệ thống lưu trữ chi tiết các thông tin liên quan đến đơn hàng như ngày đặt, ngày nhận, số lượng, đầu sách, giá thành, VAT và trạng thái thanh toán.
  o Khi sách nhập về, nhân viên kiểm tra tình trạng sách (số lượng, chất lượng, phân loại), ghi nhận các lỗi (nếu có) và cập nhật thông tin vào hệ thống thư viện.
  o Nếu phát hiện sách lỗi hoặc thiếu khi nhập, nhân viên lập biên bản và thông báo nhà cung cấp để xử lý.

---

**3.4. Quản lý nhà cung cấp**

- Hệ thống lưu trữ và quản lý các dữ liệu cơ bản về nhà cung cấp, bao gồm: tên công ty, mã nhà cung cấp, địa chỉ, người liên hệ, email/điện thoại, phương thức thanh toán, điều khoản hợp đồng. Việc thêm, sửa, xóa dữ liệu được thực hiện bởi quản lý hoặc admin.
- Đánh giá & xếp hạng:
  o Thu thập các chỉ số đánh giá gồm:
  ▪ Thời gian giao hàng (so với cam kết).
  ▪ Tỉ lệ đúng đơn (khớp số lượng – chất lượng sách).
  ▪ Tỉ lệ trả hàng/hoàn trả (Lỗi, hư hỏng).
  o Tính điểm đánh giá tổng hợp (thang 5 sao) và cho phép người quản lý bổ sung nhận xét. Điểm đánh giá được cập nhật định kỳ hoặc sau mỗi lần giao hàng.
  o Sử dụng điểm đánh giá làm một trong các tiêu chí ưu tiên lựa chọn nhà cung cấp, bên cạnh các điều kiện về giá, thời gian giao hàng và tồn kho.
- Tự động gợi ý nhà cung cấp
  o Khi tạo đơn nhập, hệ thống đề xuất danh sách nhà cung cấp phù hợp dựa trên:
  ▪ Điểm đánh giá cao.
  ▪ Giá thành cạnh tranh.
  ▪ Thời gian giao nhanh.
  o Cho phép quản lý lọc, so sánh song song các nhà cung cấp để hỗ trợ quyết định chốt đơn nhập.

---

**3.5. Quản lý nhân viên**

- Mỗi nhân viên được cấp tài khoản với vai trò xác định (nhân viên thư viện, quản lý thư viện, quản trị viên).
- Nhân viên có thể xem lịch làm việc của mình qua hệ thống.
- Nhân viên thư viện thực hiện chấm công khi đến và rời thư viện để quản lý theo dõi hiệu suất làm việc. Phương thức chấm công:
  o Tự động qua máy chấm công vân tay hoặc quẹt thẻ từ, hệ thống ghi nhận giờ đến – giờ về.
  o Thủ công trong trường hợp máy hỏng hoặc ngoại lệ khác, nhân viên có thể “chấm tay” qua hệ thống web. Các yêu cầu chấm tay cần được quản lý duyệt để đảm bảo chính xác.
- Xử lý ngoại lệ chấm công
  o Quên chấm công: nhân viên gửi yêu cầu bù công trên hệ thống, kèm thời gian thực tế, chờ quản lý duyệt trong vòng 24-48 giờ.
  o Xin nghỉ hoặc đổi ca: nhân viên tạo đơn online với lý do, ngày/ca xin nghỉ hoặc đổi ca, đính kèm giấy tờ nếu cần, quản lý xét duyệt và hệ thống tự động cập nhật ca của người thay thế.
- Khi thực hiện các tác vụ như nhập sách, duyệt phiếu mượn, thu phí phạt, hệ thống sẽ ghi nhận người thực hiện và lưu log chi tiết để hỗ trợ truy xuất.
- Đánh giá hiệu suất công việc
  o Hệ thống tổng hợp các chỉ số như số giao dịch mượn/trả/nhập, số đầu sách kiểm tra chất lượng,...
  o Tính tỷ lệ sai sót (nhập nhầm thông tin, bỏ sót quy trình, sai sót trên tổng số giao dịch) dựa trên log xử lý.
  o Báo cáo hiệu suất được lập theo tuần/tháng và dùng làm căn cứ khen thưởng (vượt chỉ tiêu, độ chính xác cao) hoặc nhắc nhở/kỷ luật (hiệu suất thấp, sai sót nhiều).
  o Quản lý hoặc phòng nhân sự chịu trách nhiệm xem xét báo cáo và ra quyết định khen thưởng/kỷ luật.

---

**3.6. Quản lý tài khoản và phân quyền**

- Hệ thống hỗ trợ ba loại tài khoản chính:
  o Độc giả: Chỉ có thể tìm kiếm, mượn sách và xem lịch sử mượn.
  o Nhân viên thư viện: Có quyền quản lý sách, xử lý mượn/trả, quản lý độc giả.
  o Quản lý: Có toàn quyền giám sát hệ thống, quản lý nhân viên, kiểm soát báo cáo.
- Ủy quyền tạm thời:
  o Nhân viên A có thể ủy quyền cho nhân viên B xử lý một số tác vụ (ví dụ: duyệt mượn/trả sách) khi vắng mặt.
  o Khi tạo ủy quyền, người tạo ủy quyền chọn phạm vi quyền hạn, thời gian bắt đầu và thời gian kết thúc.
  o Hệ thống tự động ghi nhận lịch sử ủy quyền, gửi thông báo cho cả hai bên.
  o Ủy quyền tự động hết hạn khi đến thời gian kết thúc hoặc có thể được thu hồi sớm bởi người ủy quyền hoặc quản lý thư viện.

---

**3.7. Quản lý doanh thu & báo cáo**

- Quản lý thư viện có thể xem thống kê chi tiết về hoạt động thư viện, bao gồm
  o Doanh thu: Số tiền thu được từ phí phạt và dịch vụ mượn sách.
  o Hoạt động nhân viên: lịch sử các giao dịch mượn – trả và các hoạt động liên quan của nhân viên thư viện.
  o Các thống kê và phân tích xu hướng:
  ▪ So sánh doanh thu và số lượt mượn theo tháng/quý/năm.
  ▪ Dự đoán nhu cầu mượn sách theo thể loại, tác giả, mùa (ví dụ: sách giáo trình đầu năm học, sách du lịch mùa hè).
  ▪ Đề xuất tăng giảm tồn kho sách theo xu hướng mượn – trả.
  ▪ Thống kê về hiệu suất nhà cung cấp: số đơn đặt, tổng giá trị nhập, điểm đánh giá trung bình.
  o Các báo cáo có thể được xuất ra định dạng PDF hoặc Excel để phục vụ quản lý.
- Cảnh báo bất thường:
  o Hệ thống tự động phát hiện biến động lớn về
  ▪ Doanh thu (doanh thu giảm hơn X% so với tháng trước, số lượt mượn có sự chênh lệch đột ngột).
  ▪ Hiệu suất nhà cung cấp (giao hàng chậm hơn X ngày, tỷ lệ trả hàng vượt Y%).
  o Giá trị X, Y có thể được cấu hình bởi quản lý.
  o Hệ thống gửi thông báo tức thì qua email, SMS hoặc ứng dụng nội bộ cho quản lý để kịp thời điều chỉnh chiến lược.
  o Lịch sử các cảnh báo được lưu trữ để theo dõi và phân tích sau này.
