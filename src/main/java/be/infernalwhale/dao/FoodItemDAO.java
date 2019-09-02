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

        while (rs.next()) {

            foodList.add(new FoodItem(new Ticket()).setId(rs.getInt("ticket")));
        }

        return foodList;
    }


    public FoodItem createFoodItem(FoodItem foodItem) throws SQLException {


        PreparedStatement statement = DBConnector.getConnection()
                .prepareStatement("INSERT INTO fooditem (id,name,price) VALUES (?,?,?)");

        statement.setInt(1, foodItem.getId());
        statement.setString(2, foodItem.getName());
        statement.setDouble(3, foodItem.getPrice());

        statement.executeUpdate();

        return foodItem;

    }


}
