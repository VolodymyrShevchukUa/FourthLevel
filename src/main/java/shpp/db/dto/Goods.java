package shpp.db.dto;

public class Goods {

    private Category category;
    private String name;
    private double price;

    public Goods(Category category, String name, double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }



}
