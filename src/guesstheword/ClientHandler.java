package guesstheword;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    final Socket socket;
    String name;
    boolean isLosggedIn;
    IndovinaParola indovina;
    String attuale;
    boolean playing;
    Frame serverFrame;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name, Frame serverFrame) {
        playing = false;
        this.socket = socket;
        this.name = name;
        isLosggedIn = true;
        this.serverFrame = serverFrame;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            log("ClientHander : " + ex.getMessage());
        }

    }

    @Override
    public void run() {
        String received;
        String tmp = "";
        write(output, "Your name : " + name);
        for (ClientHandler c : Server.getClients()) {
            tmp += c.name + ", ";
        }
        write(output, "Active clients: " + tmp);
        while (true) {
            received = read();
            if (received.equalsIgnoreCase(Constants.LOGOUT)) {
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }

            forwardToClients(received);
        }
        closeStreams();
    }

    private void forwardToClients(String received) {
        if (received.contains("#")) {
            StringTokenizer tokenizer = new StringTokenizer(received, "#");
            String recipient = tokenizer.nextToken().trim();
            String message = tokenizer.nextToken().trim();

            for (ClientHandler c : Server.getClients()) {
                if (c.isLosggedIn && c.name.equals(recipient)) {
                    write(c.output, recipient + " : " + message);
                    log(name + " --> " + recipient + " : " + message);
                    break;
                }
            }
        } else {
            if (playing) {
                for (ClientHandler c : Server.getClients()) {
                    if (c.isLosggedIn && c.name.equals(name)) {
                        attuale = indovina.stampaIndovina(received, attuale, this, c.output);
                        if (!attuale.contains("*")) {
                            write(c.output, indovina.vittoria());
                            write(c.output, indovina.getParola());
                            endGame(c);
                        } else {
                            write(c.output, attuale);
                        }
                        log(c.name + ":" + received);
                    }
                }
            } else {
                if (received.equalsIgnoreCase("start")) {
                    gameStart();
                    for (ClientHandler c : Server.getClients()) {
                        if (c.isLosggedIn && c.name.equals(name)) {
                            write(c.output, "Gioco iniziato");
                            write(c.output, attuale);
                            log(c.name + ":" + received);
                        }
                    }
                }
            }

        }

    }

    private String read() {
        String line = "";
        try {
            line = input.readUTF();

        } catch (IOException ex) {
            log("read : " + ex.getMessage());
            return null;
        }
        return line;
    }

    public void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);

        } catch (IOException ex) {
            log("write : " + ex.getMessage());
        }
    }

    private void closeStreams() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            log("closeStreams : " + ex.getMessage());
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            log("closeSocket : " + ex.getMessage());
        }
    }

    private void log(String msg) {
        serverFrame.log(msg);
    }

    private void gameStart() {
        indovina = Server.gameStart();
        playing = true;
        attuale = "";
        for (int i = 0; i < indovina.getParola().length(); i++) {
            attuale += "*";
        }
    }

    public void endGame(ClientHandler c) {
        playing = false;

        int actualScore = indovina.tentativi;

        String[] scores = new String[10];

        for (int i = 0; i < scores.length; i++) {
            scores[i] = "";
        }

        int x = 0;
        try {
            File readFile = new File("Scoreboard.csv");
            Scanner scan = new Scanner(readFile);
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                scores[x] = data;
                x++;
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if (scores[scores.length - 1].equals("")) {
            scores[x] = c.name + ";" + actualScore + ";\n";
        } else {
            int[] tentativi = new int[10];

            int index = -1;
            for (int i = 0; i < tentativi.length; i++) {
                tentativi[i] = Character.getNumericValue(scores[i].charAt(scores[i].length() - 2));
                if (tentativi[i] > actualScore) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                String[] tmpVett = new String[10];
                int tmp = 0;
                for (int i = index; i < scores.length; i++) {
                    tmpVett[tmp]=scores[i];
                    tmp++;
                }
                
                scores[index]=c.name + ";" + actualScore + ";\n";
                
                tmp=0;
                for (int i = index+1; i < scores.length; i++) {
                    scores[i]=tmpVett[tmp];
                    tmp++;
                }
            }

        }
        
        String write = "";
        
        for (int i = 0; i < scores.length; i++) {
            write+=scores[i];
        }
        
        try {
            FileWriter writeFile = new FileWriter("Scoreboard.csv", false);
            writeFile.write(write);
            writeFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
