module org.ifsp.scholardesktop {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // JDBC
    requires java.sql;

    // MySQL Connector
    requires mysql.connector.j;

    // dotenv
    requires io.github.cdimascio.dotenv.java;

    // hash
    requires jbcrypt;

    // Opens para o JavaFX injetar nos controllers via FXML
    opens org.ifsp.scholardesktop to javafx.fxml;
    opens org.ifsp.scholardesktop.controller to javafx.fxml;

    // Exports
    exports org.ifsp.scholardesktop;
    exports org.ifsp.scholardesktop.model;
    exports org.ifsp.scholardesktop.controller;
    exports org.ifsp.scholardesktop.service;
    exports org.ifsp.scholardesktop.dao;
    exports org.ifsp.scholardesktop.exception;
}