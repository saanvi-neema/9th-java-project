module com.memorytiles.memorytilesgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.media;

    opens com.memorytiles.memorytilesgame to javafx.fxml;
    exports com.memorytiles.memorytilesgame;
}