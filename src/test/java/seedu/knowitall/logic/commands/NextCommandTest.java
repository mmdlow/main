package seedu.knowitall.logic.commands;

import static seedu.knowitall.commons.core.Messages.MESSAGE_INVALID_COMMAND_OUTSIDE_FULLSCREEN;
import static seedu.knowitall.commons.core.Messages.MESSAGE_INVALID_NEXT_COMMAND;
import static seedu.knowitall.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.knowitall.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.knowitall.logic.commands.EndCommand.MESSAGE_END_TEST_SESSION_SUCCESS;
import static seedu.knowitall.logic.commands.NextCommand.MESSAGE_NEXT_QUESTION_SUCCESS;
import static seedu.knowitall.testutil.TypicalCards.CARD_1;
import static seedu.knowitall.testutil.TypicalCards.getTypicalFolderOneAsList;

import org.junit.Before;
import org.junit.Test;

import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.ModelManager;
import seedu.knowitall.model.UserPrefs;
import seedu.knowitall.testutil.CardFolderBuilder;
import seedu.knowitall.testutil.TypicalIndexes;

/**
 * Contains integration tests (interaction with the Model) and junit tests for {@code NextCommand}.
 */
public class NextCommandTest {
    private Model model = new ModelManager(getTypicalFolderOneAsList(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalFolderOneAsList(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.enterFolder(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        expectedModel.enterFolder(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
    }

    @Test
    public void execute_nextCommand_success() {
        model.enterFolder(TypicalIndexes.INDEX_FIRST_CARD_FOLDER.getZeroBased());
        model.startTestSession();
        expectedModel.startTestSession();

        expectedModel.setCardAsAnswered();
        model.setCardAsAnswered();
        expectedModel.testNextCard();

        CommandResult expectedCommandResult = new CommandResult(MESSAGE_NEXT_QUESTION_SUCCESS,
                CommandResult.Type.SHOW_NEXT_CARD);
        assertCommandSuccess(new NextCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_nextCommandOnLastCard_endCommandSuccess() {
        CardFolder cardFolderWithOneCard = new CardFolderBuilder().withCard(CARD_1).build();
        model.addFolder(cardFolderWithOneCard);
        expectedModel.addFolder(cardFolderWithOneCard);
        model.enterFolder(model.getCardFolders().size() - 1);
        expectedModel.enterFolder(model.getCardFolders().size() - 1);

        model.startTestSession();
        expectedModel.startTestSession();
        model.setCardAsAnswered();
        expectedModel.setCardAsAnswered();

        boolean successfullyFoundNextCard = expectedModel.testNextCard();
        assert !successfullyFoundNextCard;
        expectedModel.endTestSession();

        CommandResult expectedCommandResult = new CommandResult(MESSAGE_END_TEST_SESSION_SUCCESS,
                CommandResult.Type.END_TEST_SESSION);
        assertCommandSuccess(new NextCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidNextCommandOutsideTestSession_fail() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_OUTSIDE_FULLSCREEN);
        assertCommandFailure(new NextCommand(), model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_invalidNextCommandBeforeAnswerAttempt_fail() {
        model.startTestSession();
        String expectedMessage = String.format(MESSAGE_INVALID_NEXT_COMMAND);
        assertCommandFailure(new NextCommand(), model, commandHistory, expectedMessage);
    }
}
