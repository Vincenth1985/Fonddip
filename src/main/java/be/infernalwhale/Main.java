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
        FoodItem foodItem = new FoodItem(ticket);
        foodItem.setName("Mitraillette");
        foodItem.setPrice(5.5);
        foodItemDAO.createFoodItem(foodItem);

        foodItem.setName("Frikadelle");
        foodItem.setPrice(2.0);
        foodItemDAO.createFoodItem(foodItem);





    }
}
