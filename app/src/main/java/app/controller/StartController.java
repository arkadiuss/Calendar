package app.controller;

import app.App;
import app.presenter.LoginViewPresenter;
import app.presenter.WelcomeViewPresenter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
            System.out.println(App.class.getResource("//view"));
            URL url = new File("src/main/java/app/view/WelcomeView.fxml").toURI().toURL();
            System.out.println("LOCATION " + url);
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
            // don't do this in common apps
            e.printStackTrace();
        }

    }

    public void showLoginWindow() {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("src/main/java/app/view/LoginView.fxml").toURI().toURL();
            loader.setLocation(url);
            AnchorPane page = loader.load();

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
