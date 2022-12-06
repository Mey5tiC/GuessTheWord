/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guesstheword;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fumagalli_andrea04
 */
public class IndovinaParola {

    String parola;
    static List<String> parole;
    
    public IndovinaParola() {
        //aggiungo tutte le parole all'array
        parole=new ArrayList<>();
        
        String path = new File("src/main/resources/conf.properties").getAbsolutePath();System.out.println(path);
        try {
            List<String> allLines = Files.readAllLines(Paths.get("./words.txt"));
            for (String line : allLines) {
                parole.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
	}
        
        
        parola=parole.get(Util.Random(0, parole.size()));
        
        System.out.println("parola");
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
