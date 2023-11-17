package com.example.passwordmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The PasswordDatabase class provides methods for reading and writing password entries to a file.
 */
public class PasswordDatabase {

    // Path to the file where password entries are stored.
    private static final String FILE_PATH = "passwords.txt";

    // Delimiter used to separate fields in the file.
    private static final String DELIMITER = ",";


    /**
     * Retrieves a list of password entries from the file.
     *
     * @return A list of string arrays, each representing a password entry.
     */
    public static List<String[]> getPasswordEntries() {
        List<String[]> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Read each line from the file and split it into fields using the delimiter
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(DELIMITER);
                entries.add(fields);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }


    /**
     * Saves a new password entry to the file.
     *
     * @param entry An array of strings representing the fields of the password entry.
     */
    public static void savePasswordEntry(String[] entry) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            // Join the fields of the entry into a single line and write it to the file
            String line = String.join(DELIMITER, entry);
            writer.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the details of a specific password entry based on its title.
     *
     * @param entryTitle The title of the entry to retrieve.
     * @return The details of the entry as a string array.
     */
    public static String[] getEntryDetails(String entryTitle) {
        List<String[]> entries = getPasswordEntries();

        for (String[] entry : entries) {
            String title = entry[0];

            if (title.equals(entryTitle)) {
                return entry;
            }
        }

        return null; // Entry not found
    }


    /**
     * Updates an existing password entry in the file.
     *
     * @param entryTitle The title of the entry to update.
     * @param newDetails The new details to set for the entry.
     */
    public static void updatePasswordEntry(String entryTitle, String[] newDetails) {
        // Retrieve all existing entries from the database
        List<String[]> entries = getPasswordEntries();

        // Iterate through each entry to find and update the specified entry
        for (int i = 0; i < entries.size(); i++) {
            String[] entry = entries.get(i);
            String title = entry[0];

            if (title.equals(entryTitle)) {
                // Update the entry with new details
                entries.set(i, newDetails);
                break; // Exit the loop once the entry is updated
            }
        }

        // Update the file with the modified entries
        updateFile(entries);
    }


    /**
     * Deletes a password entry from the file.
     *
     * @param entryTitle The title of the entry to delete.
     */
    public static void deletePasswordEntry(String entryTitle) {
        // Retrieve all existing entries from the database
        List<String[]> entries = getPasswordEntries();

        // Identify and remove the entry to be deleted
        for (int i = 0; i < entries.size(); i++) {
            String[] entry = entries.get(i);
            String title = entry[0];

            if (title.equals(entryTitle)) {
                // Remove the entry from the list
                entries.remove(i);

                // Write the updated entries back to the file
                updateFile(entries);
                break;
            }
        }
    }


    /**
     * Updates the passwords.txt file with the provided list of password entries.
     *
     * @param entries The list of password entries to be written to the file.
     */
    private static void updateFile(List<String[]> entries) {
        // Write the updated entries back to the passwords.txt file
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (String[] entry : entries) {
                String line = String.join(DELIMITER, entry);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
