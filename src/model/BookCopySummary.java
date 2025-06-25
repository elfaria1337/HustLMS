package model;

public class BookCopySummary {
    private int titleId;
    private int copyCount;

    public BookCopySummary() {}

    public BookCopySummary(int titleId, int copyCount) {
        this.titleId = titleId;
        this.copyCount = copyCount;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getCopyCount() {
        return copyCount;
    }

    public void setCopyCount(int copyCount) {
        this.copyCount = copyCount;
    }
}
