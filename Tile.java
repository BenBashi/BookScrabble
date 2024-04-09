package test;

import java.util.Objects;
import java.util.Random;

public class Tile {
    public final char letter;
    public final int score;

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o; // Cast to Tile
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public static class Bag {
        private int[] quantity;
        private Tile[] tile_array;
        private static Bag b = null;


        private Bag() {
            this.quantity = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            this.tile_array = new Tile[quantity.length]; // Creating an array of 26 Tile objects

            int[] tile_values = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
            // Initializing the array of tiles with quantity values and characters 'A' to 'Z'
            char ch = 'A';
            for (int i = 0; i < quantity.length; i++) {
                tile_array[i] = new Tile(ch, tile_values[i]);
                ch++;
            }
        }

        public static Bag getBag() {
            if(b == null)
                b = new Bag();
            return b;
        }

        public Tile getRand() {
            if(this.size() == 0)
                return null;
            else{
                Random random = new Random();
                int min = 0; // Minimum value
                int max = 25; // Maximum value
                int randomNumberInRange = random.nextInt(max - min + 1) + min;
                while(quantity[randomNumberInRange] == 0) {
                    randomNumberInRange = (randomNumberInRange + 1) % 26;
                }
                quantity[randomNumberInRange]--;
                return tile_array[randomNumberInRange];
            }
        }

        public Tile getTile(char ch) {
            if(ch >= 'A' && ch <= 'Z'){
                char characterA = 'A'; // Character to convert
                int asciiValueA = (int) characterA; // Convert char to ASCII value
                int asciiValueChar = (int) ch;
                asciiValueChar-=asciiValueA;
                if(quantity[asciiValueChar] == 0)
                    return null;
                else {
                    quantity[asciiValueChar]--;
                    return tile_array[asciiValueChar];
                }
            }
            else{
                return null;
            }
        }

        private int[] getFullQuantity(){
            int[] full = new int[] {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            return full;
        }

        public void put(Tile t) {
            char characterA = 'A'; // Character to convert
            int asciiValueA = (int) characterA; // Convert char to ASCII value
            int asciiValueChar = (int) t.getLetter();
            asciiValueChar-=asciiValueA;
            int full = getFullQuantity()[asciiValueChar];
            if(full>this.quantity[asciiValueChar]){
                this.quantity[asciiValueChar]++;
            }
        }
        public int size(){
            int s = 0;
            for (int a: quantity) {
                s+=a;
            }
            return s;
        }

        // Method to return a duplicate of the quantity array
        public int[] getQuantities() {
            // Creating a new array with the same length as the quantity array
            int[] duplicateArray = new int[quantity.length];

            // Copying elements from the quantity array to the duplicate array
            for (int i = 0; i < quantity.length; i++) {
                duplicateArray[i] = quantity[i];
            }
            return duplicateArray;
        }
    }
}
