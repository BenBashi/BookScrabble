package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final Tile[][] tiles;
    private final int[][] type;
    //0- regular, 1- double letter, 2- triple letter, 3-double word, 4- triple word, 5- star.
    private boolean isStarInitialized;
    private static Board b = null;

    private Board() {
        this.isStarInitialized = false;
        this.tiles = new Tile[15][15];
        this.type = new int[15][15];
        // Coordinates for different values
        int[][] coordinates1 = {{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14}, {12, 6}, {12, 8}, {14, 3}, {14, 11}};
        int[][] coordinates2 = {{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1}, {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}};
        int[][] coordinates3 = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {10, 10}, {11, 11}, {12, 12}, {13, 13}, {1, 13}, {2, 12}, {3, 11}, {4, 10}, {13, 1}, {12, 2}, {11, 3}, {10, 4}};
        int[][] coordinates4 = {{0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}};
        int[][] coordinates5 = {{7, 7}};

        // Insert value 1 into coordinates1
        insertValues(this.type, coordinates1, 1);

        // Insert value 2 into coordinates2
        insertValues(this.type, coordinates2, 2);

        // Insert value 3 into coordinates3
        insertValues(this.type, coordinates3, 3);

        // Insert value 4 into coordinates4
        insertValues(this.type, coordinates4, 4);

        // Insert value 5 into coordinates5
        insertValues(this.type, coordinates5, 5);
    }

    public static Board getBoard() {
        if(b == null)
            b = new Board();
        return b;
    }

    // Method to create a shallow copy of the tiles array
    public Tile[][] getTiles() {
        Tile[][] copiedTiles = new Tile[15][15];

        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, copiedTiles[i], 0, tiles[i].length);
        }

        return copiedTiles;
    }

    // Method to insert values into coordinates of the array
    private void insertValues(int[][] array, int[][] coordinates, int value) {
        for (int[] coordinate : coordinates) {
            int x = coordinate[0];
            int y = coordinate[1];
            array[x][y] = value;
        }
    }

    //Not good
    public boolean boardLegal(Word w) {
        if(w.getRow() > 14 || w.getRow() < 0 || w.getCol() > 14 || w.getCol() < 0) {
            return false;
        }
        if(w.isVertical()) {
          if(w.getTiles().length + w.getRow() > 15)
              return false;
        }
        else {
            if(w.getTiles().length + w.getCol() > 15)
                return false;
        }
        if(tiles[7][7] == null) {
            if (w.isVertical()) {
                if (w.getCol() != 7)
                    return false;
                if (w.getRow() > 7)
                    return false;
                if (w.getTiles().length + w.getRow() < 7)
                    return false;
            } else {
                if (w.getRow() != 7)
                    return false;
                if (w.getCol() > 7)
                    return false;
                if (w.getTiles().length + w.getCol() < 7)
                    return false;
            }
        }
        Tile[] wordTiles = w.getTiles();
        if(w.isVertical()) {
            for (int i = 0; i < wordTiles.length; i++) {
                Tile currentTile = tiles[w.getRow() + i][w.getCol()];
                if (currentTile != null) {
                    if (!currentTile.equals(wordTiles[i])) {
                        if(wordTiles[i]!=null){
                            if (wordTiles[i].getLetter() != '_') {
                                return false; // Tile does not match and is not empty, so return false
                            }
                        }
                    }
                }
            }
        }
        else{
            for (int i = 0; i < wordTiles.length; i++) {
                Tile currentTile = tiles[w.getRow()][w.getCol() + i];
                if (currentTile != null) {
                    if (!currentTile.equals(wordTiles[i])) {
                        if(wordTiles[i]!=null){
                            if (wordTiles[i].getLetter() != '_') {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

//The function will be implemented in the future
    public boolean dictionaryLegal(Word w) {
        return true;
    }

    private int[] findWordRange(int row, int startCol, int endCol) {
        // Move startCol towards the beginning of the row until encountering a null tile or reaching the row limit
        while (startCol >= 0 && tiles[row][startCol] != null) {
            startCol--;
        }
        // Increment startCol to point to the beginning of the sequence of non-null tiles
        startCol++;

        // Move endCol towards the end of the row until encountering a null tile or reaching the row limit
        while (endCol < 15 && tiles[row][endCol] != null) {
            endCol++;
        }
        // Decrement endCol to point to the end of the sequence of non-null tiles
        endCol--;

        // Return the indices of the full row
        return new int[]{startCol, endCol};
    }

    private int[] findVerticalWordRange(int col, int startRow, int endRow) {
        // Move startRow upwards until encountering a null tile or reaching the column limit
        while (startRow >= 0 && tiles[startRow][col] != null) {
            startRow--;
        }
        // Increment startRow to point to the beginning of the sequence of non-null tiles
        startRow++;

        // Move endRow downwards until encountering a null tile or reaching the column limit
        while (endRow < 15 && tiles[endRow][col] != null) {
            endRow++;
        }
        // Decrement endRow to point to the end of the sequence of non-null tiles
        endRow--;

        // Return the indices of the full column
        return new int[]{startRow, endRow};
    }

    private Word findWordWithMaxScore(int row, int start, int end, int fullStart, int fullEnd) {
        int maxScore = 0;
        Word maxWord = null;

        // Iterate over each possible sequence of word within the given maximum range
        for (int i = fullStart; i <= fullEnd; i++) {
            for (int j = i; j <= fullEnd; j++) {
                // Check if the current sequence contains the minimum sequence represented by start and end
                if (i <= start && j >= end) {
                    // Create a Word object for the current sequence
                    Tile[] sequenceTiles = Arrays.copyOfRange(tiles[row], i, j + 1);
                    Word currentWord = new Word(sequenceTiles, row, i, false); // Assuming horizontal word

                    // Check if the word is legal according to the dictionary
                    if (dictionaryLegal(currentWord)) {
                        // Calculate the score of the word
                        int currentScore = getScore(currentWord);

                        // Update maxScore and maxWord if necessary
                        if (currentScore > maxScore) {
                            maxScore = currentScore;
                            maxWord = currentWord;
                        }
                    }
                }
            }
        }

        return maxWord;
    }

    private Word findVerticalWordWithMaxScore(int col, int start, int end, int fullStart, int fullEnd) {
        int maxScore = 0;
        Word maxWord = null;

        // Iterate over each possible sequence of word within the given maximum range
        for (int i = fullStart; i <= fullEnd; i++) {
            for (int j = i; j <= fullEnd; j++) {
                // Check if the current sequence contains the minimum sequence represented by start and end
                if (i <= start && j >= end) {
                    // Create a Word object for the current sequence
                    Tile[] sequenceTiles = new Tile[j - i + 1];
                    for (int k = i; k <= j; k++) {
                        sequenceTiles[k - i] = tiles[k][col];
                    }
                    Word currentWord = new Word(sequenceTiles, i, col, true); // Vertical word

                    // Check if the word is legal according to the dictionary
                    if (dictionaryLegal(currentWord)) {
                        // Calculate the score of the word
                        int currentScore = getScore(currentWord);

                        // Update maxScore and maxWord if necessary
                        if (currentScore > maxScore) {
                            maxScore = currentScore;
                            maxWord = currentWord;
                        }
                    }
                }
            }
        }

        return maxWord;
    }


    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> words = new ArrayList<>();
        int row = word.getRow();
        int col = word.getCol();

        if (word.isVertical()) {
            // Find word range for the vertical word
            int[] range = findVerticalWordRange(col, row, row + word.getTiles().length - 1);

            // Find word with max score for the vertical word
            Word maxWord = findVerticalWordWithMaxScore(col, row, row + word.getTiles().length - 1, range[0], range[1]);

            // Add maxWord to the ArrayList if it's not null
            if (maxWord != null && word.getTiles().length > 1) {
                words.add(maxWord);
            }
            else{
                return null;
            }

            int j = 0;
            // Iterate over the given word coordinates and find words with max score for their horizontal segments
            for (int i = row; i < row + word.getTiles().length; i++) {
                if(word.getTiles()[j] != null){
                    if(word.getTiles()[j].getLetter()>= 'A' && word.getTiles()[j].getLetter() <= 'Z') {
                        int[] range2 = findWordRange(i, col, col);
                        Word horizontalWord = findWordWithMaxScore(i, col, col, range2[0], range2[1]);
                        if (horizontalWord != null && horizontalWord.getTiles().length > 1) {
                            words.add(horizontalWord);
                        }
                    }
                }
                j++;
            }
        } else {
            // Find word range for the non-vertical word
            int[] range = findWordRange(row, col, col + word.getTiles().length - 1);

            // Find word with max score for the non-vertical word
            Word maxWord = findWordWithMaxScore(row, col, col + word.getTiles().length - 1, range[0], range[1]);

            // Add maxWord to the ArrayList if it's not null
            if (maxWord != null && word.getTiles().length > 1) {
                words.add(maxWord);
            }
            else{
                return null;
            }

            int j = 0;
            // Iterate over the given word coordinates and find words with max score for their vertical segments
            for (int i = col; i < col + word.getTiles().length; i++) {
                if(word.getTiles()[j] != null){
                    if(word.getTiles()[j].getLetter()>= 'A' && word.getTiles()[j].getLetter() <= 'Z') {
                        int[] range2 = findVerticalWordRange(i, row, row);
                        Word verticalWord = findVerticalWordWithMaxScore(i, row, row, range2[0], range2[1]);
                        if (verticalWord != null && verticalWord.getTiles().length > 1) {
                            words.add(verticalWord);
                        }
                    }
                }
                j++;
            }
        }
        return words;
    }


    public int getScore(Word word) {
        int score = 0;
        int multiplier = 1;
        int tileScore;
        if (word.isVertical()) {
            for(int i = 0; i < word.getTiles().length; i++){
                tileScore = tiles[i+ word.getRow()][word.getCol()].getScore();
                switch (type[i+ word.getRow()][word.getCol()]) {
                    case 0:
                        break;
                    case 1:
                        tileScore*=2;
                        break;
                    case 2:
                        tileScore*=3;
                        break;
                    case 3:
                        multiplier*=2;
                        break;
                    case 5:
                        if(!this.isStarInitialized) {
                            multiplier*=2;
                        }
                        break;
                    case 4:
                        multiplier*=3;
                        break;
                    default:
                        return 0;
                }
                score += tileScore;
            }
        }
        else{
            for(int i = 0; i < word.getTiles().length; i++){
                tileScore = tiles[word.getRow()][word.getCol() + i].getScore();
                switch (type[word.getRow()][word.getCol() + i]) {
                    case 0:
                        break;
                    case 1:
                        tileScore*=2;
                        break;
                    case 2:
                        tileScore*=3;
                        break;
                    case 3:
                        multiplier*=2;
                        break;
                    case 5:
                        if(!this.isStarInitialized) {
                            multiplier*=2;
                        }
                        break;
                    case 4:
                        multiplier*=3;
                        break;
                    default:
                        return 0;
                }
                score += tileScore;
            }
        }
        score*=multiplier;
        return score;
    }


    public int tryPlaceWord(Word word) {
        if (!boardLegal(word)) {
            return 0;
        }

        if (word.isVertical()) {
            int x = word.getRow();
            int y = word.getCol();
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i] != null){
                    if (word.getTiles()[i].getLetter() >= 'A' && word.getTiles()[i].getLetter() <= 'Z') {
                        tiles[x+i][y] = word.getTiles()[i];
                    }
                }
            }
        } else {
            int x = word.getRow();
            int y = word.getCol();
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i] != null){
                    if (word.getTiles()[i].getLetter() >= 'A' && word.getTiles()[i].getLetter() <= 'Z') {
                        tiles[x][y + i] = word.getTiles()[i];
                    }
                }
            }
        }
        int score = 0;
        ArrayList<Word> words = getWords(word);
        if(words == null){
            if (word.isVertical()) {
                int x = word.getRow();
                int y = word.getCol();
                for (int i = 0; i < word.getTiles().length; i++) {
                    if(word.getTiles()[i] != null) {
                        if (word.getTiles()[i].getLetter() >= 'A' && word.getTiles()[i].getLetter() <= 'Z') {
                            tiles[x+i][y] = null;
                        }
                    }
                }
            } else {
                int x = word.getRow();
                int y = word.getCol();
                for (int i = 0; i < word.getTiles().length; i++) {
                    if(word.getTiles()[i] != null) {
                        if (word.getTiles()[i].getLetter() >= 'A' && word.getTiles()[i].getLetter() <= 'Z') {
                            tiles[x][y + i] = null;
                        }
                    }
                }
            }
        }else {
            for(Word w: words) {
                score= score + getScore(w);
            }
            if(!this.isStarInitialized){
                this.isStarInitialized = true;
            }
        }
        return score;
    }
}
