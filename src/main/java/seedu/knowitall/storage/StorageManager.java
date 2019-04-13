package seedu.knowitall.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.knowitall.commons.core.LogsCenter;
import seedu.knowitall.commons.exceptions.DataConversionException;
import seedu.knowitall.model.ReadOnlyCardFolder;
import seedu.knowitall.model.ReadOnlyUserPrefs;
import seedu.knowitall.model.UserPrefs;

/**
 * Manages storage of CardFolder data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private List<CardFolderStorage> cardFolderStorageList;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(List<CardFolderStorage> cardFolderStorageList, UserPrefsStorage userPrefsStorage) {
        super();
        this.cardFolderStorageList = new ArrayList<>(cardFolderStorageList);
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ CardFolder methods ==============================

    @Override
    public void readCardFolders(List<ReadOnlyCardFolder> readFolders) throws Exception {
        requireNonNull(readFolders);

        readFolders.clear();
        Exception exception = null;
        List<CardFolderStorage> unavailableCardFolderStorages = new ArrayList<>();
        for (CardFolderStorage cardFolderStorage : cardFolderStorageList) {
            try {
                Optional<ReadOnlyCardFolder> cardFolder = readCardFolder(cardFolderStorage);
                cardFolder.ifPresent(readFolders::add);
            } catch (DataConversionException | IOException e) {
                exception = e;
                unavailableCardFolderStorages.add(cardFolderStorage);
            }
        }
        if (exception != null) {
            cardFolderStorageList.removeAll(unavailableCardFolderStorages);
            throw exception;
        }
    }

    /**
     * Reads a {@code ReadOnlyCardFolder} from a {@code CardFolderStorage}.
     * @return {@code Optional.empty} if the file is not found.
     */
    private Optional<ReadOnlyCardFolder> readCardFolder(CardFolderStorage cardFolderStorage)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + cardFolderStorage.getcardFolderFilesPath());
        return cardFolderStorage.readCardFolder();
    }

    /**
     * Saves the CardFolder to the specified filePath
     */
    @Override
    public void saveCardFolder(ReadOnlyCardFolder cardFolder, int index) throws IOException {
        assert index < cardFolderStorageList.size();
        Path filePath = cardFolderStorageList.get(index).getcardFolderFilesPath();
        logger.fine("Attempting to write to data file: " + filePath);
        cardFolderStorageList.get(index).saveCardFolder(cardFolder, filePath);
    }

    @Override
    public void saveCardFolders(List<ReadOnlyCardFolder> cardFolders, Path path) throws IOException {
        cardFolderStorageList.clear();

        // Clear directory before saving
        if (Files.exists(path)) {
            clearDirectory(path);
        }

        for (ReadOnlyCardFolder cardFolder : cardFolders) {
            Path filePath = path.resolve(cardFolder.getFolderName() + Storage.FILE_FORMAT);
            CardFolderStorage cardFolderStorage = new JsonCardFolderStorage(filePath);
            cardFolderStorageList.add(cardFolderStorage);
            cardFolderStorage.saveCardFolder(cardFolder);
        }
    }

    /**
     * Deletes every folder data file at the specified {@code path}
     * If {@code path} is a folder data file, only the file will be deleted.
     * If {@code path} is a folder, all folder data files inside the folder (but not the folder itself) will be deleted.
     */
    private void clearDirectory(Path path) throws IOException {
        // Obtain all files in directory
        List<Path> pathsToDelete = Files.walk(path)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        // Process each file
        for (Path pathToDelete : pathsToDelete) {
            // Delete if file is indeed a folder data file
            if (JsonCardFolderStorage.isCardFolderStorage(pathToDelete)) {
                Files.deleteIfExists(pathToDelete);
            }
        }
    }
}
