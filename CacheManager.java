package test;


import java.util.HashSet;

public class CacheManager {

    private final int size;
    private final CacheReplacementPolicy crp;
    private final HashSet<String> words;

    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.words = new HashSet<>();
        this.crp = crp;
    }

    public boolean query(String word) {
        return this.words.contains(word);
    }

    public void add(String word) {
        if (this.words.contains(word)) {
            crp.add(word);
        }
        else {
            if (this.words.size() >= this.size) {
                words.remove(crp.remove());
            }
            crp.add(word);
            words.add(word);
        }
    }

}
