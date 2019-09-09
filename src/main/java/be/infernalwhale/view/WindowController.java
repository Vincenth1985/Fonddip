package be.infernalwhale.view;

import be.infernalwhale.dao.DBConnector;
import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WindowController {

    private FoodItemDAO foodItemDAO = new FoodItemDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private int activeTicket;


    @FXML
    private MenuButton menuButton;

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
    private TextArea printLogger;


    //--------------------------------------------------
    public void initialize() {
        updateTicketList();
    }

    //--------------------------------------------------

    public void updateTicketList() {

        try {
            menuButton.getItems().clear();
            List<MenuItem> ticketList = ticketDAO.getTickets().stream().filter(e -> !e.getStatus().equals(Ticket.Status.FINISHED))
                    .map(e -> new MenuItem("Ticket Number " + e.getTicketID() + " : " + e.getStatus()))
                    .collect(Collectors.toList());

            menuButton.getItems().addAll(ticketList);

            menuButton.getItems().forEach(e -> e.setOnAction(t -> {
                menuButton.setText(e.getText());
                activeTicket = Integer.parseInt(menuButton.getText().substring("Ticket Number".length(), menuButton.getText().indexOf(":")).strip());
                printSelectedTicketFoodItems();
            }));

            activeTicket = menuButton.getItems().size() - 1;
            menuButton.setText(menuButton.getItems().get(activeTicket).getText());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void printSelectedTicketFoodItems() {
        searchTicket(activeTicket);
    }

    private void searchTicket(int id) {
        try {
            List<FoodItem> foodItems = ticketDAO.getTicketByID(activeTicket).getFoodItemList();
            double totalPrice = foodItems.stream().mapToDouble(FoodItem::getPrice).reduce(0, Double::sum);

            StringBuilder stringBuilder = new StringBuilder();
            foodItems.forEach(e -> stringBuilder.append(e).append("\n"));

            printDataArea.clear();
            printDataArea.appendText("Ticket NO : " + id + " " + ticketDAO.getTicketByID(id).getStatus() + "\n");
            printDataArea.appendText("-".repeat(40) + "\n");
            printDataArea.appendText(stringBuilder.toString());
            printDataArea.appendText("-".repeat(40) + "\n");
            printDataArea.appendText(java.lang.String.format("Total To Pay : %.2f  € ", totalPrice));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchItemsByTicketId() {
        try {
            activeTicket = Integer.parseInt(searchByTicketIdField.getText());
            Ticket ticket = ticketDAO.getTicketByID(activeTicket);

            searchTicket(activeTicket);
            menuButton.setText("Ticket Number " + ticket.getTicketID() + " : " + ticket.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteFoodItem() {

        try {
            foodItemDAO.deleteFoodItem(Integer.parseInt(deleteFoodItem.getText()));
            printSelectedTicketFoodItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void createTicket() {

        try {
            ticketDAO.createTicket(new Ticket().setStatus(Ticket.Status.ORDERED));

            printLogger.setText("New Ticket Created with number : "
                    + (Integer.parseInt(menuButton.getItems()
                    .get(menuButton.getItems().size() - 1).getText()
                    .substring("Ticket Number".length(), menuButton.getText().indexOf(":"))
                    .strip()) + 1)
                    + "\nStatus : ORDERED");
            printDataArea.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateTicketList();
    }

    @FXML
    public void addItemToCurrentTicket() {

        if (!(foodToAdd.getText().isEmpty()) || !(priceToAdd.getText().isEmpty())) {

            foodItemDAO.createFoodItem(foodToAdd.getText(), priceToAdd.getText(), activeTicket);
            printLogger.setText(foodToAdd.getText() + " Added on ticket : " + activeTicket);
            printSelectedTicketFoodItems();


        } else {
            printLogger.setText("Empty Field");
        }
    }
}


