package be.infernalwhale.dao;

import java.sql.SQLException;

public class NoResultFoundException extends SQLException {
    public NoResultFoundException(String message) {
        super(message);
    }
}
