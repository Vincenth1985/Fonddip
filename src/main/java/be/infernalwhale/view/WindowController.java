package be.infernalwhale.view;

import be.infernalwhale.dao.DBConnector;
import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    @FXML
    private MenuButton statusMenu;


    //--------------------------------------------------
    public void initialize() {
        updateTicketList();
        setStatusToTicket();

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
                //Pour chaque élément de la liste d'items ????
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

    public void setStatusToTicket()  {

        statusMenu.getItems().add(new MenuItem(Ticket.Status.FINISHED.toString()));
        statusMenu.getItems().add(new MenuItem(Ticket.Status.ORDERED.toString()));
        statusMenu.getItems().add(new MenuItem(Ticket.Status.PROCESSING.toString()));
        statusMenu.getItems().add(new MenuItem(Ticket.Status.PAYED.toString()));
        statusMenu.getItems().add(new MenuItem(Ticket.Status.READY.toString()));

        statusMenu.getItems().get(0).setOnAction(t -> {
            ticketDAO.updateTicket(activeTicket, Ticket.Status.FINISHED);
            updateTicketList();
        });
        statusMenu.getItems().get(1).setOnAction(t -> {
            ticketDAO.updateTicket(activeTicket, Ticket.Status.ORDERED);
            updateTicketList();
        });
        statusMenu.getItems().get(2).setOnAction(t -> {
            ticketDAO.updateTicket(activeTicket, Ticket.Status.PROCESSING);
            updateTicketList();
        });
        statusMenu.getItems().get(3).setOnAction(t -> {
            ticketDAO.updateTicket(activeTicket, Ticket.Status.PAYED);
            updateTicketList();
        });
        statusMenu.getItems().get(4).setOnAction(t -> {
            ticketDAO.updateTicket(activeTicket, Ticket.Status.READY);
            updateTicketList();
        });

        statusMenu.setText(statusMenu.getItems().get(1).getText());


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


