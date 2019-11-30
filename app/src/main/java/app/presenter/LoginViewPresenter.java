package app.presenter;

import com.google.common.base.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.User;
import logic.service.UserService;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

public class LoginViewPresenter {

    @FXML
    public TextField userNameField;
    @FXML
    public TextField passwordField;
    public Button signInButton;
    public Button cancelButton;
    private Stage dialogStage;
    private UserService userService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private boolean isInputValid() {
        return !Strings.isNullOrEmpty(userNameField.getText())
                && !Strings.isNullOrEmpty(passwordField.getText());
    }


    public void handleSignIn(ActionEvent actionEvent) {
        System.out.println("HANDLING!!!");
        if (isInputValid()) {
            Optional<User> optionalUser = userService.getUser(userNameField.getText(), passwordField.getText());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                System.out.println(user.getUsername());
                dialogStage.close();
                //todo: start calendar and close all windows
            } else {
                //todo:
                System.out.println("Couldn't find user");
            }
        } else {
            //todo: maybe popup?
            System.out.println("Username and password cannot be empty");

        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
