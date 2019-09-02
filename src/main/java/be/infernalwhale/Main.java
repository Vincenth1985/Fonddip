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
        ticketDAO.deleteTicket(8,9,10,11,12,13,14,15,16,17);

//        try {
//            ticketDAO.createTicket(ticket);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


        FoodItem foodItem = new FoodItem(ticket.setTicketID(1));
        foodItem.setPrice(3.0);
        foodItem.setName("frikadelle");

        FoodItemDAO foodItemDAO = new FoodItemDAO();
        foodItemDAO.createFoodItem(foodItem);

        //    Testing deletefoodItem.

        try {
            foodItemDAO.deleteFoodItem(4,5,6,7,8,9,11);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        foodItemDAO.updateFoodItem(foodItemDAO.getFoodItemWithId(10), "name", "boulette");


    }
}
