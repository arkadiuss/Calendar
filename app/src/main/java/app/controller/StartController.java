package app.controller;

import app.App;
import app.presenter.LoginViewPresenter;
import app.presenter.WelcomeViewPresenter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {

    private Stage primaryStage;

    public StartController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("Welcome");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class
                    .getResource("view/WelcomeView.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();

            // set initial data into controller
            WelcomeViewPresenter controller = loader.getController();
            controller.setAppController(this);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }

    }

    public void showLoginWindow() {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class
                    .getResource("view/LoginView.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the presenter.
            LoginViewPresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            //todo: add some troubleshooting
            e.printStackTrace();
        }
    }
}
