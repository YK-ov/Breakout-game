module pl.umcs.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens pl.umcs.oop to javafx.fxml;

    exports pl.umcs.oop;
}