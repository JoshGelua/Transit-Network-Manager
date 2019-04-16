package hotcupsofjava.transitsystemmanager.objects.userobjects;


import hotcupsofjava.transitsystemmanager.Logger;
import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;

import java.util.ArrayList;

public class User extends TransitSystemObject {
    private String name; // The name of this user.
    private String email; // The email of this user.
    private ArrayList<Card> cards; // The cards this user owns.

    /**
     * Constructs a user.
     *
     * @param id    The id of this user.
     * @param name  The name of this user.
     * @param email The email of this user.
     */
    public User(String id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
        this.cards = new ArrayList<>();
    }

    /**
     * Returns the cards for this user.
     *
     * @return The cards of this user.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Adds a card for this user.
     *
     * @param card The card to be added.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes the card for this user.
     *
     * @param card The card to be removed.
     */
    public void removeCard(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
            Logger.log("Removed " + card.toString());
        }
    }

    /**
     * Suspends the given card for this user.
     *
     * @param card The card to be suspended.
     * @return Whether the card was suspended.
     */
    public boolean suspendCard(Card card) {
        if (cards.contains(card)) {
            card.suspend();
            Logger.log("Suspended the " + card.toString());
            return true;
        }
        Logger.log("Failed to suspend the " + card.toString());
        return false;
    }

    /**
     * Loads the card with the given amount.
     *
     * @param card  The card which is to be loaded.
     * @param value The value to be added.
     * @return Whether the card was loaded.
     */
    public boolean loadCard(Card card, int value) {
        if (!card.isSuspended()) {
            card.addBalance(value);
            Logger.logLoadingCard("Successfully added $" + value + " to the " + card.toString());
            return true;
        }
        Logger.logLoadingCard("Failed to load $" + value + " to the " + card.toString());
        return false;
    }

    /**
     * Returns the name of this user
     *
     * @return The name of this user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this user.
     *
     * @param newName The name to be set.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the email of this user.
     *
     * @return The email of this user.
     */
    public String getEmail() {
        return email;
    }

}
