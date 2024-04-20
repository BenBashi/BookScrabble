package test;

import java.util.LinkedList;
import java.util.Queue;

public class LRU implements CacheReplacementPolicy {

    private final Queue<String> lru;

    public LRU() {
        this.lru = new LinkedList<>();
    }

    @Override
    public void add(String word) {
        // Check if the word is already in the queue
        if (lru.contains(word)) {
            // Remove the word from its current position
            lru.remove(word);
        }
        // Add the word to the end of the queue
        lru.add(word);
    }

    @Override
    public String remove() {
        return this.lru.poll();
    }
}
