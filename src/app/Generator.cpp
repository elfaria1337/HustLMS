#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <random>
#include <ctime>
#include <map>

// Random helpers
int randInt(int min, int max) {
    static std::mt19937 gen((unsigned int)time(nullptr));
    std::uniform_int_distribution<> dist(min, max);
    return dist(gen);
}

std::string randomDate(int startYear, int endYear) {
    int year = randInt(startYear, endYear);
    int month = randInt(1, 12);
    int day = randInt(1, 28);
    char buffer[11];
    snprintf(buffer, sizeof(buffer), "%04d-%02d-%02d", year, month, day);
    return std::string(buffer);
}

std::string randomPhone() {
    std::string phone = "0";
    for (int i = 0; i < 9; i++) phone += '0' + randInt(0, 9);
    return phone;
}

std::string randomEmail(const std::string& name) {
    std::string email;
    for (char c : name) {
        if (c != ' ') email += tolower(c);
    }
    email += std::to_string(randInt(1, 9999)) + "@example.com";
    return email;
}

std::string randomName() {
    const std::vector<std::string> firstNames = {"Nguyen", "Tran", "Le", "Pham", "Hoang", "Vu", "Phan", "Bui", "Do", "Ho"};
    const std::vector<std::string> middleNames = {"Van", "Thi", "Huu", "Duc", "Quang", "Minh", "Thanh", "Anh", "Tuan", "Hanh"};
    const std::vector<std::string> lastNames = {"An", "Binh", "Cuong", "Duy", "Giang", "Hung", "Khanh", "Linh", "Mai", "Nam"};
    return firstNames[randInt(0, (int)firstNames.size()-1)] + " " +
           middleNames[randInt(0, (int)middleNames.size()-1)] + " " +
           lastNames[randInt(0, (int)lastNames.size()-1)];
}

std::string randomAddress() {
    const std::vector<std::string> streets = {"Le Loi", "Tran Hung Dao", "Nguyen Trai", "Le Duan", "Hai Ba Trung"};
    const std::vector<std::string> districts = {"Quan 1", "Quan 3", "Quan 5", "Quan 10", "Binh Thanh"};
    return std::to_string(randInt(1, 200)) + " " + streets[randInt(0, (int)streets.size()-1)] + ", " + districts[randInt(0, (int)districts.size()-1)];
}

std::string randomCompanyName() {
    const std::vector<std::string> companies = {"ABC Corp", "XYZ Ltd", "Tech Solutions", "Global Trade", "Sunrise Co"};
    return companies[randInt(0, (int)companies.size()-1)];
}

std::string randomBookTitle() {
    const std::vector<std::string> titles = {
        "Lập trình C++ cơ bản", "Hướng dẫn Java", "Python cho người mới bắt đầu",
        "Thiết kế web hiện đại", "Khoa học dữ liệu", "Trí tuệ nhân tạo",
        "Mạng máy tính", "Hệ điều hành", "Toán rời rạc", "Cấu trúc dữ liệu"
    };
    return titles[randInt(0, (int)titles.size()-1)];
}

std::string randomAuthor() {
    const std::vector<std::string> authors = {
        "Nguyen Van A", "Tran Thi B", "Le Van C", "Pham Minh D", "Hoang Anh E"
    };
    return authors[randInt(0, (int)authors.size()-1)];
}

std::string randomGenre() {
    const std::vector<std::string> genres = {
        "Công nghệ", "Khoa học", "Văn học", "Lịch sử", "Kinh tế"
    };
    return genres[randInt(0, (int)genres.size()-1)];
}

std::string randomPublisher() {
    const std::vector<std::string> publishers = {
        "NXB Giáo dục", "NXB Trẻ", "NXB Khoa học", "NXB Tổng hợp", "NXB Văn hóa"
    };
    return publishers[randInt(0, (int)publishers.size()-1)];
}

std::string randomInventoryLocation() {
    const std::vector<std::string> locations = {
        "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ"
    };
    return locations[randInt(0, (int)locations.size()-1)];
}

std::string randomBookCopyState() {
    const std::vector<std::string> states = {"Sẵn sàng", "Đang mượn", "Hỏng"};
    return states[randInt(0, (int)states.size()-1)];
}

std::string randomRole() {
    const std::vector<std::string> roles = {"Reader", "Staff", "Admin"};
    return roles[randInt(0, (int)roles.size()-1)];
}

std::string randomStatus() {
    const std::vector<std::string> statuses = {"Active", "Inactive", "Locked"};
    return statuses[randInt(0, (int)statuses.size()-1)];
}

