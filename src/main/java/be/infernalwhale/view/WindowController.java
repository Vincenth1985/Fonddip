package be.infernalwhale.view;

import be.infernalwhale.dao.FoodItemDAO;
import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import be.infernalwhale.model.Ticket;

import be.infernalwhale.services.MenuItemGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WindowController {

    private FoodItemDAO foodItemDAO;
    private TicketDAO ticketDAO;


    {
        try {
            foodItemDAO = new FoodItemDAO();
            ticketDAO = new TicketDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                menuButton.setText(e.getText());
                activeTicket = Integer.parseInt(menuButton.getText().substring("Ticket Number".length(), menuButton.getText().indexOf(":")).strip());
                printSelectedTicketFoodItems();
            }));

            if (activeTicket == 0)
                selectLastTicketMade();

            printSelectedTicketFoodItems();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void selectLastTicketMade() {
        MenuItem menuItem = menuButton.getItems().get(menuButton.getItems().size() - 1);
        activeTicket = Integer.parseInt(menuItem.getText().substring("Ticket Number".length(), menuItem.getText().indexOf(":")).strip());
        menuButton.setText(menuItem.getText());
    }

    public void printSelectedTicketFoodItems() {
        searchTicket(activeTicket);
    }

    public void setStatusToTicket() {

        Ticket.Status[] statuses = new Ticket.Status[]{Ticket.Status.FINISHED, Ticket.Status.ORDERED, Ticket.Status.PAYED, Ticket.Status.PROCESSING, Ticket.Status.READY};
        for (Ticket.Status status : statuses) {
            statusMenu.getItems().add(MenuItemGenerator.generationMenuItem(status, e -> {
                ticketDAO.updateTicket(activeTicket, status);
                updateTicketList();
                statusMenu.setText(status.toString());
            }));
        }

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
    public void searchItemsByTicketId() throws InterruptedException {
        try {
            activeTicket = Integer.parseInt(searchByTicketIdField.getText());
            Ticket ticket = ticketDAO.getTicketByID(activeTicket);

            searchTicket(activeTicket);
            menuButton.setText("Ticket Number " + ticket.getTicketID() + " : " + ticket.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException nfe) {
            printLogger.setText("Please introduce a valid number");
        }
    }

    @FXML
    public void deleteFoodItem() {
//        List<Integer> integerlist = new ArrayList<>();
//
//        String[] input = deleteFoodItem.getText().split(",");
//        for (String s : input) {
//            integerlist.add(Integer.parseInt(s));
//        }
//
//        Integer[] integers = new Integer[ input.length ];
//        for (int i = 0; i < integers.length; i++) {
//            integers[ i ] = Integer.parseInt(input[ i ]);
//        }

        try {
            List<Integer> faischierputaindemerde = new ArrayList<>();
            Stream.of(deleteFoodItem.getText().split(",")).map(String::strip).mapToInt(Integer::parseInt).forEach(faischierputaindemerde::add);
            foodItemDAO.deleteFoodItem(faischierputaindemerde);
            printLogger.setText("Food item deleted");
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTicketList();
        selectLastTicketMade();
        printSelectedTicketFoodItems();


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


