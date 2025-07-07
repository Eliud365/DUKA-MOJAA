module com.drinkssystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires java.sql;

    opens com.drinkssystem to javafx.fxml;
    exports com.drinkssystem;
}