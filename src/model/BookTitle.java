package model;

public class BookTitle {
    private int titleId;
    private String titleName;
    private String author;
    private String genre;
    private String publisher;
    private Integer publishYear; // dùng Integer để có thể null

    public BookTitle() {
    }

    public BookTitle(int titleId, String titleName, String author, String genre, String publisher, Integer publishYear) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.publishYear = publishYear;
    }

    // Getters và setters
    public int getTitleId() { return titleId; }
    public void setTitleId(int titleId) { this.titleId = titleId; }

    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
}
