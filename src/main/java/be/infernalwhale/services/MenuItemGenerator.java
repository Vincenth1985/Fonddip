package be.infernalwhale.services;

import be.infernalwhale.model.Ticket;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class MenuItemGenerator {


    public static MenuItem generationMenuItem(Ticket.Status status, EventHandler<ActionEvent> setOnAction) {
        MenuItem mi = new MenuItem(status.toString());
        mi.setOnAction(setOnAction);
        return mi;
    }

}
