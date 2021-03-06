package app.view.calendar_list;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import logic.model.Calendar;

import java.util.function.BiConsumer;

public class CalendarListViewCell extends ListCell<Calendar> {
    private BiConsumer<CheckBox, Calendar> setupCheckBoxValue;
    private BiConsumer<Calendar, Button> removeCalendarCallback;
    private BiConsumer<Calendar, Boolean> selectCalendarCallback;

    public CalendarListViewCell(BiConsumer<CheckBox, Calendar> setupCheckBoxValue, BiConsumer<Calendar, Button> removeCalendarCallback,
                                BiConsumer<Calendar, Boolean> selectCalendarCallback) {
        this.setupCheckBoxValue = setupCheckBoxValue;
        this.removeCalendarCallback = removeCalendarCallback;
        this.selectCalendarCallback = selectCalendarCallback;
    }

    @Override
    public void updateItem(Calendar calendar, boolean empty) {
        super.updateItem(calendar, empty);

        if (calendar != null && !empty) {
            HBox root = new HBox(10);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 5, 5, 5));

            root.getChildren().add(new Label(calendar.getName()));

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            root.getChildren().add(region);

            Button removeButton = new Button("Delete");
            removeButton.setOnAction(event -> {
                removeCalendarCallback.accept(calendar, removeButton);

            });
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                selectCalendarCallback.accept(calendar, checkBox.isSelected());
                System.out.println("Calendar in use: " + checkBox.selectedProperty().getValue());
            });
            setupCheckBoxValue.accept(checkBox, calendar);

            root.getChildren().addAll(removeButton, checkBox);
            setText(null);
            setGraphic(root);
        } else {
            setText(null);
            setGraphic(null);
        }
    }

}
