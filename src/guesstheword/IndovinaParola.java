/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guesstheword;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        String pathToFile=Paths.get(filename).toAbsolutePath().toString();
        //System.out.println(pathToFile.toAbsolutePath());
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(pathToFile));
            Stream<String> lines = br.lines();
            parola = lines.skip(Util.Random(0, 661563)).findFirst().get();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndovinaParola.class.getName()).log(Level.SEVERE, null, ex);
            parola="errore";
        }
        
        System.out.println(parola);
    }

    public String getParola() {
        return parola;
    }
    
    public String stampaIndovina(String inserita, String attuale){
        if(inserita.length()==1){
            System.out.println("carattere");
            char carattere=inserita.charAt(0);
            if(attuale.contains(inserita)){
                System.out.println("Lettera già inserita"); 
            }else{
                if(parola.contains(inserita)){
                    for (int i = 0; i < parola.length(); i++) {
                        if(parola.charAt(i)==carattere){
                            attuale = attuale.substring(0, i) + carattere + attuale.substring(i + 1);
                        }
                    }
                }
            }
        }else{
            System.out.println("parola");
            if(inserita.trim().equals(parola)){
                attuale=parola;
            }
        }
        
        return attuale;
    }
    
    public String vittoria(){
        return "Hai indovinato la parola";
    }
    
    
}
