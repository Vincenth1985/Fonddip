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

            foodList.add(new FoodItem(ticket).setName(rs.getString("name"))
                    .setPrice(rs.getDouble("price"))
                    .setId(rs.getInt("id")));
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

    public void deleteFoodItem(Integer... integer) throws SQLException {

        for (int i = 0; i < integer.length; i++) {
            PreparedStatement statement = DBConnector
                    .getConnection()
                    .prepareStatement("DELETE FROM fooditem WHERE id = ?");
            statement.setInt(1, integer[ i ]);
            int count = statement.executeUpdate();

            if (count > 1) throw new NonUniqueResultException("More than 1 fooditem was removed from the ticket");


        }

    }

    public void updateFoodItem(FoodItem foodItem, String itemToChange, Object value) {

        try (PreparedStatement statement = DBConnector.getConnection()
                .prepareStatement("UPDATE fooditem Set " + itemToChange + " = ? WHERE id = ?")) {

            statement.setObject(1, value);
            statement.setObject(2, foodItem.getId());
            statement.executeUpdate();
            System.out.println("update ok");

        } catch (SQLException SQL) {
            SQL.printStackTrace();
            System.out.println("Error Updating");
        }

    }

    public FoodItem getFoodItemWithId(Integer id) throws SQLException {
        String query = "SELECT * FROM fooditem WHERE id = ?";
        try (Connection connection = DBConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return new FoodItem((new TicketDAO()).getTicketByID(rs.getInt("ticket")))
                    .setName(rs.getString("name"))
                    .setPrice(rs.getDouble("price"))
                    .setId(rs.getInt("id"));
        } catch (SQLException SQL) {
            System.out.println("No Items found in FoodItem");
        }
        return null;
    }


}



