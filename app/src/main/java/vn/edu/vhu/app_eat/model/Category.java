package vn.edu.vhu.app_eat.model;

public class Category {
    private int id;
    private String name;
    private String iconUrl;

    public Category(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIconUrl() { return iconUrl; }
}
