package app.controller;

import app.presenter.CalendarViewPresenter;
import app.presenter.LoginViewPresenter;
import app.presenter.RegisterViewPresenter;
import app.presenter.WelcomeViewPresenter;
import app.view.calendar_list.CalendarListViewCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.model.Calendar;
import logic.model.User;
import logic.service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class StartController {

    private final UserService userService;
    private Stage primaryStage;

    public StartController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
    }


    public void showCalendar(User user) {
        this.primaryStage.setTitle("Welcome");
        // load layout from FXML file
        FXMLLoader loader = new FXMLLoader();
        try {
            URL url = new File("src/main/java/app/view/CalendarView.fxml").toURI().toURL();
            loader.setLocation(url);
            HBox root = loader.load();

            Scene scene = new Scene(root);
            this.primaryStage.setTitle("Calendar");

            CalendarViewPresenter controller = loader.getController();
            controller.setSelectedDate(LocalDate.now());
            controller.setCurrentUser(user);
            Calendar calendar = new Calendar(user.getUsername(), user);
            controller.calendarsList.getItems().add(calendar);

            controller.calendarsList.setCellFactory(listView -> new CalendarListViewCell((lc) -> {
                controller.calendarsList.getItems().remove(lc);
            }));

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("Welcome");
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("src/main/java/app/view/WelcomeView.fxml").toURI().toURL();
            loader.setLocation(url);
            AnchorPane rootLayout = loader.load();

            // set initial data into controller
            WelcomeViewPresenter controller = loader.getController();
            controller.setAppController(this);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginWindow() {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("src/main/java/app/view/LoginView.fxml").toURI().toURL();
            Stage dialogStage = getStage(loader, url, "Login");

            // Set the person into the presenter.
            LoginViewPresenter presenter = loader.getController();
            presenter.setUserService(userService);
            presenter.setDialogStage(dialogStage);
            presenter.setStartController(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            //todo: add some troubleshooting
            e.printStackTrace();
        }
    }

    public void showRegisterWindow() {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("src/main/java/app/view/RegisterView.fxml").toURI().toURL();
            Stage dialogStage = getStage(loader, url, "Register");

            // Set the person into the presenter.
            RegisterViewPresenter presenter = loader.getController();
            presenter.setUserService(userService);
            presenter.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            //todo: add some troubleshooting
            e.printStackTrace();
        }
    }

    private Stage getStage(FXMLLoader loader, URL url, String register) throws IOException {
        loader.setLocation(url);
        AnchorPane page = loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle(register);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }
}
