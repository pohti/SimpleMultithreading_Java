/*
 * CSCI213 Assignment 4
 * --------------------------
 * File name: (state name of .java file)
 * Author: Min Marn Oo
 * Student Number: 5841021
 * Description: (A brief description of this class)
 */


package Server;

import Common.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private Socket clientSocket;
    private DataOutputStream dataOut;

    public ClientHandler(Socket clientSocket) {
        // setup clientSocket and ObjectOutputStream
        this.clientSocket = clientSocket;

        try {
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }

    }// end of constructor

    public void sendMessage (String message){
        try {
            dataOut.writeUTF(message);
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end of sendMessage

    public void end(){
        // terminate connection
        try{
            dataOut.close();
            clientSocket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}// end of class ClientHandler
