package guesstheword;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    final Socket socket;
    String name;
    boolean isLosggedIn;
    IndovinaParola indovina;
    String attuale;
    boolean playing;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name) {
        playing = false;
        this.socket = socket;
        this.name = name;
        isLosggedIn = true;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            log("ClientHander : " + ex.getMessage());
        }

        attuale = "";
        for (int i = 0; i < indovina.getParola().length(); i++) {
            attuale += "*";
        }
    }

    @Override
    public void run() {
        String received;
        String tmp = "";
        write(output, "Your name : " + name + "; Parola da indovinare:" + indovina.getParola());
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
                attuale = indovina.stampaIndovina(received, attuale);
                for (ClientHandler c : Server.getClients()) {
                    if (c.isLosggedIn) {
                        if (!attuale.contains("*")) {
                            write(c.output, indovina.vittoria());
                            endGame();
                        } else {
                            write(c.output, attuale);
                        }
                        log(received);
                    }
                }
            } else {
                if (received.equalsIgnoreCase("start")) {
                    gameStart();
                    for (ClientHandler c : Server.getClients()) {
                        if (c.isLosggedIn) {
                            write(c.output, "Gioco iniziato");
                            log(received);
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
        }
        return line;
    }

    private void write(DataOutputStream output, String message) {
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
        System.out.println(msg);
    }

    private void gameStart() {
        indovina = Server.gameStart();
        playing = true;
    }

    public void endGame() {
        playing = false;
    }
}
