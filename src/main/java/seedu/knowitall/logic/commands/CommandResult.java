package seedu.knowitall.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.knowitall.model.card.Card;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /**
     * {@code Type } representing the type of CommandResult and what response should be displayed.
     */
    public enum Type {
        SHOW_HELP, // Help information should be shown to the user.
        IS_EXIT, // The application should exit.
        ENTERED_FOLDER, // The side panel should be updated as folder was entered.
        EXITED_FOLDER, // The side panel should be updated as folder was exited.
        EDITED_FOLDER, // The side panel should be updated as a folder was edited.
        START_TEST_SESSION, // The application should enter a test session.
        END_TEST_SESSION, // The current test session should end.
        ANSWER_CORRECT,
        ANSWER_WRONG,
        ANSWER_REVEAL,
        ENTERED_REPORT,
        EXITED_REPORT,
        SHOW_NEXT_CARD, // The next card will be displayed in the current test session.
        NONE // use for "nothing to do"
    }

    private Type type;

    private Card testSessionCard;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, Type type) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.type = type;
    }

    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, Type.NONE);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public void setTestSessionCard(Card card) {
        testSessionCard = card;
    }

    public Card getTestSessionCard() {
        return testSessionCard;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && type == otherCommandResult.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, type);
    }

}
