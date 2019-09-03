package be.infernalwhale.view;

import be.infernalwhale.dao.DBConnector;
import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class WindowController {

    private FoodItemDAO foodItemDAO = new FoodItemDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private List<FoodItem> foodItems = new ArrayList<>();
    private String statusTicket;
    private int ticketId;
    private double totalPrice = 0;


    @FXML
    private TextField searchByTicketIdField;

    @FXML
    private TextArea printDataArea;

    @FXML
    private TextField deleteFoodItem;

    @FXML
    private TextField foodToAdd;

    @FXML
    private TextField priceToAdd;


    @FXML
    public List<FoodItem> searchItemsByTicketId() {

        try {
            foodItems = foodItemDAO.getItemsForTicket(Integer.parseInt(searchByTicketIdField.getText()));


            StringBuilder stringBuilder = new StringBuilder();
            foodItems.forEach(e -> stringBuilder.append(e + "\n"));

            printDataArea.setText(stringBuilder.toString());


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return foodItems;
    }

    @FXML
    public void deleteFoodItem() {

        try {
            foodItemDAO.deleteFoodItem(Integer.parseInt(deleteFoodItem.getText()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public int createTicket() {

        try {
            ticketDAO.createTicket(new Ticket().setStatus(Ticket.Status.ORDERED));
            Statement statement = DBConnector.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT ticketId,status from ticket ORDER BY ticketId DESC limit 1");
            rs.next();
            ticketId = rs.getInt("ticketId");
            statusTicket = rs.getString("status");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ticketId);
            printDataArea.setText("New Ticket Created with number : " + stringBuilder.toString() + "\nStatus : " + statusTicket);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketId;
    }

    @FXML
    public void addItemToCurrentTicket() {
        try {
            if (!(foodToAdd.getText().equals("")) || !(priceToAdd.getText().equals(""))) {

                PreparedStatement query = DBConnector
                        .getConnection()
                        .prepareStatement("INSERT INTO fooditem (name,price,ticket) VALUES (?,?,?)");
                query.setString(1, foodToAdd.getText());
                query.setFloat(2, Float.parseFloat(priceToAdd.getText()));
                query.setInt(3, ticketId);
                query.executeUpdate();
                printDataArea.setText(foodToAdd.getText() + " Added on ticket : " + ticketId);

            }


        } catch (SQLException e) {
            printDataArea.setText("Create New Ticket First");
        }
    }

    @FXML
    public void printTotalOfOrder() {

        try (PreparedStatement query = DBConnector.getConnection().prepareStatement("SELECT price from foodItem where ticket = ?")) {
            query.setInt(1, ticketId);
            ResultSet rs = query.executeQuery();
            foodItems = foodItemDAO.getItemsForTicket(ticketId);
            while (rs.next()) {
                totalPrice += rs.getFloat("price");
            }

        } catch (SQLException sql) {
            sql.printStackTrace();
        }


        StringBuilder stringBuilder = new StringBuilder();
        foodItems.forEach(e -> stringBuilder.append(e + "\n"));

        printDataArea.clear();
        printDataArea.appendText("Ticket NO : " + ticketId + " " + statusTicket + "\n");
        printDataArea.appendText("-".repeat(40) + "\n");
        printDataArea.appendText(stringBuilder.toString());
        printDataArea.appendText("-".repeat(40) + "\n");
        printDataArea.appendText(String.format("Total To Pay : %.3f  € ", totalPrice));


    }
}
