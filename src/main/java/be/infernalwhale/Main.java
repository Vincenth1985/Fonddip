package be.infernalwhale;

import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {



/* Zorg daarna dat de FooditemDAO alle CRUD bewerkingen kan uitvoeren:
  Fooditems opslaan, aanmaken, verwijderen en lezen uit de database.
- Als laatste deel van de opdracht: Voeg de naam van de klant toe aan de Ticket class
  en zorg ervoor dat we een ticket kunnen ophalen op basis van de klant.*/


       Ticket ticket = new Ticket();
       ticket.setStatus(Ticket.Status.ORDERED);


       TicketDAO ticketDAO = new TicketDAO();

        try {
            ticketDAO.createTicket(ticket);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(ticket.getTicketID());

        FoodItem foodItem = new FoodItem(ticket.setTicketID(1));
        foodItem.setPrice(3.0);
        foodItem.setName("sauces");

        FoodItemDAO foodItemDAO = new FoodItemDAO();
        foodItemDAO.createFoodItem(foodItem);

        //Testing deletefoodItem.
        try {
            foodItemDAO.deleteFoodItem(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
