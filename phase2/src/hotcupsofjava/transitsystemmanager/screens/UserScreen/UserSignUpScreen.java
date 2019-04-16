package hotcupsofjava.transitsystemmanager.screens.UserScreen;

import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserSignUpScreen extends AnchorPane{


    public TextField nameTextField;
    public TextField emailTextField;
    public TextField passwordTextField;
    public Button createUserBtn;
    public Label checkLabel;
    private UserManager userManager;
    private RouteManager routeManager;

    public UserSignUpScreen(UserManager userManager, RouteManager routeManager) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserSignUpScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.userManager = userManager;
            this.routeManager = routeManager;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void createNewUser(ActionEvent actionEvent) {
        boolean checkInput = nameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty();
        if(checkInput){
            checkLabel.setText("Please enter proper data");
            checkLabel.setTextFill(Color.valueOf("Red"));
        }
        else {
            userManager.addUser(passwordTextField.getText(),nameTextField.getText(),emailTextField.getText());
            Stage signUpScreen = (Stage) createUserBtn.getScene().getWindow();
            signUpScreen.close();
            Stage loginWindow = new Stage();
            UserLoginScreen loginController = new UserLoginScreen(userManager,routeManager);
            loginWindow.initModality(Modality.WINDOW_MODAL);
            loginWindow.setTitle("Login Screen");
            loginWindow.setScene(new Scene(loginController));
            loginWindow.show();
        }
    }
}
