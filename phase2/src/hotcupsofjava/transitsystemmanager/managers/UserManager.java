package hotcupsofjava.transitsystemmanager.managers;

import hotcupsofjava.transitsystemmanager.Logger;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Card;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Trip;
import hotcupsofjava.transitsystemmanager.objects.userobjects.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager implements Serializable {

    private static UserManager instance; // The instance of the user manager.
    private HashMap<String, User> users; // The users in the system.
    private HashMap<String, Card> cards; // The cards in the system.
    private TapManager tapManager; // The tap manager.
    private ArrayList<Trip> trips; // The overall trips made during the simulation.
    /**
     * Returns the user manager.
     *
     * @return The user manager.
     */
    public static UserManager getInstance() {
        return instance;
    }

    /**
     * Sets the user manager.
     *
     * @param m The user manager to be set.
     */
    public static void setInstance(UserManager m) {
        instance = m;
    }

    /**
     * Constructs a user manager.
     *
     * @param busCost    The charge for each bus ride.
     * @param subwayCost The charge used for a subway ride.
     */
    public UserManager(double busCost, double subwayCost) {
        users = new HashMap<>();
        cards = new HashMap<>();
        UserManager.setInstance(this);
        tapManager = new TapManager();
        trips = new ArrayList<>();
        tapManager.updateBusCost(busCost);
        tapManager.updateSubwayCost(subwayCost);

    }

    /**
     * Adds the trip to the system.
     *
     * @param trip The trip to be added.
     */
    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    /**
     * Returns the overall revenue collected at that day.
     *
     * @return The revenue collected.
     */
    public double calculateRevenue() {
        double revenue = 0;
        for (Card card : cards.values()) {
            revenue += card.getTotalSpending();
        }
        Logger.logRevenue(revenue, calculateTrueRevenue());
        return revenue;
    }

    /**
     * Calculates the total fines made that day.
     *
     * @return The total fines.
     */
    public double calculateFines() {
        int fines = 0;
        for (Card card : cards.values()) {
            fines += card.getTotalFines();
        }
        return fines;
    }

    /**
     * Returns the total distance travelled that day.
     *
     * @return The total distance travelled.
     */
    public int calculateDistanceTravelled() {
        int distance = 0;
        for (Trip trip : trips) {
            distance += trip.getDistanceTravelled();
        }
        return distance;
    }

    /**
     * The actual revenue which would have been collected.
     *
     * @return The actual revenue.
     */
    public double calculateTrueRevenue() {
        double value = 0;
        for (Trip trip : trips) {
            value += trip.getTrueValue();
        }
        return value;
    }

    /**
     * Add a user by creating a new one.
     *
     * @param id    The id of the user.
     * @param name  The name of the user.
     * @param email The email of the user.
     */
    public void addUser(String id, String name, String email) {
        User user = new User(id, name, email);
        users.put(user.getId(), user);
    }

    /**
     * Remove a card from the system.
     *
     * @param card The card to be removed.
     * @param user The user who owns that card.
     */
    public void removeCard(Card card, User user) {
        cards.remove(card.getId());
        user.removeCard(card);
    }

    /**
     * Add a card for a given user and card id.
     *
     * @param user   The user who is adding the new card.
     * @param cardId The id of the card to be added.
     */
    public Card addCard(User user, String cardId) {
        Card card = new Card(cardId, user);
        user.addCard(card);
        cards.put(card.getId(), card);
        return card;
    }

    /**
     * Return the user with the given id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    public User getUser(String id) {
        return users.get(id);
    }

    /**
     * Return the card with the given id
     *
     * @param id The id of the card.
     * @return The card to be returned.
     */
    public Card getCard(String id) {
        return cards.get(id);
    }

    /**
     * Checks if the system has such a user.
     *
     * @param id The id of the user
     * @return Return whether the system has such a user.
     */
    public boolean hasUser(String id) {
        return users.containsKey(id);
    }

    /**
     * Returns whether the system has a card.
     *
     * @param id The id of the card.
     * @return Whether the system has the card.
     */
    public boolean hasCard(String id) {
        return cards.containsKey(id);
    }

    /**
     * Return the tapManager
     *
     * @return tapManager.
     */
    public TapManager getTapManager() {
        return tapManager;
    }

    /**
     * Return the hash map of the users.
     *
     * @return users.
     */
    public HashMap<String, User> getUsers() {
        return users;
    }
}
