package be.infernalwhale.model;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private Integer ticketID;
    private List<FoodItem> foodItemList;
    private Double totalToPay;
    private Status status;

    public Integer getTicketID() {
        return ticketID;
    }

    public Ticket setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
        return this;
    }

    public Double getTotalToPay() {
        return totalToPay;
    }

    public Ticket setTotalToPay(Double totalToPay) {
        this.totalToPay = totalToPay;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Ticket setStatus(Status status) {
        this.status = status;
        return this;
    }

    public void addFoodItem(FoodItem item) {
        if (foodItemList == null) foodItemList = new ArrayList<FoodItem>();
        foodItemList.add(item);
    }

    public void removeFoodItem(FoodItem item) {
        foodItemList.remove(item);
    }

    public void setFoodItems(List<FoodItem> list) {
        this.foodItemList = list;
    }

    public enum Status {
        ORDERED, PAYED, PROCESSING, READY, FINISHED
    }
}
