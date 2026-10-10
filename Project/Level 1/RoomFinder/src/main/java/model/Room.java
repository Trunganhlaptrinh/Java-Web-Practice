package model;

public class Room {
    private int id;
    private int landlordId;
    private String title;
    private String description;
    private long price;
    private float area;
    private String address;
    private String imageUrl;
    private String status;
    private String createdAt;
    private String updatedAt;

    // Display fields from JOIN
    private String landlordName;
    private String landlordPhone;

    public Room() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public float getArea() { return area; }
    public void setArea(float area) { this.area = area; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String landlordName) { this.landlordName = landlordName; }
    public String getLandlordPhone() { return landlordPhone; }
    public void setLandlordPhone(String landlordPhone) { this.landlordPhone = landlordPhone; }
}

