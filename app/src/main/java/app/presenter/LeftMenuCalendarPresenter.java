package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.util.AlertPopup;
import app.view.calendar_list.CalendarListViewCell;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import logic.model.Calendar;
import logic.model.User;
import logic.service.CalendarService;

public class LeftMenuCalendarPresenter {

    private final AppContext appContext;
    @FXML
    private ListView<Calendar> calendarsList;
    @FXML
    private TextField newCalendarName;
    @FXML
    private Button addCalendarButton;

    private User currentUser;
    private CalendarService calendarService;
    private ObservableList<Calendar> observableCalendars = FXCollections.observableArrayList();

    public LeftMenuCalendarPresenter() {
        this.appContext = DIProvider.getAppContext();
        this.calendarService = DIProvider.getCalendarService();
    }

    @FXML
    public void initialize() {
        calendarsList.setItems(observableCalendars);

        appContext.observeUser()
                .doOnNext(user -> this.currentUser = user)
                .flatMap(calendarService::getCalendars)
                .observeOn(JavaFxScheduler.platform())
                .subscribe((calendars) -> {
                    observableCalendars.clear();
                    observableCalendars.addAll(calendars);
                });

        this.calendarsList.setCellFactory(listView ->
                new CalendarListViewCell(
                        (checkbox, calendar) -> {
                            if (appContext.getSelectedCalendars().contains(calendar.getId())) {
                                checkbox.selectedProperty().setValue(true);
                            }
                        },
                        (calendar, deleteButton) -> {
                            deleteButton.setDisable(true);
                            deleteButton.setText("Deleting...");
                            currentUser.getCalendars().remove(calendar);
                            calendarService.deleteCalendar(calendar)
                                    .observeOn(JavaFxScheduler.platform())
                                    .subscribe(() -> {
                                        // ok
                                    }, error -> {
                                        deleteButton.setDisable(false);
                                        deleteButton.setText("Remove");
                                        System.out.println(error.toString());
                                    });
                        },
                        (calendar, isSelected) -> {
                            if (isSelected)
                                appContext.selectCalendar(calendar.getId());
                            else
                                appContext.unselectCalendar(calendar.getId());
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
                .observeOn(JavaFxScheduler.platform())
                .subscribe(() -> {
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                }, err -> {
                    AlertPopup.showAlert("Error occurred while adding calendar");
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                });
    }
}
