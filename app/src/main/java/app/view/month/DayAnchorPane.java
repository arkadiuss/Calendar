package app.view.month;

import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class DayAnchorPane extends AnchorPane {

    LocalDate date;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
