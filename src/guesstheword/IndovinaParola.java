package guesstheword;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class IndovinaParola {

    String parola;
    List<String> inseriti;
    int errori;
    int status;
    int tentativi;

    public IndovinaParola() {
        inseriti = new ArrayList<String>();
        errori=0;
        status=0;
        String filename = "words.txt";
        String pathToFile = Paths.get(filename).toAbsolutePath().toString();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(pathToFile));
            Stream<String> lines = br.lines();
            parola = lines.skip(Util.Random(0, 661563)).findFirst().get();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndovinaParola.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(parola);
    }

    public String getParola() {
        return parola;
    }

    public String stampaIndovina(String inserita, String attuale, ClientHandler ch, DataOutputStream o) {
        if (inserita.length() == 1) {
            if (!inseriti.contains(inserita)) {
                tentativi++;
                inseriti.add(inserita);
                char carattere = inserita.charAt(0);
                if (parola.contains(inserita)) {
                        for (int i = 0; i < parola.length(); i++) {
                            if (parola.charAt(i) == carattere) {
                                attuale = attuale.substring(0, i) + carattere + attuale.substring(i + 1);
                            }
                        }
                    }else{
                    errori++;
                    if(errori==3){
                        status++;
                        errori=0;
                    }
                }
            }else{
                ch.write(o,"Lettera già inserita");
            }
        } else {
            if(inserita.trim().equals("")){
            }else if (inserita.trim().equalsIgnoreCase(parola)) {
                tentativi++;
                attuale = parola;
            }else{
                tentativi++;
                status++;
            }
        }

        return attuale;
    }

    public String vittoria() {
        return "Hai indovinato la parola dopo "+tentativi+" mosse";
    }
}
