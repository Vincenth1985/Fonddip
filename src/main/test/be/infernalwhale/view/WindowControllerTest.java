package be.infernalwhale.view;

import be.infernalwhale.dao.TicketDAO;
import be.infernalwhale.model.FoodItem;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class WindowControllerTest {

    @Test
    public void initialize() {
    }

    @Test
    public void updateTicketList() {

    }

    @Test
    public void printSelectedTicketFoodItems() {
    }

    @Test
    public void searchItemsByTicketId() {
        int testID = 4;

        try {
            List<FoodItem> foodItems = new TicketDAO().getTicketByID(testID).getFoodItemList();
            double totalPrice = foodItems.stream().mapToDouble(FoodItem::getPrice).reduce(0, Double::sum);

            StringBuilder stringBuilder = new StringBuilder();
            foodItems.forEach(e -> stringBuilder.append(e).append("\n"));

            assertEquals(2, totalPrice, 0);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFoodItem() {
    }

    @Test
    public void createTicket() {
    }

    @Test
    public void addItemToCurrentTicket() {
    }
}