package seedu.knowitall.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.knowitall.commons.core.Messages;
import seedu.knowitall.commons.core.index.Index;
import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.Model.State;
import seedu.knowitall.model.ReadOnlyCardFolder;

/**
 * Selects a folder identified using it's displayed index in the home directory. Also used to navigate from
 * within a folder back to the home directory.
 */
public class ChangeDirectoryCommand extends Command {

    public static final String COMMAND_WORD = "cd";

    public static final String HOME_SYMBOL = "..";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Navigates in and out of folders.\n"
            + "Parameters (at home directory): INDEX (must be a positive integer)\n"
            + "Parameters (inside folder): ..\n"
            + "Example (at home directory): " + COMMAND_WORD + " 1\n"
            + "Example (inside directory): " + COMMAND_WORD + " " + HOME_SYMBOL;

    public static final String MESSAGE_EXIT_FOLDER_SUCCESS = "Returned to home";
    public static final String MESSAGE_ENTER_FOLDER_SUCCESS = "Entered Card Folder: %1$s";

    private Index targetIndex;
    private final boolean isExitingFolder;

    public ChangeDirectoryCommand(Index targetIndex) {
        isExitingFolder = false;
        this.targetIndex = targetIndex;
    }

    public ChangeDirectoryCommand() {
        isExitingFolder = true;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (isExitingFolder) {
            return executeExitFolder(model);
        } else {
            return executeEnterFolder(model);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeDirectoryCommand // instanceof handles nulls
                && isExitingFolder == ((ChangeDirectoryCommand) other).isExitingFolder
                && sameTargetIndex((ChangeDirectoryCommand) other));
    }

    /**
     * Executes the logic to enter a folder. Model cannot already be in a folder.
     */
    private CommandResult executeEnterFolder(Model model) throws CommandException {
        List<ReadOnlyCardFolder> cardFolderList = model.getCardFolders();

        if (model.getState() != State.IN_HOMEDIR) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_INSIDE_FOLDER);
        }

        if (targetIndex.getZeroBased() >= cardFolderList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOLDER_DISPLAYED_INDEX);
        }
        model.enterFolder(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_ENTER_FOLDER_SUCCESS, targetIndex.getOneBased()),
                CommandResult.Type.ENTERED_FOLDER);
    }

    /**
     * Executes the logic to exit a folder. Model cannot already be outside folders.
     */
    private CommandResult executeExitFolder(Model model) throws CommandException {
        if (model.getState() != State.IN_FOLDER) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_OUTSIDE_FOLDER);
        }
        model.exitFolderToHome();
        return new CommandResult(MESSAGE_EXIT_FOLDER_SUCCESS, CommandResult.Type.EXITED_FOLDER);
    }

    /**
     * Compares the {@code targetIndex} of two ChangeDirectoryCommand objects, returning true if they are equal.
     */
    private boolean sameTargetIndex(ChangeDirectoryCommand other) {
        // if target indices exist, they must be the same
        if (targetIndex != null && other.targetIndex != null) {
            return targetIndex.equals(other.targetIndex);
        }

        // return true if both indices do not exist, else return false if only one target index do not exist
        return (targetIndex == null && other.targetIndex == null);
    }
}
