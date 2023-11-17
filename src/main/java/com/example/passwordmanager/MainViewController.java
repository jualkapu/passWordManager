package com.example.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    // ListView for displaying password entries
    @FXML
    private ListView<String> passwordListView;

    /**
     * Sets up the initial state of the UI.
     * - Fetches entries from the file and displays them in the ListView.
     * - Adds an event handler to handle item selection in the ListView.
     *   The handler (showSelectedEntryDetails) is triggered whenever an item in the ListView is selected.
     */
    @FXML
    public void initialize() {
        // Fetch entries from the file and display them in the ListView
        loadPasswordEntries();

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
}
