package hotcupsofjava.transitsystemmanager.objects.userobjects;

import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;

public class Card extends TransitSystemObject {

    private String cardName; // Name of this card.
    private double balance; // The balance of this card.
    private boolean suspended; // If the card is suspended or no.
    private double totalSpending; // The total spending of this card.
    private double totalFines; // The total fines for this card.

    private User user; // The user of this card.

    private Trip[] trips; // The trips made using this card.

    /**
     * Creates a new card with the id and its user.
     *
     * @param id   The id of this card.
     * @param user The user of this card.
     */
    public Card(String id, User user) {
        super(id);
        balance = 19;
        suspended = false;
        trips = new Trip[3];
        this.user = user;
    }

    /**
     * Creates a new card with the card name, the id and its user.
     *
     * @param cardName The name of this card.
     * @param id       The id of this card.
     * @param user     The user of this card.
     */
    public Card(String cardName, String id, User user) {
        this(id, user);
        this.cardName = cardName;
    }

    /**
     * Sets the name of this card.
     *
     * @param cardName The name of this card.
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Returns the name of this card.
     *
     * @return The name of this card.
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Returns the user of this card.
     *
     * @return The user of this
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the total spending of the card.
     *
     * @param totalSpending The total spending of this card.
     */
    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    /**
     * Sets the balance of this card.
     *
     * @param balance The balance of this card to be set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Returns the trips of this card.
     *
     * @return The trips of this card.
     */
    public Trip[] getTrips() {
        return trips;
    }

    /**
     * Suspends this card by setting suspended variable to be true.
     */
    protected void suspend() {
        suspended = true;
    }

    /**
     * Returns whether this card is suspended.
     *
     * @return Whether this card is suspended.
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Adds balance to this card.
     *
     * @param amount The amount to be added to this card's balance.
     */
    public void addBalance(double amount) {
        balance += amount;
    }

    /* Returns the available balance on the card.
     * @return The balance of the card
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param amount The amount to be charged when this card is used for travelling.
     */
    public void charge(double amount, double tripCap) {
        if (!suspended && getBalance() > 0) {
            trips[0].charge(amount, this, tripCap);
        }
    }

    /**
     * Charge this card.
     *
     * @param amount The amount to be charged.
     */
    public void chargeFine(double amount) {
        balance -= amount;
        totalFines += amount;
    }

    public void newTrip(Trip trip) {
        if (!suspended) {
            Trip previousTrip = getCurrentTrip();
            if (previousTrip != null) {
                previousTrip.endTrip();
            }
            trips[2] = trips[1];
            trips[1] = trips[0];
            trips[0] = trip;
            UserManager.getInstance().addTrip(trip);
        }
    }

    /**
     * Return the current trip.
     *
     * @return trips[0] The current trip this card is used for.
     */
    public Trip getCurrentTrip() {
        return trips[0];
    }

    /**
     * The total spending of this card.
     *
     * @return totalSpending instance of this card.
     */
    public double getTotalSpending() {
        return totalSpending;
    }

    /**
     * The total fines of this card.
     *
     * @return The total fines of this card.
     */
    public double getTotalFines() {
        return totalFines;
    }

    /**
     * The string representation of this card.
     *
     * @return The string of this card.
     */
    public String toString() {
        return "Card " + this.getId() + " owned by user " + user.getName();
    }

}
