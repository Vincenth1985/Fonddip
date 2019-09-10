package be.infernalwhale.dao;

import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO {
    private final Connection connection;

    public FoodItemDAO() throws SQLException {
            connection = DBConnector.getConnection();
    }


    public List<FoodItem> getItemsForTicket(int ticketID) throws SQLException {

        List<FoodItem> foodList = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM foodItem WHERE ticket = ?");
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

    public void createFoodItem(String name,String price,int ticketId) {

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO fooditem (name,price,ticket) VALUES (?,?,?)")) {

            statement.setString(1, name);
            statement.setDouble(2, Double.parseDouble(price));
            statement.setInt(3, ticketId);

            statement.executeUpdate();

        } catch (SQLException SQL) {
            SQL.printStackTrace();
            System.out.println("Error Creating");
        }

    }

    public void deleteFoodItem(List<Integer> integer) throws SQLException {

        for (int i = 0; i < integer.size(); i++) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM fooditem WHERE id = ?");
            statement.setInt(1, integer.get(i));
            int count = statement.executeUpdate();

            if (count > 1) throw new NonUniqueResultException("More than 1 fooditem was removed from the ticket");


        }

    }

    public void updateFoodItem(FoodItem foodItem, java.lang.String itemToChange, Object value) {

        try (PreparedStatement statement = connection.prepareStatement("UPDATE fooditem Set " + itemToChange + " = ? WHERE id = ?")) {

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
        java.lang.String query = "SELECT * FROM fooditem WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return new FoodItem((new TicketDAO()).getTicketByID(rs.getInt("ticket")))
                    .setName(rs.getString("name"))
                    .setPrice(rs.getDouble("price"))
                    .setId(rs.getInt("id"));
    }

 
}



