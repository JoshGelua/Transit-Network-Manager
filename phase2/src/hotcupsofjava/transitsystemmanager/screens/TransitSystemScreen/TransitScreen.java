package hotcupsofjava.transitsystemmanager.screens.TransitSystemScreen;

import hotcupsofjava.transitsystemmanager.MainSystem;
import hotcupsofjava.transitsystemmanager.managers.IDManager;
import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;
import hotcupsofjava.transitsystemmanager.screens.AdminScreen.AdminStartScreen;
import hotcupsofjava.transitsystemmanager.screens.UserScreen.UserLoginScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;


public class TransitScreen extends AnchorPane {

    public Button createSystemBtn;
    public Button loadSystemBtn;

    public TransitScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TransitSystem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("");
        }
    }

    public void createSystem(ActionEvent actionEvent) {

        Stage createWindow = new Stage();
        CreateNewSystemScreen createNewSystemScreen = new CreateNewSystemScreen();
        createWindow.initModality(Modality.WINDOW_MODAL);
        createWindow.setTitle("Login Screen");
        createWindow.setScene(new Scene(createNewSystemScreen));
        createWindow.show();
        Stage stage = (Stage) createSystemBtn.getScene().getWindow();
        stage.close();
    }

    public void loadSystem(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load System");
        dialog.setContentText("Enter the system name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            IDManager idManager;
            UserManager userManager;
            RouteManager routeManager;

            // attempt to load existing system
            try {
                String baseFilePath = String.format("instances/%s/", result.get());
                FileInputStream idFileIn = new FileInputStream(baseFilePath + "IDManager.ser");
                ObjectInputStream idObjIn = new ObjectInputStream(idFileIn);
                idManager = (IDManager) idObjIn.readObject();
                TransitSystemObject.setIdManager(idManager);
                IDManager.setInstance(idManager);
                idObjIn.close();

                FileInputStream routeFileIn = new FileInputStream(baseFilePath + "RouteManager.ser");
                ObjectInputStream routeObjIn = new ObjectInputStream(routeFileIn);
                routeManager = (RouteManager) routeObjIn.readObject();
                RouteManager.setInstance(routeManager);
                routeObjIn.close();

                FileInputStream userFileIn = new FileInputStream(baseFilePath + "UserManager.ser");
                ObjectInputStream userObjIn = new ObjectInputStream(userFileIn);
                userManager = (UserManager) userObjIn.readObject();
                UserManager.setInstance(userManager);
                userObjIn.close();
                openInitialScreens(userManager, routeManager);
                MainSystem.setInstanceName(result.get());
                Stage stage = (Stage) loadSystemBtn.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("No System");
                alert.setContentText("Warning! No such system found");
                alert.showAndWait();
            }
        }
    }

    private void openInitialScreens(UserManager userManager, RouteManager routeManager) {

        Stage loginWindow = new Stage();
        UserLoginScreen loginController = new UserLoginScreen(userManager, routeManager);
        loginWindow.initModality(Modality.WINDOW_MODAL);
        loginWindow.setTitle("Login Screen");
        loginWindow.setScene(new Scene(loginController));
        loginWindow.show();

        Stage adminWindow = new Stage();
        AdminStartScreen adminController = new AdminStartScreen(userManager, routeManager);
        adminWindow.initModality(Modality.WINDOW_MODAL);
        adminWindow.setTitle("Admin Screen");
        adminWindow.setScene(new Scene(adminController));
        adminWindow.show();

    }
}
