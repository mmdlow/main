package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.knowitall.model.Model;
import seedu.knowitall.model.card.Card;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Card> PREDICATE_MATCHING_NO_CARDS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Card> toDisplay) {
        Optional<Predicate<Card>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredCard(predicate.orElse(PREDICATE_MATCHING_NO_CARDS));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Card... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Card} equals to {@code other}.
     */
    private static Predicate<Card> getPredicateMatching(Card other) {
        return card -> card.equals(other);
    }
}
