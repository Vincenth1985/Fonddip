package be.infernalwhale.dao;

import java.sql.SQLException;

public class NonUniqueResultException extends SQLException {
    public NonUniqueResultException(String message) {
        super(message);
    }
}
