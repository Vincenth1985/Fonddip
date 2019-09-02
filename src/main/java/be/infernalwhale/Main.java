package be.infernalwhale;

import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {



/* Zorg daarna dat de FooditemDAO alle CRUD bewerkingen kan uitvoeren:
  Fooditems opslaan, aanmaken, verwijderen en lezen uit de database.
- Als laatste deel van de opdracht: Voeg de naam van de klant toe aan de Ticket class
  en zorg ervoor dat we een ticket kunnen ophalen op basis van de klant.*/


        Ticket ticket = new Ticket();
        ticket.setStatus(Ticket.Status.ORDERED);
        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.deleteTicket(1);


//        FoodItem foodItem = new FoodItem(ticket.setTicketID(1));
//        foodItem.setPrice(3.0);
//        foodItem.setName("frikadelle");
//
        FoodItemDAO foodItemDAO = new FoodItemDAO();
//        foodItemDAO.createFoodItem(foodItem);


        try {
            foodItemDAO.deleteFoodItem();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//
//        foodItemDAO.updateFoodItem(foodItemDAO.getFoodItemWithId(23), "name", "boulette");
//
//        ticket.setFoodItems(foodItemDAO.getItemsForTicket(1));
//        ticket.getFoodItemList().forEach(System.out::println);


    }
}
