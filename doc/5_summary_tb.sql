CREATE TABLE book_copy_summary (
    title_id INT PRIMARY KEY,
    copy_count INT NOT NULL DEFAULT 0
);

INSERT INTO book_copy_summary (title_id, copy_count)
SELECT title_id, COUNT(*)
FROM book_copy
GROUP BY title_id;

INSERT INTO book_copy_summary (title_id, copy_count)
SELECT title_id, COUNT(*)
FROM book_copy
GROUP BY title_id
ON CONFLICT (title_id) DO UPDATE
SET copy_count = EXCLUDED.copy_count;

CREATE OR REPLACE FUNCTION trg_update_book_copy_summary()
RETURNS TRIGGER AS $$
DECLARE
    rows_updated INT;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE book_copy_summary
        SET copy_count = copy_count + 1
        WHERE title_id = NEW.title_id;
        GET DIAGNOSTICS rows_updated = ROW_COUNT;

        IF rows_updated = 0 THEN
            INSERT INTO book_copy_summary(title_id, copy_count)
            VALUES (NEW.title_id, 1);
        END IF;

    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE book_copy_summary
        SET copy_count = copy_count - 1
        WHERE title_id = OLD.title_id;

    ELSIF (TG_OP = 'UPDATE') THEN
        IF OLD.title_id <> NEW.title_id THEN
            -- Giảm count ở title cũ
            UPDATE book_copy_summary
            SET copy_count = copy_count - 1
            WHERE title_id = OLD.title_id;

            -- Tăng count ở title mới
            UPDATE book_copy_summary
            SET copy_count = copy_count + 1
            WHERE title_id = NEW.title_id;
            GET DIAGNOSTICS rows_updated = ROW_COUNT;

            IF rows_updated = 0 THEN
                INSERT INTO book_copy_summary(title_id, copy_count)
                VALUES (NEW.title_id, 1);
            END IF;
        END IF;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_book_copy_summary
AFTER INSERT OR DELETE OR UPDATE ON book_copy
FOR EACH ROW
EXECUTE FUNCTION trg_update_book_copy_summary();
