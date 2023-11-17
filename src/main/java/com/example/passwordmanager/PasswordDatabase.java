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
}
