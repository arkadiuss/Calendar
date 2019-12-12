package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.util.AlertPopup;
import app.view.calendar_list.CalendarListViewCell;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import logic.model.Calendar;
import logic.model.User;
import logic.service.CalendarService;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LeftMenuCalendarPresenter {

    private final AppContext appContext;
    @FXML
    private ListView calendarsList;
    @FXML
    private TextField newCalendarName;
    @FXML
    private Button addCalendarButton;

    private Consumer<Calendar> addedConsumer;
    private BiConsumer<Calendar, Boolean> selectedConsumer;
    private Consumer<Calendar> removeConsumer;
    private User currentUser;
    private CalendarService calendarService = new CalendarService();

    public void setOnNewCalendarAdded(Consumer<Calendar> updateConsumer) {
        this.addedConsumer = updateConsumer;
    }

    public void setOnSelectCalendar(BiConsumer<Calendar, Boolean> updateConsumer) {
        this.selectedConsumer = updateConsumer;
    }

    public void setOnRemoveCalendar(Consumer<Calendar> removeConsumer) {
        this.removeConsumer = removeConsumer;
    }

    public LeftMenuCalendarPresenter() {
        this.appContext = DIProvider.getAppContaxt();
        appContext.observeUser().subscribe((user) -> {
            this.currentUser = user;
            user.getCalendars().forEach((calendar -> this.calendarsList.getItems().add(calendar)));
        });

    }

    @FXML
    public void initialize() {
        this.calendarsList.setCellFactory(listView ->
                new CalendarListViewCell((calendar, deleteButton) -> {
                    deleteButton.setDisable(true);
                    deleteButton.setText("Deleting...");
                    currentUser.getCalendars().remove(calendar);
                    calendarService.deleteCalendar(calendar)
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(() -> {
                                this.calendarsList.getItems().remove(calendar);
                                removeConsumer.accept(calendar);
                            }, error -> {
                                deleteButton.setDisable(false);
                                deleteButton.setText("Remove");
                                System.out.println(error.toString());
                            });
                }, (calendar, isSelected) -> {
                    if (isSelected)
                        selectedConsumer.accept(calendar, true);
                    else
                        selectedConsumer.accept(calendar, false);
                }));
    }

    public void handleAddNewCalendar(ActionEvent actionEvent) {
        if (Strings.isNullOrEmpty(newCalendarName.getText())) {
            AlertPopup.showAlert("Calendar name cannot be empty");
            return;
        }
        Calendar calendar = new Calendar(newCalendarName.getText(), currentUser);
        addCalendarButton.setText("Adding...");
        addCalendarButton.setDisable(true);
        calendarService.addCalendar(calendar)
                .andThen(calendarService.getCalendars())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(calendars -> {
                    calendarsList.getItems().add(calendar);

                    addedConsumer.accept(calendar);
                    //calendarsCombobox.getItems().add(calendar);

                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                }, error -> {
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                });
    }
}
