/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guesstheword;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;


public class ClientHandler implements Runnable{

    final Socket socket;
    final Scanner scan;
    String name;
    boolean isLosggedIn;
    IndovinaParola indovina;
    String attuale;
    
    private DataInputStream input;
    private DataOutputStream output;
    
    public ClientHandler(Socket socket, String name, IndovinaParola indovina){
        this.socket = socket;
        scan = new Scanner(System.in);
        this.name = name;
        isLosggedIn = true;
        
        try{
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
        }catch(IOException ex){
            log("ClientHander : " + ex.getMessage());
        }
        
        this.indovina=indovina;
        attuale="";
        for (int i = 0; i < indovina.getParola().length(); i++) {
            attuale+="*";
        }
    }
    @Override
    public void run() {
        String received;
        String tmp="";
        write(output, "Your name : " + name + "Parola da indovinare:" + indovina.getParola());
        for(ClientHandler c : Server.getClients()){
           tmp+=c.name+", ";
        }
        write(output,"Active clients: "+tmp);
        while(true){
            received = read();
            if(received.equalsIgnoreCase(Constants.LOGOUT)){
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }
            
            forwardToClients(received);
        }
        closeStreams();
    }
    
    private void forwardToClients(String received){
        /*StringTokenizer tokenizer = new StringTokenizer(received, "#");
        String recipient = tokenizer.nextToken().trim();
        String message = tokenizer.nextToken().trim();*/
        attuale = indovina.stampaIndovina(received, attuale);
        if(!attuale.contains("*")){
            System.out.println(indovina.vittoria());
        }
        for(ClientHandler c : Server.getClients()){
            if(c.isLosggedIn){
                write(c.output, attuale);
                log(received);
            }
        }
        
    }
    
    private String read(){
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException ex) {
            log("read : " + ex.getMessage());
        }
        return line;
    }
    
    private void write(DataOutputStream output , String message){
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
  
    private void closeSocket(){
        try{
            socket.close();
        }catch(IOException ex){
            log("closeSocket : " + ex.getMessage());
        }
    }
   
    private void log(String msg){
        System.out.println(msg);
    }
}