std::string randomActionType() {
    const std::vector<std::string> actions = {"Create", "Update", "Delete"};
    return actions[randInt(0, (int)actions.size()-1)];
}

int main() {
    std::ofstream fout("data_full.sql");

    int readerCount = 1000;
    int staffCount = 200;
    int supplierCount = 100;
    int bookTitleCount = 500;
    int inventoryCount = 10;
    int bookCopyCount = 2000;
    int accountCount = readerCount + staffCount; // simple mapping 1:1
    int loanCount = 1500;
    int fineCount = 300;
    int invoiceCount = 300;
    int reservationCount = 400;
    int loanDetailCount = 1800;
    int handleByCount = 1500;
    int bookImportCount = 200;
    int bookImportDetailCount = 500;

    // 1. Reader
    for (int i = 1; i <= readerCount; i++) {
        std::string fullName = randomName();
        std::string birthDate = randomDate(1950, 2010);
        std::string address = randomAddress();
        std::string phone = randomPhone();
        std::string email = randomEmail(fullName);
        fout << "INSERT INTO reader(full_name, birth_date, address, phone, email) VALUES ("
             << "'" << fullName << "', '" << birthDate << "', '" << address << "', '"
             << phone << "', '" << email << "');\n";
    }

    // 2. Staff
    for (int i = 1; i <= staffCount; i++) {
        std::string fullName = randomName();
        std::string birthDate = randomDate(1950, 1995);
        std::string address = randomAddress();
        std::string phone = randomPhone();
        std::string email = randomEmail(fullName);
        std::string hireDate = randomDate(2000, 2024);
        fout << "INSERT INTO staff(full_name, birth_date, address, phone, email, hire_date) VALUES ("
             << "'" << fullName << "', '" << birthDate << "', '" << address << "', '"
             << phone << "', '" << email << "', '" << hireDate << "');\n";
    }

    // 3. Supplier
    for (int i = 1; i <= supplierCount; i++) {
        std::string company = randomCompanyName();
        std::string phone = randomPhone();
        std::string email = company;
        for (auto & c: email) c = tolower(c);
        email = email + std::to_string(randInt(1,9999)) + "@supplier.com";
        std::string address = randomAddress();
        std::string contactPerson = randomName();
        fout << "INSERT INTO supplier(company_name, phone, email, address, contact_person) VALUES ("
             << "'" << company << "', '" << phone << "', '" << email << "', '"
             << address << "', '" << contactPerson << "');\n";
    }

    // 4. Book Title
    for (int i = 1; i <= bookTitleCount; i++) {
        std::string titleName = randomBookTitle();
        std::string author = randomAuthor();
        std::string genre = randomGenre();
        std::string publisher = randomPublisher();
        int publishYear = randInt(1980, 2023);
        fout << "INSERT INTO book_title(title_name, author, genre, publisher, publish_year) VALUES ("
             << "'" << titleName << "', '" << author << "', '" << genre << "', '"
             << publisher << "', " << publishYear << ");\n";
    }

    // 5. Inventory
    for (int i = 1; i <= inventoryCount; i++) {
        std::string location = randomInventoryLocation();
        fout << "INSERT INTO inventory(location_name) VALUES ('" << location << "');\n";
    }

    // 6. Account
    // Giả sử reader_id = 1..readerCount, staff_id = 1..staffCount
    for (int i = 1; i <= accountCount; i++) {
        std::string username = "user" + std::to_string(i);
        std::string password = "pass" + std::to_string(i);
        std::string role = (i <= readerCount) ? "Reader" : "Staff";
        std::string status = "Active";
        int reader_id = (i <= readerCount) ? i : 0;
        int staff_id = (i > readerCount) ? (i - readerCount) : 0;
        fout << "INSERT INTO account(username, password, role, status, reader_id, staff_id) VALUES ("
             << "'" << username << "', '" << password << "', '" << role << "', '" << status << "', "
             << (reader_id == 0 ? "NULL" : std::to_string(reader_id)) << ", "
             << (staff_id == 0 ? "NULL" : std::to_string(staff_id)) << ");\n";
    }

    // 7. Book Copy
    for (int i = 1; i <= bookCopyCount; i++) {
        std::string state = randomBookCopyState();
        int inventory_id = randInt(1, inventoryCount);
        int title_id = randInt(1, bookTitleCount);
        fout << "INSERT INTO book_copy(state, inventory_id, title_id) VALUES ('"
             << state << "', " << inventory_id << ", " << title_id << ");\n";
    }

    // 8. Loan
    // reader_id 1..readerCount, loan_date, due_date
    for (int i = 1; i <= loanCount; i++) {
        int reader_id = randInt(1, readerCount);
        std::string loan_date = randomDate(2022, 2024);
        std::string due_date = randomDate(2024, 2025);
        if (due_date < loan_date) std::swap(due_date, loan_date);
        fout << "INSERT INTO loan(loan_date, due_date, reader_id) VALUES ('"
             << loan_date << "', '" << due_date << "', " << reader_id << ");\n";
    }

    // 9. Loan Detail
    // Chọn loan_id 1..loanCount, copy_id 1..bookCopyCount, return_date có thể null
    for (int i = 1; i <= loanDetailCount; i++) {
        int loan_id = randInt(1, loanCount);
        int copy_id = randInt(1, bookCopyCount);
        std::string return_date = (randInt(0,1) == 1) ? randomDate(2022, 2024) : "NULL";
        fout << "INSERT INTO loan_detail(return_date, loan_id, copy_id) VALUES ("
             << (return_date == "NULL" ? "NULL" : ("'" + return_date + "'")) << ", "
             << loan_id << ", " << copy_id << ");\n";
    }

    // 10. Fine
    for (int i = 1; i <= fineCount; i++) {
        std::string reason = "Lỗi vi phạm số " + std::to_string(i);
        double amount = randInt(10, 500);
        std::string issue_date = randomDate(2020, 2024);
        int reader_id = randInt(1, readerCount);
        fout << "INSERT INTO fine(reason, amount, issue_date, reader_id) VALUES ('"
             << reason << "', " << amount << ", '" << issue_date << "', " << reader_id << ");\n";
    }

    // 11. Invoice
    for (int i = 1; i <= invoiceCount; i++) {
        double amount = randInt(10, 500);
        std::string payment_method = (i % 2 == 0) ? "Tiền mặt" : "Chuyển khoản";
        std::string payment_date = randomDate(2022, 2024);
        int reader_id = randInt(1, readerCount);
        // Giả định liên kết đến fine ngẫu nhiên hoặc NULL
        int fine_id = (i % 3 == 0) ? randInt(1, fineCount) : 0;
        fout << "INSERT INTO invoice(amount, payment_method, payment_date, reader_id, fine_id) VALUES ("
             << amount << ", '" << payment_method << "', '" << payment_date << "', "
             << reader_id << ", " << (fine_id == 0 ? "NULL" : std::to_string(fine_id)) << ");\n";
    }

    // 12. Reservation
    for (int i = 1; i <= reservationCount; i++) {
        std::string reservation_date = randomDate(2023, 2024);
        std::string status = "Pending";
        int reader_id = randInt(1, readerCount);
        int title_id = randInt(1, bookTitleCount);
        fout << "INSERT INTO reservation(reservation_date, status, reader_id, title_id) VALUES ('"
             << reservation_date << "', '" << status << "', " << reader_id << ", " << title_id << ");\n";
    }

    // 13. Handle By
    for (int i = 1; i <= handleByCount; i++) {
        std::string handle_date = randomDate(2022, 2024);
        std::string action_type = randomActionType();
        int staff_id = randInt(1, staffCount);
        int loan_id = randInt(1, loanCount);
        fout << "INSERT INTO handle_by(handle_date, action_type, staff_id, loan_id) VALUES ('"
             << handle_date << "', '" << action_type << "', " << staff_id << ", " << loan_id << ");\n";
    }

    // 14. Book Import
    for (int i = 1; i <= bookImportCount; i++) {
        std::string order_date = randomDate(2023, 2024);
        std::string receive_date = randomDate(2023, 2024);
        if (receive_date < order_date) std::swap(receive_date, order_date);
        int supplier_id = randInt(1, supplierCount);
        fout << "INSERT INTO book_import(order_date, receive_date, supplier_id) VALUES ('"
             << order_date << "', '" << receive_date << "', " << supplier_id << ");\n";
    }

    // 15. Book Import Detail
    for (int i = 1; i <= bookImportDetailCount; i++) {
        int quantity = randInt(1, 100);
        double price_per_unit = randInt(10000, 500000) / 100.0;
        int title_id = randInt(1, bookTitleCount);
        int import_id = randInt(1, bookImportCount);
        fout << "INSERT INTO book_import_detail(quantity, price_per_unit, title_id, import_id) VALUES ("
             << quantity << ", " << price_per_unit << ", " << title_id << ", " << import_id << ");\n";
    }

    fout.close();
    std::cout << "Data generation complete: data_full.sql\n";
    return 0;
}
