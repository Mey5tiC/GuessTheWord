package guesstheword;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    Scanner scan ;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    InetAddress ip;
    Frame userFrame;
    
    public Client(){
        try{
            ip = InetAddress.getByName("localhost");
            socket = new Socket(ip, Constants.PORT);
            
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            scan = new Scanner(System.in);
        }catch(UnknownHostException ex){
            log("Client : " + ex.getMessage());
        }catch(IOException ex){
            log("Client : " + ex.getMessage());
        }
         userFrame = new Frame("client",output);
    }
    
    public static void main(String[] args){
        Client client = new Client();
        client.readMessageThread();
        client.writeMessageThread();
    }
    
    private void readMessageThread(){
        Thread readMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try{
                        String msg = input.readUTF();
                        log(msg);
                    }catch(IOException ex){
                        log("readMessageThread : " + ex.getMessage());
                        break;
                    }
                    
                }
            }
        });
        readMessage.start();
    }
    
    private void writeMessageThread(){
        Thread sendMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    String msg = scan.nextLine();
                    try{
                        output.writeUTF(msg);
                    }catch(IOException ex){
                        log("writeMessageThread : " + ex.getMessage());
                        break;
                    }
                }
            }
        });
        sendMessage.start();
    }
    
    private void log(String msg){ 
        userFrame.log(msg);
    }
}
