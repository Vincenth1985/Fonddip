package be.infernalwhale.dao;

import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            FoodItem foodItem = new FoodItem(new Ticket());

            foodList.add(foodItem.setId(rs.getInt("ticket")));
        }


        return foodList;
    } 
}
