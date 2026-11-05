module com.example.SprotsAcademy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    requires commons.math3;


    opens com.example.SprotsAcademy to javafx.fxml;
    exports com.example.SprotsAcademy;
    exports com.example.SprotsAcademy.model;
    opens com.example.SprotsAcademy.model to javafx.fxml;
}