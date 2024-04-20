package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    String[] fileNames;
    CacheManager exist;
    CacheManager notExist;
    BloomFilter bloomFilter;

    public Dictionary(String... fileNames) {
        this.fileNames = fileNames;
        this.exist = new CacheManager(400, new LRU());
        this.notExist = new CacheManager(100, new LFU());
        this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        this.insertBloomFilter(fileNames);
    }

    private void insertBloomFilter(String... fileNames) {
        // Iterate over each file name
        for (String fileName : fileNames) {
            // The resource (reader) will be automatically closed when exiting this try block
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                Set<String> uniqueWords = new HashSet<>();

                // Read each line from the file
                while ((line = reader.readLine()) != null) {
                    // Split the line into words by whitespace and add them to the set
                    String[] words = line.split("\\s+");
                    uniqueWords.addAll(Arrays.asList(words));
                }

                // Add each unique word to the Bloom filter
                for (String word : uniqueWords) {
                    bloomFilter.add(word);
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception
            }
        }
    }

    public boolean query(String word) {
        if (this.exist.query(word)){ return true; }
        if (this.notExist.query(word)){ return false; }
        if (this.bloomFilter.contains(word)) {
            this.exist.add(word);
            return true;
        }
        else {
            this.notExist.add(word);
            return false;
        }
    }

    public boolean challenge(String word) {
        boolean result = IOSearcher.search(word, this.fileNames);
        if (result) {
            this.exist.add(word);
        }
        else {
            this.notExist.add(word);
        }
        return result;
    }
}
