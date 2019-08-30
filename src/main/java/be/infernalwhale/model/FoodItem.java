package be.infernalwhale.model;

public class FoodItem {
    private Integer id;
    private String name;
    private Double price;
    private Ticket ticket;

    public FoodItem(Ticket ticket) {
        this.ticket = ticket;
    }

    public Integer getId() {
        return id;
    }

    public FoodItem setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FoodItem setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public FoodItem setPrice(Double price) {
        this.price = price;
        return this;
    }
}
