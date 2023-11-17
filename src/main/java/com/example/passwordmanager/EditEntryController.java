package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Random;

/**
 * The AddEntryController class is the controller for the add-entry-view.fxml file,
 * managing the UI and interactions for adding a new password entry.
 */
public class EditEntryController {

    // JavaFX UI elements injected from add-entry-view.fxml
    public Button addButton;
    public Button cancelButton;
    public TextField usernameField;
    public TextField titleField;
    public Button randomizeButton;
    public TextField passwordField;

    private MainViewController mainViewController;

    /**
     * Sets the reference to the MainViewController.
     *
     * @param mainViewController The reference to the MainViewController.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * Handles the action event when the "Save" button is clicked in the Edit Entry view.
     * Retrieves input values, validates them, edits the current entry with new inputs
     * Closes the current view and refreshes the password entries in the main view.
     */
    @FXML
    private void editEntry() {
        // Retrieve input values
        String username = usernameField.getText();
        String title = titleField.getText();
        String password = passwordField.getText();

        // Validate input
        boolean isInputValid = validateInput(username, title, password);

        if (isInputValid) {
            // Retrieve the selected entry details from password database based on its title
            String selectedEntry = titleField.getText();
            String[] entryDetails = PasswordDatabase.getEntryDetails(selectedEntry);

            // Check if the entry exists. This should always be true.
            if (entryDetails != null) {
                // Update the entry details with new values
                entryDetails[0] = title;
                entryDetails[1] = username;
                entryDetails[2] = password;

                // Overwrite the existing entry in the database file
                PasswordDatabase.updatePasswordEntry(selectedEntry, entryDetails);

                // Refresh password entries in the list in the main view
                if (mainViewController != null) {
                    mainViewController.loadPasswordEntries();
                }

                // Close the current view
                closeView();
            }
        }
    }

    /**
     * Closes the current view (window).
     */
    @FXML
    private void closeView() {
        // Get the stage (window) and close it
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Generates a random password and sets it to the passwordField.
     */
    @FXML
    private void generateRandomPassword() {
        // Add logic to generate a random password and set it to the passwordField
        String randomPassword = generateRandomPasswordString();
        passwordField.setText(randomPassword);
    }

    /**
     * Generates a random password string.
     *
     * @return The generated random password.
     */
    private String generateRandomPasswordString() {
        // Add logic to generate a random password string (customize according to requirements)
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        int length = 20; // Set the desired length of the random password

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    /**
     * Validates the input fields and updates styles accordingly.
     *
     * @param username The username input.
     * @param title    The title input.
     * @param password The password input.
     * @return True if the input is valid, false otherwise.
     */
    private boolean validateInput(String username, String title, String password) {
        // Reset styles
        usernameField.setStyle("");
        titleField.setStyle("");
        passwordField.setStyle("");

        boolean isValid = true;

        if (username.isEmpty()) {
            // Set red border for the username field
            usernameField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (title.isEmpty()) {
            // Set red border for the title field
            titleField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (password.isEmpty()) {
            // Set red border for the password field
            passwordField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        return isValid;
    }


    /**
     * Sets the details of a password entry in the UI fields.
     * Used when populating the Edit Entry view with the details of an existing entry.
     *
     * @param entryDetails An array containing the details of a password entry: [title, username, password].
     *                     The method expects a non-null array with exactly three elements.
     *                     If the array is valid, it sets the corresponding UI fields with the info from entry details.
     */
    public void setEntryDetails(String[] entryDetails) {
        if (entryDetails != null && entryDetails.length == 3) {
            titleField.setText(entryDetails[0]);
            usernameField.setText(entryDetails[1]);
            passwordField.setText(entryDetails[2]);
        }
    }

}
