package app.util;

import javafx.scene.control.Alert;

public class AlertPopup {
    public static void showAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
