package vn.edu.vhu.app_eat.model;

public class Banner {
    private String title;
    private String subtitle;
    private String tag;
    private String imageUrl;
    private int backgroundColor;

    public Banner(String title, String subtitle, String tag, String imageUrl, int backgroundColor) {
        this.title = title;
        this.subtitle = subtitle;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getTag() { return tag; }
    public String getImageUrl() { return imageUrl; }
    public int getBackgroundColor() { return backgroundColor; }
}
