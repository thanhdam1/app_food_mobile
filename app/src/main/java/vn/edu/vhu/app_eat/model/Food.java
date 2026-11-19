package vn.edu.vhu.app_eat.model;

public class Food {
    private long id;
    private String name;
    private String description;
    private double price;
    private Double discountPrice;
    private String imageUrl;
    private double rating;
    private int soldCount;
    private boolean isFlashSale;
    private boolean isBestSeller;

    public Food(long id, String name, String description, double price, Double discountPrice, String imageUrl, double rating, int soldCount, boolean isFlashSale, boolean isBestSeller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.soldCount = soldCount;
        this.isFlashSale = isFlashSale;
        this.isBestSeller = isBestSeller;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Double getDiscountPrice() { return discountPrice; }
    public String getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }
    public int getSoldCount() { return soldCount; }
    public boolean isFlashSale() { return isFlashSale; }
    public boolean isBestSeller() { return isBestSeller; }
}
