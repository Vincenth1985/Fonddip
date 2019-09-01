package be.infernalwhale.dao;

import be.infernalwhale.model.Ticket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The TicketDAO class will provide CRUD functionality for the
 * Ticket data class.
 *
 */
public class TicketDAO {
    private FoodItemDAO foodItemDAO;

    public Ticket getTicketByID(int id) throws SQLException {
        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("SELECT * FROM ticket WHERE ticketID = ?");
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        Ticket result = null;
        while (rs.next()) {
            if (result == null) result = new Ticket();
            else throw new NonUniqueResultException("Found more than 1 results for ticket-search with id: " + id);

            result.setTicketID(rs.getInt("ticketID"));
            result.setStatus(Ticket.Status.valueOf(rs.getString("status")));
            result.setFoodItems(foodItemDAO.getItemsForTicket(rs.getInt("ticketID")));
        }

        if (result == null) throw new NoResultFoundException("No ticket found with id: " + id);

        return result;
    }

    public List<Ticket> getTickets() throws SQLException {
        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("SELECT * FROM ticket");
        ResultSet rs = statement.executeQuery();

        List<Ticket> result = new ArrayList<>();
        while (rs.next()) {
            Ticket ticket = new Ticket();
            ticket.setTicketID(rs.getInt("ticketID"));
            ticket.setStatus(Ticket.Status.valueOf(rs.getString("status")));
            ticket.setFoodItems(foodItemDAO.getItemsForTicket(rs.getInt("ticketID")));
            result.add(ticket);
        }

        if (result.size() == 0)
            throw new NoResultFoundException("Didn't find any tickets in the database.. how strange");

        return result;
    }

    public Ticket createTicket(Ticket ticket) throws SQLException {
        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("INSERT INTO ticket (status) VALUES (?)");
        statement.setString(1, ticket.getStatus().toString());
        statement.executeUpdate();

        String findNewID = "SELECT MAX(ticketID) AS newID FROM ticket";
        Statement findNewStatement = DBConnector.getConnection().createStatement();
        ResultSet rs = findNewStatement.executeQuery(findNewID);

        /*
         * We won't loop over this resultset. If this returns us less or more than 1 record
         * something is seriously wrong with your server. You could check for it, but we
         * aren't in this application.
         */
        rs.next();
        int newID = rs.getInt("newID");

        return getTicketByID(newID);
    }

    public boolean updateTicket(Ticket ticket) {
        return false;
    }

    /**
     * This method will remove a Ticket record.
     *
     * @param id ID of the ticket that needs removing
     * @return true if the ticket has been removed. False if the ticket could not be found in the database.
     * @throws SQLException
     */
    public boolean deleteTicket(int id) throws SQLException {
        PreparedStatement statement = DBConnector
                .getConnection()
                .prepareStatement("DELETE FROM ticket WHERE ticketID = ?");
        statement.setInt(1, id);
        int count = statement.executeUpdate();

        if (count > 1) throw new NonUniqueResultException("More than 1 ticket was removed from the database");
        return count == 1;
    }
}
