package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.knowitall.model.card.Card;

/**
 * Provides a handle for {@code CardListPanel} containing the list of {@code CardThumbnail}.
 */
public class CardListPanelHandle extends NodeHandle<ListView<Card>> {
    public static final String CARD_LIST_VIEW_ID = "#cardListView";

    private static final String CARD_PANE_ID = "#cardThumbnailPane";

    private Optional<Card> lastRememberedSelectedCardCard;

    public CardListPanelHandle(ListView<Card> cardListPanelNode) {
        super(cardListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code CardThumbnailHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public CardThumbnailHandle getHandleToSelectedCard() {
        List<Card> selectedCardList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardList.size() != 1) {
            throw new AssertionError("Card list size expected 1.");
        }

        return getAllCardNodes().stream()
                .map(CardThumbnailHandle::new)
                .filter(handle -> handle.equals(selectedCardList.get(0)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<Card> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code card}.
     */
    public void navigateToCard(Card card) {
        if (!getRootNode().getItems().contains(card)) {
            throw new IllegalArgumentException("Card does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(card);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= getRootNode().getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(index);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Selects the {@code CardThumbnail} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the card handle of a card associated with the {@code index} in the list.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public CardThumbnailHandle getCardCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(CardThumbnailHandle::new)
                .filter(handle -> handle.equals(getCard(index)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Card getCard(int index) {
        return getRootNode().getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    /**
     * Remembers the selected {@code CardThumbnail} in the list.
     */
    public void rememberSelectedCardCard() {
        List<Card> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedCardCard = Optional.empty();
        } else {
            lastRememberedSelectedCardCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code CardThumbnail} is different from the value remembered by the most recent
     * {@code rememberSelectedCardCard()} call.
     */
    public boolean isSelectedCardCardChanged() {
        List<Card> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedCardCard.isPresent();
        } else {
            return !lastRememberedSelectedCardCard.isPresent()
                    || !lastRememberedSelectedCardCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
