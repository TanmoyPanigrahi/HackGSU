package example.ivorycirrus.mlkit.barcode;

public class Item {
    public String name;
    public String id;
    public String category;
    public double price;

    public Item(String id, String name, String category, double price) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.price = price;
    }
}
