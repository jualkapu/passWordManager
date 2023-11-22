package com.example.passwordmanager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The MainViewController class is the controller for the main-view.fxml file, managing the UI and interactions.
 */
public class MainViewController {

    // JavaFX UI elements injected from main-view.fxml
    public TextField passwordTextField;
    public TextField usernameTextField;
    public Button addNewButton;
    public Text currentFileText;

    // ListView element for displaying password entries
    @FXML
    private ListView<String> passwordListView;


    /**
     * Sets up the initial state of the UI.
     * - Fetches entries from the file and displays them in the ListView.
     * - Adds an event handler to handle item selection in the ListView.
     * The handler (showSelectedEntryDetails) is triggered whenever an item in the ListView is selected.
     */
    @FXML
    public void initialize() {
        // Check if a file is currently open
        String currentFilePath = PasswordDatabase.getFilePath();

        if (currentFilePath != null && !currentFilePath.isEmpty()) {
            // Fetch entries from the file and display them in the ListView
            loadPasswordEntries();
            updateCurrentFileIndicator();
        }

        // Adds an event handler to handle item selection in the ListView
        // Handler (showSelectedEntryDetails) is triggered whenever an item in the ListView is selected.
        passwordListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedEntryDetails(newValue));

    }


    /**
     * Handles the "Add New" button click event.
     * Opens the add-entry-view.fxml in a new window for adding a new password entry.
     */
    @FXML
    public void handleAddNewButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-entry-view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            // Get the controller of the AddEntryView
            AddEntryController addEntryController = fxmlLoader.getController();

            // Pass a reference to MainViewController
            addEntryController.setMainViewController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ABC");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the "Delete" button click event.
     */
    @FXML
    public void handleDeleteButtonClick() {

        // Gets the index of the currently selected item in the ListView.
        int selectedIndex = passwordListView.getSelectionModel().getSelectedIndex();

        // If nothing is selected the index is >= 0
        if (selectedIndex >= 0) {
            // Remove the selected item from the list
            String deletedEntry = passwordListView.getItems().get(selectedIndex);

            // Remove the entry from the passwords.txt file using PasswordDatabase
            PasswordDatabase.deletePasswordEntry(deletedEntry);
            loadPasswordEntries();
        }
    }


    /**
     * Handles the "Edit" button click event.
     */
    @FXML
    public void handleEditButtonClick() {
        // Check if an item is selected in the ListView
        String selectedEntry = passwordListView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            try {
                // Load the "Edit Entry" window FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-entry-view.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                // Get the controller of the EditEntryView
                EditEntryController editEntryController = fxmlLoader.getController();

                // Pass a reference to MainViewController
                editEntryController.setMainViewController(this);

                // Retrieve details of the selected entry and pass them to the EditEntryController
                String[] entryDetails = PasswordDatabase.getEntryDetails(selectedEntry);
                editEntryController.setEntryDetails(entryDetails);

                // Set up the stage and show the window
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("Edit Entry");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Handles the "Copy" button click event for the username field.
     * Copies the current value of the usernameTextField to the clipboard.
     */
    @FXML
    public void handleUsernameCopyButtonClick() {
        String username = usernameTextField.getText();
        copyToClipboard(username);
    }


    /**
     * Handles the "Copy" button click event for the password field.
     * Copies the current value of the passwordTextField to the clipboard.
     */
    @FXML
    public void handlePasswordCopyButtonClick() {
        String password = passwordTextField.getText();
        copyToClipboard(password);
    }


    /**
     * Copies the given text to the clipboard.
     *
     * @param text The text to be copied.
     */
    private void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }


    /**
     * Updates the username and password text fields when an entry is selected.
     * This method is called when an item is selected in the ListView.
     * It retrieves the list of entries and iterates over the entries to find the one with a matching title.
     *
     * @param selectedEntry The title of the selected entry.
     */
    private void showSelectedEntryDetails(String selectedEntry) {
        // Retrieve all entries from the PasswordDatabase
        List<String[]> entries = PasswordDatabase.getPasswordEntries();

        // Iterate over entries to find the selected one
        for (String[] entry : entries) {
            String title = entry[0];

            // If a match is found, it updates the usernameTextField and passwordTextField
            // with the corresponding values from the entry.
            if (title.equals(selectedEntry)) {
                // Set the username and password in the text fields
                usernameTextField.setText(entry[1]);
                passwordTextField.setText(entry[2]);
                break;
            }
        }
    }


    /**
     * Load password entries and populate the ListView.
     * Clears the existing items in the passwordListView,
     * retrieves all entries from the PasswordDatabase, and populates the ListView with entry titles.
     */
    public void loadPasswordEntries() {
        // Clear the existing items in the passwordListView
        passwordListView.getItems().clear();

        // Retrieve all entries from the PasswordDatabase
        List<String[]> entries = PasswordDatabase.getPasswordEntries();

        // Populate the ListView with entry titles
        for (String[] entry : entries) {
            String title = entry[0];
            passwordListView.getItems().add(title);
        }
    }


    /**
     * Handles the "Quit" button click event.
     */
    @FXML
    public void handleQuitButtonClick() {
        // Terminate the JavaFX application
        Platform.exit();
    }


    /**
     * Opens a FileChooser dialog for the user to select a file.
     * If a file is selected, its path is stored, and the UI is cleared.
     * The method then triggers loading of password entries from the selected file.
     */
    @FXML
    public void handleOpenFile() {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();

        // Set the extension filter to restrict to .txt files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Check if a file was selected
        if (selectedFile != null) {
            // Get the selected file path
            String filePath = selectedFile.getAbsolutePath();

            // Logic to create a new file and set the file path
            PasswordDatabase.setFilePath(filePath);

            // Clear the password entries in the UI
            clearGUI();

            // Load password entries from the selected file
            loadPasswordEntries();
            updateCurrentFileIndicator();
        }
    }


    /**
     * Closes the currently opened file.
     * Resets the file path to the default "passwords.txt" and clears the UI.
     */
    @FXML
    public void handleCloseFile() {
        // For simplicity, set the file path back to the default
        PasswordDatabase.setFilePath("");

        // Clear the password entries in the UI
        clearGUI();
        updateCurrentFileIndicator();
    }


    /**
     * Creates a new file based on user input using a FileChooser.
     * If the selected file doesn't exist, it creates a new file and sets the file path.
     * Clears the UI and triggers the loading of password entries from the new file.
     */
    @FXML
    public void handleNewFile() {
        // Call the helper method to handle the creation of a new password database file
        String filePath = createNewPasswordDatabaseFile();

        // If new file was created successfully, load it in to the GUI
        if(filePath != null) {
            // Set the file path
            PasswordDatabase.setFilePath(filePath);

            // Clear the password entries in the UI
            clearGUI();

            // Load password entries from the new file
            loadPasswordEntries();
            updateCurrentFileIndicator();
        }
    }


    /**
     * Method to create a new password database file.
     * Opens a "FileChooser" dialog and creates or overwrites existing database file per user input
     *
     * @return The absolute path of the created file, or null if the file creation failed or the user canceled the operation.
     */
    public static String createNewPasswordDatabaseFile() {
        // Create a FileChooser object, which is a dialog that allows the user to select or create a file.
        FileChooser fileChooser = new FileChooser();

        // Set an extension filter on the FileChooser to restrict the user to selecting only .txt files.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Display the actual file creation dialog and creates createdFile object to represent the created file.
        File createdFile = fileChooser.showSaveDialog(new Stage());

        // Check if the user actually created a file (didn't cancel the dialog).
        if (createdFile != null) {
            try {
                // Open a FileWriter with append set to false (to overwrite the file)
                // append set to FALSE means it will overwrite the existing content or create a new file if it doesn't exist.
                try (FileWriter fileWriter = new FileWriter(createdFile, false)) {
                    // Write an empty string to the file
                    fileWriter.write("");
                }
                // Return the absolute path of the created or overwritten file
                return createdFile.getAbsolutePath();

            } catch (IOException e) {
                // Print the stack trace if an IOException occurs during file creation
                e.printStackTrace();

                // Return null to indicate that the file creation failed
                return null;
            }
        }

        // Return null to indicate that the user canceled the file creation operation
        return null;
    }


    /**
     * Clears the currently displayed entries from the GUI
     */
    public void clearGUI() {
        // Clear the password entries in the GUI
        passwordListView.getItems().clear();
        usernameTextField.clear();
        passwordTextField.clear();

    }


    /**
     * Updates the current file indicator text based on the open database file.
     */
    public void updateCurrentFileIndicator() {
        // Get the current file path
        String currentFilePath = PasswordDatabase.getFilePath();

        // Check if a file is currently open
        if (currentFilePath != null && !currentFilePath.isEmpty()) {
            // File is open, display the file name
            currentFileText.setText("Currently open database file: " + new File(currentFilePath).getName());
        } else {
            // No file is open
            currentFileText.setText("No database file currently open.");
        }
    }

}
