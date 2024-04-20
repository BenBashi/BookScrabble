package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    // Private constructor to prevent instantiation
    private IOSearcher() {}

    public static boolean search(String word, String... fileNames) {
        // Iterate over each file name
        for (String fileName : fileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;

                // Read each line from the file
                while ((line = reader.readLine()) != null) {
                    // Check if the line contains the word
                    if (line.contains(word)) {
                        return true; // If word found in any file, return true
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception
            }
        }
        return false; // Word not found in any file
    }
}

