# Frietjes Ticketing system

## Database creation instructions

    CREATE TABLE fooditem (
      id int primary key auto_increment,
      name varchar(255) NOT NULL,
      price float NOT NULL,
      ticket int NOT NULL,
      CONSTRAINT C_FOODITEM_TICKET
      FOREIGN KEY FK_FOODITEM_TICKET (ticket)
      REFERENCES ticket(ticketID)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
    );
    
    CREATE TABLE ticket (
      ticketID int primary key AUTO_INCREMENT,
      status VARCHAR(25)
    );

## DBConnector