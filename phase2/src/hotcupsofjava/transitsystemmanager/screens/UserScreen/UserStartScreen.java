package hotcupsofjava.transitsystemmanager.screens.UserScreen;

import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Card;
import hotcupsofjava.transitsystemmanager.objects.userobjects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class UserStartScreen extends AnchorPane{

    public Button editBtn;
    public Button addCardBtn;
    public Text emailText;
    public Text nameText;
    public TableColumn cardIDCol;
    public TableColumn cardNameCol;
    public TableColumn cardBalanceCol;
    public TableView cardsTable;
    public Button refreshBtn;
    private User user;
    private UserManager userManager;
    private RouteManager routeManager;

    public UserStartScreen(User user, UserManager userManager, RouteManager routeManager){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserStartScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.userManager = userManager;
            this.user = user;

            this.routeManager =routeManager;
            this.nameText.setText(this.user.getName());
            this.emailText.setText(this.user.getEmail());
            initializeTable();
            setCardTable();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initializeTable(){
        cardIDCol.setCellValueFactory(new PropertyValueFactory<Card,String>("id"));
        cardNameCol.setCellValueFactory(new PropertyValueFactory<Card,String>("cardName"));
        cardBalanceCol.setCellValueFactory(new PropertyValueFactory<Card,Double>("balance"));
    }

    public void setCardTable(){
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(user.getCards());
        cardsTable.setItems(cards);
    }

    public void changeName(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Name");
        dialog.setContentText("New Name");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            user.setName(result.get());
            nameText.setText(result.get());
        }
    }

    public void addNewCard(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new Card");
        dialog.setContentText("Enter card details e.g(card number|card name)");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent() && !(result.get().isEmpty())){
        	try {
	            String input = result.get();
	            System.out.println(result.get());
	            String[] splitInput = input.split("\\|");
	            Card newCard = userManager.addCard(user,splitInput[0]);
	            if (splitInput.length > 1) newCard.setCardName(splitInput[1]);
	            setCardTable();
        	} catch (Exception e) {
	        	Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("Warning Dialog");
	            alert.setHeaderText("Incomplete information");
	            alert.setContentText("Sorry, there was a problem creating your card. Please try again.");
	            alert.showAndWait();
	        }
        }
    }

    public void openCard(ActionEvent actionEvent) {
        try {
            Card card = (Card) cardsTable.getSelectionModel().getSelectedItem();
            Stage cardScreen = new Stage();
            CardScreen cardScreenController = new CardScreen(card,userManager,routeManager);
            cardScreen.initModality(Modality.WINDOW_MODAL);
            cardScreen.setTitle("Card Screen");
            cardScreen.setScene(new Scene(cardScreenController));
            cardScreen.show();
        }
        catch (NullPointerException e){
            System.out.println("");
        }
    }

    private void updateScreen(){
        setCardTable();
        cardsTable.refresh();
    }

    public void refreshScreen(ActionEvent actionEvent) {
        updateScreen();
    }
}
