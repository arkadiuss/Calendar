package app.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.User;
import logic.service.UserService;

public class RegisterViewPresenter {
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @FXML
    public TextField emailField;

    private Stage dialogStage;
    private UserService userService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }


    @FXML
    private void handleSignUp(ActionEvent actionEvent) {
        //todo validation messages (eg. email invalid)
        System.out.println("Validate input: " + validateInput());
        if(validateInput()){
            User user = new User(usernameField.getText(), passwordField.getText(), emailField.getText());
            userService.addUser(user);
            dialogStage.close();
        }
    }

    private boolean validateInput() {
        return !usernameField.getText().isEmpty() && isEmailValid(emailField.getText())
                && !passwordField.getText().isEmpty();
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
