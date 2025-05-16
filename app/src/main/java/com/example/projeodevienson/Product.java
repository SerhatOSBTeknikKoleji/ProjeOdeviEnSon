package com.example.projeodevienson;

public class Product {
    private int id;
    private int imageResId;
    private String name;
    private String price;
    private int quantity;

    // Constructor - Sepet için miktar dahil
    public Product(int id, int imageResId, String name, String price, int quantity) {
        this.id = id;
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Constructor - sadece listeleme için (örnek ürünler)
    public Product(int imageResId, String name, String price) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.quantity = 1; // varsayılan miktar 1
    }

    // Getter'lar
    public int getId() { return id; }

    public int getImageResId() { return imageResId; }

    public String getName() { return name; }

    public String getPrice() { return price; }

    public int getQuantity() { return quantity; }

    // Setter'lar
    public void setId(int id) { this.id = id; }

    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public void setName(String name) { this.name = name; }

    public void setPrice(String price) { this.price = price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Fiyatı sayıya çeviren yardımcı metot
    public double getPriceValue() {
        try {
            return Double.parseDouble(price.replace("₺", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}

