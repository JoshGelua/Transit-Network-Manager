package hotcupsofjava.transitsystemmanager;

import hotcupsofjava.transitsystemmanager.managers.IDManager;
import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.BusStop;
import hotcupsofjava.transitsystemmanager.objects.userobjects.User;
import hotcupsofjava.transitsystemmanager.screens.AdminScreen.AdminStartScreen;
import hotcupsofjava.transitsystemmanager.screens.TransitSystemScreen.TransitScreen;
import hotcupsofjava.transitsystemmanager.screens.UserScreen.UserLoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainSystem extends Application {
    private static String instanceName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage transitSystemWindow = new Stage();
        TransitScreen transitScreen = new TransitScreen();
        transitSystemWindow.initModality(Modality.WINDOW_MODAL);
        transitSystemWindow.setTitle("Login Screen");
        transitSystemWindow.setScene(new Scene(transitScreen));
        transitSystemWindow.show();
    }

    @Override
    public void stop() {
        if(!(instanceName == null)) {
            try {
                String baseFilePath = String.format("instances/%s/", instanceName);
                // Create directory
                new File(baseFilePath).mkdirs();

                FileOutputStream idFileOut = new FileOutputStream(baseFilePath + "IDManager.ser");
                ObjectOutputStream idObjOut = new ObjectOutputStream(idFileOut);
                idObjOut.writeObject(IDManager.getInstance());
                idObjOut.flush();
                idObjOut.close();

                FileOutputStream routeFileOut = new FileOutputStream(baseFilePath + "RouteManager.ser");
                ObjectOutputStream routeObjOut = new ObjectOutputStream(routeFileOut);
                routeObjOut.writeObject(RouteManager.getInstance());
                routeObjOut.flush();
                routeObjOut.close();

                FileOutputStream userFileOut = new FileOutputStream(baseFilePath + "UserManager.ser");
                ObjectOutputStream userObjOut = new ObjectOutputStream(userFileOut);
                userObjOut.writeObject(UserManager.getInstance());
                userObjOut.flush();
                userObjOut.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void setInstanceName(String instanceName) {
        MainSystem.instanceName = instanceName;
    }
}
