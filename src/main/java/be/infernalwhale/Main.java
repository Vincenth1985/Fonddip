package be.infernalwhale;

import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        TicketDAO ticketDAO = new TicketDAO();
        FoodItemDAO foodItemDAO = new FoodItemDAO();

        //Create ticket and adding on Database.
        Ticket ticket = new Ticket();
        ticket.setTicketID(1);
        ticket.setStatus(Ticket.Status.ORDERED);
        ticketDAO.createTicket(ticket);


        //FoodItems for one ticket number and adding on Database.
        FoodItem foodItem = new FoodItem(ticket.setStatus(Ticket.Status.ORDERED));

//        foodItem.setName("Mitraillette");
//        foodItem.setId(1);
//        foodItem.setPrice(5.5);
//        foodItemDAO.createFoodItem(foodItem);
//
//        foodItem.setName("Frikadelle");
//        foodItem.setId(2);
//        foodItem.setPrice(2.0);
//        foodItemDAO.createFoodItem(foodItem);
//
//        foodItem.setId(3);
//        foodItem.setName("Sauce");
//        foodItem.setPrice(0.30);
//        foodItemDAO.createFoodItem(foodItem);


        //Setting list of food per item
        ticket.setFoodItems(foodItemDAO.getItemsForTicket(1));

        //Foodlist Printing for the target ticket id.
        ticket.getFoodItemList().forEach(System.out::println);

        //Summing Total price from ticket.
        ticket.setTotalToPay(ticket.getFoodItemList().stream().mapToDouble(e -> e.getPrice()).reduce(0, (a, b) -> a + b));


        //Testing output.
        System.out.println(ticket.getTotalToPay() + "€ to pay for your order");


    }
}
