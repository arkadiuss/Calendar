package app.controller;

import app.presenter.*;
import app.util.ViewUtils;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.User;

import java.time.LocalDate;

public class StartController {
    private Stage primaryStage;


    public StartController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void showCalendar(User user) {
        this.primaryStage.setTitle("Welcome");
        ViewUtils.LoadedView<HBox, CalendarViewPresenter> loadedView = ViewUtils.loadView("CalendarView.fxml");
        Scene scene = new Scene(loadedView.view);
        this.primaryStage.setTitle("Calendar");

        CalendarViewPresenter controller = loadedView.controller;
        controller.setCurrentUser(user);
        controller.setSelectedDate(LocalDate.now());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initRootLayout() {
        this.primaryStage.setTitle("Welcome");
        ViewUtils.LoadedView<AnchorPane, WelcomeViewPresenter> loadedView = ViewUtils.loadView("WelcomeView.fxml");

        AnchorPane rootLayout = loadedView.view;
        WelcomeViewPresenter controller = loadedView.controller;
        controller.setAppController(this);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void showLoginWindow() {
        ViewUtils.LoadedView<AnchorPane, LoginViewPresenter> loadedView = ViewUtils.loadView("LoginView.fxml");
        Stage dialogStage = getStage(loadedView.view, "Login");
        LoginViewPresenter presenter = loadedView.controller;
        presenter.setDialogStage(dialogStage);
        presenter.setStartController(this);

        dialogStage.showAndWait();

    }

    public void showRegisterWindow() {
        ViewUtils.LoadedView<AnchorPane, RegisterViewPresenter> loadedView = ViewUtils.loadView("RegisterView.fxml");
        Stage dialogStage = getStage(loadedView.view, "Register");
        RegisterViewPresenter presenter = loadedView.controller;
        presenter.setDialogStage(dialogStage);
        dialogStage.showAndWait();

    }

    public void showEventDetailsWindow(Calendar calendar, Event event) {
        ViewUtils.LoadedView<AnchorPane, EventDetailsViewPresenter> loadedView = ViewUtils.loadView("EventDetailsView.fxml");
        Stage dialogStage = getStage(loadedView.view, "Event details");

        EventDetailsViewPresenter presenter = loadedView.controller;
        presenter.setCalendar(calendar);
        presenter.setEvent(event);
        presenter.fillWithEventData();
        presenter.setDialogStage(dialogStage);
        dialogStage.showAndWait();
    }

    private Stage getStage(AnchorPane page, String register) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(register);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }
}
