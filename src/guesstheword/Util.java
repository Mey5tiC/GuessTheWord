/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guesstheword;

/**
 *
 * @author fumagalli_andrea04
 */
public class Util {

    public static int Random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
