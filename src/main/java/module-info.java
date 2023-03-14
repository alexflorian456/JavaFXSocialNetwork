module com.example.labgui2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.labgui2 to javafx.fxml;
    opens Domain to javafx.base;
    opens DTO to javafx.base;

    exports com.example.labgui2;
}