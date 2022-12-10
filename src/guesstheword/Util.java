package guesstheword;

public class Util {

    public static int Random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
