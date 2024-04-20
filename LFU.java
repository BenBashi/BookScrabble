package test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class LFU implements CacheReplacementPolicy {
    private final Map<String, Integer> counterMap; // Holds word frequency
    private final TreeMap<Integer, TreeSet<String>> frequencyMap; // Holds words grouped by frequency

    public LFU() {
        counterMap = new HashMap<>();
        frequencyMap = new TreeMap<>();
    }
    @Override
    public void add(String word) {
        // Increase the counter for the word
        int count = counterMap.getOrDefault(word, 0);
        counterMap.put(word, count + 1);

        // Remove the word from its previous frequency group
        if (count > 0) {
            frequencyMap.get(count).remove(word);
            if (frequencyMap.get(count).isEmpty()) {
                frequencyMap.remove(count);
            }
        }

        // Add the word to the new frequency group
        frequencyMap.computeIfAbsent(count + 1, k -> new TreeSet<>()).add(word);
    }
    @Override
    public String remove() {
        // Get the lowest frequency group
        int lowestFrequency = frequencyMap.firstKey();

        // Get the least frequently used word from the lowest frequency group
        String wordToRemove = frequencyMap.get(lowestFrequency).iterator().next();

        // Update the data structures
        frequencyMap.get(lowestFrequency).remove(wordToRemove);
        if (frequencyMap.get(lowestFrequency).isEmpty()) {
            frequencyMap.remove(lowestFrequency);
        }
        counterMap.remove(wordToRemove);

        return wordToRemove;
    }
}
