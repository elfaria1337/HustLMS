-- Bảng reader
CREATE TABLE reader (
    reader_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100)
);

-- Bảng staff
CREATE TABLE staff (
    staff_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    hire_date DATE NOT NULL
);

-- Bảng supplier
CREATE TABLE supplier (
    supplier_id SERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_person VARCHAR(100) NOT NULL
);

-- Bảng book_title
CREATE TABLE book_title (
    title_id SERIAL PRIMARY KEY,
    title_name VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    publish_year INT
);

-- Bảng inventory
CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    location_name VARCHAR(255) NOT NULL
);

-- Bảng account
CREATE TABLE account (
    account_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reader_id INT,
    staff_id INT,
    CONSTRAINT fk_account_reader FOREIGN KEY (reader_id) REFERENCES reader(reader_id),
    CONSTRAINT fk_account_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

-- Bảng fine
CREATE TABLE fine (
    fine_id SERIAL PRIMARY KEY,
    reason VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    issue_date DATE NOT NULL,
    reader_id INT NOT NULL,
    CONSTRAINT fk_fine_reader FOREIGN KEY (reader_id) REFERENCES reader(reader_id)
);

-- Bảng invoice
CREATE TABLE invoice (
    invoice_id SERIAL PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_date DATE NOT NULL,
    reader_id INT NOT NULL,
    fine_id INT,
    CONSTRAINT fk_invoice_reader FOREIGN KEY (reader_id) REFERENCES reader(reader_id),
    CONSTRAINT fk_invoice_fine FOREIGN KEY (fine_id) REFERENCES fine(fine_id)
);

-- Bảng reservation
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    reservation_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    reader_id INT NOT NULL,
    title_id INT NOT NULL,
    CONSTRAINT fk_reservation_reader FOREIGN KEY (reader_id) REFERENCES reader(reader_id),
    CONSTRAINT fk_reservation_book_title FOREIGN KEY (title_id) REFERENCES book_title(title_id)
);

-- Bảng loan
CREATE TABLE loan (
    loan_id SERIAL PRIMARY KEY,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    reader_id INT NOT NULL,
    CONSTRAINT fk_loan_reader FOREIGN KEY (reader_id) REFERENCES reader(reader_id)
);

-- Bảng handle_by
CREATE TABLE handle_by (
    handle_id SERIAL PRIMARY KEY,
    handle_date DATE NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    staff_id INT NOT NULL,
    loan_id INT NOT NULL,
    CONSTRAINT fk_handleby_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
    CONSTRAINT fk_handleby_loan FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);

-- Bảng book_copy
CREATE TABLE book_copy (
    copy_id SERIAL PRIMARY KEY,
    state VARCHAR(50) NOT NULL,
    inventory_id INT NOT NULL,
    title_id INT NOT NULL,
    CONSTRAINT fk_bookcopy_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id),
    CONSTRAINT fk_bookcopy_booktitle FOREIGN KEY (title_id) REFERENCES book_title(title_id)
);

-- Bảng loan_detail
CREATE TABLE loan_detail (
    loan_detail_id SERIAL PRIMARY KEY,
    return_date DATE,
    loan_id INT NOT NULL,
    copy_id INT NOT NULL,
    CONSTRAINT fk_loan_detail_loan FOREIGN KEY (loan_id) REFERENCES loan(loan_id),
    CONSTRAINT fk_loan_detail_copy FOREIGN KEY (copy_id) REFERENCES book_copy(copy_id)
);

-- Bảng book_import
CREATE TABLE book_import (
    import_id SERIAL PRIMARY KEY,
    order_date DATE NOT NULL,
    receive_date DATE NOT NULL,
    supplier_id INT NOT NULL,
    CONSTRAINT fk_book_import_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);

-- Bảng book_import_detail
CREATE TABLE book_import_detail (
    import_detail_id SERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    title_id INT NOT NULL,
    import_id INT NOT NULL,
    CONSTRAINT fk_import_detail_title FOREIGN KEY (title_id) REFERENCES book_title(title_id),
    CONSTRAINT fk_import_detail_import FOREIGN KEY (import_id) REFERENCES book_import(import_id)
);
