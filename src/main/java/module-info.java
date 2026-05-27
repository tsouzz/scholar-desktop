module org.ifsp.scholardesktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.ifsp.scholardesktop to javafx.fxml;
    exports org.ifsp.scholardesktop;
}