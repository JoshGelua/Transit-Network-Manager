package user;

import main.IDManager;

import java.util.HashMap;

public class UserManager {
    private IDManager idManager;
    private HashMap<String, User> users;
    private HashMap<String, Card> cards;

    public UserManager(IDManager idManager) {
        this.idManager = idManager;
        users = new HashMap<>();
        cards = new HashMap<>();
    }

    public double calculateRevenue() {
        int revenue = 0;
        for (Card card : cards.values()) {
            revenue += card.getTotalSpending();
        }
        return revenue;
    }

    public double calculateFines() {
        int fines = 0;
        for (Card card : cards.values()) {
            fines += card.getTotalFines();
        }
        return fines;
    }

    public void addUser(String id, String name, String email) {
        idManager.addId(id);
        User user = new User(id, name, email);
        users.put(id, user);
    }

    public void addCard(User user, String cardId) {
        idManager.addId(cardId);
        Card card = new Card(cardId, user);
        user.addCard(card);
        cards.put(cardId, card);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Card getCard(String id) {
        return cards.get(id);
    }

    public boolean hasUser(String id) {
        return users.containsKey(id);
    }

    public boolean hasCard(String id) {
        return cards.containsKey(id);
    }
}
