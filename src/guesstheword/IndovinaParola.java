/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guesstheword;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author fumagalli_andrea04
 */
public class IndovinaParola {

    String parola;
    
    public IndovinaParola() {
        
        String filename="words.txt";
        Path pathToFile = Paths.get(filename);
        System.out.println(pathToFile.toAbsolutePath());
        
        try (Stream<String> lines = Files.lines(pathToFile.toAbsolutePath())) {
            parola = lines.skip(Util.Random(0, lines.toArray().length)-1).findFirst().get();
        } catch (IOException ex) {
            Logger.getLogger(IndovinaParola.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(parola);
    }
    
    public String stampaIndovina(String tmp, String attuale){
        if(tmp.length()==1){
            char carattere=tmp.charAt(0);
            if(attuale.contains(tmp)){
                System.out.println("Lettera già inserita"); 
            }else{
                if(parola.contains(tmp)){
                    for (int i = 0; i < parola.length(); i++) {
                        if(parola.charAt(i)==carattere){
                            attuale = attuale.substring(0, i) + carattere + attuale.substring(i + 1);
                        }
                    }
                }
            }
        }else{
            if(tmp.equals(parola)){
                vittoria();
            }
        }
        
        return "";
    }
    
    public String vittoria(){
        return "Hai indovinato la parola";
    }
    
    
}
