package seedu.knowitall.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.knowitall.commons.exceptions.DataConversionException;
import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.ReadOnlyCardFolder;

/**
 * Represents a storage for {@link CardFolder}.
 */
public interface CardFolderStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getcardFolderFilesPath();

    /**
     * Returns CardFolder data as a {@link ReadOnlyCardFolder}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCardFolder> readCardFolder() throws DataConversionException, IOException;

    /**
     * @see #getcardFolderFilesPath()
     */
    Optional<ReadOnlyCardFolder> readCardFolder(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCardFolder} to the storage.
     * @param cardFolder cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCardFolder(ReadOnlyCardFolder cardFolder) throws IOException;

    /**
     * @see #saveCardFolder(ReadOnlyCardFolder)
     */
    void saveCardFolder(ReadOnlyCardFolder cardFolder, Path filePath) throws IOException;

    /**
     * Deletes the CardFolder at the {@code filePath}.
     */
    void deleteCardFolder(Path filePath) throws IOException;

}
