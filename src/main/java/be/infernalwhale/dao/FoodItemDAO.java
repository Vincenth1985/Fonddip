package be.infernalwhale.dao;

import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO {


    public List<FoodItem> getItemsForTicket(int ticketID) throws SQLException {

        List<FoodItem> foodList = new ArrayList<>();

        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("SELECT * FROM foodItem WHERE ticket = ?");
        statement.setInt(1, ticketID);

        ResultSet rs = statement.executeQuery();

        Ticket ticket = new Ticket();
        ticket.setTicketID(ticketID);

        while (rs.next()) {

            foodList.add(new FoodItem(ticket).setId(rs.getInt("id"))
                    .setPrice(rs.getDouble("price"))
                    .setName("name"));
        }

        return foodList;
    }


    public void createFoodItem(FoodItem foodItem) {


        try (PreparedStatement statement = DBConnector.getConnection()
                .prepareStatement("INSERT INTO fooditem (name,price,ticket) VALUES (?,?,?)")) {

            statement.setString(1, foodItem.getName());
            statement.setDouble(2, foodItem.getPrice());
            statement.setInt(3, foodItem.getTicket().getTicketID());

            statement.executeUpdate();

        } catch (SQLException SQL) {
            SQL.printStackTrace();
            System.out.println("Error Creating");
        }

    }

    public boolean deleteFoodItem(int id) throws SQLException {
        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("DELETE FROM fooditem WHERE id = ?");
        statement.setInt(1, id);
        int count = statement.executeUpdate();

        if (count > 1) throw new NonUniqueResultException("More than 1 fooditem was removed from the ticket");
        return count == 1;
    }


}
