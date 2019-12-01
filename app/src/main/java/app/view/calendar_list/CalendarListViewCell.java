package app.view.calendar_list;

import com.google.common.base.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import logic.model.Calendar;

import java.util.function.Consumer;

public class CalendarListViewCell extends ListCell<Calendar> {
    private Consumer<ListCell<Calendar>> removeCalendarCallback;


    public CalendarListViewCell(Consumer<ListCell<Calendar>> removeCalendarCallback) {
        this.removeCalendarCallback = removeCalendarCallback;
    }

    @Override
    public void updateItem(Calendar calendar, boolean empty) {
        super.updateItem(calendar, empty);
        System.out.println("TIME FOR UPDATE" + calendar + empty);

        if (calendar != null) {
            HBox root = new HBox(10);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 5, 5, 5));

            root.getChildren().add(new Label(calendar.getName()));

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            root.getChildren().add(region);

            Button btnAddFriend = new Button("Remove");
            btnAddFriend.setOnAction(event -> {
                System.out.println("Remove " + calendar.getName());
                removeCalendarCallback.accept(this);
            });
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                //todo in next iteration, when events added
                System.out.println("Calendar in use: " + checkBox.selectedProperty().getValue());
            });

            root.getChildren().addAll(btnAddFriend, checkBox);

            setText(null);
            setGraphic(root);
        } else {
            setText(null);
            setGraphic(null);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarListViewCell that = (CalendarListViewCell) o;
        return Objects.equal(removeCalendarCallback, that.removeCalendarCallback);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(removeCalendarCallback);
    }
}
