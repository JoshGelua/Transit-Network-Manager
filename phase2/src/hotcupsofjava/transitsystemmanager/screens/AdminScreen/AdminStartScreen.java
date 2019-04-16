package hotcupsofjava.transitsystemmanager.screens.AdminScreen;

import hotcupsofjava.transitsystemmanager.Logger;
import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Stop;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Card;
import hotcupsofjava.transitsystemmanager.objects.userobjects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.io.File;

public class AdminStartScreen extends VBox{

    public Button stopsVisitedBtn;
    public Button revenueCollectedBtn;
    public Button finesBtn;
    public Button trueValueBtn;
    public Button statsBtn;
    public Text stopsText;
    public Text revenueText;
    public Text finesText;
    public Text trueValueText;
    public Label statsLabel;
    public TableView tab4StopTable;
    public TableColumn stopNameCol;
    public TableView userTable;
    public Button userStatsBtn;
    public TableColumn userNameCol;
    public Label userStatsLabel;
    public Button changeBusCostBtn;
    public Button changeSubwayCostBtn;
    public Button changeTripCapBtn;
    private UserManager userManager;
    private RouteManager routeManager;
    public Button startDayBtn;
    public Button endDayBtn;
    
    public Button genLogBtn;
    public Button refreshBtn;
    public Button readLogBtn;
    public ComboBox<String> logCbx;
    public TextArea logArea;
    
    private String selectedLog;

    public AdminStartScreen(UserManager userManager, RouteManager routeManager){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminStartScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.routeManager = routeManager;
            this.userManager = userManager;
        } catch (IOException e) {
            e.printStackTrace();
        }
        logCbx.setEditable(false);
        logArea.setEditable(false);
        updateTransitInformation();
        initializeTables();
        setStopsTable();
        setUsersTable();
    }


    private void initializeTables(){
        stopNameCol.setCellValueFactory(new PropertyValueFactory<Stop,String>("name"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    }

    private void setStopsTable(){
        ObservableList<Stop> stops = FXCollections.observableArrayList();
        stops.addAll(routeManager.getStations().values());
        stops.addAll(routeManager.getStops().values());
        tab4StopTable.setItems(stops);
    }

    private void setUsersTable(){
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(userManager.getUsers().values());
        userTable.setItems(users);
    }
    public void startDay(javafx.event.ActionEvent actionEvent) {
        if(!Logger.isActive()) {
            Logger.startDay();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("System On");
            alert.setContentText("Warning! The system is already active");
            alert.showAndWait();
        }
    }

    public void endDay(javafx.event.ActionEvent actionEvent) {
        if(Logger.isActive()) {
            Logger.endDay();
            updateTransitInformation();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("System Off");
            alert.setContentText("Warning! The system is already not active");
            alert.showAndWait();
        }
    }
    
    public void generateLog() {
      Logger.generateLog();
    }
    
    public void refreshLogs() {
      File logFolder = new File("logs/");
      File[] contents = logFolder.listFiles();
      
      ObservableList<String> fileNames = FXCollections.observableArrayList();
      if (contents != null) {
	      for (File f : contents) {
	        fileNames.add(f.getName());
	      }
      }
      logCbx.setItems(fileNames);
    }
    
    public void readLog() {
      File selectedFile = new File("logs/"+logCbx.getValue());
      StringBuilder output = new StringBuilder();
      try {
        Scanner scanner = new Scanner(selectedFile);
        while (scanner.hasNextLine()) {
          output.append(scanner.nextLine() + "\n");
        }
        scanner.close();
      } catch (Exception e) {
        System.out.println("Error reading logfile.");
      }
      logArea.setText(output.toString());
    }

    public void updateTransitInformation(){
        revenueText.setText(String.valueOf(userManager.calculateRevenue()));
        finesText.setText(String.valueOf(userManager.calculateFines()));
        stopsText.setText(String.valueOf(userManager.calculateDistanceTravelled()));
        trueValueText.setText(String.valueOf(userManager.calculateTrueRevenue()));
    }

    public void generateStopStats(ActionEvent actionEvent) {
        try {
            Stop stop = (Stop) tab4StopTable.getSelectionModel().getSelectedItem();
            setStopStats(stop);
        }
        catch (NullPointerException e){
            System.out.println("");
        }
    }

    private void setStopStats(Stop stop){
        String details = stop.getName() + "'s details:" + "\n";
        details = details+ "Total taps: " + stop.getTaps() + "\n";
        details = details + "Total revenue:" + stop.getRevenueAtStop() + "\n";
        details = details + "Total fine Value: " + stop.getFineValue() + "\n";
        statsLabel.setText(details);
    }

    private void setUserStats(User user){
        String details = user.getName() + "'s details:" + "\n";
        int spending = 0;
        int fines = 0;
        for(Card card: user.getCards()){
            spending += card.getTotalSpending();
            fines += card.getTotalFines();
        }
        details = details + "Total spending: " + spending + "\n";
        details = details + "Total fines: " + fines;
        userStatsLabel.setText(details);


    }

    public void refreshTab2Info(ActionEvent actionEvent) {
        updateTransitInformation();
    }

    public void userStatistics(ActionEvent actionEvent) {
        try {
            User user = (User) userTable.getSelectionModel().getSelectedItem();
            setUserStats(user);
        }
        catch (NullPointerException e){
            System.out.println("");
        }
    }

    public void changeBusCost(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Bus Ride Cost");
        dialog.setContentText("Enter the new amount to be set");
        Optional<String> result = dialog.showAndWait();
        try{
            userManager.getTapManager().updateBusCost(Double.parseDouble(result.get()));
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Improper Value");
            alert.setContentText("Warning! Please enter a proper value");
            alert.showAndWait();
        }
    }

    public void changeSubwayCost(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Subway Ride Cost");
        dialog.setContentText("Enter the new amount to be set");
        Optional<String> result = dialog.showAndWait();
        try {
            userManager.getTapManager().updateSubwayCost(Double.parseDouble(result.get()));
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Improper Value");
            alert.setContentText("Warning! Please enter a proper value");
            alert.showAndWait();
        }
    }
    public void changeTripCap(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Trip Cap Cost");
        dialog.setContentText("Enter the new amount to be set");
        Optional<String> result = dialog.showAndWait();
        try{
            userManager.getTapManager().updateTripCap(Double.parseDouble(result.get()));
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Improper Value");
            alert.setContentText("Warning! Please enter a proper value");
            alert.showAndWait();
        }
    }
}
